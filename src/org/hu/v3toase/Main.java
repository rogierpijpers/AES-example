/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hu.v3toase;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Rogier
 */
public class Main {
    public static void main(String[] args){
        try{
            String key = "E1BB465D57CAE7ACDBBE8091F9CE83DF"; // hex string. Deze kan ook gegenereerd worden door het javax.crypto component.
            String plainText = "Ik zal spoedig versleuteld worden.";

            String encryptedText = AESEncryption.encrypt(plainText, key);
            System.out.println("Cipher:\n"+encryptedText);

            String decryptedText = AESEncryption.decrypt(encryptedText, key);
            System.out.println("\nDecrypted text:\n"+decryptedText);
            
        }catch(IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e){
            // handel exceptions nóóit zo af in productie code, maar hier mag het.
            e.printStackTrace();
        }
        
    }
}
