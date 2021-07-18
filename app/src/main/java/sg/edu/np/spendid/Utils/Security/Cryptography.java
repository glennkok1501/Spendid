package sg.edu.np.spendid.Utils.Security;

import android.content.Context;
import android.content.SharedPreferences;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.SecretKey;

import static android.content.Context.MODE_PRIVATE;

public class Cryptography {
    private Context context;

    private final String PREF_NAME = "sharedPrefs";
    private final String PRIVATE_KEY = "privateKey";
    private final String PUBLIC_KEY = "publicKey";

    private CryptographyAES aes;
    private CryptographyRSA rsa;
    private CryptographySHA256 sha;
    private CryptographyBase64 b64;
    private SharedPreferences prefs;

    public Cryptography(Context context) {
        this.context = context;

        prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        aes = new CryptographyAES();
        rsa = new CryptographyRSA();
        sha = new CryptographySHA256();
        b64 = new CryptographyBase64();
    }

    public String Encrypt(String message, String PUBLIC_KEY) throws Exception {
        byte[] messageDigest = sha.digest(message); //get message digest
        SecretKey secretKey = aes.GenerateKey(); //generate random secret key
        byte[] encryptedDigest = aes.Encrypt(messageDigest, secretKey); //encrypt message digest
        String encodedDigest = b64.bytesToB64(encryptedDigest); //encoded message digest
        byte[] encodedPublicKey = b64.b64ToBytes(PUBLIC_KEY); //get public key in b64
        PublicKey publicKey = rsa.setPublicKey(encodedPublicKey); //convert b64 to public key
        byte[] encryptedText = aes.Encrypt(message.getBytes(StandardCharsets.UTF_8), secretKey); //encrypt message with secret key
        String encodedText = b64.bytesToB64(encryptedText); //encode encrypted message with b64
        byte[] encryptedKey = rsa.Encrypt(secretKey.getEncoded(), publicKey); //encrypt secret key with public key
        String encodedKey = b64.bytesToB64(encryptedKey); //encode encrypted secret key with b64
        String encryptedMessage = encodedKey+";"+encodedText+";"+encodedDigest; //append encoded messages
        return encryptedMessage;
    }

    public String Decrypt(String input) throws Exception{
        String[] encryptedMessage = input.split(";"); //separate secret key and message
        byte[] encodedPrivateKey = b64.b64ToBytes(prefs.getString(PRIVATE_KEY, null)); //get public key in b64
        PrivateKey privateKey = rsa.setPrivateKey(encodedPrivateKey); //convert b64 to private key
        byte[] decodedKey = b64.b64ToBytes(encryptedMessage[0]); //decode b64 secret key
        byte[] decryptedSecretKey = rsa.Decrypt(decodedKey, privateKey); //decrypt secret key with private key
        SecretKey secretKey = aes.setSecretKey(decryptedSecretKey); //convert secret key from bytes
        byte[] decodedMessage = b64.b64ToBytes(encryptedMessage[1]); //decode encrypted message from b64
        byte[] messageBytes = aes.Decrypt(decodedMessage, secretKey); //decrypt message with secret key
        String message = new String(messageBytes, StandardCharsets.UTF_8);
        byte[] decodedDigest = b64.b64ToBytes(encryptedMessage[2]); //get current message digest
        byte[] decryptedDigest = aes.Decrypt(decodedDigest, secretKey); //decrypt message digest
        //compare message digest
        if (Arrays.equals(decryptedDigest, sha.digest(message))){
            return message;
        }
        else{
            return null;
        }
    }

    public boolean checkKeyPair(){
        return (prefs.getString(PUBLIC_KEY, null) == null || prefs.getString(PRIVATE_KEY, null) == null);
    }

    public void newKeyPair() {
        KeyPair keyPair = new CryptographyRSA().GenerateKeyPair();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PUBLIC_KEY, b64.bytesToB64(keyPair.getPublic().getEncoded()));
        editor.putString(PRIVATE_KEY, b64.bytesToB64(keyPair.getPrivate().getEncoded()));
        editor.apply();
    }

    public void importKeyPair(String publicKey, String privateKey){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PUBLIC_KEY, publicKey);
        editor.putString(PRIVATE_KEY, privateKey);
        editor.apply();
    }
}