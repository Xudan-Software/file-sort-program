import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * Test the World class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class WorldTest {
    private final TestHelper testHelper = new TestHelper();
    private RandomAccessFile sortFile;  // unsortedfile to be sorted.
    private World world;


    /**
     * Setup the world class for test runs.
     */
    @Before public void setUp() throws IOException {
        String filename = "worldTest.bin";
        sortFile = testHelper.createRecordFileForTests(filename, 8192 * 2);
        testHelper.createRecordFileForTests("test.bin", 8192);
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
     * Tests that when passed a file of 20 records, it sorts them into
     * runs.bin.
     *
     * @throws IOException if there are issues with any underlying files used.
     */
    @Test public void testSortSmallFile() throws IOException {
        String smallFile = "smallWorld.bin";
        RandomAccessFile sortSmall =
            testHelper.createRecordFileForTests(smallFile, 20);
        World littleWorld = new World(new File(smallFile));
        littleWorld.sortFile();
        sortSmall.readLong();
        double lastDouble = sortSmall.readDouble();
        double newDouble;
        while (sortSmall.getFilePointer() < sortSmall.length()) {
            sortSmall.readLong();
            newDouble = sortSmall.readDouble();
            Assert.assertTrue(newDouble >= lastDouble);
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
            Assert.assertTrue(newDouble >= lastDouble);
            lastDouble = newDouble;
        }

    }


    /**
     * test sort sample file
     *
     * @throws IOException if file not exist
     */
    @Test public void testSortSampleFile() throws IOException {
        RandomAccessFile sortSample = new RandomAccessFile("test.bin", "rw");
        World sampleWorld = new World(new File("test.bin"));
        sampleWorld.sortFile();
        sortSample.readLong();
        double lastDouble = sortSample.readDouble();
        double newDouble;
        while (sortSample.getFilePointer() < sortSample.length()) {
            sortSample.readLong();
            newDouble = sortSample.readDouble();
            Assert.assertTrue(newDouble >= lastDouble);
            lastDouble = newDouble;
        }
    }


    /**
     * test sort number for random file
     *
     * @throws IOException if file not exist
     */
    @Test public void testWorldProducesSameNumberOfValuesOutAsIn()
        throws IOException {
        RandomAccessFile randFile8800 =
            testHelper.createRecordFileForTests("temp.bin", 8800);
        world = new World(new File("temp.bin"));
        world.sortFile();
        for (int i = 0; i < 8800; i++) {
            randFile8800.readLong();
            randFile8800.readDouble();
        }
        Assert
            .assertEquals(randFile8800.getFilePointer(), randFile8800.length());
    }


    /**
     * test if world not produce duplicate record
     *
     * @throws IOException when file not exist
     */
    @Test public void testWorldDoesNotProduceDuplicates() throws IOException {
        HashMap<Long, Double> recordIdValue = new HashMap<>();
        RandomAccessFile sortSample = new RandomAccessFile("test.bin", "rw");
        World sampleWorld = new World(new File("test.bin"));
        sampleWorld.sortFile();
        Long l;
        double d;
        while (sortSample.getFilePointer() < sortSample.length()) {
            l = sortSample.readLong();
            d = sortSample.readDouble();
            Assert.assertFalse(recordIdValue.containsKey(l));
            recordIdValue.put(l, d);
        }
    }
}
