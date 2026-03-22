package processor.impl;

import processor.Processor;
import processor.ProcessorDecorator;

import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * MD5 Checksum Decorator
 * Computes MD5 hash, prints it, then calls callTrailer() with original data.
 */
public class Md5Decorator extends ProcessorDecorator {

    public Md5Decorator(Processor trailer) {
        super(trailer);
    }

    @Override
    public void execute(byte[] data) throws Exception {
        MessageDigest md   = MessageDigest.getInstance("MD5");
        String hash        = HexFormat.of().formatHex(md.digest(data));
        System.out.println("  [MD5]    " + hash);
        callTrailer(data); // checksum doesn't transform data, just reports
    }
}
