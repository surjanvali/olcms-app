package in.apcfss.struts.eCourt.apis;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionSample {
	
	//private static final String key = "PxaCV2s2kzKI";
	// private static final String initVector = "encryptionIntVec";
	//private static final String initVector = "abcdef987654";

	public static String encrypt(String value, String key, String initVector) {
		try {
			
			Random rand = new SecureRandom();
	    	byte[] bytes = new byte[16];
	    	rand.nextBytes(initVector.getBytes("UTF-8"));
			
			IvParameterSpec iv = new IvParameterSpec(bytes);
			
	        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

	        KeySpec spec = new PBEKeySpec(key.toCharArray(), bytes, 65536, 128); // AES-256

	        byte[] keyBytes = f.generateSecret(spec).getEncoded();

			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	public static String decrypt(String encrypted, String key, String initVector) {
		try {
			
			Random rand = new SecureRandom();
	    	byte[] bytes = new byte[16];
	    	rand.nextBytes(initVector.getBytes("UTF-8"));
			
			IvParameterSpec iv = new IvParameterSpec(bytes);
			
	        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

	        KeySpec spec = new PBEKeySpec(key.toCharArray(), bytes, 65536, 128); // AES-256

	        byte[] keyBytes = f.generateSecret(spec).getEncoded();

			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
			

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {
		// String originalString = "password";
		String originalString = "cino=MHAU010092312018";
		String key = "PxaCV2s2kzKI";
		String initVector = "abcdef987654";

		System.out.println("Original String to encrypt - " + originalString);
		String encryptedString = encrypt(originalString, key, initVector);
		System.out.println("Encrypted String - " + encryptedString);
		String decryptedString = decrypt(encryptedString, key, initVector);
		System.out.println("After decryption - " + decryptedString);
	}
	
	
	/*
	 * 
	// ORIGINAL CODE 
	public static void main(String[] args) {
		// String originalString = "password";
		String originalString = "cino=MHAU010092312018";
		System.out.println("Original String to encrypt - " + originalString);
		String encryptedString = encrypt(originalString);
		System.out.println("Encrypted String - " + encryptedString);
		String decryptedString = decrypt(encryptedString);
		System.out.println("After decryption - " + decryptedString);
	}
	
	private static final String key = "aesEncryptionKey";
	private static final String initVector = "encryptionIntVec";
	 
	public static String encrypt(String value) {
	    try {
	        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	 
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	 
	        byte[] encrypted = cipher.doFinal(value.getBytes());
	        return Base64.encodeBase64String(encrypted);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
	
	public static String decrypt(String encrypted) {
	    try {
	        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	 
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
	        byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
	 
	        return new String(original);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	 
	    return null;
	}
*/	
}