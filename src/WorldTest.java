import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
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
     * Setup the world class for test runs.
     */
    @Before public void setUp() throws FileNotFoundException {
        world = new World(new File("src/sampleInput16.bin"));
        worldSimple=new World(new File("src/test.bin"));
    }


    /**
     * Tests that when the world object is initialized, it loads the heap with
     * 8 blocks (4096 records).
     */
    @Test public void testLoadHeap() {
        world.loadHeap();
        Assert.assertEquals(4096, world.getHeap().heapsize());
    }


    /**
     * Tests that when the world object is initialized, one block size (512)
     * records is loaded into input buffer.
     */
    @Test public void testLoadInputBuffer() {
        // TODO: Look into this method and see if it's doing what you want it
        //  to do. What exactly is limit, and position?
//        world.sortFile();
        XuBuffer inputBuffer = world.getInputBuffer();
//        Assert.assertEquals(512 * 16, inputBuffer.capacity());
//        Assert.assertEquals(512 * 16, inputBuffer.limit());
//        Assert.assertEquals(512 * 16, inputBuffer.position());
        world.loadInputBuffer();
        Assert.assertTrue(inputBuffer.isFull());
    }


    @Test public void testLoadValFromInputBufferLargerThanLast() {

    }


    @Test public void testLoadValFromInputBufferSmallerThanLast() {

    }
}
