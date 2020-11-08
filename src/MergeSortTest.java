import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * tests for merge sort class
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class MergeSortTest {
    private TestHelper testHelper;


    /**
     * Create test helper for tests
     */
    @Before public void setUp() {
        testHelper = new TestHelper();
    }


    /**
     * Delete temporary files
     */
    @After public void tearDown() {
        testHelper.deleteTestFiles();
    }


    /**
     * Tests that given runs, the merge sort class can merge the runs into one
     * sorted file.
     *
     * @throws IOException If there are any file i/o errors.
     */
    @Test public void testMergeSortSortsRuns() throws IOException {
        RandomAccessFile sortSample =
            testHelper.createRecordFileForTests("test.bin", 20000);
        File testFile = new File("test.bin");
        World world = new World(testFile);
        world.createRuns();
        Runs runs = world.getOutputBuffer().getRuns();
        MergeSort mergeSort = new MergeSort(runs, testFile);
        mergeSort.sortRuns();
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
}
