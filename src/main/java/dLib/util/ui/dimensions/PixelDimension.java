package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PixelDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class PixelDimension extends AbstractStaticDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    private int size;

    public PixelDimension(int size){
        this.size = size;
    }

    @Override
    public int calculateDimension(UIElement self) {
        return size;
    }

    @Override
    public void resizeBy(UIElement self, int amount) {
        if(refDimension == ReferenceDimension.WIDTH){
            resizeWidthBy(self, amount);
        } else if(refDimension == ReferenceDimension.HEIGHT){
            resizeHeightBy(self, amount);
        }
    }

    public void resizeWidthBy(UIElement self, int amount) {
        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            size += amount;
        } else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            size -= amount;
        }
    }

    public void resizeHeightBy(UIElement self, int amount) {
        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            size += amount;
        } else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            size -= amount;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PixelDimension)) {
            return false;
        }

        PixelDimension other = (PixelDimension) obj;
        return other.size == size;
    }

    @Override
    public void setValueFromString(String value) {
        size = Integer.parseInt(value);
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PixelDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PixelDimensionValueEditor((DimensionProperty) property);
    }

    public int getValueRaw(){
        return size;
    }

    @Override
    public AbstractDimension cpy() {
        PixelDimension pixelDimension = new PixelDimension(size);
        pixelDimension.setReferenceDimension(refDimension);
        return pixelDimension;
    }

    @Override
    public String getSimpleDisplayName() {
        return "px";
    }

    @Override
    public String toString() {
        return "Px[" + size + "]";
    }
}
