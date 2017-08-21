package imageDetectionTests;

import imageDetection.Helper.Range;
import imageDetection.ImageMatrix.CatImageArray;
import imageDetection.ImageMatrix.ImageArray;
import imageDetection.ResponseModel.Coordinate;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ImageArrayIT {
    private ImageArray mImageArray;
    private ArrayList<String > mPerfectCatImage;

    @Before
    public void setUp() {
        mImageArray = new ImageArray();
        mPerfectCatImage = mImageArray.getPerfectImage(CatImageArray.perfectCatPath);

    }
    @Test
    public void Should_Return3Ranges_Coordinate2x2() {
        ArrayList<Range> ranges = mImageArray.getMatrixArea(new Coordinate(2,2), 3, 3, 7);
        int rowChange = 0;
        int colChange = 2;
        for(Range range : ranges) {
            assertEquals(16 + rowChange, range.getLow());
            assertEquals(16 + rowChange  + colChange, range.getHigh());
            rowChange += 7;

        }
    }

    @Test
    public void Should_Test() {
//        mImageArray.removeOverlappingMatches()
    }

    @Test
    public void ShouldReturn1Coordinate_Overlap() {
        ArrayList<String> image = mImageArray.getPerfectImage(ImageMatchingControllerIT.imageWithCats);
        System.out.println("image row size is " +  image.size());
        System.out.println("image col size is " + image.get(0).length());
    }
}
