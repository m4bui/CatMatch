package imageDetection.ResponseModel;

import imageDetection.ResponseModel.Coordinate;

public class Match {

    private Coordinate position;
    private int confidenceValue;

    public Match(Coordinate position, int confidenceValue){
        this.position = position;
        this.confidenceValue = confidenceValue;
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getConfidenceValue() {
        return confidenceValue;
    }
}
