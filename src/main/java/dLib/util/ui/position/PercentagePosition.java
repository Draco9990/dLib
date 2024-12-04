package dLib.util.ui.position;

import dLib.ui.elements.UIElement;

public class PercentagePosition extends AbstractPosition {
    private float percentage;

    public PercentagePosition(float percentage){
        if(percentage < 0) percentage = 0;
        if(percentage > 1) percentage = 1;

        this.percentage = percentage;
    }

    @Override
    public int getLocalX(UIElement element) {
        return (int)(element.getWidth() * percentage);
    }

    @Override
    public int getLocalY(UIElement element) {
        return (int)(element.getHeight() * percentage);
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
