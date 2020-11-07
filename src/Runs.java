import java.io.IOException;
import java.util.LinkedList;

/**
 * a runs object contains all runs
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class Runs {
    private final LinkedList<Run> runList = new LinkedList<>();
    private Run lastRun = null;


    /**
     * add a run into runs object
     *
     * @param run a run to be added
     */
    public void addRun(Run run) {
        runList.add(run);
        lastRun = run;
    }


    /**
     * add end index to the most recent run to close this run
     *
     * @param endIndex the end index to be added
     */
    public void addEndIndexToMostRecentRun(long endIndex) {
        long startIdx = lastRun.getInitialStartIndex();
        long length = endIndex - startIdx;
        lastRun.addLength(length);
    }


    /**
     * if the runs object is empty
     *
     * @return return true if it is empty, else return false
     */
    public boolean isEmpty() {
        return runList.isEmpty();
    }


    /**
     * get next minimum record from the runs object
     *
     * @return the next minimum record for multiway merge
     * @throws IOException if the file not exist
     */
    public Record getNextMinRecord() throws IOException {
        Record minVal = null;
        Run nextRunToPop = null;
        if (runList.isEmpty()) {
            throw new IllegalStateException();
        }
        for (Run run : runList) {

            if (minVal == null || run.peekNextVal().compareTo(minVal) < 0) {
                minVal = run.peekNextVal();
                nextRunToPop = run;
            }
        }
        nextRunToPop.popNextVal();
        if (nextRunToPop.isExhausted()) {
            // delete the run
            runList.remove(nextRunToPop);
        }
        return minVal;
    }


    /**
     * Initialized the Run objects for MergeSort. This allocates a specific
     * size in memory to each Run objects buffer.
     *
     * @param size The size of each runs buffer in bytes.
     */
    public void initialize(int size) throws IOException {
        for (Run run : runList) {
            run.initializeRunBufferOfSize(size);
        }
    }


    /**
     * return how many runs in the runs object
     *
     * @return return size of the linkedlist of Runs object
     */
    public int numberOfRuns() {
        return runList.size();
    }


    /**
     * getter of the runList
     *
     * @return the LinkedList that holds each run object
     */
    public LinkedList<Run> getRunList() {
        return this.runList;
    }
}
