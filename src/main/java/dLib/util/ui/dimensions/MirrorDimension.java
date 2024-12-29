package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.MirrorDimensionValueEditor;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class MirrorDimension extends AbstractDynamicDimension implements Serializable {
    @Override
    public int getWidth(UIElement self) {
        return self.getHeight();
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
    public void setValueFromString(String value) {

    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new MirrorDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new MirrorDimensionValueEditor((DimensionProperty) property);
    }

    @Override
    public AbstractDimension cpy() {
        return new MirrorDimension();
    }

    @Override
    public String getSimpleDisplayName() {
        return "mirror";
    }
}
