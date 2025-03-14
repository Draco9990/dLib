package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentageDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;
import dLib.util.helpers.UIHelpers;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "%")
public class PercentageDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    private float percentage;

    //region Constructors

    public PercentageDimension(float percentage){
        this.percentage = percentage;
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    @Override
    protected void setCalculatedValue(UIElement forElement, int value) {
        if(reference == ReferenceDimension.WIDTH){
            value -= forElement.getPaddingRight();
        }
        else if(reference == ReferenceDimension.HEIGHT){
            value -= forElement.getPaddingTop();
        }

        super.setCalculatedValue(forElement, value);
    }

    //region Width

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Width(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_PERCENTAGE, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, (int) (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage)),
                () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null,
                () -> !forElement.getPaddingRightRaw().needsRecalculation()
        ));
    }

    //endregion

    //region Height

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_PERCENTAGE, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, (int) (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage)),
                () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null,
                () -> !forElement.getPaddingTopRaw().needsRecalculation()
        ));
    }

    //endregion

    //endregion

    //region Value

    public float getValueRaw(){
        return percentage;
    }

    @Override
    public void setValueFromString(String value) {
        percentage = Float.parseFloat(value);
    }

    //endregion

    //region Utility Methods

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentageDimension)) {
            return false;
        }

        PercentageDimension pd = (PercentageDimension) obj;
        return pd.percentage == this.percentage;
    }

    @Override
    public AbstractDimension cpy() {
        PercentageDimension percDim = new PercentageDimension(percentage);
        percDim.copyFrom(this);
        return percDim;
    }

    @Override
    public String toString() {
        return "%[" + percentage + "]";
    }


    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PercentageDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PercentageDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //region Resizing

    @Override
    protected boolean canResize() {
        return true;
    }

    @Override
    public void resizeBy(UIElement self, int amount) {
        if(reference == ReferenceDimension.WIDTH){
            resizeWidthBy(self, amount);
        } else {
            resizeHeightBy(self, amount);
        }
    }

    public void resizeWidthBy(UIElement self, int amount) {
        int parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(self);

        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            percentage += (float)amount / parentWidth;
        } else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            percentage -= (float)amount / parentWidth;
        }
    }

    public void resizeHeightBy(UIElement self, int amount) {
        int parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(self);

        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            percentage += (float)amount / parentHeight;
        } else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            percentage -= (float)amount / parentHeight;
        }
    }

    //endregion

    //endregion
}
