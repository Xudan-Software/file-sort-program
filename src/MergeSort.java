import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Class for sorting all the runs into a single, sorted run.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class MergeSort {
    private final Runs runs;
    private final RandomAccessFile outputFile;


    /**
     * Initialize a new MergeSort object.
     *
     * @param runs       the Runs object containing the data for each run.
     * @param outputFile the file to write the one sorted run to.
     * @throws IOException if there is a problem with run input buffers reading
     *                     from the run file.
     */
    public MergeSort(Runs runs, File outputFile) throws IOException {
        this.runs = runs;
        this.outputFile = new RandomAccessFile(outputFile, "rw");
        initializeRuns();
    }


    /**
     * Initialize the runs for sorting. This method sets the input buffers
     * for each run in the Runs object.
     *
     * @throws IOException if there is a problem with run input buffers reading
     *                     from the run file.
     */
    private void initializeRuns() throws IOException {
        int memorySize = (8192 * 8) / runs.numberOfRuns();
        int leftoverBytes = memorySize % 16;
        memorySize = memorySize - leftoverBytes;
        runs.initialize(memorySize);
    }


    /**
     * Sort the runs. Getting the next smallest value from each run is taken
     * care of by the Runs object - so this method essential only asks for the
     * next smallest value from the Runs object and puts it in the output
     * buffer.
     *
     * @throws IOException if there is a problem with run input buffers reading
     *                     from the run file.
     */
    public void sortRuns() throws IOException {
        RecordOutputBuffer outputBuffer =
            new RecordOutputBuffer(8192, outputFile);
        while (!runs.isEmpty()) {
            outputBuffer.insertRecord(runs.getNextMinRecord());
        }
        outputBuffer.writeRemainingContentsToFile();
    }
}
