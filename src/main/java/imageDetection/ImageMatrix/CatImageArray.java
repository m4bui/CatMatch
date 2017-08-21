package imageDetection.ImageMatrix;


import imageDetection.Application;
import imageDetection.ResponseModel.Match;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public class CatImageArray extends ImageArray {

    public static final int PERFECT_CAT_ROW_COUNT = 13;
    public static final int PERFECT_CAT_COL_COUNT = 15;
    public static final int PERFECT_CAT_PIXEL_COUNT = PERFECT_CAT_ROW_COUNT * PERFECT_CAT_COL_COUNT; //13x15

    private ArrayList<String> mPerfectCatImage = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();

    private int mThreshold; //if user doesn't specify confidence value will set it to this default value

    public CatImageArray(MultipartFile file, int threshold) {
        //call parent class to initalize the confidence values 0 - 100
        super();
        mPerfectCatImage = getPerfectImage("/Users/michellebui/Desktop/cat_match/perfect_cat_image.txt");
        mImage = setImageArray(file);
        this.mThreshold = threshold;
    }

    public ArrayList<Match> getMatches() {
        setConfidenceValues(mPerfectCatImage, mImage, PERFECT_CAT_PIXEL_COUNT);
        ArrayList<Match> matches = getAllMatches(mThreshold, getConfidenceValues());
        return removeOverlappingMatches(PERFECT_CAT_ROW_COUNT, PERFECT_CAT_COL_COUNT, matches, getClientImageCol());
    }

    public ArrayList<String> getPerfectCatImage() {
        return mPerfectCatImage;
    }
}
