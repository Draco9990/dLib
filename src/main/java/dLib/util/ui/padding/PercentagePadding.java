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
        return (int) (owner.getWidthUnpadded() * perc);
    }

    @Override
    public int getVertical(UIElement owner) {
        return (int) (owner.getHeightUnpadded() * perc);
    }

    @Override
    public AbstractPadding cpy() {
        return null;
    }
}
