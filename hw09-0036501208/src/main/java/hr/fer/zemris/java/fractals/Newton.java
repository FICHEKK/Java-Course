package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.cmplxcache.ComplexCache;
import hr.fer.zemris.java.fractals.cmplxcache.ComplexCacheImpl;
import hr.fer.zemris.java.fractals.cmplxcache.IComplexCache;
import hr.fer.zemris.java.fractals.cmplxcache.IThreadBoundComplexCache;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * A simple program demonstrating the usage of thread pooling
 * on the generation of Newton-Raphson's fractal.
 *
 * @author Filip Nemec
 */
public class Newton {
	
	private static ComplexPolynomial polynomial;
	
	private static ComplexRootedPolynomial polynomialRooted;
	
	private static ComplexPolynomial derived;
	
	/**
	 * Program starts from here.
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		List<Complex> rootsList = new ArrayList<>();
		int rootsEntered = 0;
		
		while(true) {
			System.out.print("Root " + (rootsEntered + 1) + " > ");
			String input = scanner.nextLine();
			
			if(input.equals("done")) {
				if(rootsEntered < 2) {
					System.out.println("You haven't entered 2 roots yet!");
					continue;
				} else {
					System.out.println("Image of fractal will appear shortly. Thank you.");
					break;
				}
			}
			
			try {
				rootsList.add(parseComplex(input));
				rootsEntered++;
			} catch(NumberFormatException e) {
				System.out.println("'" + input + "' cannot be parsed to a complex number.");
				System.out.println("Input should be of format 'a + ib'.");
			}
		}
		
		
		Complex[] roots = rootsList.toArray(new Complex[0]);
		
		polynomialRooted = new ComplexRootedPolynomial(Complex.ONE, roots);
		polynomial = polynomialRooted.toComplexPolynom();
		derived = polynomial.derive();
		
		FractalViewer.show(new NewtonFractalProducer());
		
		scanner.close();
	}
	
	/**
	 * Models the Newton-Raphson's iteration.
	 *
	 * @author Filip Nemec
	 */
	public static class NewtonFractalProducer implements IFractalProducer {
		
		/** The pool of threads which are being reused. */
		private static ExecutorService pool;
		
		/** The number of threads used by the {@code ExecutorService}. */
		private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
		
		/**
		 * Constructs a new {@code IFractalProducer} for Newton-Raphson's iteration.
		 *
		 * @param polynomialRooted the complex rooted polynomial
		 * @param pool the pool of threads, since this task is CPU heavy
		 */
		public NewtonFractalProducer() {
			pool = Executors.newFixedThreadPool(THREAD_COUNT, new DaemonicThreadFactory());
		}
		
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			short[] data = new short[width * height];
			
			int numberOfPortions = 8 * THREAD_COUNT;
			int portionRows = height / numberOfPortions;
			
			List<Future<Void>> results = new ArrayList<>();
			
			long startTime = System.currentTimeMillis();
			
			for(int i = 0; i < numberOfPortions; i++) {
				int yStart = i * portionRows;
				int yEnd = (i+1) * portionRows;
				
				if(i == (numberOfPortions - 1)) {
					yEnd = height - 1;
				}
				
				var job = new CalculatePortionOfFractalJob(data, width, height, yStart, yEnd, reMin, reMax, imMin, imMax);
				results.add(pool.submit(job));
			}
			
			for(Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}
			
			System.out.println(System.currentTimeMillis() - startTime + "ms");
			
			System.out.println("Finished");
			observer.acceptResult(data, (short) (polynomial.order()+1), requestNo);
		}
	}
	
	/**
	 * A {@code Callable} class that calculates the portion of the fractal
	 * and saves the result to the given data array. 
	 *
	 * @author Filip Nemec
	 */
	private static class CalculatePortionOfFractalJob implements Callable<Void> {
		
		/** The minimal value on the real axis. */
		private double reMin;
		
		/** The maximal value on the real axis. */
		private double reMax;
		
		/** The minimal value on the imaginary axis. */
		private double imMin;
		
		/** The maximal value on the imaginary axis. */
		private double imMax;
		
		/** The width of the display screen, in pixels. */
		private int width;
		
		/** The height of the display screen, in pixels. */
		private int height;
		
		/** The y-coordinate of the starting row that needs to be calculated. */
		private int yStart;
		
		/** The y-coordinate of the ending row. This row will be excluded from the calculation. */
		private int yEnd;
		
		/** The calculated data storage array. */
		private short[] data;
		
		/** The maximum number of iterations. */
		private static final int MAX_ITER = 64;
		
		/** The convergence threshold. */
		private static final double THRESHOLD = 0.001;
		
		/**
		 * Constructs a new job that will calculate the portion of the fractal.
		 *
		 * @param data the calculated data storage
		 * @param width the width of the display screen, in pixels
		 * @param height the height of the display screen, in pixels
		 * @param yStart the y-coordinate of the starting row that needs to be calculated
		 * @param yEnd the y-coordinate of the ending row. This row will be excluded from the calculation.
		 * @param reMin the minimal value on the real axis
		 * @param reMax the maximal value on the real axis
		 * @param imMin the minimal value on the imaginary axis
		 * @param imMax the maximal value on the imaginary axis
		 */
		public CalculatePortionOfFractalJob(short[] data, int width, int height, int yStart, int yEnd,
									   double reMin, double reMax, double imMin, double imMax) {
			this.data = data;
			
			this.width = width;
			this.height = height;
			
			this.yStart = yStart;
			this.yEnd = yEnd;
			
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
		}

		@Override
		public Void call() throws Exception {
			IComplexCache cache = ComplexCache.getCache();
			
			int offset = yStart * width;
			
			for(int y = yStart; y < yEnd; y++) {
				for(int x = 0; x < width; x++) {
					Complex zn = mapToComplexPlain(x, y, width, height, reMin, reMax, imMin, imMax);
					Complex znOld;
					Complex numerator;
					Complex denominator;
					
					int iter = 0;
					double module;

					do {
						numerator = polynomialRooted.apply(zn);
						denominator = derived.apply(zn);
						znOld = zn;
						
						numerator.modifyDivide(denominator);
						
						Complex znCopy = cache.get(zn);
						znCopy.modifySub(numerator);
						zn = znCopy;

						Complex znOldCopy = cache.get(znOld);
						znOldCopy.modifySub(zn);
						
						module = znOldCopy.module();
						iter++;
						
						cache.release(znCopy);
						cache.release(numerator);
						cache.release(denominator);
						cache.release(znOldCopy);
						
					} while (module > THRESHOLD && iter < MAX_ITER);
					
//					System.out.println(cache.size());

					int index = polynomialRooted.indexOfClosestRootFor(zn, THRESHOLD);
					data[offset++] = (short) (index + 1);
					
					cache.release(zn);
				}
			}
			
			return null;
		}
		
		/**
		 * Maps the given (x, y) screen coordinate to the complex number.
		 *
		 * @param x the x coordinate
		 * @param y the y coordinate
		 * @param w the screen width in pixels
		 * @param h the screen height in pixels
		 * @param uMin the minimum real point
		 * @param uMax the maximum real point
		 * @param vMin the minimum imaginary point
		 * @param vMax the maximum imaginary point
		 * @return the complex number at point (x, y)
		 */
		private static Complex mapToComplexPlain(int x, int y, int w, int h,
												 double uMin, double uMax,
												 double vMin, double vMax) {
			double re = x / (w-1.0) * (uMax - uMin) + uMin;
			double im = (h-1.0-y) / (h-1) * (vMax - vMin) + vMin;
			
			IComplexCache cache = ComplexCache.getCache();
			Complex result = cache.get(re, im);
			
			return result;
		}
	}
	
	/**
	 * Parses the given {@code String} sequence and converts it
	 * to the instance of {@code Complex} number, if possible.
	 *
	 * @param sequence the sequence to be parsed
	 * @return the {@code Complex} number represented by the given sequence
	 * @throws NumberFormatException if parsing was unsuccessful
	 */
	private static Complex parseComplex(String sequence) {
		Objects.requireNonNull(sequence, "Can't parse a null reference.");
		
		sequence = sequence.replaceAll("\\s", "");
		
		if(sequence.equals("i")) return Complex.IM;
		if(sequence.equals("+i")) return Complex.IM;
		if(sequence.equals("-i")) return Complex.IM_NEG;
		
		// Counting all the 'i' characters.
		int iCounter = sequence.length() - sequence.replace("i", "").length();
		int iIndex = sequence.indexOf('i');
		
		if(iCounter > 1)
			throw new NumberFormatException("Sequence contains more than one 'i'.");
		
		double re = 0, im = 0;
		
		if(iIndex == -1) {
			re = Double.parseDouble(sequence);
			
		} else if(iIndex == 0) {
			im = Double.parseDouble(sequence.substring(1));
			
		} else if(iIndex == 1) {
			im = Double.parseDouble(sequence.substring(2));
			im = processSignBeforeImaginaryUnit(im, sequence, 0);
			
		} else if(iIndex == sequence.length() - 1) {
			re = Double.parseDouble(sequence.substring(0, iIndex - 1));
			im = 1.0;
			im = processSignBeforeImaginaryUnit(im, sequence, iIndex - 1);
			
		} else {
			re = Double.parseDouble(sequence.substring(0, iIndex - 1));
			im = Double.parseDouble(sequence.substring(iIndex + 1));
			im = processSignBeforeImaginaryUnit(im, sequence, iIndex - 1);
		}
		
		return new Complex(re, im);
	}
	
	/**
	 * Helper method that processes the sign before imaginary unit.
	 *
	 * @param im the value of imaginary part
	 * @param sequence the sequence
	 * @param signIndex the index of the sign character
	 * @return the new imaginary part value
	 */
	private static double processSignBeforeImaginaryUnit(double im, String sequence, int signIndex) {
		char sign = sequence.charAt(signIndex);
		
		if(sign == '-') {
			im = -im;
		} else if(sign != '+') {
			throw new NumberFormatException("Expected '+' or '-' before 'i', was '" + sign + "'");
		}
		
		return im;
	}
	
	private static class CustomThread extends Thread implements IThreadBoundComplexCache {
		
		private IComplexCache cache = new ComplexCacheImpl();
		
		public CustomThread(Runnable r) {
			super(r);
		}
		
		@Override
		public IComplexCache getComplexCache() {
			return cache;
		}
	};
	
	/**
	 * A simple {@code ThreadFactory} that produces exclusively <i>daemonic</i> threads.
	 *
	 * @author Filip Nemec
	 */
	private static class DaemonicThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			CustomThread thread = new CustomThread(r);
			thread.setDaemon(true);
			return thread;
		}
	}
}
