package imageDetection.ImageMatrix;

import com.sun.istack.internal.Nullable;
import imageDetection.Helper.Range;
import imageDetection.ResponseModel.Coordinate;
import imageDetection.ResponseModel.Match;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageArray {

    protected static final int PERFECT_MATCH = 100;
    protected ArrayList<ArrayList<Coordinate>> mConfidenceValues = new ArrayList<>();
    private int mClientImageRow = 0;
    private int mClientImageCol = 0;

    private int mPerfectMatchRow;
    private int mPerfectMatchCol;

    /**
     * initialize confidence value array which represents the level of confidence that the perfect image
     * is at the starting coordinate of the image being searched. 0 being not confident and 100 being confident
     */
    public ImageArray() {
        for(int i = 0; i < 101; i++) {
            mConfidenceValues.add(new ArrayList<>());
        }
    }
    /**
     * Gets an array list representation of the textfile image
     * @param path
     * @return
     */
    public ArrayList<String> getPerfectImage(String path) {
        ArrayList<String> perfectImage = new ArrayList<>();
        ArrayList<String> finalPerfectImage = new ArrayList<>();
        try {
            int maxLength = 0;
            for(String line : Files.readAllLines(Paths.get(path))) {
                //save the longest string length to use as col
                maxLength = Math.max(maxLength, line.length());
                if(line.length() == 0)
                    break;
                else
                    perfectImage.add(line);
            }

            if(perfectImage != null && perfectImage.size() > 0) {
                mPerfectMatchRow = perfectImage.size();
                mPerfectMatchCol = maxLength;
            }

            //Create a perfect grid for easier computing
            for(String line : perfectImage) {
                while(line.length() < mPerfectMatchCol)
                    line += " ";
                finalPerfectImage.add(line);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return  finalPerfectImage;
    }

    public ArrayList<String> setImageArray(MultipartFile file) {
        ArrayList<String> image = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line; //to read lines from file
            int maxLength = 0; //file's longest string

            while ((line = bufferedReader.readLine()) != null)
            {
                //save the longest string length to use as col
                maxLength = Math.max(maxLength, line.length());
                //character that doesn't exist in the file this is to account for the case when there's an empty line
                if(line.length() == 0)
                    break;
                else
                    image.add(line);
            }

            if(image != null && image.size() > 0) {
                mClientImageRow = image.size();
                mClientImageCol = maxLength;
            }
            System.out.println("client max row is " +  mClientImageRow);
            System.out.println("client max col is " + mClientImageCol);

        } catch(IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public int getClientImageCol() {
        return mClientImageCol;
    }

    public int getmClientImageRow() {
        return mClientImageRow;
    }
    /**
     * Get confidence values of image match
     * @return array 0-100 representing the confidence 0 (low) to 100 (high). Within each confidence index
     * are coordinates that match the condifence level
     *
     */
    public ArrayList<ArrayList<Coordinate>> getConfidenceValues() {
        return mConfidenceValues;
    }

    /**
     * Set confidence values array mapping the coordinate and their confidence value to the correct index 0-100
     * @param mPerfectImage image to search for
     * @param image image being searched
     * @param perfectImagePixelCount the total pixels of the perfect image
     */
    public void setConfidenceValues( ArrayList<String> mPerfectImage, ArrayList<String> image, int perfectImagePixelCount) {
        for (int row = 0; row < image.size(); row++) {
            for(int col = 0; col < image.get(row).length(); col++) {
                Coordinate coordinate = new Coordinate(row,col);
                mConfidenceValues.get(getConfidenceValue(coordinate, mPerfectImage, image, perfectImagePixelCount)).add(coordinate);
            }
        }
    }

    /**
     * Gets the confidence value 0(no match) to 100(strong match) of the image matching the perfect image
     * @param startingPoint (x,y) coordinate of the start of the image to compare
     * @param perfectImage the perfect image
     * @param image the image to search for the perfect image
     * @param perfectImagePixelCount the number of pixels that make up the perfect image
     * @return
     */
    public int getConfidenceValue(Coordinate startingPoint, ArrayList<String> perfectImage, ArrayList<String> image, int perfectImagePixelCount) {
        double confidenceValue = 0;
        int imageDifference = 0;
        int row = startingPoint.getX();
        int col = startingPoint.getY();
        if(image != null && image.size() > 0) {
            for(int i = 0; i < perfectImage.size(); ++i) {
                int rowToSearch = row + i;
                if(rowToSearch >= image.size()) //no more rows left to search
                    return (int) confidenceValue;
                int imageColEnd = col + mPerfectMatchCol;
                String perfectImageString = perfectImage.get(i);
                while(perfectImageString.length() < mPerfectMatchCol)
                    perfectImageString += " ";

                String imageString = image.get(rowToSearch);
                while(imageString.length() < mClientImageCol)
                    imageString += " ";

                if (imageColEnd > mClientImageCol - 1 || col > image.get(rowToSearch).length() - 1) //make sure we're within bounds to call substr
                    imageDifference += getDifference(perfectImageString, imageString.substring(col));
                else
                    imageDifference += getDifference(perfectImageString, imageString.substring(col, imageColEnd));
            }
            confidenceValue = (perfectImagePixelCount - imageDifference)/(double)perfectImagePixelCount * 100;
        }
        return (int)confidenceValue;
    }

    /**
     * Returns all matches equal to or greater than threshold given
     * @param threshold
     * @param confidenceValues
     * @return all possible matches for the threshold specified
     */
    public ArrayList<Match> getAllMatches(int threshold, ArrayList<ArrayList<Coordinate>> confidenceValues) {
        ArrayList<Match> matches = new ArrayList<>(); //contains all matches: coordinates w/ confidence value

        if (confidenceValues == null)
            return matches;

        int currThreshold = PERFECT_MATCH; //start current Threshold at 100: perfect match

        //Start with perfect match and move down to threshold adding in all coordinates that qualify
        while (currThreshold >= threshold) {
            for(Coordinate coordinate : confidenceValues.get(currThreshold)) {
                matches.add(new Match(coordinate, currThreshold));
            }
            currThreshold--;
        }
        return matches;
    }

    /**
     * Given an array of Matches and
     * @param matches array of matches that contain the coordinate values
     * @return array of matches without an overlapping images
     */
    public ArrayList<Match> removeOverlappingMatches (int perfectImageRow, int perfectImageCol, ArrayList<Match> matches, int imageCol) {
        final int maxRow = perfectImageRow;
        final int maxCol = perfectImageCol;

        ArrayList<Match> finalMatches = new ArrayList<>(); //all non overlapping matches
        ArrayList<Match> allMatches = matches;

        int gridPosition;
        boolean exists;

        if(allMatches != null && allMatches.size() > 0) { //make sure there's something in the matches to compare to
            finalMatches.add(allMatches.get(0)); //add first point as a starting point
            for(Match match : allMatches) {
                exists = false; //set to false, don't know if the match coordinate is an overlapping image
                Coordinate coordinate = match.getPosition(); //get Coordinate of the existing matches
                gridPosition = getGridPosition(coordinate.getX(), coordinate.getY(), maxCol); //get its grid position

                //compare the current match with the final matches, if it overlaps don't add it
                for (Match finalMatch : finalMatches) {
                    //range of grid values for the coordinate that qualify as the match to be returned
                    ArrayList<Range> ranges = getMatrixArea(finalMatch.getPosition(), maxRow, maxCol, imageCol);
                    for(Range range : ranges) {
                        //if grid position already exists break don't need to look at the remaining ranges if any
                        if(range.contains(gridPosition)) {
                            exists = true;
                            break;
                        }
                    }

                    //if it exists break don't need to look at the remaining matches in the finalMatch if any
                    if(exists == true)
                        break;
                }

                if(exists == false)
                    finalMatches.add(match);
            }
        }
        return  finalMatches;
    }


    /**
     * Calculates the grid position based on a given coordinate
     * @param row
     * @param col
     * @return
     */
    //TODO remove this once you get the other one working
    public int getGridPosition(int row, int col, int maxCol) {
        return row * maxCol + col;
    }

    /**
     * Get the area that the image covers given it's starting coordinate position
     * @param position x,y position where the image starts
     * @return Arraylist of Ranges represeting the grid positions the image covers
     */
    public ArrayList<Range> getMatrixArea (Coordinate position, int perfectImageRow, int perfectImageCol,  int imageCol) {
        ArrayList<Range> ranges = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();
//        int gridPosition = getGridPosition(x,y, col);
        for(int i = x; i < x + perfectImageRow ; i++ ) {
            int currentPosition = getGridPosition(i, y , imageCol);
            ranges.add(new Range(currentPosition, currentPosition+ perfectImageCol));
        }

        return ranges;
    }


    /**
     * Returns the number of differences between two strings of any length
     * @param string1
     * @param string2
     * @return difference count
     */
    public int getDifference(String string1, String string2) {
        int length = Math.min(string1.length(), string2.length()); //use the shorter length to avoid out of bounds indexing
        int difference = 0;

        //Compare each char in string, if they aren't the same increment difference count
        for(int i = 0; i < length; i++) {
            if(string1.charAt(i) != string2.charAt(i)) {
                difference++;
            }
        }

        //add to difference count to account for when one string is of a longer length
        difference += Math.abs(string1.length() - string2.length());
        return difference;
    }

    //--------------------------------------Optimization & Extras --------------------------------------------------------------

    /**
     * Rotates the image by 90 degrees
     * @param image ArrayList representation of text image to translate
     * @return the newly translated image
     */
    public ArrayList<String> translate90Degrees(ArrayList<String> image) {
        ArrayList<String> newImage = new ArrayList<>();
        int col;
        String str;
        if(image.size() > 0) {
            int maxCol = image.get(0).length();
            for (col = 0; col < maxCol; col++) {
                int row = image.size() - 1;
                str = "";
                while (row >= 0) {
                    if (col + 1 == maxCol)
                        str += image.get(row).substring(maxCol - 1);
                    else
                        str += image.get(row).substring(col, Math.min(maxCol - 1, col + 1));
                    row--;
                }
                newImage.add(str);
            }
        }
        return newImage;
    }

    /**
     * Rotates the image by 180 degrees
     * @param image ArrayList representation of text image to translate
     * @return the newly translated image
     */
    public ArrayList<String> translate180Degrees(ArrayList<String> image) {
        int rowIndex;
        String str;
        ArrayList<String> newArray = new ArrayList<>();
        if (image.size() > 0) {
            for(rowIndex = image.size() - 1; rowIndex >= 0; rowIndex-- ) {
                str = image.get(rowIndex);
                newArray.add(new StringBuffer(str).reverse().toString());
            }
        }
        return newArray;
    }

    /**
     * Rotates the image by 270 degrees
     * @param image ArrayList representation of text image to translate
     * @return the newly translated image
     */
    public ArrayList<String> translate270Degrees(ArrayList<String> image) {
        ArrayList<String> newImage = new ArrayList<>();
        int col;
        String str;
        if(image.size() > 0) {
            int maxRow = image.size();
            int maxCol = image.get(0).length();
            for(col = maxCol - 1; col >= 0; col--) {
                int row = 0;
                str = "";
                while(row < maxRow) {
                    if(col + 1 == maxCol)
                        str += image.get(row).substring(maxCol - 1);
                    else
                        str += image.get(row).substring(col, col + 1);
                    row++;
                }
                newImage.add(str);
            }
        }
        return newImage;
    }
}
