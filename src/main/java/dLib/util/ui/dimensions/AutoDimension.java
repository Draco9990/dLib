package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AutoDimensionValueEditor;
import dLib.ui.elements.UIElement;
import dLib.util.ui.bounds.PositionBounds;

public class AutoDimension extends AbstractDynamicDimension {
    public AutoDimension(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AutoDimension;
    }

    @Override
    public int getWidth(UIElement self) {
        PositionBounds childBounds = self.getFullChildLocalBoundsForAutoDim();
        if(childBounds == null) return 1;
        return childBounds.right - childBounds.left;
    }

    @Override
    public int getHeight(UIElement self) {
        PositionBounds childBounds = self.getFullChildLocalBoundsForAutoDim();
        if(childBounds == null) return 1;
        return childBounds.top - childBounds.bottom;
    }

    @Override
    public void setValueFromString(String value) {

    }

    @Override
    public void resizeWidthBy(UIElement self, int amount) {

    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {

    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new AutoDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new AutoDimensionValueEditor((DimensionProperty) property);
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
