package dLib.util.ui.dimensions;

public class Dim {
    public static PixelDimension px(int value) {
        return new PixelDimension(value);
    }
    public static PixelDimension px(float value) {
        return new PixelDimension(value);
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

    public static MirrorDimension mirror(){
        return new MirrorDimension();
    }
}
