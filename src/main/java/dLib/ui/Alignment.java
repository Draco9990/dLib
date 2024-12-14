package dLib.ui;

public class Alignment {
    public HorizontalAlignment horizontalAlignment;
    public VerticalAlignment verticalAlignment;

    public Alignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment){
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    public Alignment(Alignment copy){
        this.horizontalAlignment = copy.horizontalAlignment;
        this.verticalAlignment = copy.verticalAlignment;
    }

    public enum HorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalAlignment {
        BOTTOM,
        CENTER,
        TOP
    }

    @Override
    public String toString() {
        return "[" + horizontalAlignment + ", " + verticalAlignment + "]";
    }
}
