package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

public class PercentageDimension extends AbstractDimension {
    private float percentage;

    public PercentageDimension(float percentage){
        setPercentage(percentage);
    }

    public void setPercentage(float percentage){
        if(percentage < 0) percentage = 0;
        if(percentage > 100) percentage = 100;

        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentageDimension)) {
            return false;
        }

        PercentageDimension pd = (PercentageDimension) obj;
        return pd.percentage == this.percentage;
    }

    @Override
    public int getWidth(UIElement self) {
        int parentWidth = self.getParent() != null ? self.getParent().getWidth() : 1920;
        return (int) (parentWidth * percentage / 100);
    }

    @Override
    public int getHeight(UIElement self) {
        int parentHeight = self.getParent() != null ? self.getParent().getHeight() : 1080;
        return (int) (parentHeight * percentage / 100);
    }

    @Override
    public AbstractDimension cpy() {
        return new PercentageDimension(percentage);
    }
}
