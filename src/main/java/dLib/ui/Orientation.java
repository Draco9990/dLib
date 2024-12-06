package dLib.ui;

public class Orientation {
    public OrientationType orientationType;

    public Orientation(OrientationType orientationType){
        this.orientationType = orientationType;
    }

    public enum OrientationType {
        HORIZONTAL,
        VERTICAL
    }
}
