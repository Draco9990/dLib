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
            return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(() -> forElement.setCalculatedLocalPositionX(position)));
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(() -> forElement.setCalculatedLocalPositionX(0)));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> forElement.setCalculatedLocalPositionX((int) ((UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - forElement.getCalculatedWidth()) * 0.5f)),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedWidth() != null
                ));
            }
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> forElement.setCalculatedLocalPositionX(UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - position),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> forElement.setCalculatedLocalPositionX(UIHelpers.getCalculatedParentWidthInHierarchy(forElement) - forElement.getCalculatedWidth() - position),
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
            return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(() -> forElement.setCalculatedLocalPositionY(position)));
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(0, new ElementCalculationManager.ElementCalculationInstruction(() -> forElement.setCalculatedLocalPositionY(0)));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> forElement.setCalculatedLocalPositionY((int) ((UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - forElement.getCalculatedHeight()) * 0.5f)),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> forElement.getCalculatedHeight() != null
                ));
            }
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> forElement.setCalculatedLocalPositionY(UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - position),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null
                ));
            }
            else{
                return new Pair<>(4, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> forElement.setCalculatedLocalPositionY(UIHelpers.getCalculatedParentHeightInHierarchy(forElement) - forElement.getCalculatedHeight() - position),
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
        copyValues(pos);
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

    //region Resizing



    //endregion

    //endregion




















    @Override
    public void offsetHorizontal(UIElement element, int amount) {
        if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || element.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            position += amount;
        }
        else if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            position -= amount;
        }
    }
    @Override
    public void offsetVertical(UIElement element, int amount) {
        if(element.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || element.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            position += amount;
        }
        else if(element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            position -= amount;
        }
    }
}
