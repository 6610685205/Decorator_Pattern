package processor.impl;

import processor.Processor;
import processor.ProcessorDecorator;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * ZIP Compression Decorator
 * Uses Java's DeflaterOutputStream (itself a Decorator over OutputStream).
 * Compresses data, then calls callTrailer() to continue the chain.
 */
public class ZipDecorator extends ProcessorDecorator {

    public ZipDecorator(Processor trailer) {
        super(trailer);
    }

    @Override
    public void execute(byte[] data) throws Exception {
        // Use Java stream (Decorator pattern built-in) to compress
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (DeflaterOutputStream zipStream = new DeflaterOutputStream(buffer,
                new Deflater(Deflater.BEST_COMPRESSION))) {
            zipStream.write(data);
        }
        byte[] compressed = buffer.toByteArray();
        System.out.printf("  [ZIP]    %d B → %d B%n", data.length, compressed.length);
        callTrailer(compressed);  // pass compressed bytes down the chain
    }
}
