package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentageDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class PercentageDimension extends AbstractStaticDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    private float percentage;

    public PercentageDimension(float percentage){
        this.percentage = percentage;
    }

    @Override
    public int calculateDimension(UIElement self) {
        if(refDimension == ReferenceDimension.WIDTH){
            return calculateWidth(self);
        } else {
            return calculateHeight(self);
        }
    }

    public int calculateWidth(UIElement self) {
        int parentWidth = self.getParent() != null ? self.getParent().getWidthUnscaled() : 1920;
        return (int) (parentWidth * percentage);
    }

    public int calculateHeight(UIElement self) {
        int parentHeight = self.getParent() != null ? self.getParent().getHeightUnscaled() : 1080;
        return (int) (parentHeight * percentage);
    }

    @Override
    public void resizeBy(UIElement self, int amount) {
        if(refDimension == ReferenceDimension.WIDTH){
            resizeWidthBy(self, amount);
        } else {
            resizeHeightBy(self, amount);
        }
    }

    public void resizeWidthBy(UIElement self, int amount) {
        int parentWidth = self.getParent() != null ? self.getParent().getWidthUnscaled() : 1920;

        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            percentage += (float)amount / parentWidth;
        } else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            percentage -= (float)amount / parentWidth;
        }
    }

    public void resizeHeightBy(UIElement self, int amount) {
        int parentHeight = self.getParent() != null ? self.getParent().getHeightUnscaled() : 1080;

        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            percentage += (float)amount / parentHeight;
        } else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            percentage -= (float)amount / parentHeight;
        }
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
        PercentageDimension percDim = new PercentageDimension(percentage);
        percDim.setReferenceDimension(refDimension);
        return percDim;
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
