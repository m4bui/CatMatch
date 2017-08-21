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

@RunWith(SpringJUnit4ClassRunner.class)
public class ImageMatchingControllerIT {
    @Test
    public void testConfidenceValues() throws Exception {
        String fileDir = "/Users/michellebui/Desktop/cat_match/";
        String catFiles = "_cat_image.txt";
        for(int i = 0; i < CatImageArray.PERFECT_CAT_PIXEL_COUNT; i++) {
            double expectConfidenceValue = (i / (double)CatImageArray.PERFECT_CAT_PIXEL_COUNT) * 100;
            String catFile = String.valueOf(i) + catFiles;
            FileInputStream fis = new FileInputStream(fileDir + catFile);
            MockMultipartFile multipartFile = new MockMultipartFile(catFile, fis);
            ImageMatchingController controller  = new ImageMatchingController();
            ResponseEntity<ArrayList<Match>> responseEntity = controller.create(multipartFile, 0);
            ArrayList<Match> matches = responseEntity.getBody();

            if(i == 0) {
                System.out.println("Matches size is: " + matches.size());
                System.out.println("Status code is: " + responseEntity.getStatusCode());
            }

            for(Match match : matches) {
                String coordinateErr = match.getPosition().getX() + "," + match.getPosition().getY();
                if (match.getPosition().getX() == 0 && match.getPosition().getY() == 0)
                    assertEquals("Percentage doesn't match for coordinate: " + coordinateErr, (int)expectConfidenceValue, match.getConfidenceValue());
            }
        }
    }

    @Test
    public void testResultArraySize() throws Exception {

    }

}
