import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Test the Printer class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class PrinterTest {
    Printer printer;
    RandomAccessFile testFile;
    TestHelper testHelper;


    @Before public void setUp() throws IOException {
        testHelper = new TestHelper();
        File originalSampleInput16 = new File("sampleInput16-original.bin");
        File copiedSampleInput16 = new File("sampleInput16.bin");
        testHelper.copyFile(originalSampleInput16, copiedSampleInput16);
        testFile = new RandomAccessFile("sampleInput16.bin", "rw");
        World world = new World(new File("sampleInput16.bin"));
        world.sortFile();
        printer = new Printer(testFile);
    }


    @After public void tearDown() {
        testHelper.deleteTestFiles();
    }


    @Test public void testRunIsExhausted() throws IOException {
        printer.print();
    }
}
