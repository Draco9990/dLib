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
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_X(UIElement forElement) {
        if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(((int) (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage)) + forElement.getOffsetX()),
                    () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null));
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(0 + forElement.getOffsetX())
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(((int) ((UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - forElement.getCalculatedWidth()) * 0.5f)) + forElement.getOffsetX()),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedWidth() != null
                ));
            }
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(((int) (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage)) - forElement.getOffsetX()),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue((UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - forElement.getCalculatedWidth() - (int)(UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage)) - forElement.getOffsetX()),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedWidth() != null
                ));
            }
        }

        return null;
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Y(UIElement forElement) {
        if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
            return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(((int) (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage)) + forElement.getOffsetY()),
                    () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null));
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(0 + forElement.getOffsetY())
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(((int) ((UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - forElement.getCalculatedHeight()) * 0.5f)) + forElement.getOffsetY()),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedHeight() != null
                ));
            }
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(((int) (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage)) - forElement.getOffsetY()),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue((UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - forElement.getCalculatedHeight() - (int)(UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage)) - forElement.getOffsetY()),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedHeight() != null
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
