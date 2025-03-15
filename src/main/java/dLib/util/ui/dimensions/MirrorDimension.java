package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.MirrorDimensionValueEditor;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "mirror")
public class MirrorDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = -1521206809273241997L; // TODO set to 1 after removing currently generated elements

    //region Constructors

    public MirrorDimension(){
        super();
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    //region Width

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Width(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_MIRROR, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, forElement.getHeight()),
                () -> !forElement.needsHeightCalculation()));
    }

    //endregion

    //region Height

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_MIRROR, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, forElement.getWidth()),
                () -> !forElement.needsWidthCalculation()));
    }

    //endregion

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new MirrorDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new MirrorDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //region Utility

    @Override
    public AbstractDimension cpy() {
        MirrorDimension mirror = new MirrorDimension();
        mirror.copyFrom(this);
        return mirror;
    }

    //endregion

    //endregion
}
