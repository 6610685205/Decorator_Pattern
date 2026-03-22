package processor;

/**
 * Abstract Component class (Decorator Pattern)
 * 
 * Analogous to "Component" in the CN332 Decorator Pattern slides.
 * Defines the common operation that both ConcreteComponent 
 * and Decorator must implement.
 */
public abstract class Processor {
    protected String outputPath;

    public abstract void execute(byte[] data) throws Exception;
}
