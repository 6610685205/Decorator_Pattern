package processor.impl;

import processor.Processor;
import processor.ProcessorDecorator;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipDecorator extends ProcessorDecorator {

    public GzipDecorator(Processor trailer) {
        super(trailer);
    }

    @Override
    public void execute(byte[] data) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(buffer)) {
            gzipStream.write(data);
        }
        byte[] compressed = buffer.toByteArray();
        System.out.printf("  [GZIP]   %d B → %d B%n", data.length, compressed.length);
        callTrailer(compressed);
    }
}
