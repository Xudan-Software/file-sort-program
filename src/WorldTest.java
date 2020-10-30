import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Test the World class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class WorldTest {
    private World world;
    private World worldSimple;


    /**
     * make 16 byte-long array that contains 8 long and 8 double
     *
     * @param l long value
     * @param d double value
     * @return a byte array contains long and double values
     */
    public byte[] makeRecArray(long l, double d) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(l);
        bb.putDouble(d);
        return bb.array();
    }


    /**
     * Setup the world class for test runs.
     */
    @Before public void setUp() throws FileNotFoundException {
        world = new World(new File("src/sampleInput16.bin"));
        worldSimple = new World(new File("src/test.bin"));
    }


    /**
     * Tests that when the world object is initialized, it loads the heap with
     * 8 blocks (4096 records).
     */
    @Test public void testLoadHeap() {
        world.initializeHeap();
        Assert.assertEquals(4096, world.getHeap().heapsize());
    }


    /**
     * Tests that when the world object is initialized, one block size (512)
     * records is loaded into input buffer.
     */
    @Test public void testLoadInputBuffer() {
        world.loadInputBuffer();
        XuBuffer inputBuffer = world.getInputBuffer();
        Assert.assertTrue(inputBuffer.isFull());
    }


    @Test public void testSortFileWithTwoRuns() throws FileNotFoundException {
//        XuBuffer buffer = new XuBuffer(16*10);
//        for (int i=1; i<11; i++) {
//            buffer.put(new Record(makeRecArray(i, i)).getCompleteRecord());
//        }
//        buffer.writeToFile(new RandomAccessFile(new File("smallWorldTestOrdered.bin"), "rw"));
        // Unordered file has 10, 9, .... 2, 1
        File testFile = new File("smallWorldTestUnordered.bin");
        World smallWorld = new World(testFile, 5, 5);
        smallWorld.sortFile();
//        File raFile = new File("runs.bin");


    }
}
