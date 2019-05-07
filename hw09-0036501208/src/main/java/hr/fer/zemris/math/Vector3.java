package hr.fer.zemris.math;

/**
 * A simple 3D vector class that offers some basic
 * vector operations such as dot and cross product.
 * <p>
 * It is also worth noting that this class is
 * immutable, meaning that any {@code Vector3} can
 * not be changed, but will instead always return a
 * new instance of {@code Vector3} class.
 */
public class Vector3 {
	
	/** The x component of this vector */
	private final double x;
	
	/** The y component of this vector */
	private final double y;
	
	/** The z component of this vector */
	private final double z;
	
	/**
	 * Constructs a new vector with the given x, y
	 * and z components.
	 * 
	 * @param x the x component of this vector
	 * @param y the y component of this vector
	 * @param z the z component of this vector
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Adds the given {@code Vector3} to this vector
	 * and returns the result as a new instance of
	 * {@code Vector3}.
	 *
	 * @param other the other {@code Vector3}
	 * @return a new {@code Vector3} that is a result of the
	 * 		   addition between {@code this} and {@code other}
	 * 		   {@code Vector3}
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	/**
	 * Subtracts the given {@code Vector3} to this vector
	 * and returns the result as a new instance of
	 * {@code Vector3}.
	 *
	 * @param other the other {@code Vector3}
	 * @return a new {@code Vector3} that is a result of the
	 * 		   subtraction between {@code this} and {@code other}
	 * 		   {@code Vector3}
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	/**
	 * Scales {@code this} vector by the given factor and
	 * returns the result as a new {@code Vector3} .
	 *
	 * @param s the scaling factor
	 * @return a new {@code Vector3} that is a result of scaling
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}
	
	/**
	 * Returns the norm (length) of this vector.
	 * 
	 * @return norm (length) of this vector
	 */
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Normalizes this vector and returns the result
	 * as a new {@code Vector3}.
	 *
	 * @return the normalization result as a new {@code Vector3}
	 */
	public Vector3 normalized() {
		double norm = norm();
		return new Vector3(x / norm, y / norm, z / norm);
	}
	
	/**
	 * Returns the dot product of {@code this} and {@code other}
	 * vector.
	 *
	 * @param other the other vector
	 * @return the dot product
	 */
	public double dot(Vector3 other) {
		return x*other.x + y*other.y + z*other.z;
	}
	
	/**
	 * Returns the cross product of {@code this} and {@code other}
	 * vector. The resulting vector will be <b>this</b> x <b>other</b>.
	 *
	 * @param other the other vector
	 * @return the cross product of {@code this} vector with {@code other}
	 */
	public Vector3 cross(Vector3 other) {
		double i = y*other.z - z*other.y;
		double j = z*other.x - x*other.z;
		double k = x*other.y - y*other.x;
		
		return new Vector3(i, j, k);
	}
	
	/**
	 * Returns the cosine of angle between {@code this} and
	 * {@code other} vector.
	 *
	 * @param other the other vector
	 * @return the cosine of angle
	 * @throws IllegalArgumentException if any vector is a null vector
	 * 									(vector of length zero)
	 */
	public double cosAngle(Vector3 other) {
		double normThis = norm();
		double normOther = other.norm();
		
		if(normThis == 0)
			throw new IllegalArgumentException("Length of this vector is 0.");
		
		if(normOther == 0)
			throw new IllegalArgumentException("Length of other vector is 0.");
		
		return dot(other) / (normThis * normOther);
	}
	
	/**
	 * Returns the x, y and z components of this vector
	 * in an array format.
	 *
	 * @return the array of x, y and z components
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	//-----------------------------------------------------------------
	//							GETTERS
	//-----------------------------------------------------------------
	
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
	 * Returns the z component of this vector.
	 * 
	 * @return z component of this vector
	 */
	public double getZ() {
		return z;
	}
}

