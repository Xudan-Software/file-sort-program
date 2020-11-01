import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Testing class for Externalsort
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class ExternalsortTest {

    /**
     * set up for tests
     */
    @Before public void setUp() {
        //nothing to set up.
    }


    /**
     * Get code coverage of the class declaration.
     */
    @Test public void testExternalsortInit() throws IOException {
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        Externalsort.main(null);
    }
}
