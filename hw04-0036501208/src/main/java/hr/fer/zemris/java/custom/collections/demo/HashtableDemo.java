package hr.fer.zemris.java.custom.collections.demo;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

@SuppressWarnings("javadoc")
public class HashtableDemo {

	public static void main(String[] args) {
		
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(256);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		// query collection:
		Integer kristinaGrade = examMarks.get("Kristina");
		System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
		// What is collection's size? Must be four!
		System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4
		System.out.println();
		
		//------------------------------------------------------------------------------
		//								iterator demo
		//------------------------------------------------------------------------------
		
		for (var pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}
		
		System.out.println();
		
		//------------------------------------------------------------------------------
		//							kartezijev produkt demo
		//------------------------------------------------------------------------------
		
		for(SimpleHashtable.TableEntry<String,Integer> pair1 : examMarks) {
			for(SimpleHashtable.TableEntry<String,Integer> pair2 : examMarks) {
				System.out.printf(
						"(%s => %d) - (%s => %d)%n",
						pair1.getKey(), pair1.getValue(),
						pair2.getKey(), pair2.getValue()
				);
			}
		}
		
		System.out.println();
		
		//------------------------------------------------------------------------------
		//							iterator.remove demo
		//------------------------------------------------------------------------------
		
		System.out.println("Prije brisanja \"Ivana\": ");
		examMarks.printHashtableState();
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			//System.out.println(pair);
			if (pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}
		
		System.out.println("Nakon brisanja \"Ivana\": ");
		examMarks.printHashtableState();
		System.out.println();
		
		//------------------------------------------------------------------------------
		//							iterator.remove exception demo
		//------------------------------------------------------------------------------
		try {
			Iterator<SimpleHashtable.TableEntry<String, Integer>> iter2 = examMarks.iterator();
			while (iter2.hasNext()) {
				SimpleHashtable.TableEntry<String, Integer> pair = iter2.next();
				if (pair.getKey().equals("Ante")) {
					iter2.remove();
					iter2.remove();
				}
			}
		} catch(IllegalStateException ise) {
			System.out.println("Called remove() twice in a row. Don't do that!");
		}
		
		System.out.println();
		
		//------------------------------------------------------------------------------
		//							final demo
		//------------------------------------------------------------------------------
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter3 = examMarks.iterator();
		while (iter3.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter3.next();
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter3.remove();
		}
		System.out.printf("Veličina: %d%n", examMarks.size());
		System.out.println();
	}
}
