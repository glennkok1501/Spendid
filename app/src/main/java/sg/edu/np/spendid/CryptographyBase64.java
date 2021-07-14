package sg.edu.np.spendid;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class CryptographyBase64 {
    public CryptographyBase64() {
    }

    public String bytesToB64(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    public byte[] b64ToBytes(String b64) {
        return Base64.decode(b64.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
    }
}