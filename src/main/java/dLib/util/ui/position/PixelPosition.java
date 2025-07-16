package dLib.util.ui.position;

import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PixelPositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.dimensions.FillDimension;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "px")
public class PixelPosition extends AbstractPosition implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private float position;

    //endregion

    //region Constructors

    public PixelPosition(float position){
        this.position = position;
    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    protected Float tryCalculateValue_X(UIElement forElement) {
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;

        Float calculatedVal = null;

        if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            calculatedVal = position;
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                calculatedVal = 0f;
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

                calculatedVal = (parentWidth + position);
            }
            else{
                Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);
                if(parentWidth == null) return null;
                if(forElement.getWidthRaw().needsRecalculation()) return null;

                calculatedVal = (parentWidth - forElement.getWidth() + position);
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
            calculatedVal = position;
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                calculatedVal = 0f;
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

                calculatedVal = (parentHeight + position);
            }
            else{
                Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);
                if(parentHeight == null) return null;
                if(forElement.getHeightRaw().needsRecalculation()) return null;

                calculatedVal = (parentHeight - forElement.getHeight() + position);
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
        return position;
    }

    @Override
    public void setValueFromString(String value) {
        position = Float.parseFloat(value);
    }

    //endregion

    //region Utility Methods

    @Override
    public String toString() {
        return "Px[" + position + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PixelPosition)) {
            return false;
        }

        return ((PixelPosition)obj).position == position;
    }

    @Override
    public AbstractPosition cpy() {
        PixelPosition pos = new PixelPosition(position);
        pos.copyFrom(this);
        return pos;
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PixelPositionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PixelPositionValueEditor((PositionProperty) property);
    }

    //endregion

    //endregion
}
