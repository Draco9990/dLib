package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentageDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
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

    //region Width

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Width(UIElement forElement) {
        return new Pair<>(2, new ElementCalculationManager.ElementCalculationInstruction(
                () -> forElement.setCalculatedWidth((int) (UIHelpers.getCalculatedParentWidthInHierarchy(forElement) * percentage)),
                () -> UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null));
    }

    //endregion

    //region Height

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement) {
        return new Pair<>(2, new ElementCalculationManager.ElementCalculationInstruction(
                () -> forElement.setCalculatedHeight((int) (UIHelpers.getCalculatedParentHeightInHierarchy(forElement) * percentage)),
                () -> UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null));
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
        percDim.setReferenceDimension(refDimension);
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

    //endregion



























    @Override
    public void resizeBy(UIElement self, int amount) {
        if(refDimension == ReferenceDimension.WIDTH){
            resizeWidthBy(self, amount);
        } else {
            resizeHeightBy(self, amount);
        }
    }

    public void resizeWidthBy(UIElement self, int amount) {
        int parentWidth = self.getParent() != null ? self.getParent().getWidthUnscaled() : 1920;

        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            percentage += (float)amount / parentWidth;
        } else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            percentage -= (float)amount / parentWidth;
        }
    }

    public void resizeHeightBy(UIElement self, int amount) {
        int parentHeight = self.getParent() != null ? self.getParent().getHeightUnscaled() : 1080;

        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            percentage += (float)amount / parentHeight;
        } else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            percentage -= (float)amount / parentHeight;
        }
    }
}
