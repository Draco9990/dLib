package dLib.util.ui.padding;

import basemod.Pair;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;
import dLib.util.helpers.UIHelpers;

import java.io.Serializable;

public class PercentagePadding extends AbstractPadding implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private float perc;

    //endregion

    //region Constructors

    public PercentagePadding(float perc){
        if(perc < 0) perc = 0;
        if(perc > 1) perc = 1;

        this.perc = perc;
    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Horizontal(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.PADDING_PERCENTAGE, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, (int) (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * perc)),
                () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null
        ));
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Vertical(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.PADDING_PERCENTAGE, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, (int) (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * perc)),
                () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null
        ));
    }

    //endregion

    //region Utility

    @Override
    public PercentagePadding cpy() {
        PercentagePadding padding = new PercentagePadding(perc);
        padding.copyFrom(this);
        return padding;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentagePadding)) {
            return false;
        }

        PercentagePadding padding = (PercentagePadding) obj;
        return padding.perc == perc;
    }

    //endregion

    //endregion
}
