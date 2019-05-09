package hr.fer.zemris.java.raytracer.model;

/**
 * Models the intersection between a ray and a sphere.
 *
 * @author Filip Nemec
 */
public class SphereRayIntersection extends RayIntersection {
	
	/** Reference to the sphere this ray is intersecting with. */
	private Sphere sphere;
	
	/**
	 * Constructs a new sphere-ray intersection.
	 *
	 * @param sphere the sphere this ray is intersecting with
	 * @param point the point of intersection
	 * @param distance the distance the ray traveled
	 * @param outer if this 
	 */
	public SphereRayIntersection(Sphere sphere, Point3D point, double distance, boolean outer) {
		super(point, distance, outer);
		this.sphere = sphere;
	}

	@Override
	public Point3D getNormal() {
		return getPoint().sub(sphere.getCenter());
	}

	@Override
	public double getKdr() {
		return sphere.getKdr();
	}

	@Override
	public double getKdg() {
		return sphere.getKdg();
	}

	@Override
	public double getKdb() {
		return sphere.getKdb();
	}

	@Override
	public double getKrr() {
		return sphere.getKrr();
	}

	@Override
	public double getKrg() {
		return sphere.getKrg();
	}

	@Override
	public double getKrb() {
		return sphere.getKrb();
	}

	@Override
	public double getKrn() {
		return sphere.getKrn();
	}
}
