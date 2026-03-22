package processor;

public abstract class Processor {
    protected String outputPath;

    public abstract void execute(byte[] data) throws Exception;
}
