package dLib.util.ui.position;

import basemod.Pair;
import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.Binding;

import java.io.Serializable;

public abstract class AbstractPosition extends Binding implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    protected ReferencePosition refPosition;

    //endregion

    //region Constructors

    public AbstractPosition(){

    }

    //endregion

    //region Class Methods

    //region Calculation

    public Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationInstruction(UIElement forElement) {
        if(refPosition == ReferencePosition.X){
            return getCalculationFormula_X(forElement);
        }
        else if(refPosition == ReferencePosition.Y){
            return getCalculationFormula_Y(forElement);
        }

        return null;
    }

    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_X(UIElement forElement);
    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Y(UIElement forElement);

    //endregion

    //region Reference Position

    public void setReferencePosition(ReferencePosition position){
        this.refPosition = position;
    }

    public enum ReferencePosition {
        X,
        Y
    }

    //endregion

    //region Utility Methods

    public abstract void setValueFromString(String value);

    public abstract AbstractPosition cpy();

    //endregion

    //endregion























    public abstract void offsetHorizontal(UIElement element, int amount);
    public abstract void offsetVertical(UIElement element, int amount);
}
