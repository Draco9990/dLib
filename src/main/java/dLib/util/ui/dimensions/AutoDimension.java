package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AutoDimensionValueEditor;
import dLib.ui.elements.UIElement;
import dLib.util.ui.bounds.StaticBounds;

public class AutoDimension extends AbstractDimension {
    public AutoDimension(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AutoDimension;
    }

    @Override
    public int getWidth(UIElement self) {
        StaticBounds childBounds = self.getFullChildLocalBounds();
        if(childBounds == null) return 1;
        return childBounds.right - childBounds.left;
    }

    @Override
    public int getHeight(UIElement self) {
        StaticBounds childBounds = self.getFullChildLocalBounds();
        if(childBounds == null) return 1;
        return childBounds.top - childBounds.bottom;
    }

    @Override
    public void resizeWidthBy(UIElement self, int amount) {

    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {

    }

    @Override
    public AbstractValueEditor makeEditorFor(AbstractDimension width, AbstractDimension height) {
        return new AutoDimensionValueEditor(this, width, height);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property, AbstractDimension width, AbstractDimension height) {
        return new AutoDimensionValueEditor((DimensionProperty) property, width, height);
    }

    @Override
    public AbstractDimension cpy() {
        return new AutoDimension();
    }

    @Override
    public String getSimpleDisplayName() {
        return "auto";
    }

    @Override
    public String toString() {
        return "auto";
    }
}
