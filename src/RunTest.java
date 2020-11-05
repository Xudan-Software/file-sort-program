import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Test the Run class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class RunTest {
    Runs runs;
    TestHelper testHelper;


    @Before public void setUp() throws IOException {
        testHelper = new TestHelper();
        testHelper.createRecordFileForTests("default.bin", 100);
        World world = new World(new File("default.bin"));
        world.sortFile();
        runs = world.getOutputBuffer().getRuns();
        Assert.assertEquals(1, runs.numberOfRuns());
    }


    @Test public void testRunIsExhausted() throws IOException {
        Run run = runs.getRunList().getFirst();
        for (int i=0; i<100; i++) {
            run.popNextVal();
        }
        Assert.assertTrue(run.isExhausted());
    }
}
