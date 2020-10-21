import org.junit.Assert;
import org.junit.Test;

public class MinHeapTest {


    @Test
    public void testBuildHeap(){
        Comparable[] array={10,8,4,2,0};
        MinHeap heap=new MinHeap(array,5,10);
        Assert.assertArrayEquals(new Comparable[] {0,2,4,10,8},array);

    }

}
