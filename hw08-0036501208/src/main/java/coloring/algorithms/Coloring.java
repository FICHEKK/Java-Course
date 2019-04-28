package coloring.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;

/**
 * A simple coloring class defines a {@code Supplier, Consumer, Function}
 * and {@code Predicate} strategies for how the coloring process should
 * be handled.
 *
 * @author Filip Nemec
 */
public class Coloring implements Supplier<Pixel>, Consumer<Pixel>, Function<Pixel, List<Pixel>>, Predicate<Pixel> {
	
	/** The starting pixel used for coloring. */
	private Pixel reference;
	 
	/** The {@code Picture} being colored. */
	private Picture picture;
	 
	/** The color that is used for filling the selected area. */
	private int fillColor;
	 
	/** The reference color. If the testing pixel is of this color, it will get colored to {@link #fillColor}. */
	private int refColor;
	
	/**
	 * Constructs a new coloring for the given {@code Picture}.
	 *
	 * @param reference the coloring starting {@code Pixel}
	 * @param picture the {@code Picture} being colored
	 * @param fillColor the fill color
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		this.reference = reference;
		this.picture = picture;
		this.fillColor = fillColor;
		
		refColor = picture.getPixelColor(reference.x, reference.y);
	}
	

	@Override
	public boolean test(Pixel t) {
		return picture.getPixelColor(t.x, t.y) == refColor;
	}

	@Override
	public List<Pixel> apply(Pixel t) {
		var neighbourPixels = new LinkedList<Pixel>();
		
		addIfValid(neighbourPixels, t.x + 1, t.y);
		addIfValid(neighbourPixels, t.x, t.y - 1);
		addIfValid(neighbourPixels, t.x - 1, t.y);
		addIfValid(neighbourPixels, t.x, t.y + 1);
		
		
		return neighbourPixels;
	}

	@Override
	public void accept(Pixel t) {
		picture.setPixelColor(t.x, t.y, fillColor);
	}

	@Override
	public Pixel get() {
		return reference;
	}
	
	/**
	 * Adds the pixel to the given list if the given pixel coordinates
	 * are valid. Coordinates will be valid if they are in bounds:
	 * <br> 0 <= x < picture width
	 * <br> 0 <= y < picture height
	 *
	 * @param list the list that the pixel will be added to, if all the tests are passed
	 * @param x the x-coordinate of a pixel
	 * @param y the y-coordinate of a pixel
	 */
	private void addIfValid(List<Pixel> list, int x, int y) {
		if(x < 0) return;
		if(y < 0) return;
		if(x >= picture.getWidth()) return;
		if(y >= picture.getHeight()) return;
		
		list.add(new Pixel(x, y));
	}
}
