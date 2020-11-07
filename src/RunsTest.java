
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * Test class for testing the Runs.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class RunsTest {
    private World world;
    TestHelper testHelper = new TestHelper();
    RandomAccessFile sortFile;  // unsortedfile to be sorted.
    @Before public void setUp() throws IOException {
        File originalSampleInput16 = new File("sampleInput16-original.bin");
        File copiedSampleInput16 = new File("sampleInput16.bin");
        testHelper.copyFile(originalSampleInput16, copiedSampleInput16);
        sortFile= new RandomAccessFile( copiedSampleInput16,"rw");
        world= new World(copiedSampleInput16);
        world.createRuns();
    }
    @After public void tearDown(){
        testHelper.deleteTestFiles();
    }

    @Test public void testRunsNotDuplicateWithOtherRuns() throws IOException {
           Runs runs= world.getOutputBuffer().getRuns();
           runs.initialize(8192);
        HashMap<Long, Double> recordIdValue = new HashMap<>();
        while(!runs.isEmpty()){
           Record lowestRecord = runs.getNextMinRecord();
           Assert.assertFalse(recordIdValue.containsKey(lowestRecord.getID()));
         recordIdValue.put(lowestRecord.getID(),lowestRecord.getKey());
        }


    }
}
