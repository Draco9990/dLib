package dLib.util.ui.position;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.MirrorDimensionValueEditor;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "mirror")
public class MirrorPosition extends AbstractPosition implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public MirrorPosition(){
        super();
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    //region Width

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_X(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.POSITION_MIRROR, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, forElement.getLocalPositionY()),
                () -> !forElement.getLocalPositionYRaw().needsRecalculation()));
    }

    //endregion

    //region Height

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Y(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.POSITION_MIRROR, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, forElement.getLocalPositionX()),
                () -> !forElement.getLocalPositionXRaw().needsRecalculation()));
    }

    //endregion

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        // TODO
        throw new UnsupportedOperationException("MirrorPosition does not support makeEditorFor without a property.");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        // TODO
        throw new UnsupportedOperationException("MirrorPosition does not support makeEditorFor with a property.");
    }

    //endregion

    //region Utility

    @Override
    public AbstractPosition cpy() {
        MirrorPosition mirror = new MirrorPosition();
        mirror.copyFrom(this);
        return mirror;
    }

    //endregion

    //endregion
}
