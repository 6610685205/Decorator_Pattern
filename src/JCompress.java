import factory.ProcessorFactory;
import processor.Processor;

import java.nio.file.*;

public class JCompress {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String inputFile   = args[0];
        String compression = null;
        String encryption  = null;
        String checksum    = null;

        for (int i = 1; i < args.length; i++) {
            switch (args[i].toUpperCase()) {
                case "-ZIP"    -> compression = "ZIP";
                case "-GZIP"   -> compression = "GZIP";
                case "-DES"    -> encryption  = "DES";
                case "-AES"    -> encryption  = "AES";
                case "-MD5"    -> checksum    = "MD5";
                case "-SHA256" -> checksum    = "SHA256";
                default        -> System.out.println("Unknown flag: " + args[i]);
            }
        }

        // Read input
        byte[] data       = Files.readAllBytes(Paths.get(inputFile));
        String outputFile = inputFile + ".jc";

        System.out.println("=== JCompress ===");
        System.out.println("Input      : " + inputFile + " (" + data.length + " bytes)");
        System.out.println("Compression: " + (compression != null ? compression : "-"));
        System.out.println("Encryption : " + (encryption  != null ? encryption  : "-"));
        System.out.println("Checksum   : " + (checksum    != null ? checksum    : "-"));
        System.out.println();
        System.out.println("Processing chain:");

        // Factory builds the decorator chain
        Processor processor = ProcessorFactory.build(outputFile, compression, encryption, checksum);

        // Execute — triggers the entire decorator chain
        processor.execute(data);

        System.out.println();
        System.out.println("Output: " + outputFile);
    }

    private static void printUsage() {
        System.out.println("Usage: jcompress <file> [-zip|-gzip] [-DES|-AES] [-MD5|-SHA256]");
        System.out.println("Example: jcompress a.txt -zip -DES -MD5");
    }
}
