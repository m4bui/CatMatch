package imageDetectionTests;

import imageDetection.Application;
import imageDetection.Helper.Range;
import imageDetection.ImageMatrix.CatImageArray;
import imageDetection.ImageMatrix.ImageArray;
import imageDetection.ResponseModel.Coordinate;
import imageDetection.ResponseModel.Match;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class ImageArrayTest {

    private ImageArray mImageArray;
    private ArrayList<String > mPerfectCatImage;
    public static final String imageWithCats = ImageMatchingControllerIT.fileDir + "image_with_cats.txt";

    @Before
    public void setUp() {
        mImageArray = new ImageArray();
        mPerfectCatImage = mImageArray.getPerfectImage(Application.perfectCatPath);

    }

    /**
     *  sanity check tests
     */

    @Test
    public void Should_ReturnAllRowsWithSameLength_When_RowColSpecified() {
        for(String string : mPerfectCatImage) {
            assertEquals(CatImageArray.PERFECT_CAT_COL_COUNT, string.length());
        }
    }

    @Test
    public void Should_ReturnImageWithSpecifiedMaxRow_When_MaxRowSpecified() {
        ArrayList<String> image = mImageArray.getPerfectImage(Application.perfectCatPath);
        assertEquals(CatImageArray.PERFECT_CAT_ROW_COUNT, image.size());
    }

    /**
     * getConfidenceValues() test
     */

    @Test
    public void Should_BeSize101_When_ImageArrayInstantiated() {
        assertEquals(101, mImageArray.getConfidenceValues().size());
    }


    /**
     *  getConfidenceValue() Tests
     */
    @Test //General level of confidence test case
    public void Should_Return100_When_CalledWithSameImage() {
        Coordinate origin = new Coordinate(0,0);
        ArrayList<String> image = mImageArray.getPerfectImage(Application.perfectCatPath);
        int condfidenceValue = mImageArray.getConfidenceValue(origin, image, image, CatImageArray.PERFECT_CAT_PIXEL_COUNT );
        assertEquals(100, condfidenceValue);
    }

    @Test
    public void Should_Return0_When_CalledWithEmptyFile() throws Exception {
        ArrayList<String> emptyfile = new ArrayList<>();
        Coordinate origin = new Coordinate(0,0);
        confidenceValueTest(emptyfile, origin,0);
    }

    @Test
    public void Should_Return50_When_CalledWith98FilledPixels() throws Exception {
        ArrayList<String> fiftyPercentImageFile = new ArrayList<>();
        Coordinate origin = new Coordinate(0,0);
        int rows = 98 /CatImageArray.PERFECT_CAT_COL_COUNT; // 6.5 -> 6
        int col =  98 % CatImageArray.PERFECT_CAT_COL_COUNT ; // 8
        System.out.println("number of rows is " + rows);
        System.out.println("number of cols " + col);
        for(int row = 0; row < rows; row++) {
            fiftyPercentImageFile.add(mPerfectCatImage.get(row));
        }

        String lastCopy = mPerfectCatImage.get(rows).substring(0, col);
        for(int i = 0; i < CatImageArray.PERFECT_CAT_COL_COUNT - col; i++) {
            lastCopy += "z";
        }

        fiftyPercentImageFile.add(lastCopy);

        String filler = "";
        for(int i = 0; i < CatImageArray.PERFECT_CAT_COL_COUNT; i++) {
            filler += "z";
        }

        for(int row = rows; row < CatImageArray.PERFECT_CAT_ROW_COUNT; row++) {
            fiftyPercentImageFile.add(filler);
        }

        for(String line : fiftyPercentImageFile) {
            System.out.println(line);
        }

        confidenceValueTest(fiftyPercentImageFile, origin,50);
    }

    /**
     *  getDifference() Tests
     */
    @Test
    public void Should_Return3_When_ComparingAttackWithAttackbcd() {
        int difference = mImageArray.getDifference("Attack", "Attackbcd");
        assertEquals(3, difference);
    }

    @Test
    public void Should_Return2_When_ComparingYumWithY() {
        assertEquals(2, mImageArray.getDifference("Yum", "Y"));
    }

    @Test
    public void Should_ReturnStringLength_When_ComparingStringWithEmptyString() {
        String string = "ThisIsAString";
        String emptyString = "";
        assertEquals(string.length(), mImageArray.getDifference(string, emptyString));
    }


    //Helper Method to get Confidence Value
    public void confidenceValueTest(ArrayList<String> imageToCompare, Coordinate coordinate, int expected) {
        ArrayList<String> image = mImageArray.getPerfectImage(Application.perfectCatPath);
        int confidenceValue = mImageArray.getConfidenceValue(coordinate, image, imageToCompare, CatImageArray.PERFECT_CAT_PIXEL_COUNT);
        assertEquals(expected, confidenceValue);

    }


    /**
     * test getRanges for a given coordinate to see what area it covers
     */


    @Test
    public void Should_ReturnCorrectLowRanges_WhenGiven8047() {
        ArrayList<Range> ranges = mImageArray.getMatrixArea(new Coordinate(80,47), CatImageArray.PERFECT_CAT_ROW_COUNT, CatImageArray.PERFECT_CAT_COL_COUNT, 100);
        int inc = 0;
        for(Range range : ranges) {
            assertEquals(8047 + inc, range.getLow());
            inc += 100;
        }
    }


    @Test
    public void Should_ReturnRanges_WhenGiven4984() {
        ArrayList<Range> ranges = mImageArray.getMatrixArea(new Coordinate(49,84), CatImageArray.PERFECT_CAT_ROW_COUNT, CatImageArray.PERFECT_CAT_COL_COUNT, 100);
        int inc = 0;
        for(Range range : ranges) {
            assertEquals(4984 + inc, range.getLow());
            inc += 100;
        }
    }

    @Test
    public void Should_GetRange_WhenPositionx0y0() {
        ArrayList<Range> ranges = mImageArray.getMatrixArea(new Coordinate(0,0), CatImageArray.PERFECT_CAT_ROW_COUNT, CatImageArray.PERFECT_CAT_COL_COUNT, CatImageArray.PERFECT_CAT_COL_COUNT);
        int inc = 0;
        for(Range range : ranges) {
            assertEquals(0 + inc, range.getLow());
            inc += CatImageArray.PERFECT_CAT_COL_COUNT;
        }
    }



    @Test
    public void translate90DegreesTest() {
        ArrayList<String> translatedImage = mImageArray.translate90Degrees(mPerfectCatImage);
        printImage("90 Degrees", translatedImage);
        testDimen90n270(translatedImage, mPerfectCatImage);
    }

    @Test
    public void translate180DegreesTest() {
        ArrayList<String> translatedImage = mImageArray.translate180Degrees(mPerfectCatImage);
        printImage("180 Degrees", translatedImage);
        assertEquals("row dimension incorrect", translatedImage.size(), mPerfectCatImage.size());
        for(int i = 0; i < translatedImage.size(); i++)
            assertEquals("col dim incorrect", translatedImage.get(i).length(), mPerfectCatImage.get(i).length());
    }

    @Test
    public void translate270DegreesTest() {
        ArrayList<String> translatedImage = mImageArray.translate270Degrees(mPerfectCatImage);
        printImage("270 Degrees", translatedImage);
        testDimen90n270(translatedImage, mPerfectCatImage);
    }

    /**
     *  getGridPosition
     */

    @Test
    public void Should_GetGridPosition() {
        ArrayList<String> image = mImageArray.getPerfectImage(imageWithCats);
        int maxCol = image.get(0).length();
        int pixel = 0;
        for(int row = 0; row < image.size(); row++) {
            for(int col = 0; col < maxCol ; col++) {
                assertEquals(pixel, mImageArray.getGridPosition(row, col, maxCol));
                pixel++;
            }
        }
    }

    @Test
    public void Should_ReturnPosition5_WhenCoordinatex0y5() {
        assertEquals(5, mImageArray.getGridPosition(0,5, CatImageArray.PERFECT_CAT_COL_COUNT));
    }

    @Test
    public void Should_Get8484_When_Positionx84y84() {
        ArrayList<String> image = mImageArray.getPerfectImage(imageWithCats);
        int maxCol = image.get(0).length();
        assertEquals(8484, mImageArray.getGridPosition(84, 84, maxCol));
    }

    @Test
    public void Should_Get4984_WhenPositionx49y84() {
        ArrayList<String> image = mImageArray.getPerfectImage(imageWithCats);
        int maxCol = image.get(0).length();
        assertEquals(4984, mImageArray.getGridPosition(49, 84, maxCol));
    }

    @Test
    public void Should_Get8047_WhenPositionx80y47() {
        ArrayList<String> image = mImageArray.getPerfectImage(imageWithCats);
        int maxCol = image.get(0).length();
        assertEquals(8047, mImageArray.getGridPosition(80, 47, maxCol));
    }

    @Test
    public void Should_Get4042_WhenPositionx40y42() {
        ArrayList<String> image = mImageArray.getPerfectImage(imageWithCats);
        int maxCol = image.get(0).length();
        assertEquals(4042, mImageArray.getGridPosition(40, 42, maxCol));
    }


    @Test
    public void Should_Get80_WhenPositionx0y80() {
        ArrayList<String> image = mImageArray.getPerfectImage(imageWithCats);
        int maxCol = image.get(0).length();
        assertEquals(80, mImageArray.getGridPosition(0, 80, maxCol));
    }

    @Test
    public void Should_Get145_WhenPositionx1y45() {
        ArrayList<String> image = mImageArray.getPerfectImage(imageWithCats);
        int maxCol = image.get(0).length();
        assertEquals(145, mImageArray.getGridPosition(1, 45, maxCol));
    }

    @Test
    public void Should_ReturnFalse_PositionOutOfRanges() {
        ArrayList<Range> range = new ArrayList<>();
        range.add(new Range(50,80));

        ArrayList<Range> rangeToCompare = new ArrayList<>();
        rangeToCompare.add(new Range(18,22));
        rangeToCompare.add(new Range(0, 8));
        rangeToCompare.add(new Range(24, 30));
        assertFalse(mImageArray.isImageOverlap(range, rangeToCompare));
    }

    @Test
    public void Should_ReturnTrue_PositionInRange() {
        ArrayList<Range> rangeToCompare = new ArrayList<>();
        rangeToCompare.add(new Range(0,2));

        ArrayList<Range> range = new ArrayList<>();
        range.add(new Range(18,22));
        range.add(new Range(0, 8));
        range.add(new Range(24, 30));
        assertTrue(mImageArray.isImageOverlap(range, rangeToCompare));
    }

    @Test
    public void Should_Return4056_Positionx40y41() {
        ArrayList<Range> ranges = mImageArray.getMatrixArea(new Coordinate(40, 41), CatImageArray.PERFECT_CAT_ROW_COUNT, CatImageArray.PERFECT_CAT_COL_COUNT, 100);
        for(Range range : ranges) {
            System.out.println(range.getLow() + "-" + range.getHigh());
        }

    }

    public void testDimen90n270(ArrayList<String> imageArray, ArrayList<String> perfectImageArray) {
        assertEquals("row dimension incorrect", imageArray.size(), perfectImageArray.get(0).length());
        for(int i = 0; i < imageArray.size(); i++)
            assertEquals("col dim incorrect", imageArray.get(i).length(), perfectImageArray.size());
    }

    public void printImage(String title, ArrayList<String> imageArray) {
        System.out.println("**************************** " + title + " ********************************");
        for (String string : imageArray) {
            System.out.println(string);
        }
    }

}
