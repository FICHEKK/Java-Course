package coloring.algorithms;

import java.util.Objects;

/**
 * Models a single pixel.
 *
 * @author Filip Nemec
 */
public class Pixel {
	
	/** Screen x-coordinate of this pixel. */
	public int x;
	
	/** Screen y-coordinate of this pixel. */
	public int y;
	
	/**
	 * Constructs a new pixel with the given
	 * x and y coordinates.
	 *
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pixel))
			return false;
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
