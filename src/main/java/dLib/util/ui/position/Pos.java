package dLib.util.ui.position;

public class Pos {
    public static PixelPosition px(int pos){
        return new PixelPosition(pos);
    }

    public static PercentagePosition perc(float pos){
        return new PercentagePosition(pos);
    }
    public static PercentagePosition perc(double pos){
        return new PercentagePosition((float)pos);
    }
}
