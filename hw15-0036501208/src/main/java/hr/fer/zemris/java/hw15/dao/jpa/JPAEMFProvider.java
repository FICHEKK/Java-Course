package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * The provider of the entity manager factory.
 *
 * @author Filip Nemec
 */
public class JPAEMFProvider {

	/** The entity manager factory. */
	private static EntityManagerFactory emf;
	
	/**
	 * Returns the entity manager factory.
	 *
	 * @return entity manager factory
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Sets the entity manager factory.
	 *
	 * @param emf the new entity manager factory
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}