package processor;

/**
 * Abstract Decorator (Decorator Pattern)
 * 
 * Analogous to "TicketDecorator" in the CN332 slides.
 * 
 * Key design:
 *   - Extends Processor (same parent as ConcreteComponent)
 *   - Contains a reference to another Processor ("myTrailer")
 *   - Provides callTrailer() to delegate to the next in chain
 * 
 * This mirrors the slide code:
 *   abstract class TicketDecorator extends Component {
 *     private Component myTrailer;
 *     public void callTrailer() {
 *       if (myTrailer != null) myTrailer.prtTicket();
 *     }
 *   }
 */
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
