import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Test the Run class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class RunTest {
    private TestHelper testHelper;
    private RandomAccessFile runFile;


    /**
     * set up the test file for run test
     *
     * @throws IOException if file not exist
     */
    @Before public void setUp() throws IOException {
        testHelper = new TestHelper();
        runFile = testHelper.createRecordFileForTests("default.bin", 100);
        World world = new World(new File("default.bin"));
        world.sortFile();
    }


    /**
     * delete generated files after test
     */
    @After public void tearDown() {
        testHelper.deleteTestFiles();
    }


    /**
     * test if a run is exhausted.
     *
     * @throws IOException if file not exist
     */
    @Test public void testRunIsExhausted() throws IOException {
        Run run = new Run(0, runFile);
        run.addLength(100 * 16);
        run.initializeRunBufferOfSize(100 * 32);
        for (int i = 0; i < 100; i++) {
            run.popNextVal();
        }
        Assert.assertTrue(run.isExhausted());
    }


    /**
     * if run is sorted
     *
     * @throws IOException if file not exist
     */
    @Test public void testIfRunSorted() throws IOException {

        Run run = new Run(0, runFile);

        run.addLength(100 * 16);
        run.initializeRunBufferOfSize(100 * 32);
        Record record = run.popNextVal();
        Record nextRecord;
        for (int i = 0; i < 99; i++) {
            nextRecord = run.popNextVal();
            Assert.assertTrue(record.compareTo(nextRecord) <= 0);
            record = nextRecord;
        }
    }


    /**
     * test peek and pop next value method in the run class
     *
     * @throws IOException file not exist
     */
    @Test public void testPeekAndPopNextValue() throws IOException {
        Run run = new Run(0, runFile);

        run.addLength(100 * 16);
        run.initializeRunBufferOfSize(100 * 32);
        for (int i = 0; i < 100; i++) {
            Record peekRecord = run.peekNextVal();
            Record popRecord = run.popNextVal();
            Assert.assertTrue(peekRecord.compareTo(popRecord) == 0);
        }
    }

}
