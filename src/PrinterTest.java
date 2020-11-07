import org.junit.After;
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
    World world;


    @Before public void setUp() throws IOException {
        testHelper = new TestHelper();
        File originalSampleInput16 = new File("sampleInput16-original.bin");
        File copiedSampleInput16 = new File("sampleInput16.bin");
        testHelper.copyFile(originalSampleInput16, copiedSampleInput16);
        testFile = new RandomAccessFile(copiedSampleInput16, "rw");
        world = new World(copiedSampleInput16);
        world.sortFile();
        printer = new Printer(testFile);
    }


    @After public void tearDown() {
        testHelper.deleteTestFiles();
    }


    @Test public void testPrintCorrect() throws IOException {
        // TODO: Redo this test to actually test that we print stuff
        //  correctly.
//        printer.print();
        int n = 0;
        Long l = testFile.readLong();
        double lastDouble = testFile.readDouble();
        double newDouble;
        while (testFile.getFilePointer() < testFile.length()) {
            l = testFile.readLong();
            newDouble = testFile.readDouble();
            n++;
//            Assert.assertTrue(newDouble >= lastDouble);
            lastDouble = newDouble;
        }
    }
}
