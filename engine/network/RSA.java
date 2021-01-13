package network;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Provides helper methods permitting RSA encryption.
 */
public class RSA {
	
	/**
	 * String representing the RSA algorithm.
	 */
	private static final String RSA = "RSA";

	/**
	 * Generates a key pair (public and private key) using the RSA algorithm.
	 * @param keySize The size of the keys in bytes.
	 * @return {@link KeyPair} The generated key pair.
	 */
	public static KeyPair generateKeyPair(int keySize) {
		
		try {
			
			// RSA key generator.
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(RSA);
			
			// Init the keygen with the specified size.
			keygen.initialize(keySize);
			
			// Generate the key pair.
			return keygen.generateKeyPair();
			
		} catch (NoSuchAlgorithmException e) {
			
			// Return a null reference in case of an exception.
			return null;
		
		}
		
	}
	
	/**
	 * Encrypts a byte array of data using the provided public key.
	 * @param key The public key to use for encryption.
	 * @param data The data to encrypt.
	 * @return [<b>byte[]</b>] The encrypted data.
	 */
	public static byte[] encrypt(PublicKey key, byte[] data) {
		
		try {
			
			// Cipher instance.
			Cipher cipher = Cipher.getInstance(RSA);
			
			// Initialize the cipher in decrypt mode.
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			// Decrypt the data.
			return cipher.doFinal(data);
			
		}catch(Exception e) {
			
			// Return a null reference in case of an exception
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	/**
	 * Decrypts a byte array of data using the provided private key.
	 * @param key The private key to use for decryption.
	 * @param data The data to decrypt.
	 * @return [<b>byte[]</b>] The decrypted data.
	 */
	public static byte[] decrypt(PrivateKey key, byte[] data) {
		
		try {
			
			// Cipher instance.
			Cipher cipher = Cipher.getInstance(RSA);
			
			// Initialize the cipher in decrypt mode.
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			// Decrypt the data.
			return cipher.doFinal(data);
			
		}catch(Exception e) {
			
			// Return a null reference in case of an exception.
			return null;
		
		}
		
	}
	
	/**
	 * Creates an RSA public key from its encoded byte array format.
	 * @return {@link PublicKey} The public key.
	 */
	public static PublicKey toPublicKey(byte[] bytes) {
		try {
			return KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(bytes));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Creates an RSA private key from its encoded byte array format.
	 * @return {@link PrivateKey} The public key.
	 */
	public static PrivateKey toPrivateKey(byte[] bytes) {
		try {
			return KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(bytes));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Exports a public key into a binary file.
	 * @param key The {@link PublicKey} to export.
	 * @param fileName The name of the file in which the {@link PublicKey} is to be saved.
	 * @return <b>boolean</b> Whether or not the export has succeeded.
	 */
	public static boolean exportToFile(PublicKey key, String fileName) {
		
		try {
			
			// Create output stream
			FileOutputStream out = new FileOutputStream(fileName);
			
			// Write to file
			out.write(key.getEncoded());
			out.flush();
			out.close();
			
			// Success
			return true;
			
		} catch (IOException e) {
			
			// Failure
			return false;
			
		}
		
	}
	
	/**
	 * Exports a private key into a binary file.
	 * @param key The {@link PrivateKey} to export.
	 * @param fileName The name of the file in which the {@link PrivateKey} is to be saved.
	 * @return <b>boolean</b> Whether or not the export has succeeded.
	 */
	public static boolean exportToFile(PrivateKey key, String fileName) {
		
		try {
			
			// Create output stream
			FileOutputStream out = new FileOutputStream(fileName);
			
			// Write to file
			out.write(key.getEncoded());
			out.flush();
			out.close();
			
			// Success
			return true;
			
		} catch (IOException e) {
			
			// Failure
			return false;
			
		}
		
	}
	
	/**
	 * Exports a private key into a binary file.
	 * @param keypair The {@link KeyPair} to export.
	 * @param fileName The name of the files in which the keys to be saved (file extensions are .public and .private).
	 * @return <b>boolean</b> Whether or not the export has succeeded.
	 */
	public static boolean exportToFile(KeyPair keypair, String fileName) {
		
		try {
			
			// Create output streams
			FileOutputStream outPublic = new FileOutputStream(fileName + ".public");
			FileOutputStream outPrivate = new FileOutputStream(fileName + ".private");
			
			// Write to files
			outPublic.write(keypair.getPublic().getEncoded());
			outPrivate.write(keypair.getPrivate().getEncoded());
			
			// Close streams
			outPublic.flush();
			outPublic.close();
			
			outPrivate.flush();
			outPrivate.close();
			
			// Success
			return true;
			
		} catch (IOException e) {
			
			// Failure
			return false;
			
		}
		
	}
	
	/**
	 * Imports a public key from a binary file.
	 * @param fileName The name of the file to import.
	 * @return {@link PublicKey} The resulting {@link PublicKey}.
	 */
	public static PublicKey importPublicKeyFromFile(String fileName) {
		
		try {
			
			// Create input stream
			FileInputStream in = new FileInputStream(fileName);
			
			// Read file and parse into key
			PublicKey result = toPublicKey(in.readAllBytes());
			
			// Close the stream
			in.close();
			
			// Return the resulting key
			return result;
			
		} catch (Exception e) {
			
			// Return a null reference in case of an exception.
			return null;
			
		}
		
	}
	
	/**
	 * Imports a private key from a binary file.
	 * @param fileName The name of the file to import.
	 * @return {@link PrivateKey} The resulting {@link PrivateKey}.
	 */
	public static PrivateKey importPrivateKeyFromFile(String fileName) {
		
		try {
			
			// Create input stream
			FileInputStream in = new FileInputStream(fileName);
			
			// Read file and parse into key
			PrivateKey result = toPrivateKey(in.readAllBytes());
			
			// Close the stream
			in.close();
			
			// Return the resulting key
			return result;
			
		} catch (Exception e) {
			
			// Return a null reference in case of an exception.
			return null;
			
		}
		
	}
	
	/**
	 * Imports a key pair from a set of binary files.
	 * @param fileName The name of the files to import (file extensions are .public and .private).
	 * @return {@link KeyPair} The resulting {@link KeyPair}.
	 */
	public static KeyPair importKeyPairFromFiles(String fileName) {
		
		try {
			
			// Create input streams
			FileInputStream inPublic = new FileInputStream(fileName + ".public");
			FileInputStream inPrivate = new FileInputStream(fileName + ".private");
			
			// Read files and parse into keys
			PublicKey publicKey = toPublicKey(inPublic.readAllBytes());
			PrivateKey privateKey = toPrivateKey(inPrivate.readAllBytes());
			
			// Close the streams
			inPublic.close();
			inPrivate.close();
			
			// Return the resulting key
			return new KeyPair(publicKey, privateKey);
			
		} catch (Exception e) {
			
			// Return a null reference in case of an exception.
			return null;
			
		}
		
	}
	
	
}
