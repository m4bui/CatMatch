package imageDetection.ImageMatrix;


import imageDetection.ResponseModel.Coordinate;
import imageDetection.ResponseModel.Match;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CatImageArray extends ImageArray {

    public static final int PERFECT_CAT_ROW_COUNT = 13;
    public static final int PERFECT_CAT_COL_COUNT = 15;
    public static final int PERFECT_CAT_PIXEL_COUNT = PERFECT_CAT_ROW_COUNT * PERFECT_CAT_COL_COUNT; //13x15

    public static final String perfectCatPath = "/Users/michellebui/Desktop/cat_match/perfect_cat_image.txt";

    private ArrayList<String> mPerfectCatImage = new ArrayList<>();
    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<Match> mMatches = new ArrayList<>();

    private int mThreshold = 85; //if user doesn't specify confidence value will set it to this default value

    public CatImageArray(MultipartFile file, int threshold) {
        //call parent class to initalize the confidence values 0 - 100
        super();
        mPerfectCatImage = getPerfectImage(perfectCatPath);
        mImage = setImageArray(file);
        this.mThreshold = threshold;
        System.out.println("file name is: " + file.getName());
        System.out.println("original file name is: " + file.getOriginalFilename());
    }

    public ArrayList<Match> getMatches() {
        setConfidenceValues(mPerfectCatImage, mImage, PERFECT_CAT_PIXEL_COUNT);
        ArrayList<Match> matches = getAllMatches(mThreshold, getConfidenceValues());
        return removeOverlappingMatches(PERFECT_CAT_ROW_COUNT, PERFECT_CAT_COL_COUNT, matches, getClientImageCol());
    }
}
