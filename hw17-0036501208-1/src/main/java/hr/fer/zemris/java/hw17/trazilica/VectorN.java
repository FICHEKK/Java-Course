package hr.fer.zemris.java.hw17.trazilica;

import java.util.Vector;

/**
 * Helper class used by the search engine. It offers
 * basic n-dimensional vector calculations.
 *
 * @author Filip Nemec
 */
final class VectorN {
	
	/* Don't allow instances of this class. */
	private VectorN() {}

	/**
	 * Returns the norm of the given n-dimensional vector.
	 *
	 * @param vector the vector
	 * @return the norm of the given n-dimensional vector
	 */
	public static final double norm(Vector<Double> vector) {
		double underRoot = 0;
		
		for(int i = 0; i < vector.size(); i++) {
			underRoot += vector.get(i) * vector.get(i);
		}
		
		return Math.sqrt(underRoot);
	}
	
	/**
	 * Calculates the dot product of n-dimensional vectors
	 * {@code v1} and {@code v2}.
	 *
	 * @param v1 the first vector
	 * @param v2 the second vector
	 * @return the dot product of the given n-dimensional vectors
	 * @throws IllegalArgumentException if vectors are not of the same dimensions
	 * 									or if the dimension is not at least 1.
	 */
	public static final double dotProduct(Vector<Double> v1, Vector<Double> v2) {
		if(v1.size() != v2.size())
			throw new IllegalArgumentException("Vectors must be of equal dimensions.");
		
		if(v1.size() < 1)
			throw new IllegalArgumentException("Dimension should be at least 1.");
		
		double dotProduct = 0;
		
		for(int i = 0; i < v1.size(); i++) {
			dotProduct += v1.get(i) * v2.get(i);
		}
		
		return dotProduct;
	}
	
	/**
	 * Returns the similarity coefficient of the given documents.
	 *
	 * @param d1 the vector representing the first document
	 * @param d2 the vector representing the second document
	 * @return the similarity coefficient of the given documents
	 */
	public static final double similarity(Vector<Double> d1, Vector<Double> d2) {
		return dotProduct(d1, d2) / (norm(d1) * norm(d2));
	}
}
