package dLib.util.ui.position;

public class Pos {
    public static StaticPosition px(int pos){
        return new StaticPosition(pos);
    }
    public static StaticPosition dpx(int pos){
        return new DynamicPosition(pos);
    }

    public static PercentagePosition perc(float pos){
        return new PercentagePosition(pos);
    }
    public static PercentagePosition perc(double pos){
        return new PercentagePosition((float)pos);
    }
}
