package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple program that demonstrated the usage
 * of Java crypto classes and methods.
 *
 * @author Filip Nemec
 */
public class Crypto {
	
	/** Defines how big the reading block is. */
	private static final int BLOCK_SIZE = 4096;
	
	/**
	 * Program starts from here
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 2 || args.length > 3) {
			printValidCommands();
			return;
		}
		
		String command = args[0];
		
		if(command.equals("checksha")) {
			String filename = args[1];
			processShaDigest(filename);
			
		} else if(command.equals("encrypt")) {
			processCrypt(args[1], args[2], true);
			
		} else if(command.equals("decrypt")) {
			processCrypt(args[1], args[2], false);
			
		} else {
			System.err.println("'" + command + "' is an invalid command.");
			printValidCommands();
			return;
		}
	}
	
	/**
	 * Processes the encryption/decryption request.
	 *
	 * @param source the source
	 * @param destination the destination
	 * @param isEncrypting {@code true} for encrypting mode, {@code false} for decrypting
	 */
	private static void processCrypt(String source, String destination, boolean isEncrypting) {
		Scanner scanner = new Scanner(System.in);
		
		String keyText = getInput(scanner, "Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
		String ivText = getInput(scanner, "Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
		
		Path src  = Paths.get("src/main/resources/" + source);
		Path dest = Paths.get("src/main/resources/" + destination);
		
		if(isEncrypting) {
			encrypt(src, dest, keyText, ivText);
		} else {
			decrypt(src, dest, keyText, ivText);
		}
	}
	
	/**
	 * Processes the SHA digest for the given file.
	 *
	 * @param filename name of the file
	 */
	private static void processShaDigest(String filename) {
		try(Scanner scanner = new Scanner(System.in)) {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			
			String expected = getInput(scanner, "Please provide expected sha-256 digest for " + filename + ":\n> ");
			String digest = generateDigest(Paths.get("src/main/resources/" + filename), messageDigest);
			
			System.out.println("Expected digest : " + expected);
			System.out.println("Generated digest: " + digest);
			System.out.println();
			
			if(digest.equals(expected)) {
				System.out.println("Digesting completed. Digest of " + filename + " matches expected digest.");
			} else {
				System.out.println("Digesting completed. Digest of hw06test.bin does not match the expected digest. Digest\r\n" + 
						"was: 2e7b3a91235ad72cb7e7f6a721f077faacfeafdea8f3785627a5245bea112598");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Generates the digest (hash) for the given file.
	 *
	 * @param path file path
	 * @param messageDigest the message digest
	 * @return the digest (hash)
	 */
	private static String generateDigest(Path path, MessageDigest messageDigest) {
		String digest = null;
		
		try(BufferedInputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
			while(true) {
				byte[] data = new byte[BLOCK_SIZE];
				int r = is.read(data);
				
				if(r <= 0) break;
				
				if(r < BLOCK_SIZE) {
					messageDigest.update(data, 0, r);
					break;
				}
				
				messageDigest.update(data);
			}
			
			digest = Util.byteToHex(messageDigest.digest(is.readAllBytes()));
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return digest;
	}
	
	/**
	 * Encrypts the given source and stored it to the given destination.
	 *
	 * @param source the source
	 * @param destination the destination
	 * @param key the key
	 * @param initVector the initialization vector
	 */
	private static void encrypt(Path source, Path destination, String key, String initVector) {
		boolean wasSuccess = crypt(source, destination, true, key, initVector);
		
		String srcName = source.toFile().getName();
		String destName = destination.toFile().getName();
		if(wasSuccess) {
			System.out.println("Encryption completed. Generated file " + destName + " based on file " + srcName + ".");
		} else {
			System.out.println("Encrypting did not succeed.");
		}
	}
	
	/**
	 * Decrypts the given source and stored it to the given destination.
	 *
	 * @param source the source
	 * @param destination the destination
	 * @param key the key
	 * @param initVector the initialization vector
	 */
	private static void decrypt(Path source, Path destination, String key, String initVector) {
		boolean wasSuccess = crypt(source, destination, false, key, initVector);
		
		String srcName = source.toFile().getName();
		String destName = destination.toFile().getName();
		if(wasSuccess) {
			System.out.println("Decryption completed. Generated file " + destName + " based on file " + srcName + ".");
		} else {
			System.out.println("Decrypting did not succeed.");
		}
	}
	
	/**
	 * Performs the crypting operation: encrypting or decrypting.
	 *
	 * @param source the source
	 * @param destination the destination
	 * @param encrypting if passed {@code true}, source will get encrypted and stored to destination.
	 * 					 If passed {@code false}, source will get decrypted and stored to destination.
	 * @param key the key
	 * @param initVector the initialization vector
	 * @return {@code true} if crypting was successful, otherwise {@code false}
	 */
	private static boolean crypt(Path source, Path destination, boolean encrypting, String key, String initVector) {
		final int blockSize = 4096;
		
		try {
			if(!destination.toFile().createNewFile()) return false;
		} catch (IOException e) {
			return false;
		}
		
		try(BufferedInputStream is = new BufferedInputStream(Files.newInputStream(source)); 
			BufferedOutputStream os = new BufferedOutputStream(Files.newOutputStream(destination))) {
			
			// Creating a cipher.
			SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(key), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(initVector));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypting ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
			
			while(true) {
				byte[] data = new byte[blockSize];
				int r = is.read(data);
				
				if(r <= 0) break;
				
				if(r < blockSize) {
					os.write(cipher.update(data, 0, r));
					break;
				}
				
				os.write(cipher.update(data));
			}
			
			os.write(cipher.doFinal());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return true;
	}
	
	//--------------------------------------------------------------
	//						HELPING METHODS
	//--------------------------------------------------------------
	/**
	 * Returns the used input.
	 *
	 * @param scanner scanner that gets the input
	 * @param message the message shown to the user
	 * @return user input
	 */
	private static String getInput(Scanner scanner, String message) {
		System.out.print(message);
		return scanner.next();
	}
	
	/**
	 * Prints all the valid commands.
	 */
	private static void printValidCommands() {
		System.err.println("Valid commands are:");
		System.err.println("a) checksha <filename>");
		System.err.println("b) encrypt <source file> <destination file>");
		System.err.println("b) decrypt <source file> <destination file>");
	}
}
