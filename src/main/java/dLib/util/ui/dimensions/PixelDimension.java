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
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Width(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_PIXEL, new ElementCalculationManager.ElementCalculationInstruction(() -> forElement.setCalculatedWidth(size)));
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_PIXEL, new ElementCalculationManager.ElementCalculationInstruction(() -> forElement.setCalculatedHeight(size)));
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
        px.setReferenceDimension(refDimension);
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
    }

    //endregion

    //endregion
}
