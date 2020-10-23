import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinHeapTest {
    private MinHeap<Integer> complexHeap;
    private MinHeap<Integer> emptyHeap;


    @Before public void setUp() {
        complexHeap =
            new MinHeap(new Comparable[] { 10, 8, 9, 6, 5, 4, 3, 2, 1, null },
                9, 10);
        emptyHeap = new MinHeap(new Comparable[10], 0, 10);
    }


    @Test public void testBuildHeap() {
        Comparable<Integer>[] array = new Integer[]{ 10, 8, 4, 2, 0 };
        new MinHeap(array, 5, 10);
        Assert.assertArrayEquals(new Comparable[] { 0, 2, 4, 10, 8 }, array);

    }


    @Test public void testInsertIntoAlmostFullHeap() {
        complexHeap.insert(9);
        Assert.assertArrayEquals(
            new Comparable[] { 1, 2, 3, 6, 5, 4, 9, 8, 10, 9 },
            complexHeap.getArray());
    }

    @Test public void testInsertIntoEmptyHeap() {
        emptyHeap.insert(5);
        Assert.assertEquals(5, emptyHeap.getArray()[0]);
        emptyHeap.insert(4);
        Assert.assertEquals(4, emptyHeap.getArray()[0]);
    }

}
