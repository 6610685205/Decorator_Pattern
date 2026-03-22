package processor;

import java.nio.file.*;

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
