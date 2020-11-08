import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Test the Printer class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class PrinterTest {
    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Printer printer;
    private RandomAccessFile testFile;
    private TestHelper testHelper;
    private World world;


    /**
     * generate test file by using test helper. set up world and printer object
     *
     * @throws IOException if file doesn't found
     */
    @Before public void setUp() throws IOException {
        testHelper = new TestHelper();
        testFile = testHelper.createRecordFileForTests("test.bin", 8192);
        world = new World(new File("test.bin"));
        world.sortFile();
        printer = new Printer(testFile);
        System.setOut(new PrintStream(outContent));
    }


    /**
     * delete the test file after testing
     */
    @After public void tearDown() {
        testHelper.deleteTestFiles();
        System.setOut(originalOut);
    }


    /**
     * test if print method print correctly
     *
     * @throws IOException if the file doesn't exist
     */
    @Test public void testPrinterPrintsCorrectNumberOfRecords()
        throws IOException {
        printer.print();
        Assert.assertEquals(32, outContent.toString().
            split("[ ,\n]+").length);

    }
}

