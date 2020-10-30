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
    private int numRecords = 512;  // number of records in a block
    private int blockSize = 16 * numRecords; // block size in bytes
    private int heapSize = 8 * numRecords;  // heap can hold 8 blocks of records


    /**
     * Initialize a new World object with the given file.
     *
     * @param file the file of records stored as bytes.
     */
    public World(File file) throws IOException {
        this.theHeap = new MinHeap(new Record[heapSize], 0, heapSize,
            new InputBuffer(blockSize, new RandomAccessFile(file, "r")));
        this.outputBuffer = new RecordOutputBuffer(blockSize,
            new RandomAccessFile("runs.bin", "rw"));
        this.theHeap.initialize();
    }


    public World(File file, int numRecords) throws IOException {
        this.numRecords = numRecords;
        this.blockSize = numRecords * 16;
        this.heapSize = 8 * numRecords;
        this.theHeap = new MinHeap(new Record[heapSize], 0, heapSize,
            new InputBuffer(blockSize, new RandomAccessFile(file, "r")));
        this.outputBuffer = new RecordOutputBuffer(blockSize,
            new RandomAccessFile("runs.bin", "rw"));
        this.theHeap.initialize();
    }


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() throws IOException {
        while (!theHeap.isFinished()) {
            Record minRec = theHeap.removemin();
            System.out.println("Record is: " + minRec);
            outputBuffer.insertRecord(minRec);
        }
        // there might still be records in the output buffer, so write them
        // to the run file.
        outputBuffer.writeRemainingContentsToFile();
    }


    public MinHeap getTheHeap() {
        return theHeap;
    }

}
