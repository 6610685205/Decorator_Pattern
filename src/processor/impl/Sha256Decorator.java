package processor.impl;

import processor.Processor;
import processor.ProcessorDecorator;

import java.security.MessageDigest;
import java.util.HexFormat;

public class Sha256Decorator extends ProcessorDecorator {

    public Sha256Decorator(Processor trailer) {
        super(trailer);
    }

    @Override
    public void execute(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String hash      = HexFormat.of().formatHex(md.digest(data));
        System.out.println("  [SHA256] " + hash);
        callTrailer(data);
    }
}
