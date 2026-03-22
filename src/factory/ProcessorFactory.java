package factory;

import processor.Processor;
import processor.FileWriteProcessor;
import processor.impl.*;

public class ProcessorFactory {

    public static Processor build(String outputPath,
                                  String compression,
                                  String encryption,
                                  String checksum) {

        // Start with ConcreteComponent (innermost)
        Processor chain = new FileWriteProcessor(outputPath);

        // Wrap with encryption decorator (if any)
        if (encryption != null) {
            chain = switch (encryption.toUpperCase()) {
                case "DES" -> new DesDecorator(chain);
                case "AES" -> new AesDecorator(chain);
                default    -> throw new IllegalArgumentException("Unknown encryption: " + encryption);
            };
        }

        // Wrap with compression decorator (if any)
        if (compression != null) {
            chain = switch (compression.toUpperCase()) {
                case "ZIP"  -> new ZipDecorator(chain);
                case "GZIP" -> new GzipDecorator(chain);
                default     -> throw new IllegalArgumentException("Unknown compression: " + compression);
            };
        }

        // Wrap with checksum decorator outermost (computes hash on original data)
        if (checksum != null) {
            chain = switch (checksum.toUpperCase()) {
                case "MD5"    -> new Md5Decorator(chain);
                case "SHA256" -> new Sha256Decorator(chain);
                default       -> throw new IllegalArgumentException("Unknown checksum: " + checksum);
            };
        }

        return chain;
    }
}
