import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Test class for testing the Runs.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class RunsTest {
    TestHelper testHelper = new TestHelper();
    private World world;


    @Before public void setUp() throws IOException {
        testHelper.createRecordFileForTests("test.bin", 8192);
        world = new World(new File("test.bin"));
        world.createRuns();
    }


    @After public void tearDown() {
        testHelper.deleteTestFiles();
    }


    @Test public void testRunsNotDuplicateWithOtherRuns() throws IOException {
        Runs runs = world.getOutputBuffer().getRuns();
        runs.initialize(8192);
        HashMap<Long, Double> recordIdValue = new HashMap<>();
        while (!runs.isEmpty()) {
            Record lowestRecord = runs.getNextMinRecord();
            Assert.assertFalse(recordIdValue.containsKey(lowestRecord.getID()));
            recordIdValue.put(lowestRecord.getID(), lowestRecord.getKey());
        }

    }
}
