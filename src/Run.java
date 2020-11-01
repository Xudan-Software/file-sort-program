import java.io.RandomAccessFile;
import java.util.Iterator;

public class Run {
    long startIndex;
    long runLength;
    RandomAccessFile runFile;
    // can use a linked list
    // can use an array
    // all that matters is the popNextVal returns its next smallest value
    // and that isExhausted returns true if the run has no values left

    public Run(long startIndex, long runLength, RandomAccessFile runfile) {
        this.startIndex = startIndex;
        this.runLength = runLength;
        this.runLength = runLength;
    }


    /**
     * Returns the next value in the run, but does not pop it and remove it
     * from the run.
     * @return
     */
    public Record peekNextVal() {

    }


    /**
     * Remove and return the next record from the run
     * @return
     */
    public Record popNextVal() {

    }

    public boolean isExhausted() {

    }

    /*
    Run[] runArray = new Run[runLinkedList.length()];
    for (int i=0; runLinkedList.next() != null; i++) {
        // inserting the runs into the run array
        runArray[i] = new Run(runLinkedList.next(), this - last, runFile),
    }
    while(there are runs) {
        // now lets iterate over the runs and put them in the heaps array
        Record minRecord;
        Run nextRunToTakeValFrom; // rename this later
        for (Run run : runArray) {
            if (minRecord == null || run.peekNextVal().compareTo(minRecord) < 0) {
                minRecord = run.peekNextVal;
                nextRunToTakeValFrom = run;
            }
        }
        minRecord = nextRunToTakeValFrom.popNextVal();
        multiwayOutputBuffer.put(minRecord);
    }
     */
}
