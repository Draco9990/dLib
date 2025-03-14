package dLib.util.ui.position;

import basemod.Pair;
import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentagePositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
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
    protected void setCalculatedValue(UIElement forElement, int value) {
        if(reference == ReferencePosition.X){
            value += forElement.getOffsetX();
            value += forElement.getPaddingLeft();
        }
        else if(reference == ReferencePosition.Y){
            value += forElement.getOffsetY();
            value += forElement.getPaddingBottom();
        }

        super.setCalculatedValue(forElement, value);
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_X(UIElement forElement) {
        if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(forElement, ((int) (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage))),
                    () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                    () -> !forElement.getPaddingLeftRaw().needsRecalculation()
            ));
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_DIRECT, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, 0),
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, ((int) ((UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - forElement.getCalculatedWidth()) * 0.5f))),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedWidth() != null,
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation()
                ));
            }
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, ((int) (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage))),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - forElement.getCalculatedWidth() - (int)(UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage))),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedWidth() != null,
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation()
                ));
            }
        }

        return null;
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Y(UIElement forElement) {
        if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
            return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(forElement, ((int) (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage))),
                    () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                    () -> !forElement.getPaddingBottomRaw().needsRecalculation()
            ));
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_DIRECT, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, 0),
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, ((int) ((UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - forElement.getCalculatedHeight()) * 0.5f))),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedHeight() != null,
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation()
                ));
            }
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, ((int) (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage))),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PERCENTAGE_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - forElement.getCalculatedHeight() - (int)(UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage))),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedHeight() != null,
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation()
                ));
            }
        }

        return null;
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
