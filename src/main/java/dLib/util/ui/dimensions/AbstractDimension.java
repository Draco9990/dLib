package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptor;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.ItemBox;

import java.io.Serializable;

public abstract class AbstractDimension extends ElementDescriptor<AbstractDimension.ReferenceDimension> implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public AbstractDimension(){

    }

    //endregion

    //region Methods

    //region Calculations

    public Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationInstruction(UIElement forElement) {
        if(reference == ReferenceDimension.WIDTH){
            return getCalculationFormula_Width(forElement);
        }
        else if(reference == ReferenceDimension.HEIGHT){
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

    protected boolean isWithinVerticalBox(UIElement forElement){
        return forElement.getParent() instanceof ItemBox && ((ItemBox)forElement.getParent()).getContentAlignmentType() == Alignment.AlignmentType.VERTICAL;
    }
    protected boolean isWithinHorizontalBox(UIElement forElement){
        return forElement.getParent() instanceof ItemBox && ((ItemBox)forElement.getParent()).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL;
    }

    //endregion

    //region Resizing

    protected boolean canResize(){
        return false;
    }

    public void resizeBy(UIElement self, float amount){
        requestRecalculation();
    }

    //endregion

    //endregion

    //region Enums

    public enum ReferenceDimension {
        WIDTH,
        HEIGHT
    }

    //endregion
}
