package imageDetection.Helper;

/**
 *  Class to hold ranges
 */

public class Range {

    private int low;
    private int high;

    public Range(int low, int high){
        this.low = low;
        this.high = high;
    }

    /**
     * Returns true or false based on whether the given numbers is within range
     * @param number number to test the range
     * @return true, the given number is within range
     *         false, the number is NOT within range
     */
    public boolean contains(int number){
        return (number >= low && number <= high);
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }
}