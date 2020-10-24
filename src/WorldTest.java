import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Test the World class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class WorldTest {
    private World world;


    /**
     * Setup the world class for test runs.
     */
    @Before public void setUp() {
        world = new World(new File("src/sampleInput16.bin"));
    }


    /**
     * Tests that when the world object is initialized, it loads the heap with
     * 8 blocks (4096 records).
     */
    @Test public void testLoadHeap() {
        world.sortFile();
        Assert.assertEquals(4096, world.getHeap().heapsize());
    }


    /**
     * Tests that when the world object is initialized, one block size (512)
     * records loaded into input buffer.
     */
    @Test public void testLoadInputBuffer(){
        world.sortFile();
        for(Record record:world.getInputBuffer()){
            Assert.assertNotNull(record);
        }

    }
}
