package dLib.util.ui.dimensions;

import dLib.properties.ui.elements.IEditableValue;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.descriptors.ElementDescriptor;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.ItemBox;

import java.io.Serializable;

public abstract class AbstractDimension extends ElementDescriptor<AbstractDimension.ReferenceDimension, AbstractDimension> implements IEditableValue, Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public AbstractDimension(){

    }

    //endregion

    //region Methods

    //region Calculations

    @Override
    protected Float tryCalculateValue(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(reference == AbstractDimension.ReferenceDimension.WIDTH){
            return tryCalculateValue_Width(forElement, calculationPass);
        }
        else if(reference == AbstractDimension.ReferenceDimension.HEIGHT){
            return tryCalculateValue_Height(forElement, calculationPass);
        }

        return null;
    }

    protected abstract Float tryCalculateValue_Width(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass);
    protected abstract Float tryCalculateValue_Height(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass);

    //endregion

    //region Value

    public void setValueFromString(String value){
        throw new UnsupportedOperationException("setValueFromString is not supported for " + getClass());
    }

    //endregion

    //region Utility

    public abstract AbstractDimension cpy();

    protected boolean isVerticalBox(UIElement forElement){
        return forElement instanceof ItemBox && ((ItemBox)forElement).getContentAlignmentType() == Alignment.AlignmentType.VERTICAL;
    }
    protected boolean isHorizontalBox(UIElement forElement){
        return forElement instanceof ItemBox && ((ItemBox)forElement).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL;
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
