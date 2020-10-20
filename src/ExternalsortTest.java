import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * @author {Your Name Here}
 * @version {Put Something Here}
 */
public class ExternalsortTest{
    
    
    /**
     * set up for tests
     */
    @Before
    public void setUp() {
        //nothing to set up.
    }
    
    /**
     * Get code coverage of the class declaration.
     */
    @Test
    public void testExternalsortInit() {
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        Externalsort.main(null);
    }

}
