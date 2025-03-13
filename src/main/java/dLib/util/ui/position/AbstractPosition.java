package dLib.util.ui.position;

import basemod.Pair;
import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptor;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class AbstractPosition extends ElementDescriptor<AbstractPosition.ReferencePosition> implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public AbstractPosition(){

    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    public Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationInstruction(UIElement forElement) {
        if(reference == ReferencePosition.X){
            return getCalculationFormula_X(forElement);
        }
        else if(reference == ReferencePosition.Y){
            return getCalculationFormula_Y(forElement);
        }

        return null;
    }

    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_X(UIElement forElement);
    protected abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Y(UIElement forElement);

    //endregion

    //region Utility Methods

    @Override
    public abstract AbstractPosition cpy();

    //endregion

    //endregion

    //region Reference Position

    public enum ReferencePosition {
        X,
        Y
    }

    //endregion
}
