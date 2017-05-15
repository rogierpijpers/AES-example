/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hu.v3toase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Rogier
 */
public class AESEncryption {
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    public static String encrypt(String plainText, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, IOException{
        // first convert the hex key to binary
        byte[] binaryKey = DatatypeConverter.parseHexBinary(key);
	SecretKeySpec skeySpec = new SecretKeySpec(binaryKey, "AES");
        
        // create an instance of the AES algorithm
	Cipher cipher = Cipher.getInstance(ALGORITHM);
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        
        // encrypt the plain text
	byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));
        
	// create initialisation vector. 
        // the IV ensures that the first block of data differs from the same block of encrypted text.
        byte[] iv = cipher.getIV();
	
        // write the encrypted text to a stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	outputStream.write(iv);
	outputStream.write(cipherText);
	byte[] finalData = outputStream.toByteArray();
        
        // so we can convert it back to a String
	String encodedFinalData = DatatypeConverter.printBase64Binary(finalData);
        
        // finally we can return the result
	return encodedFinalData;
    }
    
    public static String decrypt(String encodedInitialData, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        // first convert the hex key to binary (same as in the encryption method)
        byte[] raw = DatatypeConverter.parseHexBinary(key);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        
        // convert the string to a byte array
        byte[] encryptedData = DatatypeConverter.parseBase64Binary(encodedInitialData);
	
        // extract the iv and cipher text from the byte array
	byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);
	byte[] cipherText = Arrays.copyOfRange(encryptedData, 16, encryptedData.length);
        
        // create an instance of the algorithm again
	Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec iv_specs = new IvParameterSpec(iv);
	cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv_specs);
        
        // decrypt the data
	byte[] plainTextBytes = cipher.doFinal(cipherText);
	String plainText = new String(plainTextBytes);
        
	return plainText;
    }
}
