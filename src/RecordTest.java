import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Holds a single record
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class RecordTest {
    private byte[] aBite;


    /**
     * The setup for the tests
     */
    @Before public void setUp() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES + Double.BYTES);
        buffer.putLong(7);
        buffer.putDouble(8, 1);
        aBite = buffer.array();
    }


    /**
     * Tests the first constructor
     */
    @Test public void testConstruct1() {
        Record rec = new Record(aBite);
        assertEquals(1, rec.getKey(), 0.00);
        assertEquals(aBite, rec.getCompleteRecord());
        assertTrue(rec.toString().equals("1.0"));
    }


    /**
     * Tests the first constructor
     */
    @Test public void testCompareTo() {
        Record rec = new Record(aBite);
        Record recToBeCompared = new Record(aBite);
        assertEquals(rec.compareTo(recToBeCompared), 0);
    }


    /**
     * Tests for record equals method
     */
    @Test public void testEquals() {
        Record rec = new Record(aBite);
        Record recToBeCompared = new Record(aBite);
        assertTrue(rec.equals(recToBeCompared));
    }
}
