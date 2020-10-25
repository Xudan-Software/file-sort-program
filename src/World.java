import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    private final Record[] inputBuffer;
    private final Record[] outputBuffer;
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
        inputBuffer = new Record[blockSize];
        outputBuffer = new Record[blockSize];
//        waitingArray = new MinHeap<>(new Record[heapSize], 0, heapSize);
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
        Record record;
        for (int i = 0; i < blockSize && inputSign != -1; i++) {
            record = new Record(
                Arrays.copyOfRange(recordBytes, 16 * i, 16 * (i + 1)));
            inputBuffer[i] = record;
        }

    }


    /**
     * get the input buffer array
     *
     * @return array of input buffer
     */
    public Record[] getInputBuffer() {
        return inputBuffer;
    }


    // TODO: See if you can use buffers which keep track of current location in
    //  input and output buffers, so we don't have to use index parameters.
    private void loadValFromInputBufferToHeap(
        int idxInputBuffer,
        int idxOutputBuffer) {
        Record record = inputBuffer[idxInputBuffer];
        // Check whether the next value from the input buffer to the min heap
        // is smaller than the last value in the output buffer.
        if (record.compareTo(outputBuffer[idxOutputBuffer]) < 0) {
            // insert the next input buffer value into the heap, but dont use
            // it during this run.
            theHeap.insertAndDecrement(record);
        }
    }

//    //todo: load the input buffer again
//    //todo: create the run and load records into the run when output is full
//    public void replaceSelection() {
//        int currIn = 0;
//        int currOut = 0;
//        while (shouldContinueRun()) {
//            Record removeValue = theHeap.removemin();
//            theHeap.insert(inputBuffer[currIn]);
//            if (removeValue.getKey() > outputBuffer[currOut - 1].getKey()) {
//                //compare with the last value in the output buffer
//                outputBuffer[currOut] = removeValue;
//            }
//            else {
//                waitingArray.insert(removeValue);
//            }
//            currOut++;
//            currIn++;
//        }
//    }
//
//
//    private boolean shouldContinueRun() {
//        return (waitingArray.heapsize() != heapSize && inputSign != -1);
//    }

}
