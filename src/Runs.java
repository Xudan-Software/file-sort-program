import java.io.IOException;
import java.util.LinkedList;

public class Runs {
    LinkedList<Run> runList = new LinkedList<>();
    private Run lastRun = null;


    public void addRun(Run run) {
        runList.add(run);
        lastRun = run;
    }


    public void addEndIndexToMostRecentRun(long endIndex) {
        long startIdx = lastRun.getInitalStartIndex();
        long length = endIndex - startIdx;
        lastRun.addLength(length);
    }


    public boolean isEmpty() {
        return runList.isEmpty();
    }


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
        for (Run run: runList) {
            run.initializeRunBufferOfSize(size);
        }
    }

    public int numberOfRuns() {
        return runList.size();
    }
}
