package imageDetectionTests;

import imageDetection.ImageMatchingController;
import imageDetection.ImageMatrix.CatImageArray;
import imageDetection.ResponseModel.Match;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class ImageMatchingControllerIT {

    public static final String workingDir = System.getProperty("user.dir");
    public static final String fileDir = workingDir +  "/src/test/cat_image_files/";
    public static final String catFiles = "_cat_image.txt";
    public static final String imageWithCats = fileDir + "image_with_cats.txt";

    @Test
    public void testConfidenceValues() throws Exception {
        for(int i = 0; i < CatImageArray.PERFECT_CAT_PIXEL_COUNT; i++) {
            double expectConfidenceValue = (i / (double)CatImageArray.PERFECT_CAT_PIXEL_COUNT) * 100;
            String catFile = String.valueOf(i) + catFiles;
            FileInputStream fis = new FileInputStream(fileDir + catFile);
            MockMultipartFile multipartFile = new MockMultipartFile(catFile, fis);
            ImageMatchingController controller  = new ImageMatchingController();
            ResponseEntity<ArrayList<Match>> responseEntity = controller.create(multipartFile, 0);
            ArrayList<Match> matches = responseEntity.getBody();

            for(Match match : matches) {
                String coordinateErr = match.getPosition().getX() + "," + match.getPosition().getY();
                if (match.getPosition().getX() == 0 && match.getPosition().getY() == 0)
                    assertEquals("Percentage doesn't match for coordinate: " + coordinateErr, (int)expectConfidenceValue, match.getConfidenceValue());
            }
        }
    }

    @Test
    public void Should_Return6_WhenThreshold90() throws Exception {
        String catFile = "image_with_cats.txt";
        FileInputStream fis = new FileInputStream(fileDir + catFile);
        MockMultipartFile multipartFile = new MockMultipartFile(catFile, fis);
        ImageMatchingController controller  = new ImageMatchingController();
        ResponseEntity<ArrayList<Match>> responseEntity = controller.create(multipartFile, 90);
        ArrayList<Match> matches = responseEntity.getBody();
        assertEquals(6, matches.size());

    }

    @Test
    public void Should_Return1_WhenThreshold0() throws Exception {
        String catFile = "perfect_cat_image.txt";
        FileInputStream fis = new FileInputStream(fileDir + catFile);
        MockMultipartFile multipartFile = new MockMultipartFile(catFile, fis);
        ImageMatchingController controller  = new ImageMatchingController();
        ResponseEntity<ArrayList<Match>> responseEntity = controller.create(multipartFile, 0);
        ArrayList<Match> matches = responseEntity.getBody();
        assertEquals(1, matches.size());

    }

    @Test
    public void Should_Return0_WhenEmptyFile() throws Exception {
        String catFile = "emptyfile.txt";
        FileInputStream fis = new FileInputStream(fileDir + catFile);
        MockMultipartFile multipartFile = new MockMultipartFile(catFile, fis);
        ImageMatchingController controller  = new ImageMatchingController();
        ResponseEntity<ArrayList<Match>> responseEntity = controller.create(multipartFile, 0);
        ArrayList<Match> matches = responseEntity.getBody();
        assertEquals(0, matches.size());

    }


    @Test
    public void Should_ReturnTrueLessThan51_WhenThreshold0() throws Exception {
        FileInputStream fis = new FileInputStream(imageWithCats);
        MockMultipartFile multipartFile = new MockMultipartFile("image_with_cats.txt", fis);
        ImageMatchingController controller  = new ImageMatchingController();
        ResponseEntity<ArrayList<Match>> responseEntity = controller.create(multipartFile, 0);
        ArrayList<Match> matches = responseEntity.getBody();
        //51 is the max possible cats you can find in the cats with image. 100 x 100 / 195
        //if matches array has no overlap it's size is definitely 51 or under
        assertTrue(matches.size() <= 51);

    }

}
