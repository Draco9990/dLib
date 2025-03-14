package dLib.util.ui.padding;

import basemod.Pair;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public class PixelPadding extends AbstractPadding implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private int val;

    //endregion

    //region Constructors

    public PixelPadding(int val){
        this.val = val;
    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Horizontal(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.PADDING_PIXEL, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, val)
        ));
    }

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Vertical(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.PADDING_PIXEL, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, val)
        ));
    }


    //endregion

    //region Utility

    @Override
    public AbstractPadding cpy() {
        PixelPadding padd = new PixelPadding(val);
        padd.copyFrom(this);
        return padd;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PixelPadding)) {
            return false;
        }

        PixelPadding other = (PixelPadding) obj;
        return other.val == val;
    }

    //endregion

    //endregion
}
