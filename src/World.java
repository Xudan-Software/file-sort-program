import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * World class for handling all program logic and controlling sequence of
 * program execution.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class World {
    private final MinHeap<Record> theHeap;
    private final XuBuffer inputBuffer;
    private final XuBuffer outputBuffer;
    private final RandomAccessFile raFile;
    private final RandomAccessFile runFile;
    private int numRecords = 512;
    private int blockSize = 16 * numRecords; // block size in bytes
    private int heapSize = 8 * numRecords;
    private int inputSign;
    private List<Long> runPositions;


    /**
     * Initialize a new World object with the given file.
     *
     * @param file the file of records stored as bytes.
     */
    public World(File file) throws FileNotFoundException {
        this.theHeap = new MinHeap<>(new Record[heapSize], 0, heapSize);
        inputBuffer = new XuBuffer(blockSize);
        outputBuffer = new XuBuffer(blockSize);
        inputSign = 0;
        raFile = new RandomAccessFile(file, "r");
        runFile = new RandomAccessFile("runs.bin", "rw");
        runPositions = new LinkedList<>();

    }

//    /**
//     * Initialize a new World object with the given file, block and heap size.
//     * This is used for testing, but can also provide better control over
//     * the creation of the heap.
//     *
//     * @param file      the file of records stored as bytes.
//     * @param blockSize the size of input blocks
//     * @param heapSize  the size of the underlying min heap.
//     */
//    public World(File file, int blockSize, int heapSize)
//        throws FileNotFoundException {
//        this.blockSize = blockSize;
//        this.heapSize = heapSize;
//        this.theHeap = new MinHeap<>(new Record[heapSize], 0, heapSize);
//        inputBuffer = ByteBuffer.allocate(blockSize);
//        outputBuffer = ByteBuffer.allocate(blockSize);
//        inputSign = 0;
//        raFile = new RandomAccessFile(file, "r");
//        runFile = new RandomAccessFile("runs.bin", "wr");
//        runPositions = new LinkedList<>();
//    }


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() {
        while (inputSign != -1) {
            createARun();
            try {
                runPositions.add(runFile.getFilePointer());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void writeOutputBufferToRunFile() {
        outputBuffer.writeToFile(runFile);
    }


    public void createARun() {
        // TODO: We need to reload the heap and input buffer after they are
        //  exhausted.
//        loadHeap();
//        loadInputBuffer();
//        while (shouldContinueRun()) {
//            if (inputBufferIsEmpty()) {
//            if (inputBuffer.isEmpty()) {
//                inputBuffer.clear();
//                loadInputBuffer();
//                inputBuffer.setFront(0);
//            }
//            outputBuffer.put(theHeap.removemin().getCompleteRecord());
//            loadValFromInputBufferToHeap();
//            if (outputBufferIsFull()) {
//                writeOutputBufferToRunFile();
//                outputBuffer.clear();
//            }
//        }
    }


    /**
     * Gets the minimum heap.
     *
     * @return the minimum heap.
     */
    public MinHeap<Record> getHeap() {
        return this.theHeap;
    }


    /**
     * Load the minimum heap object with 8 blocks of data.
     */
    public void loadHeap() {
        byte[] recordBytes = new byte[heapSize * 16];
        try {
            raFile.read(recordBytes, 0, heapSize * 16);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Record record;
        for (int i = 0; i < heapSize; i++) {
            record = new Record(
                Arrays.copyOfRange(recordBytes, 16 * i, 16 * (i + 1)));
            theHeap.insert(record);
        }
    }


    /**
     * Load the input buffer object with one blocks of data.
     */
    public void loadInputBuffer() {

        byte[] recordBytes = new byte[blockSize];
        try {

            inputSign = raFile.read(recordBytes, 0, blockSize);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        inputBuffer.put(recordBytes);
    }


    /**
     * get the input buffer array
     *
     * @return array of input buffer
     */
    public XuBuffer getInputBuffer() {
        return inputBuffer;
    }


    /**
     * Loads a value from the input buffer into the heap. If the new value
     * is smaller than the record that just went into the output buffer, then
     * the new input record is given to the heap but wont be available. If the
     * new record is larger than the record just put into the output buffer,
     * then we can safely insert the new input record into the heap with no
     * further considerations.
     */
    public void loadValFromInputBufferToHeap() {
        Record inputRecord = new Record(inputBuffer.popFirstXBytes(16));
        Record lastOutputRecord = new Record(outputBuffer.getLastXBytes(16));
        // Check whether the next value from the input buffer to the min heap
        // is smaller than the last value in the output buffer.
        if (inputRecord.compareTo(lastOutputRecord) < 0) {
            // insert the next input buffer value into the heap, but dont use
            // it during this run.
            theHeap.insertAndDecrement(inputRecord);
        }
        else {  // the new input value can safely enter the heap
            theHeap.insert(inputRecord);
        }
    }


    private boolean shouldContinueRun() {
        return (this.theHeap.heapsize() > 0 || inputSign != -1);
    }

}
