import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * World class for handling all program logic and controlling sequence of
 * program execution.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class World {
    private static final int blockSize = 512;
    private static final int heapSize = 8 * blockSize;
    private final File file;
    private final MinHeap<Record> theHeap;
    private final ByteBuffer inputBuffer;
    private final ByteBuffer outputBuffer;
    //    private final MinHeap<Record> waitingArray;
    private final RandomAccessFile raFile;
    private int inputSign;


    /**
     * Initialize a new World object.
     *
     * @param file the file of records stored as bytes.
     */
    public World(File file) throws FileNotFoundException {
        this.file = file;
        this.theHeap = new MinHeap<>(new Record[heapSize], 0, heapSize);
        inputBuffer = ByteBuffer.allocate(blockSize * 16);
        outputBuffer = ByteBuffer.allocate(blockSize * 16);
        inputSign = 0;
        raFile = new RandomAccessFile(file, "r");

    }


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() {
        loadHeap();
        loadInputBuffer();
//        createRuns();
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
    private void loadHeap() {
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
    private void loadInputBuffer() {
        byte[] recordBytes = new byte[blockSize * 16];
        try {
            inputSign = raFile.read(recordBytes, 0, blockSize * 16);
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
    public ByteBuffer getInputBuffer() {
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
    private void loadValFromInputBufferToHeap() {
        byte[] inputRecordBytes = new byte[16];
        // put 16 bytes from input buffer to recordBytes array
        inputBuffer.get(inputRecordBytes, 0, 16);
        // Create the input Record object from these 16 bytes
        Record inputRecord = new Record(inputRecordBytes);

        // Now we need to compare the input record to the last placed output
        // record.
        byte[] outputRecordBytes = new byte[16];
        // put last 16 bytes from output buffer to recordBytes array
        outputBuffer.get(outputRecordBytes, outputBuffer.position() - 16, 16);
        // Create the output Record object from these 16 bytes
        Record lastOutputRecord = new Record(inputRecordBytes);
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

//
//
//    private boolean shouldContinueRun() {
//        return (waitingArray.heapsize() != heapSize && inputSign != -1);
//    }

}
