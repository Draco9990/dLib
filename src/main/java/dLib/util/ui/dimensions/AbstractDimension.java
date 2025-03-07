package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class AbstractDimension implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    protected ReferenceDimension refDimension;

    //endregion

    //region Constructors

    public AbstractDimension(){

    }

    //endregion

    //region Methods

    //region Calculations

    public Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationInstruction(UIElement forElement) {
        if(refDimension == ReferenceDimension.WIDTH){
            return getCalculationFormula_Width(forElement);
        }
        else if(refDimension == ReferenceDimension.HEIGHT){
            return getCalculationFormula_Height(forElement);
        }

        return null;
    }

    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Width(UIElement forElement);
    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement);

    //endregion

    //region Value

    public void setValueFromString(String value){
        throw new UnsupportedOperationException("setValueFromString is not supported for " + getClass());
    }

    //endregion

    //region Utility

    public abstract AbstractDimension cpy();

    //endregion

    //region Reference Dimension

    public void setReferenceDimension(ReferenceDimension dimension){
        this.refDimension = dimension;
    }

    //endregion

    //endregion

    //region Enums

    public enum ReferenceDimension {
        WIDTH,
        HEIGHT
    }

    //endregion



















    public abstract void resizeBy(UIElement self, int amount);
}
