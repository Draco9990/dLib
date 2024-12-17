package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.HeightMirrorDimensionValueEditor;
import dLib.ui.elements.UIElement;

public class HeightMirrorDimension extends AbstractDimension{
    @Override
    public int getWidth(UIElement self) {
        return self.getHeight();
    }

    @Override
    public int getHeight(UIElement self) {
        throw new UnsupportedOperationException("Height mirror dimension does not support getHeight");
    }

    @Override
    public void resizeWidthBy(UIElement self, int amount) {

    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {

    }

    @Override
    public void setValueFromString(String value) {

    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new HeightMirrorDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new HeightMirrorDimensionValueEditor((DimensionProperty) property);
    }

    @Override
    public AbstractDimension cpy() {
        return new HeightMirrorDimension();
    }

    @Override
    public String getSimpleDisplayName() {
        return "mirror (height)";
    }
}
