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


    @Before public void setUp() throws IOException {

        testFile = new RandomAccessFile("sampleInput16.bin", "rw");
        World world = new World(new File("sampleInput16.bin"));
        world.sortFile();
        printer = new Printer(testFile);
    }


    @Test public void testRunIsExhausted() throws IOException {
        printer.print();

    }
}
