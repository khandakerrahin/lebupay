/**
 * @formatter:off
 *
 */
package com.lebupay.common;
 
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Component;

/**
 * This Class is used for Encryption & Decryption.
 * @author Java Team
 *
 */
@Component
public class Encryption {

	/**
	 * Encodes a String in AES-128 with a given key
	 * @param password
	 * @param text
	 * @return String Base64 and AES encoded String
	 * @throws NoPassGivenException
	 * @throws NoTextGivenException
	 */
	public String encode(String password, String text)
			 throws NoPassGivenException, NoTextGivenException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
	    
	        SecretKeySpec skeySpec = getKey(password);
	        byte[] clearText = text.getBytes("UTF8");

	        //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
	        final byte[] iv = new byte[16];
	        Arrays.fill(iv, (byte) 0x00);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);	        
	        
	        // Cipher is not thread safe
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

	        String encrypedValue = new Base32().encodeAsString( cipher.doFinal(clearText));	        
	       
	        return encrypedValue;

	}

	/**
	 * Decodes a String using AES-128 and Base64
	 * @param password
	 * @param text
	 * @return desoded String
	 * @throws NoPassGivenException
	 * @throws NoTextGivenException
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws UnsupportedEncodingException 
	 */
	public String decode(String password, String text) throws Exception {   

	        SecretKey key = getKey(password);
	        /*if(text != null || text != ""){
	        	if(text.length() > 24)
	        		throw new Exception();
	        }*/
	        
	        //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
	        final byte[] iv = new byte[16];
	        Arrays.fill(iv, (byte) 0x00);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);	       
	        byte[] encrypedPwdBytes = new Base32().decode(text);
	        // cipher is not thread safe
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
	        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

	        String decrypedValue = new String(decrypedValueBytes);	        
	       
	        return decrypedValue;
	}

	/**
	 * Generates a SecretKeySpec for given password
	 * @param password
	 * @return SecretKeySpec
	 * @throws UnsupportedEncodingException
	 */
	public static SecretKeySpec getKey(String password)
	        throws UnsupportedEncodingException {


	    int keyLength = 128;
	    byte[] keyBytes = new byte[keyLength / 8];
	    // explicitly fill with zeros
	    Arrays.fill(keyBytes, (byte) 0x0);

	    // if password is shorter then key length, it will be zero-padded
	    // to key length
	    byte[] passwordBytes = password.getBytes("UTF-8");
	    int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
	            : keyBytes.length;
	    System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
	    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    return key;
	}

	@SuppressWarnings("serial")
	public class NoTextGivenException extends Exception {
	    public NoTextGivenException(String message) {
	        super(message);
	    }

	}

	@SuppressWarnings("serial")
	public class NoPassGivenException extends Exception {
	    public NoPassGivenException(String message) {
	        super(message);
	    }

	}
	
}
