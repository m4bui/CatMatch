package imageDetectionTests;

import imageDetection.ResponseModel.Coordinate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class CoordinateTest {

    @Test public void testPositiveCoordinate() {
        //Coordinate (5,8)
        int x = 5;
        int y = 8;
        Coordinate coordinate = new Coordinate(x, y);
        assertEquals("Coordinate x is incorrect", x, coordinate.getX());
        assertEquals("Coordinate y is incorrect", y, coordinate.getY());
    }

    @Test public void testNegativeCoordinate() {
        int x = -10;
        int y = -8;
        Coordinate coordinate = new Coordinate(x, y);
        assertEquals("Coordinate x is incorrect", x, coordinate.getX());
        assertEquals("Coordinate y is incorrect", y, coordinate.getY());
    }

    @Test public void testMixedSignCoordinate() {
        int x = 0;
        int y = -7;
        Coordinate coordinate = new Coordinate(x, y);
        assertEquals("Coordinate x is incorrect", x, coordinate.getX());
        assertEquals("Coordinate y is incorrect", y, coordinate.getY());

        int x1 = -8;
        int y1 = 100;
        Coordinate coordinate1 = new Coordinate(x1, y1);
        assertEquals("Coordinate x is incorrect", x1, coordinate1.getX());
        assertEquals("Coordinate y is incorrect", y1, coordinate1.getY());
    }

}
