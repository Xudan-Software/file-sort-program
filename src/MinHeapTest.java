import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Test the MinHeap class.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class MinHeapTest {
    TestHelper testHelper = new TestHelper();
    private MinHeap complexHeap;
    private InputBuffer complexInputBuffer;


    /**
     * Setup heaps for tests.
     */
    @Before public void setUp() throws IOException {
        Record[] tenRecordArray = new Record[10];
        RandomAccessFile heapFile = testHelper
            .createRecordFileForTests("tempComplexHeapRecords.bin", 8192);
        complexInputBuffer = new InputBuffer(1024, heapFile);
        complexHeap = new MinHeap(tenRecordArray, 0, 10, complexInputBuffer);
    }


    /**
     * Delete any test files created for test runs.
     */
    @After public void tearDown() {
        testHelper.deleteTestFiles();
    }


    /**
     * Test that a call to remove an item from the heap, when the heap is null,
     * causes the heap to fill itself from the buffer.
     */
    @Test public void testInitialize() throws IOException {
        Assert.assertEquals(0, complexHeap.heapsize());
        complexHeap.removemin();
        Assert.assertEquals(10,
            complexHeap.heapsize() + complexHeap.numBadVals());
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
            complexHeap.removemin();
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


    /**
     * Test that the min buffer does not give back a bunch of zeros when
     * reading from an unsorted file which is not a divisible of a block
     * size. Instead it should throw an error.
     */
    @Test(expected = IllegalStateException.class)
    public void testBufferWithLargeNonBlockMultipleUnsortedFile()
        throws IOException {
        RandomAccessFile weirdFile = testHelper.createRecordFileForTests("weird.bin", 500);
        InputBuffer weirdBuffer = new InputBuffer(1024, weirdFile);
        MinHeap heap = new MinHeap(new Record[1000], 0, 1000, weirdBuffer);
        for (int i=0; i<501; i++) {
            heap.removemin();
        }
    }
}
