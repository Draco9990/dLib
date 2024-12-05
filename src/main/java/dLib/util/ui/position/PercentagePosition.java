package dLib.util.ui.position;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.FillDimension;

public class PercentagePosition extends AbstractPosition {
    private float percentage;

    public PercentagePosition(float percentage){
        if(percentage < 0) percentage = 0;
        if(percentage > 1) percentage = 1;

        this.percentage = percentage;
    }

    @Override
    public int getLocalX(UIElement element) {
        if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;
            return (int)(parentWidth * percentage);
        }
        else if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

            return parentWidth / 2;
        }
        else{ //element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

            return parentWidth - (int)(parentWidth * percentage);
        }
    }

    @Override
    public int getLocalY(UIElement element) {
        if(element.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;
            return (int)(parentHeight * percentage);
        }
        else if(element.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

            return parentHeight / 2;
        }
        else{ //element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

            return parentHeight - (int)(parentHeight * percentage);
        }
    }

    @Override
    public AbstractPosition cpy() {
        return new PercentagePosition(percentage);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentagePosition)) {
            return false;
        }

        return ((PercentagePosition)obj).percentage == percentage;
    }
}
