package processor.impl;

import processor.Processor;
import processor.ProcessorDecorator;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;

/**
 * AES Encryption Decorator
 * Uses AES-128 with CipherOutputStream.
 */
public class AesDecorator extends ProcessorDecorator {

    private static final byte[] AES_KEY = "JavaAESKey123456".getBytes(); // 16 bytes

    public AesDecorator(Processor trailer) {
        super(trailer);
    }

    @Override
    public void execute(byte[] data) throws Exception {
        SecretKey key = new SecretKeySpec(AES_KEY, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (CipherOutputStream cipherStream = new CipherOutputStream(buffer, cipher)) {
            cipherStream.write(data);
        }
        byte[] encrypted = buffer.toByteArray();
        System.out.printf("  [AES]    %d B → %d B (encrypted)%n", data.length, encrypted.length);
        callTrailer(encrypted);
    }
}
