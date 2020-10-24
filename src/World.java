import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * World class for handling all program logic and controlling sequence of
 * program execution.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class World {
    private final File file;
    private final MinHeap<Record> theHeap;
    private final Record[] inputBuffer;
    private final Record[] outputBuffer;
    private static final int blockSize = 512;
    private static final int heapSize = 8 * blockSize;
        // 8 blocks of 512 records


    /**
     * Initialize a new World object.
     *
     * @param file the file of records stored as bytes.
     */
    public World(File file) {
        this.file = file;
        this.theHeap = new MinHeap<>(new Record[heapSize], 0, heapSize);
        inputBuffer = new Record[blockSize];
        outputBuffer=new Record[blockSize];
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
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Record record;
        for (int i = 0; i < heapSize; i++) {
            byte[] recordBytes = new byte[16];
            try {
                fileInputStream.read(recordBytes, 0, 16);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            record = new Record(recordBytes);
            theHeap.insert(record);
        }
    }

    /**
     * Load the input bufger object with one blocks of data.
     */
    private void loadInputBuffer() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Record record;
        for (int i = 0; i < blockSize; i++) {
            byte[] recordBytes = new byte[16];
            try {
                fileInputStream.read(recordBytes, 0, 16);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            record = new Record(recordBytes);
            inputBuffer[i] = record;
        }

    }


    /**
     * get the input buffer array
     * @return array of input buffer
     */
    public Record[] getInputBuffer(){
        return inputBuffer;
    }

    public void replaceSelection(){

    }

    private boolean shouldContinueRun(){
        return true;
    }
}
