import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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


    @Test public void testSortFileWithTwoRuns() throws IOException {
        ArrayList<Record> arrayList = new ArrayList<>();
        for (int i = 6; i < 11; i++) {
            arrayList.add(new Record(makeRecArray(i, i)));
        }
        for (int i = 1; i < 6; i++) {
            arrayList.add(new Record(makeRecArray(i, i)));
        }
        // Unordered file has 10, 9, .... 2, 1
        File testFile = new File("smallWorldTestUnordered.bin");
        World smallWorld = new World(testFile, 5, 5);
        smallWorld.sortFile();
        RandomAccessFile ra = new RandomAccessFile(new File("runs.bin"), "r");
        ArrayList<Record> aListRuns = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            byte[] byteRecord = new byte[16];
            ra.read(byteRecord, 0, 16);
            aListRuns.add(new Record(byteRecord));
        }
        Assert.assertArrayEquals(arrayList.toArray(), aListRuns.toArray());

    }


    @Test public void testSortWithTwoAndHalfRuns() throws IOException {
        ArrayList<Record> arrayList = new ArrayList<>();
        for (int i = 8; i < 13; i++) {
            arrayList.add(new Record(makeRecArray(i, i)));
        }
        for (int i = 3; i < 5; i++) {
            arrayList.add(new Record(makeRecArray(i, i)));
        }
        for (int i = 1; i < 3; i++) {
            arrayList.add(new Record(makeRecArray(i, i)));
        }
        for (int i = 5; i < 8; i++) {
            arrayList.add(new Record(makeRecArray(i, i)));
        }
        XuBuffer buffer = new XuBuffer(12 * 16);
        for (int i = 12; i > 0; i--) {
            buffer.put(makeRecArray(i, i));
        }
        File testFile = new File("testTwoAndHalf.bin");
        buffer.writeToFile(new RandomAccessFile(testFile, "rw"));
        World smallWorld = new World(testFile, 12, 5);
        smallWorld.sortFile();
        RandomAccessFile ra = new RandomAccessFile(new File("runs.bin"), "r");
        ArrayList<Record> aListRuns = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            byte[] byteRecord = new byte[16];
            ra.read(byteRecord, 0, 16);
            aListRuns.add(new Record(byteRecord));
        }
        Assert.assertArrayEquals(arrayList.toArray(), aListRuns.toArray());

    }

}

