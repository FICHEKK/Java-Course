package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;

@SuppressWarnings("javadoc")
public class ElementsGetterArrayDemo {

	public static void main(String[] args) {
			Collection col = new ArrayIndexedCollection();
			col.add("Ivo");
			col.add("Ana");
			col.add("Jasna");
			ElementsGetter getter = col.createElementsGetter();
			System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
			System.out.println("Jedan element: " + getter.getNextElement());
			System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
			System.out.println("Jedan element: " + getter.getNextElement());
			System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
			System.out.println("Jedan element: " + getter.getNextElement());
			System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
			System.out.println("Jedan element: " + getter.getNextElement()); // Should throw exception
	}
}
