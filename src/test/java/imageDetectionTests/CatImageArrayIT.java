package imageDetectionTests;

import imageDetection.Application;
import imageDetection.ImageMatrix.CatImageArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
public class CatImageArrayIT {
    private ArrayList<String> perfectCatImage = new ArrayList<>();
    private CatImageArray mCatImageArray;
    /**
     * Create the necessary test files
     */
    @Before public void setUp() throws Exception {
        FileInputStream fis = new FileInputStream(Application.perfectCatPath);
        MockMultipartFile multipartFile = new MockMultipartFile("perfect_cat_image.txt", fis);
        CatImageArray catImageArray = new CatImageArray(multipartFile, 0);
        perfectCatImage = catImageArray.getPerfectImage(Application.perfectCatPath);
    }

    /**
     * Test that the perfect cat image is initialized to the correct dimensions when creating the CatImageArray
     *
     * @throws Exception
     */
    @Test public void testImageArray() throws Exception {
        int maxCol = -1;
        for(int i = 0; i < CatImageArray.PERFECT_CAT_PIXEL_COUNT; i++) {
            String catFile = "/" + String.valueOf(i) + "_cat_image.txt";
            FileInputStream fis = new FileInputStream(Application.fileDir + catFile);
            MockMultipartFile multipartFile = new MockMultipartFile(catFile, fis);
            CatImageArray catImageArray = new CatImageArray(multipartFile, 0);
            perfectCatImage = catImageArray.getPerfectCatImage();
            for (int row = 0; row < perfectCatImage.size(); row++) {
                maxCol = Math.max(maxCol, perfectCatImage.get(row).length());
            }
            assertEquals("col dimen are incorrect", CatImageArray.PERFECT_CAT_COL_COUNT, maxCol);
            assertEquals("row dimen are incorrect", CatImageArray.PERFECT_CAT_ROW_COUNT, perfectCatImage.size());
        }
    }

    /**
     * Creates 195 variations of the perfect cat image into files with the naming convention
     * [number of pixels matching original perfect cat image]_cat_image.txt
     * ex 9_cat_image.txt
     *
     *
     */
    private void createCatTestFiles()  {
        try {
            ArrayList<String> perfectCatImageCopy = new ArrayList<>(); //holds copy of perfect cat image

            for (String string : perfectCatImage){ //copy origin perfect cat image
                perfectCatImageCopy.add(string);
            }

            //make incremental changes to the cat file and store them in a file name that correlates
            for (int row = 0; row < CatImageArray.PERFECT_CAT_ROW_COUNT; row++) {
                String replace = "";
                for(int col = 0; col < CatImageArray.PERFECT_CAT_COL_COUNT; col++) {
                    if(perfectCatImageCopy.get(row) != null && perfectCatImageCopy.get(row).length() > 0) {
                        replace += "z";
                        int subString_index = Math.min(col + 1, perfectCatImageCopy.get(row).length());
                        perfectCatImageCopy.add(row, replace + perfectCatImageCopy.get(row).substring(subString_index));
                        perfectCatImageCopy.remove(row + 1);
                        String textFile = String.valueOf(CatImageArray.PERFECT_CAT_PIXEL_COUNT - (CatImageArray.PERFECT_CAT_COL_COUNT * row + col + 1)) + "_cat_image.txt";
                        Path file = Paths.get(Application.workingDir + "/" + textFile);
                        Files.write(file, perfectCatImageCopy, Charset.forName("UTF-8"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
