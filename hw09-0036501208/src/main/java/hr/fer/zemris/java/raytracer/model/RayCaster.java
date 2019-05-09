package hr.fer.zemris.java.raytracer.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * A simple class that shows the usage and creating
 * a 3d scene view using ray-casting.
 *
 * @author Filip Nemec
 */
public class RayCaster {
	
	/**
	 * Program starts from here.
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(),
				new Point3D(10, 0, 0),
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10),
				20, 20
		);
	}
	
	/**
	 * Produces and returns the {@code IRayTracerProducer}.
	 *
	 * @return the {@code IRayTracerProducer} instance
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				
				System.out.println("Započinjem izračune...");
				
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];
				
				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				
				Point3D screenCorner = view.sub( xAxis.scalarMultiply(horizontal / 2.0) )
										   .add( yAxis.scalarMultiply(vertical / 2.0) );
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add( xAxis.scalarMultiply(x * horizontal / (width - 1.0)) )
														  .sub( yAxis.scalarMultiply(y * vertical / (height - 1.0)) );
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						
						offset++;
					}
				}
				
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
	
	/**
	 * Casts a ray and if it hits an object in the scene,
	 * it will calculate the lighting and store the appropriate
	 * pixel color.
	 *
	 * @param scene the scene
	 * @param ray the ray
	 * @param rgb the red-green-blue values storage for the pixel
	 */
	protected static void tracer(Scene scene, Ray ray, short[] rgb) {
		RayIntersection closest = findClosestIntersection(scene, ray);
		
		if(closest == null) {
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;
			
			return;
		}
		
		// Ambient component
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;

		List<LightSource> lights = scene.getLights();
		
		final double EPSILON = 1E-4;
		for(LightSource light : lights) {
			Ray lightRay = Ray.fromPoints(light.getPoint(), closest.getPoint());
			
			double lightToIntersection = light.getPoint().sub(closest.getPoint()).norm();
			RayIntersection lightRayIntersection = findClosestIntersection(scene, lightRay);
			
			if(lightRayIntersection != null && lightRayIntersection.getDistance() + EPSILON < lightToIntersection) {
				continue;
			} else {
				// Diffuse component
				Point3D n = closest.getNormal().normalize(); // normal
				Point3D l = (light.getPoint().sub(closest.getPoint())).normalize(); // intersection -> light
				double cosTheta = Math.max(0, l.scalarProduct(n));
	
				//Reflection component
				Point3D d = l.negate();
				Point3D reflect = d.sub(n.scalarMultiply(2).scalarMultiply(d.scalarProduct(n))).normalize();
				Point3D vision = ray.start.sub(closest.getPoint()).normalize();
				double cosAlphaPowerN = Math.pow(Math.max(0, reflect.scalarProduct(vision)), closest.getKrn());
				
				rgb[0] += light.getR() * (closest.getKdr() * cosTheta + closest.getKrr() * cosAlphaPowerN);
				rgb[1] += light.getG() * (closest.getKdg() * cosTheta + closest.getKrg() * cosAlphaPowerN);
				rgb[2] += light.getB() * (closest.getKdb() * cosTheta + closest.getKrb() * cosAlphaPowerN);
			}
		}
	}
	
	/**
	 * A simpler version of {@linkplain #tracer(Scene, Ray, short[])}.
	 * If the object gets hit, white pixel will be drawn. If no 
	 * objects were hit, stores the black pixel.
	 *
	 * @param scene
	 * @param ray
	 * @param rgb
	 */
	protected static void tracerSimple(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		
		RayIntersection closest = findClosestIntersection(scene, ray);
		
		if (closest == null) return;
		
		rgb[0] = 255;
		rgb[1] = 255;
		rgb[2] = 255;
	}
	
	/**
	 * Finds the closest intersection for the given ray in the
	 * given scene.
	 *
	 * @param scene the scene
	 * @param ray the ray
	 * @return the closest intersection for the given ray in the given scene
	 * 
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		List<GraphicalObject> objects = scene.getObjects();
		
		double minDistance = Double.MAX_VALUE;
		RayIntersection closestIntersection = null;
		
		for(GraphicalObject object : objects) {
			RayIntersection intersection = object.findClosestRayIntersection(ray);
			
			if(intersection == null) continue;
			
			double distance = intersection.getDistance();
			
			if(distance < minDistance) {
				minDistance = distance;
				closestIntersection = intersection;
			}
		}
		
		return closestIntersection;
	}
}
