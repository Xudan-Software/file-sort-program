import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * Test class for testing the InputBuffer.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class InputBufferTest {
    TestHelper testHelper = new TestHelper();
    RandomAccessFile randAccFile;


    /**
     * Set up state for use in tests.
     *
     * @throws IOException if a file with the given name already exists.
     */
    @Before public void setUp() throws IOException {
        randAccFile = testHelper.createRecordFileForTests("temp.bin", 100);
    }


    @After public void tearDown() {
        testHelper.deleteTestFiles();
    }


    /**
     * Test that we can create a new InputBuffer with a good file and it does
     * not break.
     *
     * @throws IOException if there is an error loading data from the random
     *                     access file.
     */
    @Test public void testInitialization() throws IOException {
        new InputBuffer(10, randAccFile);
    }


    /**
     * Tests that when the input buffer is given a file less than it's size,
     * and we try to take too many records, that it throws the correct
     * exception.
     */
    @Test(expected = IllegalStateException.class)
    public void testBufferThrowsIllegalStateWhenTryingToPopToManySmallFile()
        throws IOException {
        InputBuffer buffer = new InputBuffer(8192, randAccFile);
        // randAccFile has 100 records
        for (int i = 0; i < 101; i++) {
            Record record = new Record(buffer.popFirstXBytes(16));
        }
    }


    /**
     * Tests that when the input buffer is given a large file, and we pop one
     * extra record, that it throws the correct exception.
     */
    @Test(expected = IllegalStateException.class)
    public void testBufferThrowsIllegalStateWhenTryingToPopToManyBigFile()
        throws IOException {
        testHelper.createRecordFileForTests("bigFile.bin", 10000);
        InputBuffer buffer = new InputBuffer(8192, randAccFile);
        // randAccFile has 100 records
        for (int i = 0; i < 10000; i++) {
            buffer.popFirstXBytes(16);
        }
        Assert.assertTrue(buffer.isExhausted());
        buffer.popFirstXBytes(16);
    }


    /**
     * Tests that when the input buffer is given a file less than it's size,
     * it does not fill itself with zeros.
     */
    @Test public void testIsEmpty() throws IOException {
        InputBuffer buffer = new InputBuffer(8192, randAccFile);
        // randAccFile has 100 records
        for (int i = 0; i < 100; i++) {
            buffer.popFirstXBytes(16);
        }
        Assert.assertTrue(buffer.isExhausted());
    }


    @Test public void testWhetherInputBufferIsDuplicate() throws IOException {
        HashMap<Long, Double> recordIdValue = new HashMap<>();
        File originalSampleInput16 = new File("sampleInput16-original.bin");
        File copiedSampleInput16 = new File("sampleInput16.bin");
        testHelper.copyFile(originalSampleInput16, copiedSampleInput16);
        InputBuffer duplicateInputBuffer = new InputBuffer(1024,
            new RandomAccessFile(copiedSampleInput16, "r"));
        while (!duplicateInputBuffer.isExhausted()) {
            Record record = new Record(duplicateInputBuffer.popFirstXBytes(16));
            Assert.assertFalse(recordIdValue.containsKey(record.getID()));
            recordIdValue.put(record.getID(), record.getKey());
            testHelper.deleteTestFiles();
        }
    }
}
