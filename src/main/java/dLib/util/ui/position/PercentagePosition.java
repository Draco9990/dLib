package dLib.util.ui.position;

import basemod.Pair;
import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentagePositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
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
    protected Float tryCalculateValue_X(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingLeftRaw());

        Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
        if(parentWidth.getKey() == null) return null;
        if(parentWidth.getValue() != null) registerDependency(parentWidth.getValue().getWidthRaw());

        Float calculatedVal = null;

        if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            calculatedVal = parentWidth.getKey() * percentage;
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                calculatedVal = parentWidth.getKey() * percentage;
            }
            else{
                if(forElement.getWidthRaw().needsRecalculation()) return null;
                registerDependency(forElement.getWidthRaw());

                calculatedVal = ((parentWidth.getKey() - forElement.getWidth()) * 0.5f);
            }
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            if(forElement.getWidthRaw() instanceof FillDimension){
                calculatedVal = parentWidth.getKey() * percentage + parentWidth.getKey();
            }
            else{
                if(forElement.getWidthRaw().needsRecalculation()) return null;
                registerDependency(forElement.getWidthRaw());

                calculatedVal = (parentWidth.getKey() - forElement.getWidth() + (parentWidth.getKey() * percentage));
            }
        }

        if(calculatedVal != null){
            calculatedVal += forElement.getOffsetX();

            calculatedVal += forElement.getPaddingLeft();
            registerDependency(forElement.getPaddingLeftRaw());
        }

        return calculatedVal;
    }

    // Dont forget we are renbdering bottom to top
    @Override
    protected Float tryCalculateValue_Y(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingBottomRaw());

        Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
        if(parentHeight.getKey() == null) return null;
        if(parentHeight.getValue() != null) registerDependency(parentHeight.getValue().getHeightRaw());

        Float calculatedVal = null;

        if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
            calculatedVal = parentHeight.getKey() * percentage + parentHeight.getKey();
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                calculatedVal = parentHeight.getKey() * percentage;
            }
            else{
                if(forElement.getHeightRaw().needsRecalculation()) return null;
                registerDependency(forElement.getHeightRaw());

                calculatedVal = ((parentHeight.getKey() - forElement.getHeight()) * 0.5f);
            }
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            calculatedVal = parentHeight.getKey() * percentage;
        }

        if(calculatedVal != null){
            calculatedVal += forElement.getOffsetY();

            calculatedVal += forElement.getPaddingBottom();
            registerDependency(forElement.getPaddingBottomRaw());
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
