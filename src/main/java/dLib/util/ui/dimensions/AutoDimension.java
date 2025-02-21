package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AutoDimensionValueEditor;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.text.TextBox;
import dLib.util.ui.bounds.PositionBounds;

import java.io.Serializable;

public class AutoDimension extends AbstractDynamicDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    public AutoDimension(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AutoDimension;
    }

    @Override
    public int calculateDimension(UIElement self) {
        if(refDimension == ReferenceDimension.HEIGHT){
            return calculateHeight(self);
        }
        else if (refDimension == ReferenceDimension.WIDTH){
            return calculateWidth(self);
        }

        return 1;
    }

    public int calculateWidth(UIElement self) {
        PositionBounds childBounds = self.getFullChildLocalBoundsForAutoDim();

        if(self instanceof TextBox){
            int textWidth = ((TextBox) self).getTextWidth();
            if(childBounds == null || textWidth > (childBounds.right - childBounds.left)){
                return textWidth + self.getPaddingLeft() + self.getPaddingRight();
            }
        }
        else if(childBounds == null){
            return 1;
        }

        childBounds.right += self.getPaddingRight();
        childBounds.left -= self.getPaddingLeft();

        return (childBounds.right - childBounds.left);
    }

    public int calculateHeight(UIElement self) {
        PositionBounds childBounds = self.getFullChildLocalBoundsForAutoDim();

        if(self instanceof TextBox){
            int textHeight = ((TextBox) self).getTextHeight();
            if(childBounds == null || textHeight > (childBounds.top - childBounds.bottom)){
                return textHeight + self.getPaddingTop() + self.getPaddingBottom();
            }
        }
        else if(childBounds == null){
            return 1;
        }

        childBounds.top += self.getPaddingTop();
        childBounds.bottom -= self.getPaddingBottom();

        return (childBounds.top - childBounds.bottom);
    }

    @Override
    public void resizeBy(UIElement self, int amount) {}

    @Override
    public void setValueFromString(String value) {
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
        AutoDimension cpy = new AutoDimension();
        cpy.setReferenceDimension(refDimension);
        return cpy;
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
