package dLib.util.ui.padding;

import basemod.Pair;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptor;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class AbstractPadding extends ElementDescriptor<AbstractPadding.ReferenceDimension> implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public AbstractPadding(){

    }

    //endregion

    //region Class Methods

    //region Calculation

    public Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationInstruction(UIElement forElement) {
        if(reference == ReferenceDimension.HORIZONTAL){
            return getCalculationFormula_Horizontal(forElement);
        }
        else if(reference == ReferenceDimension.VERTICAL){
            return getCalculationFormula_Vertical(forElement);
        }

        return null;
    }

    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Horizontal(UIElement forElement);
    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Vertical(UIElement forElement);

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
