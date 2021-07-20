package sg.edu.np.spendid.Utils.Security;


import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class CryptographyRSA {

    public CryptographyRSA(){}

    //generate public key and private key
    public KeyPair GenerateKeyPair(){
        try{
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //convert bytes to public key
    public PublicKey setPublicKey(byte[] bytes) {
        try{
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //convert bytes to private key
    public PrivateKey setPrivateKey(byte[] bytes) {
        try{
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //encrypt with public key
    public byte[] Encrypt(byte[] secretKey, PublicKey key){
        try{
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedSecretKeyBytes = encryptCipher.doFinal(secretKey);
            return encryptedSecretKeyBytes;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //decrypt with private key
    public byte[] Decrypt(byte[] encryptedSecretKey, PrivateKey key){
        try{
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedSecretKeyBytes = decryptCipher.doFinal(encryptedSecretKey);
            return decryptedSecretKeyBytes;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}