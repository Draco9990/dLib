package dLib.util.ui.dimensions;

public class Dim {
    public static StaticDimension px(int value) {
        return new StaticDimension(value);
    }

    public static PercentageDimension perc(float value) {
        return new PercentageDimension(value);
    }
    public static PercentageDimension perc(double value) {
        return new PercentageDimension((float)value);
    }

    public static FillDimension fill(){
        return new FillDimension();
    }

    public static AutoDimension auto(){
        return new AutoDimension();
    }

    public static WidthMirrorDimension width(){
        return new WidthMirrorDimension();
    }

    public static HeightMirrorDimension height(){
        return new HeightMirrorDimension();
    }
}
