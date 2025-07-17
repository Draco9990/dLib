package dLib.util.ui.position;

import basemod.Pair;
import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PixelPositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
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
    protected Float tryCalculateValue_X(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingLeftRaw());

        Float calculatedVal = null;

        if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            calculatedVal = position;
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                calculatedVal = 0f;
            }
            else{
                Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
                if(parentWidth.getKey() == null) return null;
                if(parentWidth.getValue() != null) registerDependency(parentWidth.getValue().getWidthRaw());

                if(forElement.getWidthRaw().needsRecalculation()) return null;
                registerDependency(forElement.getWidthRaw());

                calculatedVal = ((parentWidth.getKey() - forElement.getWidth()) * 0.5f);
            }
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return 0f;
            }
            else{
                Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
                if(parentWidth.getKey() == null) return null;
                if(parentWidth.getValue() != null) registerDependency(parentWidth.getValue().getWidthRaw());

                if(forElement.getWidthRaw().needsRecalculation()) return null;
                registerDependency(forElement.getWidthRaw());

                calculatedVal = (parentWidth.getKey() - forElement.getWidth() + position);
            }
        }

        if(calculatedVal != null){
            calculatedVal += forElement.getPaddingLeft();
            registerDependency(forElement.getPaddingLeftRaw());
        }

        return calculatedVal;
    }

    @Override
    protected Float tryCalculateValue_Y(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingBottomRaw());

        Float calculatedVal = null;

        if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
            calculatedVal = position;
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                calculatedVal = 0f;
            }
            else{
                Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
                if(parentHeight.getKey() == null) return null;
                if(parentHeight.getValue() != null) registerDependency(parentHeight.getValue().getHeightRaw());

                if(forElement.getHeightRaw().needsRecalculation()) return null;
                registerDependency(forElement.getHeightRaw());

                calculatedVal = ((parentHeight.getKey() - forElement.getHeight()) * 0.5f);
            }
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return 0f;
            }
            else{
                Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
                if(parentHeight.getKey() == null) return null;
                if(parentHeight.getValue() != null) registerDependency(parentHeight.getValue().getHeightRaw());

                if(forElement.getHeightRaw().needsRecalculation()) return null;
                registerDependency(forElement.getHeightRaw());

                calculatedVal = (parentHeight.getKey() - forElement.getHeight() + position);
            }
        }

        if(calculatedVal != null){
            calculatedVal += forElement.getPaddingBottom();
            registerDependency(forElement.getPaddingBottomRaw());
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
    public void offset(UIElement forElement, float amount) {
        position += amount;
        requestRecalculation();
    }

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
