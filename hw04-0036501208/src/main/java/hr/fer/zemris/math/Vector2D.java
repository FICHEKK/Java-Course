package hr.fer.zemris.math;

/**
 * A simple 2D vector class that offers some basic
 * vector transformations - scaling, rotation and translation.
 */
public class Vector2D {
	
	/** The x component of this vector */
	private double x;
	
	/** The y component of this vector */
	private double y;
	
	/**
	 * Constructs a new vector with the given x and y
	 * components.
	 * 
	 * @param x the x component of this vector
	 * @param y the y component of this vector
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x component of this vector.
	 * 
	 * @return x component of this vector
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the y component of this vector.
	 * 
	 * @return y component of this vector
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Translates this vector by the given vector.
	 * 
	 * @param offset the offset vector
	 */
	public void translate(Vector2D offset) {
		x += offset.x;
		y += offset.y;
	}
	
	/**
	 * Generates a new vector translated by the given offset.
	 * It does not change this vector.
	 * 
	 * @param offset the offset vector
	 * @return new vector translated by the given offset
	 */
	public Vector2D translated(Vector2D offset) {
		return new Vector2D(x + offset.x, y + offset.y);
	}
	
	/**
	 * Rotates this vector by the given angle.
	 * 
	 * @param angle angle of rotation <b>in radians</b>
	 */
	public void rotate(double angle) {
		double currentAngle = angle();
		double rotatedAngle = currentAngle + angle;
		
		double length = length(); // Cache so we don't call sqrt twice.
		
		x = length * Math.cos(rotatedAngle);
		y = length * Math.sin(rotatedAngle);
	}
	
	/**
	 * Returns a new vector that is this vector rotated by the
	 * given angle. It does not change this vector.
	 * 
	 * @param angle angle of rotation <b>in radians</b>
	 * @return newly generated rotated vector
	 */
	public Vector2D rotated(double angle) {
		Vector2D copy = copy();
		copy.rotate(angle);
		return copy;
	}
	
	/**
	 * Scales this vector by the given scaler.
	 * 
	 * @param scaler the scaler
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	/**
	 * Generates a new vector that is equal to this vector,
	 * but scaled by the given scaler. It does not change
	 * this vector.
	 * 
	 * @param scaler the scaler
	 * @return newly generated scaled vector
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}
	
	/**
	 * Returns a copy of this vector.
	 * @return copy of this vector
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
	/**
	 * Returns the length of this vector.
	 * @return length of this vector
	 */
	public double length() {
		return Math.hypot(x, y);
	}
	
	/**
	 * Returns the angle of this vector in radians [0, 2*PI].
	 * @return angle of this vector in radians, in range from 0 to 2 pi.
	 */
	public double angle() {
		double radians = Math.atan2(y, x);
		return radians >= 0 ? radians : radians + 2 * Math.PI;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
