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

    private int position;

    //endregion

    //region Constructors

    public PixelPosition(int position){
        this.position = position;
    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_X(UIElement forElement) {
        if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
            return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(position + forElement.getOffsetX())
            ));
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
                        () -> setCalculatedValue(UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - position - forElement.getOffsetX()),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - forElement.getCalculatedWidth() - position - forElement.getOffsetX()),
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
            return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(position + forElement.getOffsetY())
            ));
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
                        () -> setCalculatedValue(UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - position - forElement.getOffsetY()),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - forElement.getCalculatedHeight() - position - forElement.getOffsetY()),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedHeight() != null
                ));
            }
        }

        return null;
    }

    //endregion

    //region Value

    public int getValueRaw(){
        return position;
    }

    @Override
    public void setValueFromString(String value) {
        position = Integer.parseInt(value);
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
