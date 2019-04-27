package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;

/**
 * Models a simple collection of the first <i>n</i> primes.
 *
 * @author Filip Nemec
 */
public class PrimesCollection implements Iterable<Integer>{
	
	/** Total number of first primes this collection can return. */
	private int numberOfPrimes;
	
	/**
	 * Constructs a new primes collection for the first
	 * <i>n</i> primes.
	 *
	 * @param numberOfPrimes number of primes this collection will return
	 */
	public PrimesCollection(int numberOfPrimes) {
		this.numberOfPrimes = numberOfPrimes;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PrimeIterator();
	}
	
	/**
	 * Checks if the given number is a prime number.
	 *
	 * @param number number to be evaluated
	 * @return {@code true} if prime, {@code false} otherwise
	 */
	private static boolean isPrime(int number) {
		if(number < 2) return false;
		
		int sqrt = (int) Math.sqrt(number);
		for(int i = 2; i <= sqrt; i++) {
			if(number % i == 0) return false;
		}
		
		return true;
	}
	
	/**
	 * Models a simple iterator of prime numbers.
	 *
	 * @author Filip Nemec
	 */
	private class PrimeIterator implements Iterator<Integer> {
		
		/** Counts the number of already returned prime numbers. */
		private int counter = 0;
		
		/** The last found prime. */
		private int lastPrime = 2;

		@Override
		public boolean hasNext() {
			return counter < numberOfPrimes;
		}

		@Override
		public Integer next() {
			if(!hasNext()) 
				throw new IllegalStateException("Last prime was already given away.");
			
			counter++;

			for(; !isPrime(lastPrime); lastPrime++);
			return lastPrime++;
		}
	}
}
