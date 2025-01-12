package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.MirrorDimensionValueEditor;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class MirrorDimension extends AbstractDynamicDimension implements Serializable {
    @Override
    public int calculateDimension(UIElement self) {
        if(refDimension == AbstractDimension.ReferenceDimension.WIDTH){
            return self.getHeight();
        }
        else if(refDimension == AbstractDimension.ReferenceDimension.HEIGHT){
            return self.getWidth();
        }

        return 1;
    }

    @Override
    public void resizeBy(UIElement self, int amount) {
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
        MirrorDimension mirror = new MirrorDimension();
        mirror.refDimension = this.refDimension;
        return mirror;
    }

    @Override
    public String getSimpleDisplayName() {
        return "mirror";
    }
}
