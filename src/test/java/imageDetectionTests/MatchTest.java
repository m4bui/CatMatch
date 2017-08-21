package imageDetectionTests;

import imageDetection.ResponseModel.Coordinate;
import imageDetection.ResponseModel.Match;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
public class MatchTest {

    @Test public void testGetPosition() {
        Coordinate coordinate = new Coordinate(-5, -8);
        Match match = new Match(coordinate, 74);
        assertSame("Position is not correct", coordinate, match.getPosition());
    }

    @Test public void testGetConfidenceValue() {
        Coordinate coordinate = new Coordinate(800, 500);
        Match match = new Match(coordinate, 2);
        for(int actual = 0; actual < 101; actual++) {
            if(actual == 2) {
                assertSame("Confidence value isn't the same", actual, match.getConfidenceValue());
            } else {
                assertNotSame("Confidence value is actually the same", actual, match.getConfidenceValue());
            }
        }
    }
}
