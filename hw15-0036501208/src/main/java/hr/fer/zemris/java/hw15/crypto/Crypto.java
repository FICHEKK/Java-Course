package hr.fer.zemris.java.hw15.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple class that offers the password hashing.
 *
 * @author Filip Nemec
 */
public class Crypto {
	
	private static final String algorithm = "SHA-1";
	
	/**
	 * Hashes the given password {@code String} using the provided hashing
	 * algorithm.
	 *
	 * @param algorithm the hashing algorithm
	 * @param password  the password to be hashed
	 * @return the generated hash of the given password
	 * @throws NoSuchAlgorithmException if the provided algorithm does not exist
	 */
	public static String getPasswordHash(String password) {
		MessageDigest messageDigest = null;
		
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException ignorable) {
		}
		
		messageDigest.update(password.getBytes());

		return Util.byteToHex(messageDigest.digest());
	}
}
