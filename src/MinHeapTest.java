import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinHeapTest {
    private MinHeap complexHeap;
    private MinHeap emptyHeap;


    @Before public void setUp() {
        complexHeap =
            new MinHeap(new Comparable[] { 10, 8, 9, 6, 5, 4, 3, 2, 1,null}, 9, 10);
        emptyHeap = new MinHeap(new Comparable[] {}, 0, 10);
    }


    @Test public void testBuildHeap() {
        Comparable[] array = { 10, 8, 4, 2, 0 };
        MinHeap heap = new MinHeap(array, 5, 10);
        Assert.assertArrayEquals(new Comparable[] { 0, 2, 4, 10, 8 }, array);

    }


    @Test public void testInsert() {
        complexHeap.insert(9);
        System.out.println("");
        Assert.assertArrayEquals(new Comparable[]{1,2,3,6,5,4,9,8,10,9},
            complexHeap.getArray());
    }

}
