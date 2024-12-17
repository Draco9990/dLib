package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.StaticDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;

public class StaticDimension extends AbstractDimension {
    private int size;

    public StaticDimension(int size){
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StaticDimension)) {
            return false;
        }

        StaticDimension other = (StaticDimension) obj;
        return other.size == size;
    }

    @Override
    public int getWidth(UIElement self) {
        return size;
    }

    @Override
    public int getHeight(UIElement self) {
        return size;
    }

    @Override
    public void resizeWidthBy(UIElement self, int amount) {
        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            size += amount;
        } else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            size -= amount;
        }
    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {
        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            size += amount;
        } else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            size -= amount;
        }
    }

    @Override
    public void setValueFromString(String value) {
        size = Integer.parseInt(value);
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new StaticDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new StaticDimensionValueEditor((DimensionProperty) property);
    }

    public int getValueRaw(){
        return size;
    }

    @Override
    public AbstractDimension cpy() {
        return new StaticDimension(size);
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
