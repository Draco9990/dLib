package dLib.util.ui.padding;

public class Padd {
    public static AbstractPadding px(int val){
        return new PixelPadding(val);
    }

    public static PercentagePadding perc(float perc){
        return new PercentagePadding(perc);
    }
    public static PercentagePadding perc(double perc){
        return new PercentagePadding((float) perc);
    }
}
