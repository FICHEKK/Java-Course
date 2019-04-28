package searching.slagalica;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulated the puzzle configuration.
 *
 * @author Filip Nemec
 */
public class KonfiguracijaSlagalice {
	
	/** Required puzzle configuration length. */
	private static final int LENGTH = 9;
	
	/** Array that stores the puzzle state. */
	private int[] polje;
	
	/** The index of the empty space spot. */
	private int spaceIndex;
	
	/**
	 * Constructs a new puzzle configuration.
	 *
	 * @param polje the array that stores the puzzle state
	 */
	public KonfiguracijaSlagalice(int[] polje) {
		validateArrayLength(polje);
		validateDigits(polje);
		
		this.polje = polje;
		this.spaceIndex = findSpaceIndex();
	}
	
	//----------------------------------------------------------------
	//							GETTERS
	//----------------------------------------------------------------
	
	/**
	 * Returns the copy of an internal state array.
	 *
	 * @return the copy of an internal state array
	 */
	public int[] getPolje() {
		return Arrays.copyOf(polje, polje.length);
	}
	
	/**
	 * Returns the index of the empty space spot.
	 *
	 * @return the index of the empty space spot
	 */
	public int indexOfSpace() {
		return spaceIndex;
	}
	
	//----------------------------------------------------------------
	//					toString, hashCode, equals
	//----------------------------------------------------------------
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		for(int i = 0; ; i++) {
			for(int j = 0; ; j++) {
				if(polje[j + i*3] == 0) {
					sb.append("*");
				} else {
					sb.append(polje[j + i*3]);
				}
				
				if(j == 2) break;
				
				sb.append(' ');
			}
			
			if(i == 2) break;

			sb.append(System.lineSeparator());
		}
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(polje);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KonfiguracijaSlagalice))
			return false;
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(polje, other.polje);
	}
	
	//----------------------------------------------------------------
	//						HELPER METHODS
	//----------------------------------------------------------------
	
	/**
	 * Helper method that is being called only once upon
	 * the object construction. It finds and returns the
	 * index of the empty space spot.
	 *
	 * @return the empty space spot index
	 */
	private int findSpaceIndex() {
		for(int i = 0; i < LENGTH; i++) {
			if(polje[i] == 0) {
				return i;
			}
		}
		throw new IllegalArgumentException("Empty space slot was not found.");
	}
	
	/**
	 * Validates that the given array length is of correct size.
	 * This helper method is used only upon object construction.
	 *
	 * @param polje the array to be checked
	 */
	private static void validateArrayLength(int[] polje) {
		if(polje.length != LENGTH) {
			String msg = "Given configuration should be of size " + LENGTH + ". " +
						 "Was: " + polje.length;
			throw new IllegalArgumentException(msg);
		}
	}
	
	/**
	 * Validates that the given array digits are of correct format.
	 * This helper method is used only upon object construction.
	 *
	 * @param polje the array that stores the digits that will be checked
	 */
	private static void validateDigits(int[] polje) {
		Integer[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8};
		Set<Integer> requiredDigits = new HashSet<Integer>(Arrays.asList(digits));
		
		for(int i = 0; i < polje.length; i++) {
			requiredDigits.remove(polje[i]);
		}
		
		if(!requiredDigits.isEmpty()) {
			throw new IllegalArgumentException("These digits are missing: " + requiredDigits);
		}
	}
}
