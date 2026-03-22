package processor;

public abstract class ProcessorDecorator extends Processor {

    // "myTrailer" — same naming as in the lecture slides
    protected Processor myTrailer;

    public ProcessorDecorator(Processor myTrailer) {
        this.myTrailer  = myTrailer;
        this.outputPath = myTrailer.outputPath;
    }

    /**
     * Passes the (transformed) data down to the next decorator or
     * the concrete component — same as callTrailer() in slides.
     */
    protected void callTrailer(byte[] data) throws Exception {
        if (myTrailer != null) {
            myTrailer.execute(data);
        }
    }
}
