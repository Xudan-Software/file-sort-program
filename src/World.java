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
    private Record lastOutputReco

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


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() throws IOException {

        replacementSelection();
        // TODO: later we will implement merging runs here

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


    /**
     * Load the input buffer object with one block of data.
     */
    public void loadInputBuffer() {

        try {
            //read the smaller number from blocksize or file length
            int readSize = (int)Math
                .min(blockSize, (raFile.length() - raFile.getFilePointer()));
            byte[] recordBytes = new byte[readSize];
            inputSign = raFile.read(recordBytes, 0, readSize);
            if (inputSign != -1) {
                inputBuffer.put(recordBytes);

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


    public void replacementSelection() throws IOException {
        initializeHeap();
        loadInputBuffer();
        inputBuffer.flip();

        recurReplacementSelection();
    }


    public void createARun() {
        while (theHeap.heapsize() > 0 && !inputBuffer.isEmpty()) {
            Record minRecord = theHeap.removemin();
            outputBuffer.put(minRecord.getCompleteRecord());
            lastOutputRecord = minRecord;
            Record recordFromInputBuffer =
                new Record(inputBuffer.popFirstXBytes(16));
            if (recordFromInputBuffer.compareTo(lastOutputRecord) < 0) {
                theHeap.insertAndDecrement(recordFromInputBuffer);
            }
            else {
                theHeap.selectionInsert(recordFromInputBuffer);
            }
            if (inputBuffer.isEmpty() && inputSign != -1) {
                loadInputBuffer();
            }
            // now we have to check whether the output buffer is full, and should be
            // written
            if (outputBuffer.isFull()) {
                outputBuffer.flip();
                outputBuffer.writeToFile(runFile);
                outputBuffer.clear();
            }
        }
    }


    private void heapToRunFile(RandomAccessFile runFile) {
        //initialize a temp buffer of heap size byte for write to file purpose only
        XuBuffer tempBuffer = new XuBuffer(heapSize * 16);
        while (theHeap.heapsize() > 0) {
            Record minRecord = theHeap.removeminForRunHeap();
            tempBuffer.put(minRecord.getCompleteRecord());
        }
        tempBuffer.flip();
        tempBuffer.writeToFile(runFile);
    }


    private void recurReplacementSelection() throws IOException {
        createARun();
        if (!outputBuffer.outputBufferIsEmpty()) {
            outputBuffer.flip();
            outputBuffer.writeToFile(runFile);
            outputBuffer.clear();
            runPositions.add(runFile.getFilePointer());
        }
        /* Heap is full with bad values(n reduce to 0), input buffer is empty.
        Reheapify the heap and output a new run with values inside the heap
        (8 block size run)
        */
        if (inputBuffer.isEmpty()) {
            //re-heapify the heap and ready to output to the last run
            theHeap.setNumberOfItemsInHeap(heapSize);
            theHeap.buildheap();
            heapToRunFile(runFile);
            runPositions.add(runFile.getFilePointer());
        }
        if (theHeap.heapsize() == 0 && !inputBuffer.isEmpty()) {
            //re-heapify the heap and run the possible run
            theHeap.setNumberOfItemsInHeap(heapSize);
            theHeap.buildheap();
            recurReplacementSelection();
        }

    }
}

