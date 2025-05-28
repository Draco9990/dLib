package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PixelDimensionValueEditor;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "px")
public class PixelDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private int size;

    //endregion

    //region Constructors

    public PixelDimension(int size){
        this.size = size;
    }

    //endregion

    //region Methods

    //region Calculations

    @Override
    protected void setCalculatedValue(UIElement forElement, int value) {
        if(reference == ReferenceDimension.WIDTH){
            value -= forElement.getPaddingRight();
        }
        else if(reference == ReferenceDimension.HEIGHT){
            value -= forElement.getPaddingTop();
        }

        super.setCalculatedValue(forElement, value);
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Width(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_PIXEL, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, size),
                () -> !forElement.getPaddingRightRaw().needsRecalculation()
        ));
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_PIXEL, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, size),
                () -> !forElement.getPaddingTopRaw().needsRecalculation()
        ));
    }

    //endregion

    //region Value

    public int getValueRaw(){
        return size;
    }

    @Override
    public void setValueFromString(String value) {
        size = Integer.parseInt(value);
    }

    //endregion

    //region Utility

    @Override
    public AbstractDimension cpy() {
        PixelDimension px = Dim.px(size);
        px.copyFrom(this);
        return px;
    }

    @Override
    public String toString() {
        return "Px[" + size + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PixelDimension)) {
            return false;
        }

        PixelDimension other = (PixelDimension) obj;
        return other.size == size;
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PixelDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PixelDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //region Resizing

    @Override
    protected boolean canResize() {
        return true;
    }

    @Override
    public void resizeBy(UIElement self, int amount) {
        size += amount;

        super.resizeBy(self, amount);
    }

    //endregion

    //endregion
}
