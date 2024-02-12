package dLib.ui;

public class Alignment {
    public HorizontalAlignment horizontalAlignment;
    public VerticalAlignment verticalAlignment;

    public Alignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment){
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    public enum HorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalAlignment {
        TOP,
        CENTER,
        BOTTOM
    }
}
