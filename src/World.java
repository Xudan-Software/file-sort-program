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
    private File unsortedFile;
    private RandomAccessFile runFile = new RandomAccessFile("runs.bin", "rw");


    /**
     * Initialize a new World object with the given file.
     *
     * @param file the file of records stored as bytes.
     */
    public World(File file) throws IOException {
        unsortedFile = file;
//        if (file.length() / 16 < heapSize) {
//            hea
//        }
        this.theHeap = new MinHeap(new Record[heapSize], 0, heapSize,
            new InputBuffer(blockSize, new RandomAccessFile(file, "r")));
        this.outputBuffer = new RecordOutputBuffer(blockSize, runFile);

    }


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() throws IOException {
        while (!theHeap.isFinished()) {
            Record minRec = theHeap.removemin();
            outputBuffer.insertRecord(minRec);
        }
        // there might still be records in the output buffer, so write them
        // to the run file.
        outputBuffer.writeRemainingContentsToFile();
        MergeSort mergeSort =
            new MergeSort(outputBuffer.getRuns(), unsortedFile);
        mergeSort.sortRuns();
    }
}
