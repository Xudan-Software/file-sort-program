import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * World class for handling all program logic and controlling sequence of
 * program execution.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class World {
    private final MinHeap theHeap;
    private final RecordOutputBuffer outputBuffer;
    private final int numRecords = 512;  // number of records in a block
    private final int blockSize = 16 * numRecords; // block size in bytes
    private final int heapSize = 8 * numRecords;  // heap can hold 8 blocks
    private final File unsortedFile;
    private final RandomAccessFile runFile =
        new RandomAccessFile("runs.bin", "rw");


    /**
     * Initialize a new World object with the given file.
     *
     * @param file the file of records stored as bytes.
     */
    public World(File file) throws IOException {
        unsortedFile = file;
        this.theHeap = new MinHeap(new Record[heapSize], 0, heapSize,
            new InputBuffer(blockSize, new RandomAccessFile(file, "r")));
        this.outputBuffer = new RecordOutputBuffer(blockSize, runFile);

    }


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() throws IOException {
        createRuns();
        MergeSort mergeSort =
            new MergeSort(outputBuffer.getRuns(), unsortedFile);
        mergeSort.sortRuns();
    }


    /**
     * return the output buffer
     *
     * @return the outputBuffer
     */
    public RecordOutputBuffer getOutputBuffer() {
        return this.outputBuffer;
    }


    /**
     * create Runs method to creates runs in the outputbuffer
     *
     * @throws IOException if file not exists
     */
    public void createRuns() throws IOException {
        while (!theHeap.isFinished()) {
            Record minRec = theHeap.removemin();
            outputBuffer.insertRecord(minRec);
        }
        outputBuffer.writeRemainingContentsToFile();
    }

}
