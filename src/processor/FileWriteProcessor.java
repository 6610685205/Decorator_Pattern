package processor;

import java.nio.file.*;

/**
 * Concrete Component (Decorator Pattern)
 * 
 * Analogous to "SalesTicket" in the CN332 slides.
 * This is the base object that gets decorated — it simply writes
 * the (possibly already transformed) bytes to disk.
 */
public class FileWriteProcessor extends Processor {

    public FileWriteProcessor(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void execute(byte[] data) throws Exception {
        Files.write(Paths.get(outputPath), data);
        System.out.println("  [FileWriter] wrote " + data.length + " bytes → " + outputPath);
    }
}
