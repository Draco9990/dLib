package dLib.util.ui.position;

import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptor;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class AbstractPosition extends ElementDescriptor<AbstractPosition.ReferencePosition, AbstractPosition> implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public AbstractPosition(){

    }

    //endregion

    //region Class Methods

    //region Calculation

    @Override
    protected Float tryCalculateValue(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(reference == ReferencePosition.X){
            return tryCalculateValue_X(forElement, calculationPass);
        }
        else if(reference == ReferencePosition.Y){
            return tryCalculateValue_Y(forElement, calculationPass);
        }

        return null;
    }

    protected abstract Float tryCalculateValue_X(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass);
    protected abstract Float tryCalculateValue_Y(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass);

    //endregion

    //region Utility Methods

    @Override
    public abstract AbstractPosition cpy();

    public void offset(UIElement forElement, float amount) {}

    //endregion

    //endregion

    //region Reference Position

    public enum ReferencePosition {
        X,
        Y
    }

    //endregion
}
