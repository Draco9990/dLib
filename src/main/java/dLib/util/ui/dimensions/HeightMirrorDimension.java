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
    public AbstractValueEditor makeEditorFor(AbstractDimension width, AbstractDimension height) {
        return new HeightMirrorDimensionValueEditor(this, width, height);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property, AbstractDimension width, AbstractDimension height) {
        return new HeightMirrorDimensionValueEditor((DimensionProperty) property, width, height);
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
