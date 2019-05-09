package hr.fer.zemris.java.raytracer.model;

/**
 * Models the sphere object that will be
 * shown in the scene.
 *
 * @author Filip Nemec
 */
public class Sphere extends GraphicalObject {
	
	/** The center of this sphere. */
	private Point3D center;
	
	/** The radius of this sphere. */
	private double radius;
	
	/** The red diffuse component. */
	private double kdr;
	
	/** The green diffuse component. */
	private double kdg;
	
	/** The blue diffuse component. */
	private double kdb;
	
	/** The red reflective component. */
	private double krr;
	
	/** The green reflective component. */
	private double krg;
	
	/** The blue reflective component. */
	private double krb;
	
	/** The shininess factor. */
	private double krn;
	
	/**
	 * Constructs a new sphere with the given parameters.
	 *
	 * @param center the center of the sphere
	 * @param radius the radius of the sphere
	 * @param kdr the red diffuse component
	 * @param kdg the green diffuse component
	 * @param kdb the blue diffuse component
	 * @param krr the red reflective component
	 * @param krg the green reflective component
	 * @param krb the blue reflective component
	 * @param krn the shininess factor
	 */
	public Sphere(Point3D center, double radius,
				  double kdr, double kdg, double kdb,
				  double krr, double krg, double krb,
				  double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}
	
	/**
	 * Finds the closest ray intersection point with this
	 * sphere.
	 */
	public RayIntersection findClosestRayIntersection(Ray ray) {
		// Ray formula P(t)=A + tB (A = starting point, B = ray direction vector)
	    Point3D A = ray.start;
	    Point3D B = ray.direction;
	    Point3D C = this.center;
	    Point3D OC = A.sub(C);

	    double a = B.scalarProduct(B);
	    double b = 2.0 * OC.scalarProduct(B);
	    double c = OC.scalarProduct(OC) - radius * radius;
	    
	    double discriminant = b*b - 4*a*c;
	    
	    // There was no intersection.
	    if(discriminant < 0) return null;
	    
	    // There was intersection.
	    double distanceLower = (-b - Math.sqrt(discriminant)) / (2.0*a);
	    Point3D intersection = A.add( B.scalarMultiply(distanceLower) );
	    
	    return new SphereRayIntersection(this, intersection, distanceLower, true);
	}
	
	//---------------------------------------------------------------
	//							GETTERS
	//---------------------------------------------------------------
	
	/**
	 * Returns the center point of this sphere.
	 *
	 * @return the center point of this sphere
	 */
	public Point3D getCenter() {
		return center;
	}
	
	/**
	 * Returns the radius of this sphere.
	 *
	 * @return the radius of this sphere
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Returns the diffusion coefficient - the red component.
	 *
	 * @return the diffusion coefficient - the red component
	 */
	public double getKdr() {
		return kdr;
	}
	
	/**
	 * Returns the diffusion coefficient - the green component.
	 *
	 * @return the diffusion coefficient - the green component
	 */
	public double getKdg() {
		return kdg;
	}

	/**
	 * Returns the diffusion coefficient - the blue component.
	 *
	 * @return the diffusion coefficient - the blue component
	 */
	public double getKdb() {
		return kdb;
	}

	/**
	 * Returns the reflection coefficient - the red component.
	 *
	 * @return the reflection coefficient - the red component
	 */
	public double getKrr() {
		return krr;
	}

	/**
	 * Returns the reflection coefficient - the green component.
	 *
	 * @return the reflection coefficient - the green component
	 */
	public double getKrg() {
		return krg;
	}

	/**
	 * Returns the reflection coefficient - the blue component.
	 *
	 * @return the reflection coefficient - the blue component
	 */
	public double getKrb() {
		return krb;
	}

	/**
	 * Returns the shininess factor.
	 *
	 * @return the shininess factor
	 */
	public double getKrn() {
		return krn;
	}
}
