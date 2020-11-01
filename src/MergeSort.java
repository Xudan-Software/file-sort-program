import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class MergeSort {
    private LinkedList<Long> runIndex;
    private RandomAccessFile runFile;
    private Long[] runIndexArray;



    public MergeSort(LinkedList<Long> runIndex, RandomAccessFile runFile) {
        this.runFile = runFile;
        this.runIndex = runIndex;
        runIndexArray = (Long[])runIndex.toArray();

    }


    public void initializeRunArray() throws IOException {

        Run[] runArray = new Run[runIndex.size()];
        int memorySize=(8192*4)/runIndexArray.length;
        for (int i = 0; i < runIndexArray.length - 1; i++) {
            runArray[i] = new Run(runIndexArray[i],
                runIndexArray[i + 1] - runIndexArray[i], runFile,memorySize);
        }
        runArray[runIndexArray.length - 1] =
            new Run(runIndexArray[runIndexArray.length - 1],
                runFile.length() - runIndexArray[runIndexArray.length - 1],
                runFile,memorySize);
    }
//        // inserting the runs into the run array
//        runArray[i] = new Run(runLinkedList.next(), this - last, runFile),
//    }

//    Run[] runArray = new Run[runIndex.length()];
//    for (int i=0; runIndex.next() != null; i++) {
//        // inserting the runs into the run array
//        runArray[i] = new Run(runLinkedList.next(), this - last, runFile),
//    }
//    while(there are runs) {
//        // now lets iterate over the runs and put them in the heaps array
//        Record minRecord;
//        Run nextRunToTakeValFrom; // rename this later
//        for (Run run : runArray) {
//            if (minRecord == null ||(run.peekNextVal()!=null && run.peekNextVal().compareTo(minRecord) < 0)) {
//                minRecord = run.peekNextVal;
//                nextRunToTakeValFrom = run;
//            }
//        }
//        minRecord = nextRunToTakeValFrom.popNextVal();
//        multiwayOutputBuffer.put(minRecord);
//    }
    }
