import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class MergeSort {
    private Iterator<Long> runIndexIterator;
    private RandomAccessFile runFile;
    private RandomAccessFile outputFile;
    private Run[] runArray;


    public MergeSort(
        Iterator<Long> runIndexIterator,
        int numberOfRuns,
        RandomAccessFile runFile,
        RandomAccessFile outputFile) throws IOException {
        this.runIndexIterator = runIndexIterator;
        runArray = new Run[numberOfRuns];
        this.runFile = runFile;
        this.outputFile = outputFile;
        initializeRunArray();
    }


    private void initializeRunArray() throws IOException {
        int memorySize = (8192 * 4) / runArray.length;
        Long[] runIndexArray = new Long[runArray.length];
        for (int i = 0; i < runArray.length; i++) {
            runIndexArray[i] = runIndexIterator.next();
        }
        for (int i = 0; i < runArray.length - 1; i++) {
            runArray[i] = new Run(runIndexArray[i],
                runIndexArray[i + 1] - runIndexArray[i], runFile, memorySize);
        }
        runArray[runIndexArray.length - 1] =
            new Run(runIndexArray[runIndexArray.length - 1],
                runFile.length() - runIndexArray[runIndexArray.length - 1],
                runFile, memorySize);
    }


    public void sortRuns() throws IOException {
        RecordOutputBuffer outputBuffer =
            new RecordOutputBuffer((4 * 8192), outputFile);
        Record minRecord = null;
        Run nextRunToTakeValFrom = runArray[0]; // rename this later
        boolean isFinished = false;
        while (!isFinished) {
            for (int i = 0; i < runArray.length; i++) {
                isFinished = true;
                if (!runArray[i].isExhausted()) {
                    isFinished = false;
                }
                if (minRecord == null || (!runArray[i].isExhausted()
                    && runArray[i].peekNextVal().compareTo(minRecord) < 0)) {
                    minRecord = runArray[i].peekNextVal();
                    nextRunToTakeValFrom = runArray[i];
                }
            }
            if (!isFinished) {
                minRecord = nextRunToTakeValFrom.popNextVal();
                outputBuffer.insertRecord(minRecord);
            }
        }
        outputBuffer.writeRemainingContentsToFile();
    }
}
