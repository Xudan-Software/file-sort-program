import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MergeSort {
    private final Runs runs;
    private final RandomAccessFile outputFile;


    public MergeSort(Runs runs, File outputFile) throws IOException {
        this.runs = runs;
        this.outputFile = new RandomAccessFile(outputFile, "rw");
        initializeRuns();
    }


    private void initializeRuns() throws IOException {
        int memorySize = (8192 * 8) / runs.numberOfRuns();
        int leftoverBytes = memorySize % 16;
        memorySize = memorySize - leftoverBytes;
        runs.initialize(memorySize);
    }


    public void sortRuns() throws IOException {
        RecordOutputBuffer outputBuffer =
            new RecordOutputBuffer(8192, outputFile);
        while (!runs.isEmpty()) {
            outputBuffer.insertRecord(runs.getNextMinRecord());
        }
        outputBuffer.writeRemainingContentsToFile();
    }
}
