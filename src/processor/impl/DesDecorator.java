package processor.impl;

import processor.Processor;
import processor.ProcessorDecorator;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;

public class DesDecorator extends ProcessorDecorator {

    private static final byte[] DES_KEY = "JavaKey!".getBytes(); // 8 bytes

    public DesDecorator(Processor trailer) {
        super(trailer);
    }

    @Override
    public void execute(byte[] data) throws Exception {
        SecretKey key    = new SecretKeySpec(DES_KEY, "DES");
        Cipher cipher    = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        // CipherOutputStream is itself a Decorator over OutputStream
        try (CipherOutputStream cipherStream = new CipherOutputStream(buffer, cipher)) {
            cipherStream.write(data);
        }
        byte[] encrypted = buffer.toByteArray();
        System.out.printf("  [DES]    %d B → %d B (encrypted)%n", data.length, encrypted.length);
        callTrailer(encrypted);
    }
}
