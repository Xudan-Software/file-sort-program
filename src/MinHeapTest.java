import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Test the MinHeap class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class MinHeapTest {
    private final Random value = new Random();
    private MinHeap complexHeap;
    private InputBuffer complexInputBuffer;
    private ArrayList<File> testFiles = new ArrayList<>();


    private RandomAccessFile createRecordFileForTests(
        String filename, int numRecords) throws IOException {
        File newFile = new File(filename);
        boolean created = newFile.createNewFile();
        if (!created) {
            throw new FileAlreadyExistsException(filename);
        }
        RandomAccessFile raFile = new RandomAccessFile(newFile, "rw");
        DataOutputStream file = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(filename)));
        long val;
        double val2;
        for (int i = 0; i < numRecords; i++) {
            val = value.nextLong();
            file.writeLong(val);
            val2 = value.nextDouble();
            file.writeDouble(val2);
        }
        file.flush();
        file.close();
        testFiles.add(newFile);
        return raFile;
    }


    /**
     * Setup heaps for tests.
     */
    @Before public void setUp() throws IOException {
        Record[] tenRecordArray = new Record[10];
        // This is a file with 8192 records!
        RandomAccessFile heapFile =
            createRecordFileForTests("tempComplexHeapRecords.bin", 8192);
        complexInputBuffer = new InputBuffer(1024, heapFile);
        complexHeap = new MinHeap(tenRecordArray, 0, 10, complexInputBuffer);
    }


    @After public void tearDown() {
        for (File f : testFiles) {
            f.delete();
        }
    }


    /**
     * Test that initializing inserts ten items into the heap.
     */
    @Test public void testInitialize() throws IOException {
        Assert.assertEquals(0, complexHeap.heapsize());
        complexHeap.initialize();
        Assert.assertEquals(10, complexHeap.heapsize());
    }


    /**
     * Test that complex heap is not finished when the buffer it uses is
     * not exhausted.
     */
    @Test public void testIsFinishedWhenBufferNotExhausted()
        throws IOException {
        Assert.assertFalse(complexHeap.isFinished());
    }


    /**
     * Test that complex heap is finished when the buffer it uses is exhausted.
     */
    @Test public void testIsFinishedWhenBufferAndHeapExhausted()
        throws IOException {
        for (int i = 0; i < 8192; i++) {  // remove all values from the heap
            System.out.println(i);
            System.out.println(complexHeap.removemin());
        }
        Assert.assertTrue(complexHeap.isFinished());
        Assert.assertTrue(complexInputBuffer.isExhausted());
    }


    /**
     * Test that complex heap is finished when the buffer it uses is exhausted.
     */
    @Test(expected = IllegalStateException.class)
    public void testRemoving1MoreRecordThanExistsThrowsException()
        throws IOException {
        for (int i = 0; i < 8193; i++) {  // remove all values from the heap
            complexHeap.removemin();
        }
    }
}
