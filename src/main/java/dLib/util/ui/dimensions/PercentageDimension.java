package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentageDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;

public class PercentageDimension extends AbstractStaticDimension {
    private float percentage;

    public PercentageDimension(float percentage){
        if(percentage < 0) percentage = 0;
        if(percentage > 1) percentage = 1;

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
    public void resizeWidthBy(UIElement self, int amount) {
        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            percentage += amount;
        } else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            percentage -= amount;
        }
    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {
        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            percentage += amount;
        } else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            percentage -= amount;
        }
    }

    @Override
    public int getWidth(UIElement self) {
        int parentWidth = self.getParent() != null ? self.getParent().getWidth() : 1920;
        return (int) (parentWidth * percentage);
    }

    @Override
    public int getHeight(UIElement self) {
        int parentHeight = self.getParent() != null ? self.getParent().getHeight() : 1080;
        return (int) (parentHeight * percentage);
    }

    @Override
    public void setValueFromString(String value) {
        percentage = Float.parseFloat(value);
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PercentageDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PercentageDimensionValueEditor((DimensionProperty) property);
    }

    public float getValueRaw(){
        return percentage;
    }

    @Override
    public AbstractDimension cpy() {
        return new PercentageDimension(percentage);
    }

    @Override
    public String getSimpleDisplayName() {
        return "%";
    }

    @Override
    public String toString() {
        return "%[" + percentage + "]";
    }
}
