package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.Processor;
import hr.fer.zemris.java.custom.collections.Tester;

@SuppressWarnings("javadoc")
public class GenericsDemo {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		Collection<R2> kolekcija = new ArrayIndexedCollection<>();
		
//		kolekcija.add(new Object()); // da ili ne?
//		kolekcija.add(new R1()); // da ili ne?
		kolekcija.add(new R2()); // da ili ne?
		kolekcija.add(new R3()); // da ili ne?
		
		Processor<Object> p0 = new Processor<Object>() {
			@Override
			public void process(Object element) {
				System.out.println(element.hashCode());
			}
		};
		
		Processor<R1> p1 = new Processor<R1>() {
			@Override
			public void process(R1 element) {
				System.out.println(element.hashCode() + element.a);
			}
		};
		
		Processor<R2> p2 = new Processor<R2>() {
			@Override
			public void process(R2 element) {
				System.out.println(element.hashCode() + element.a + element.c);
			}
		};
		
		Processor<R3> p3 = new Processor<R3>() {
			@Override
			public void process(R3 element) {
				System.out.println(element.hashCode() + element.a + element.c + element.e);
			}
		};
		
		kolekcija.forEach(p0); // da ili ne?
		kolekcija.forEach(p1); // da ili ne?
		kolekcija.forEach(p2); // da ili ne?
//		kolekcija.forEach(p3); // da ili ne?
		
		Tester<Object> t0 = new Tester<Object>() {
			@Override
			public boolean test(Object element) {
				return element.hashCode() > 3;
			}
		};
		
		Tester<R1> t1 = new Tester<R1>() {
			@Override
			public boolean test(R1 element) {
				return element.hashCode() > 3 && element.a < 5;
			}
		};
		
		Tester<R2> t2 = new Tester<R2>() {
			@Override
			public boolean test(R2 element) {
				return element.hashCode() > 3 && element.a < 5 && element.c < 8;
			}
		};
		
		Tester<R3> t3 = new Tester<R3>() {
			@Override
			public boolean test(R3 element) {
				return element.hashCode() > 3 && element.a < 5 && element.c < 8 && element.d < 12;
			}
		};
	}

}
