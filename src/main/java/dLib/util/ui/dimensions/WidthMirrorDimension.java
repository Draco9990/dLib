package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.WidthMirrorDimensionValueEditor;
import dLib.ui.elements.UIElement;

public class WidthMirrorDimension extends AbstractDimension{
    @Override
    public int getWidth(UIElement self) {
        throw new UnsupportedOperationException("Width mirror dimension does not support getWidth");
    }

    @Override
    public int getHeight(UIElement self) {
        return self.getWidth();
    }

    @Override
    public void resizeWidthBy(UIElement self, int amount) {

    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {

    }

    @Override
    public AbstractValueEditor makeEditorFor(AbstractDimension width, AbstractDimension height) {
        return new WidthMirrorDimensionValueEditor(this, width, height);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property, AbstractDimension width, AbstractDimension height) {
        return new WidthMirrorDimensionValueEditor((DimensionProperty) property, width, height);
    }

    @Override
    public AbstractDimension cpy() {
        return new WidthMirrorDimension();
    }

    @Override
    public String getSimpleDisplayName() {
        return "mirror (width)";
    }
}
