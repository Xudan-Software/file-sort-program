import java.io.IOException;
import java.util.LinkedList;

public class Runs {
    LinkedList<Run> runList = new LinkedList<>();
    Run lastRun = null;


    public void addRun(Run run) {
        runList.add(run);
        lastRun = run;
    }


    public void addEndIndexToMostRecentRun(long endIndex) {
        long startIdx = lastRun.getInitalStartIndex();
        long length = startIdx - endIndex;
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
            if (run.isExhausted()) {
                // delete the run
                runList.remove(run);
            }
            if (minVal == null || run.peekNextVal().compareTo(minVal) < 0) {
                minVal = run.peekNextVal();
                nextRunToPop = run;
            }
        }
        return nextRunToPop.popNextVal();
    }
}
