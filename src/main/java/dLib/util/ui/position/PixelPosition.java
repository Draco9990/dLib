package dLib.util.ui.position;

import basemod.Pair;
import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PixelPositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
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
            return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_DIRECT, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(forElement, position),
                    () -> !forElement.getPaddingLeftRaw().needsRecalculation()
            ));
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_DIRECT, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, 0),
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, ((int) ((forElement.getParentWidthSafe() - forElement.getWidth()) * 0.5f))),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> !forElement.getWidthRaw().needsRecalculation(),
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation(),
                        () -> forElement.getParent() == null || !forElement.getParent().getWidthRaw().needsRecalculation()
                ));
            }
        }
        else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            if(forElement.getWidthRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, forElement.getParentWidthSafe() - position),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation(),
                        () -> forElement.getParent() == null || !forElement.getParent().getWidthRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, forElement.getParentWidthSafe() - forElement.getWidth() - position),
                        () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                        () -> !forElement.getWidthRaw().needsRecalculation(),
                        () -> !forElement.getPaddingLeftRaw().needsRecalculation(),
                        () -> forElement.getParent() == null || !forElement.getParent().getWidthRaw().needsRecalculation()
                ));
            }
        }

        return null;
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Y(UIElement forElement) {
        if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
            return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_DIRECT, new ElementCalculationManager.ElementCalculationInstruction(
                    () -> setCalculatedValue(forElement, position),
                    () -> !forElement.getPaddingBottomRaw().needsRecalculation()
            ));
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_DIRECT, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, 0),
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, ((int) ((forElement.getParentHeightSafe() - forElement.getHeight()) * 0.5f))),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> !forElement.getHeightRaw().needsRecalculation(),
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation(),
                        () -> forElement.getParent() == null || !forElement.getParent().getHeightRaw().needsRecalculation()
                ));
            }
        }
        else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            if(forElement.getHeightRaw() instanceof FillDimension){
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, forElement.getParentHeightSafe() - position),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation(),
                        () -> forElement.getParent() == null || !forElement.getParent().getHeightRaw().needsRecalculation()
                ));
            }
            else{
                return new Pair<>(ElementDescriptorCalcOrders.POSITION_PIXEL_VARIED, new ElementCalculationManager.ElementCalculationInstruction(
                        () -> setCalculatedValue(forElement, forElement.getParentHeightSafe() - forElement.getHeight() - position),
                        () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                        () -> !forElement.getHeightRaw().needsRecalculation(),
                        () -> !forElement.getPaddingBottomRaw().needsRecalculation(),
                        () -> forElement.getParent() == null || !forElement.getParent().getHeightRaw().needsRecalculation()
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
