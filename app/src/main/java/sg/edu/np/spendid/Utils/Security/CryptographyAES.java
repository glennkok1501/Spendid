package sg.edu.np.spendid.Utils.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptographyAES {
    private SecretKey secretKey;
    private KeyGenerator keyGenerator;

    public CryptographyAES() { }

    //generate secret key
    public SecretKey GenerateKey(){
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            secretKey = keyGenerator.generateKey();
            return secretKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //encrypt message with secret key
    public byte[] Encrypt(byte[] plaintext, SecretKey key){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] cipherText = cipher.doFinal(plaintext);
            return cipherText;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //decrypt message with secret key
    public byte[] Decrypt(byte[] cipherText, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //convert bytes to secret key
    public SecretKey setSecretKey(byte[] bytes){
        return new SecretKeySpec(bytes, "AES");
    }


}