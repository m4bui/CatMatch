package imageDetection;

import imageDetection.ImageMatrix.CatImageArray;
import imageDetection.ResponseModel.Coordinate;
import imageDetection.ResponseModel.Match;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ImageMatchingController {
    /**
     * The Image Detection API detects cat images given a text file
     */
    private final String version = "/imageDetect/v1";

    @RequestMapping("/")
    public String defaultPage() {
        return "imageDetection";
    }

    /**
     * Detects the perfect cat image in a given text file representing a data frame
     *
     * @param file text file representing data frame
     * @param threshold confidence value of image match, defaults to 75
     * @return coordinates with confidence value greater or equal to threshold.
     */
    @RequestMapping(value = version + "/cat", method = RequestMethod.POST, consumes = "multipart/form-data", produces="application/json")
    public ResponseEntity<ArrayList<Match>> create(@RequestParam("file") MultipartFile file, @RequestParam(value = "threshold", defaultValue = "75") int threshold) {
        System.out.println("---------INSIDE ORDER----------");
        ArrayList<Match> matches;
        CatImageArray catImageArray = new CatImageArray(file, threshold);
        matches = catImageArray.getMatches();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
}

