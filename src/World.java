import java.io.*;

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
    private static final int heapSize = 8 * 512;  // 8 blocks of 512 records


    /**
     * Initialize a new World object.
     *
     * @param file the file of records stored as bytes.
     */
    public World(File file) {
        this.file = file;
        this.theHeap = new MinHeap<>(new Record[heapSize], 0, heapSize);
    }


    /**
     * Sort the file given to the World class.
     */
    public void sortFile() {
        loadHeap();
//        createRuns();
    }


    /**
     * Gets the minimum heap.
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
        for (int i=0; i<heapSize; i++) {
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
}
