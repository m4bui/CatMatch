package imageDetectionTests;

import imageDetection.Application;
import imageDetection.Helper.Range;
import imageDetection.ImageMatrix.CatImageArray;
import imageDetection.ImageMatrix.ImageArray;
import imageDetection.ResponseModel.Coordinate;
import imageDetection.ResponseModel.Match;
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
        mPerfectCatImage = mImageArray.getPerfectImage(Application.perfectCatPath);

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
    public void Should_GetPerfectPixelCount195_When_AllMatches() {
        ArrayList<String> image = mImageArray.getPerfectImage(Application.perfectCatPath);

        mImageArray.setConfidenceValues(mPerfectCatImage, image, CatImageArray.PERFECT_CAT_PIXEL_COUNT );
        ArrayList<ArrayList<Coordinate>> confidenceValues = mImageArray.getConfidenceValues();
        ArrayList<Match> matches = mImageArray.getAllMatches(0, confidenceValues);
        assertEquals(CatImageArray.PERFECT_CAT_PIXEL_COUNT, matches.size());
    }

}
