import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Test the World class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class WorldTest {
    TestHelper testHelper = new TestHelper();
    RandomAccessFile sortFile;  // unsortedfile to be sorted.

    private World world;


    /**
     * Setup the world class for test runs.
     */
    @Before public void setUp() throws IOException {
        String filename = "worldTest.bin";
        sortFile = testHelper.createRecordFileForTests(filename, 8192 * 2);
        world = new World(new File(filename));
    }


    /**
     * Remove any temporary files created for test runs.
     */
    @After public void tearDown() {
        testHelper.deleteTestFiles();
        File runFile = new File("runs.bin");
       runFile.delete();
       File worldTest = new File("worldTest.bin");
       worldTest.delete();
    }
    /**
     * Tests that when passed a file of 512 records, it sorts them into
     * runs.bin.
     *
     * @throws IOException if there are issues with any underlying files used.
     */
    @Test public void testSortSmallFile() throws IOException{
        String smallWorld="smallWorld.bin";
        RandomAccessFile sortSmall;
        sortSmall=testHelper.createRecordFileForTests(smallWorld, 20);
        World littleWorld = new World(new File(smallWorld));
        littleWorld.sortFile();
        sortSmall.readLong();
        double lastDouble = sortSmall.readDouble();
        double newDouble;
        while (sortSmall.getFilePointer() < sortSmall.length()) {
            sortSmall.readLong();
            newDouble = sortSmall.readDouble();
            System.out.println(lastDouble);
        //Assert.assertTrue(newDouble >= lastDouble);
            lastDouble = newDouble;
        }
    }

    /**
     * Tests that when passed a file of 16,384 records, it sorts them into
     * runs.bin.
     *
     * @throws IOException if there are issues with any underlying files used.
     */
    @Test public void testSortLargeFile() throws IOException {
        world.sortFile();
        sortFile.readLong();
        double lastDouble = sortFile.readDouble();
        double newDouble;
        while (sortFile.getFilePointer() < sortFile.length()) {
            sortFile.readLong();
            newDouble = sortFile.readDouble();
            System.out.println(lastDouble);
          Assert.assertTrue(newDouble >= lastDouble);
            lastDouble = newDouble;
        }
        //System.out.println(lastDouble);
    }


}
