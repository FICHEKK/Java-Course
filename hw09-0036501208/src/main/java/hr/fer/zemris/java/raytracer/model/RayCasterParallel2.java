package hr.fer.zemris.java.raytracer.model;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * The multi-threaded version of the ray-cast renderer.
 * It uses the <i>Fork-Join</i> framework and {@code RecursiveAction}.
 *
 * @author Filip Nemec
 */
public class RayCasterParallel2 {
	
	/**
	 * Program starts from here.
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30);
	}

	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {
			long time;

			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}

			@Override
			public Point3D getViewUp() { // fixed in time
				return new Point3D(0, 0, 10);
			}

			@Override
			public Point3D getView() { // fixed in time
				return new Point3D(-2, 0, -0.5);
			}

			@Override
			public long getTargetTimeFrameDuration() {
				return 150; // redraw scene each 150 milliseconds
			}

			@Override
			public Point3D getEye() { // changes in time
				double t = (double) time / 10000 * 2 * Math.PI;
				double t2 = (double) time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}
		};
	}
	
	/**
	 * Defines a recursive action for the {@code ForkJoinPool}.
	 * This class will split the pixel color calculation until
	 * the job hits the threshold, after which the job will 
	 * get processed.
	 *
	 * @author Filip Nemec
	 */
	public static class RayCasterAction extends RecursiveAction {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		/** The maximum size of the job unit. If it is higher than this, job will get forked. */
		private static final int THRESHOLD = 100;
		
		/** The starting y-coordinate of this action. */
		private int yStart;
		
		/** The ending y-coordinate of this action. */
		private int yEnd;
		
		/**
		 * The red, green and blue storage arrays. Main job of this {@code RecursiveAction}  
		 * is to fill these color arrays.
		 */
		short[] r, g, b;
		
		/** The width of the screen. */
		private int width;
		
		/** The height of the screen. */
		private int height;
		
		/** The horizontal length. */
		private double horizontal;
		
		/** The vertical length. */
		private double vertical;
		
		/** The scene that is being shown. */
		private Scene scene;
		
		/** The point of the human observer. */
		private Point3D eye;
		
		/** The x-axis. */
		private Point3D xAxis;
		
		/** The y-axis. */
		private Point3D yAxis;
		
		/** The point which represents the corner of the screen. */
		private Point3D screenCorner;
	 
		/**
		 * Constructs a new {@code RayCasterAction}.
		 *
		 * @param yStart the starting y-coordinate of this action
		 * @param yEnd the starting x-coordinate of this action
		 * @param r the red color component storage array
		 * @param g the green color component storage array
		 * @param b the blue color component storage array
		 * @param width the width of the screen
		 * @param height the height of the screen
		 * @param horizontal the horizontal length
		 * @param vertical the vertical length
		 * @param scene the scene being observer
		 * @param eye the point of the human observer
		 * @param xAxis the x-axis
		 * @param yAxis the y-axis
		 * @param screenCorner the point which represents the corner of the screen
		 */
		public RayCasterAction(int yStart, int yEnd, short[] r, short[] g, short[] b, int width, int height,
				double horizontal, double vertical, Scene scene, Point3D eye, Point3D xAxis, Point3D yAxis,
				Point3D screenCorner) {
			super();
			this.yStart = yStart;
			this.yEnd = yEnd;
			this.r = r;
			this.g = g;
			this.b = b;
			this.width = width;
			this.height = height;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.scene = scene;
			this.eye = eye;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.screenCorner = screenCorner;
		}

		@Override
		protected void compute() {
			if(yEnd - yStart + 1 <= THRESHOLD) {
				computeJobDirectly();
				return;
			}
			
			int start1 = yStart;
			int end1   = yStart + (yEnd - yStart) / 2;
			
			int start2 = end1 + 1;
			int end2   = yEnd;
			
			invokeAll(
					new RayCasterAction(start1, end1, r, g, b, width, height, horizontal, vertical, scene, eye, xAxis, yAxis, screenCorner),
					new RayCasterAction(start2, end2, r, g, b, width, height, horizontal, vertical, scene, eye, xAxis, yAxis, screenCorner)
			);
		}
		
		private void computeJobDirectly() {
			short[] rgb = new short[3];

			int offset = yStart * width;

			for (int y = yStart; y <= yEnd; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add( xAxis.scalarMultiply(x * horizontal / (width - 1.0)) )
							  						  .sub( yAxis.scalarMultiply(y * vertical / (height - 1.0)) );

					Ray ray = Ray.fromPoints(eye, screenPoint);

					tracer(scene, ray, rgb);

					r[offset] = rgb[0] > 255 ? 255 : rgb[0];
					g[offset] = rgb[1] > 255 ? 255 : rgb[1];
					b[offset] = rgb[2] > 255 ? 255 : rgb[2];

					offset++;
				}
			}
		}
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
				
				Scene scene = RayTracerViewer.createPredefinedScene2();
				
                ForkJoinPool pool = new ForkJoinPool();
                
                pool.invoke(new RayCasterAction(0, height - 1, red, green, blue, width, height,
                								horizontal, vertical, scene, eye, xAxis, yAxis, screenCorner));

                pool.shutdown();
				
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
