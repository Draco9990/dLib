package dLib.util.ui.position;

import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentagePositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.dimensions.FillDimension;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "%")
public class PercentagePosition extends AbstractPosition implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private float percentage;

    //endregion

    //region Constructors

    public PercentagePosition(float percentage){
        this.percentage = percentage;
    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    protected Float tryCalculateValue_X(UIElement forElement) {
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;

        Float calculatedVal = null;

        if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);
            if(parentWidth == null) return null;

            calculatedVal = parentWidth * percentage;
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);
                if(parentWidth == null) return null;

                calculatedVal = parentWidth * percentage;
            }
            else{
                Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);
                if(parentWidth == null) return null;
                if(forElement.getWidthRaw().needsRecalculation()) return null;

                calculatedVal = ((parentWidth - forElement.getWidth()) * 0.5f);
            }
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            if(forElement.getWidthRaw() instanceof FillDimension){
                Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);
                if(parentWidth == null) return null;

                calculatedVal = parentWidth * percentage + parentWidth;
            }
            else{
                Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);
                if(parentWidth == null) return null;
                if(forElement.getWidthRaw().needsRecalculation()) return null;

                calculatedVal = (parentWidth - forElement.getWidth() + (parentWidth * percentage));
            }
        }

        if(calculatedVal != null){
            calculatedVal += forElement.getOffsetX();
            calculatedVal += forElement.getPaddingLeft();
        }

        return calculatedVal;
    }

    @Override
    protected Float tryCalculateValue_Y(UIElement forElement) {
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;

        Float calculatedVal = null;

        if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
            Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);
            if(parentHeight == null) return null;

            calculatedVal = parentHeight * percentage;
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);
                if(parentHeight == null) return null;

                calculatedVal = parentHeight * percentage;
            }
            else{
                Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);
                if(parentHeight == null) return null;
                if(forElement.getHeightRaw().needsRecalculation()) return null;

                calculatedVal = ((parentHeight - forElement.getHeight()) * 0.5f);
            }
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            if(forElement.getHeightRaw() instanceof FillDimension){
                Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);
                if(parentHeight == null) return null;

                calculatedVal = parentHeight * percentage + parentHeight;
            }
            else{
                Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);
                if(parentHeight == null) return null;
                if(forElement.getHeightRaw().needsRecalculation()) return null;

                calculatedVal = (parentHeight - forElement.getHeight() + (parentHeight * percentage));
            }
        }

        if(calculatedVal != null){
            calculatedVal += forElement.getOffsetY();
            calculatedVal += forElement.getPaddingBottom();
        }

        return calculatedVal;
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

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PercentagePositionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PercentagePositionValueEditor((PositionProperty) property);
    }

    //endregion

    //region Utility Methods

    @Override
    public AbstractPosition cpy() {
        PercentagePosition cpy = new PercentagePosition(percentage);
        cpy.copyFrom(this);
        return cpy;
    }

    @Override
    public String toString() {
        return "%[" + percentage + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentagePosition)) {
            return false;
        }

        return ((PercentagePosition)obj).percentage == percentage;
    }

    //endregion

    //endregion
}
