import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    private final List<Long> runPositions = new LinkedList<>();
    private int numRecords = 512;  // number of records in a block
    private int blockSize = 16 * numRecords; // block size in bytes
    private int heapSize = 8 * numRecords;  // heap can hold 8 blocks of records
    private int inputSign;
    private Record lastOutputRecord;


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
    }


    /**
     * Initialize a new World object with the given file. This constructor
     * should only be used for explicit custom behavior - such as the behavior
     * needed to easily test the World class.
     *
     * @param file       the file of records stored as bytes.
     * @param numRecords the number of records per block.
     * @param heapSize   how many records the heap should hold.
     */
    public World(File file, int numRecords, int heapSize)
        throws FileNotFoundException {
        this.heapSize = heapSize;
        this.numRecords = numRecords;
        blockSize = 16 * numRecords;
        this.theHeap = new MinHeap<>(new Record[heapSize], 0, heapSize);
        inputBuffer = new XuBuffer(blockSize);
        outputBuffer = new XuBuffer(blockSize);
        inputSign = 0;
        raFile = new RandomAccessFile(file, "r");
        runFile = new RandomAccessFile("runs.bin", "rw");
    }

//    public boolean shouldContinueMakingRuns() {
//        return (!inputBuffer.isEmpty() || theHeap.heapsize() != 0 ||
//    }


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() {
        initializeHeap();
        loadInputBuffer();
        createRuns();
        // TODO: later we will implement merging runs here



//        while (inputSign != -1) {
//            if (theHeap.heapsize() == 0) {
//                theHeap.setNumberOfItemsInHeap(theHeap.getBadVals());
////                theHeap.setNumberOfItemsInHeap(heapSize);
//            }
//            createARun();
//            try {
//                runPositions.add(runFile.getFilePointer());
//            }
//            catch (IOException e) {
//                e.printStackTrace();
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
     * Load the minimum heap object with 8 blocks of data. This should only
     * be called when the program first starts. Assumes the heap is completely
     * empty.
     */
    public void initializeHeap() {
        byte[] recordBytes = new byte[heapSize * 16];
        try {
            raFile.read(recordBytes, 0, recordBytes.length);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Record record;
        for (int i = 0; i < recordBytes.length / 16; i++) {
            record = new Record(
                Arrays.copyOfRange(recordBytes, 16 * i, 16 * (i + 1)));
            theHeap.insert(record);
        }
    }


//    /**
//     * Load the minimum heap object with 8 blocks of data.
//     */
//    public void loadHeap() {
//        byte[] recordBytes;
//        if (theHeap.getBadVals() > 0) {
//            recordBytes = new byte[(heapSize - theHeap.getBadVals()) * 16];
//        }
//        else {
//            recordBytes = new byte[heapSize * 16];
//        }
//        theHeap.setNumberOfItemsInHeap(theHeap.getBadVals());
//        try {
//            raFile.read(recordBytes, 0, recordBytes.length);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        Record record;
//        for (int i = 0; i < recordBytes.length / 16; i++) {
//            record = new Record(
//                Arrays.copyOfRange(recordBytes, 16 * i, 16 * (i + 1)));
//            theHeap.insert(record);
//        }
//    }


    /**
     * Load the input buffer object with one block of data.
     */
    public void loadInputBuffer() {
        byte[] recordBytes = new byte[blockSize];
        try {
            inputSign = raFile.read(recordBytes, 0, blockSize);
            if (inputSign != -1) {
                inputBuffer.put(recordBytes);
                inputBuffer.flip();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
        if (inputRecord.compareTo(lastOutputRecord) < 0) {
            theHeap.insertAndDecrement(inputRecord);
        }
        else {  // the new input value can safely enter the heap
            theHeap.selectionInsert(inputRecord);
        }
    }


    private boolean shouldContinueRun() {
        return (this.theHeap.heapsize() > 0 || inputSign != -1);
    }


    public void createRuns() {
        if (theHeap.heapsize() > 0) {
            Record minRecord = theHeap.removemin();
            outputBuffer.put(minRecord.getCompleteRecord());
            lastOutputRecord = minRecord;
            if (inputSign != -1) {
                Record recordFromInputBuffer = new Record(inputBuffer.popFirstXBytes(16));
                if (recordFromInputBuffer.compareTo(lastOutputRecord) < 0) {
                    theHeap.insertAndDecrement(recordFromInputBuffer);
                }
                else {
                    theHeap.selectionInsert(recordFromInputBuffer);
                }
            }
            // now we need to put a value in the heap
            // now we have to check whether the output buffer is full, and should be
            // written
            if (outputBuffer.isFull()) {
                outputBuffer.writeToFile(runFile);
                outputBuffer.clear();
            }
        }
        else {
            // handle heap size being zero

        }



















        while (shouldContinueRun()) {
            if (inputBuffer.isEmpty()) {
                inputBuffer.clear();
                loadInputBuffer();
                inputBuffer.setFront(0);
            }
            lastOutputRecord = theHeap.removemin();
            outputBuffer.put(lastOutputRecord.getCompleteRecord());
            loadValFromInputBufferToHeap();
            if (outputBuffer.isFull()) {
                outputBuffer.writeToFile(runFile);
                outputBuffer.clear();
            }
        }
    }
}
