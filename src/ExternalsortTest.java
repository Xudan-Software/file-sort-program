import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertNotNull;

/**
 * Testing class for Externalsort
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class ExternalsortTest {
    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TestHelper testHelper;


    /**
     * Create test helper for tests
     */
    @Before public void setUp() {
        testHelper = new TestHelper();
        System.setOut(new PrintStream(outContent));
    }


    /**
     * Delete temporary files
     */
    @After public void tearDown() {
        testHelper.deleteTestFiles();
        System.setOut(originalOut);
    }


    /**
     * Get code coverage of the class declaration.
     */
    @Test public void testExternalsortInit() throws IOException {
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        Externalsort.main(null);
    }


    /**
     * Test that external sort prints the correct number of records.
     *
     * @throws IOException If there are any File I/O errors.
     */
    @Test public void testExternalSortPrintsCorrectly() throws IOException {
        testHelper.createRecordFileForTests("test.bin", 8192);
        Externalsort.main(new String[] { "test.bin" });
        Assert.assertEquals(32, outContent.toString().
            split("[ ,\n]+").length);
    }
}
