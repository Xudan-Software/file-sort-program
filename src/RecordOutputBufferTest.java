import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * Test class for testing the RecordOutputBuffer.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class RecordOutputBufferTest {
    TestHelper testHelper = new TestHelper();
    RecordOutputBuffer recordOutputBuffer;
    File runFile;


    /**
     * Setup state for test execution.
     *
     * @throws IOException if there are any issues creating a run file.
     */
    @Before public void setUp() throws IOException {
        runFile = new File("recordOutputRunFile.bin");
        boolean created = runFile.createNewFile();
        if (!created) {
            throw new IllegalStateException("Run file already exists");
        }
        recordOutputBuffer =
            new RecordOutputBuffer(160, new RandomAccessFile(runFile, "rw"));
    }


    /**
     * Delete any temporary files created for tests.
     */
    @After public void tearDown() {
        runFile.delete();
    }


    /**
     * Tests that we can insert a good Record into an empty buffer with no
     * issues.
     *
     * @throws IOException if the RecordBuffer has some issue with it's
     *                     underlying run file.
     */
    @Test public void testInsertFirstRecordNoIssues() throws IOException {
        Record record = new Record(testHelper.makeRecArray(1, 1));
        recordOutputBuffer.insertRecord(record);
    }


    /**
     * Tests that inserting a record which is smaller than the previous record
     * creates a new run location at the correct place.
     *
     * @throws IOException if the RecordBuffer has some issue with it's
     *                     underlying run file.
     */
    @Test public void testInsertSmallerRecordCreatesRun() throws IOException {
        Record record;
        for (int i = 0; i < 8; i++) {
            record = new Record(testHelper.makeRecArray(i, i));
            recordOutputBuffer.insertRecord(record);
        }
        record = new Record(testHelper.makeRecArray(0, 0));
        recordOutputBuffer.insertRecord(record);
        LinkedList<Long> llist = recordOutputBuffer.getRunIndexes();
        Assert.assertTrue(0L == llist.getFirst());
        Assert.assertTrue(128L == llist.getLast());
    }
}
