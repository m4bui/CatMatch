package imageDetectionTests;

import imageDetection.Helper.Range;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class RangeTest {

    private final String OUT_OF_RANGE = "Value is out of range";

    @Test
    public void positiveValues() {
        int low = 5;
        int high = 10;
        Range range = new Range(low, high);
        for(; low <= high; low ++) {
            assertTrue(OUT_OF_RANGE, range.contains(low));
        }
    }

    @Test public void negativeValues() {
        int low = -100;
        int high = -5;
        Range range = new Range(low, high);
        for(; low <= high; low ++) {
            assertTrue(OUT_OF_RANGE, range.contains(low));
        }
    }

    @Test public void mixedSignValues() {
        int low = -100;
        int high = 5;
        Range range = new Range(low, high);
        for(; low <= high; low ++) {
            assertTrue(OUT_OF_RANGE, range.contains(low));
        }
    }

    @Test public void incorrectRangeValues() {
        int low = 10;
        int high = 5;

        Range range = new Range(low, high);
        for(; high < low; high++) {
            assertFalse(OUT_OF_RANGE, range.contains(high));
        }

    }
}
