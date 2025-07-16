package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentageDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;
import dLib.util.helpers.UIHelpers;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "%")
public class PercentageDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    private float percentage;

    //region Constructors

    public PercentageDimension(float percentage){
        this.percentage = percentage;
    }

    //endregion

    //region Class Methods

    //region Calculation Methods


    @Override
    protected Float tryCalculateValue_Width(UIElement forElement) {
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;

        Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);
        if(parentWidth == null) return null;

        return (parentWidth * percentage) - forElement.getPaddingLeft() - forElement.getPaddingRight();
    }

    @Override
    protected Float tryCalculateValue_Height(UIElement forElement) {
        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;

        Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);
        if(parentHeight == null) return null;

        return (parentHeight * percentage) - forElement.getPaddingTop() - forElement.getPaddingBottom();
    }

    //endregion

    //region Value

    public float getValueRaw(){
        return percentage;
    }

    @Override
    public void setValueFromString(String value) {
        percentage = Float.parseFloat(value);
    }

    //endregion

    //region Utility Methods

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentageDimension)) {
            return false;
        }

        PercentageDimension pd = (PercentageDimension) obj;
        return pd.percentage == this.percentage;
    }

    @Override
    public AbstractDimension cpy() {
        PercentageDimension percDim = new PercentageDimension(percentage);
        percDim.copyFrom(this);
        return percDim;
    }

    @Override
    public String toString() {
        return "%[" + percentage + "]";
    }


    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PercentageDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PercentageDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //region Resizing

    @Override
    protected boolean canResize() {
        return true;
    }

    @Override
    public void resizeBy(UIElement self, float amount) {
        if(reference == ReferenceDimension.WIDTH){
            resizeWidthBy(self, amount);
        } else {
            resizeHeightBy(self, amount);
        }

        super.resizeBy(self, amount);
    }

    public void resizeWidthBy(UIElement self, float amount) {
        float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(self);

        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            percentage += amount / parentWidth;
        } else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            percentage -= amount / parentWidth;
        }
    }

    public void resizeHeightBy(UIElement self, float amount) {
        float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(self);

        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            percentage += amount / parentHeight;
        } else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            percentage -= amount / parentHeight;
        }
    }

    //endregion

    //endregion
}
