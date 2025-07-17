package dLib.util.ui.padding;

import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptor;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class AbstractPadding extends ElementDescriptor<AbstractPadding.ReferenceDimension, AbstractPadding> implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public AbstractPadding(){

    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    protected Float tryCalculateValue(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(reference == ReferenceDimension.HORIZONTAL){
            return tryCalculateValue_Horizontal(forElement, calculationPass);
        }
        else if(reference == ReferenceDimension.VERTICAL){
            return tryCalculateValue_Vertical(forElement, calculationPass);
        }

        return null;
    }

    protected abstract Float tryCalculateValue_Horizontal(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass);
    protected abstract Float tryCalculateValue_Vertical(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass);

    //endregion

    //region Utility

    @Override
    public abstract AbstractPadding cpy();

    //endregion

    //endregion

    //region Enums

    public enum ReferenceDimension {
        HORIZONTAL,
        VERTICAL
    }

    //endregion
}
