import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Test class for testing the InputBuffer.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class InputBufferTest {
    TestHelper testHelper = new TestHelper();
    RandomAccessFile randAccFile;


    /**
     * Set up state for use in tests.
     *
     * @throws IOException if a file with the given name already exists.
     */
    @Before public void setUp() throws IOException {
        randAccFile = testHelper.createRecordFileForTests("temp.bin", 100);
    }


    /**
     * Test that we can create a new InputBuffer with a good file and it does
     * not break.
     *
     * @throws IOException if there is an error loading data from the random
     *                     access file.
     */
    @Test public void testInitialization() throws IOException {
        new InputBuffer(10, randAccFile);
    }
}
