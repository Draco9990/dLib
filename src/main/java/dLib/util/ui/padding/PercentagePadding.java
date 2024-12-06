package dLib.util.ui.padding;

import dLib.ui.elements.UIElement;

public class PercentagePadding extends AbstractPadding{
    private float perc;

    public PercentagePadding(float perc){
        if(perc < 0) perc = 0;
        if(perc > 1) perc = 1;

        this.perc = perc;
    }

    @Override
    public int getHorizontal(UIElement owner) {
        int parentWidth = owner.getParent() != null ? owner.getParent().getWidth() : 1920;
        return (int) (parentWidth * perc);
    }

    @Override
    public int getVertical(UIElement owner) {
        int parentHeight = owner.getParent() != null ? owner.getParent().getHeight() : 1080;
        return (int) (parentHeight * perc);
    }

    @Override
    public AbstractPadding cpy() {
        return null;
    }
}
