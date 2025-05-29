package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.FillDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.util.helpers.UIHelpers;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "fill")
public class FillDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public FillDimension(){
        super();
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    @Override
    protected void setCalculatedValue(UIElement forElement, float value) {
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
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_FILL, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, calculateWidth(forElement)),
                () -> canCalculateWidth(forElement),
                () -> !forElement.getPaddingRightRaw().needsRecalculation()
        ));
    }

    private Float calculateWidth(UIElement forElement){
        Float parentWidth = UIHelpers.getCalculatedParentWidthInHierarchy(forElement);

        if(!isWithinHorizontalBox(forElement)){
            if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
                return parentWidth - forElement.getLocalPositionX();
            }
            else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
                return parentWidth;
            }
            else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
                return forElement.getLocalPositionX();
            }
        }
        else{
            ItemBox itemBox = forElement.getParent();

            float staticWidth = 0;
            float fillElementCount = 0;
            for(UIElement sibling : ((ItemBox) forElement.getParent()).getActiveChildren()){
                if(sibling.getWidthRaw() instanceof FillDimension){
                    fillElementCount++;
                }
                else{
                    staticWidth += sibling.getWidth();
                }
            }

            staticWidth += (((ItemBox) forElement.getParent()).getActiveChildren().size() - 1) * itemBox.getItemSpacing();

            return Math.max((parentWidth - staticWidth) / fillElementCount, 1f);
        }

        return null;
    }

    private boolean canCalculateWidth(UIElement forElement){
        if(!isWithinHorizontalBox(forElement)){
            return UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null && (forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER || !forElement.getLocalPositionXRaw().needsRecalculation());
        }
        else{
            for (UIElement sibling : ((ItemBox) forElement.getParent()).getActiveChildren()){
                if(sibling.getWidthRaw() instanceof FillDimension) continue;
                if(!sibling.needsWidthCalculation()) continue;

                return false;
            }

            return UIHelpers.getCalculatedParentWidthInHierarchy(forElement) != null;
        }
    }

    //endregion

    //region Height

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_FILL, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, calculateHeight(forElement)),
                () -> canCalculateHeight(forElement),
                () -> !forElement.getPaddingTopRaw().needsRecalculation()
        ));
    }

    private Float calculateHeight(UIElement forElement){
        Float parentHeight = UIHelpers.getCalculatedParentHeightInHierarchy(forElement);

        if(!isWithinVerticalBox(forElement)){
            if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
                return parentHeight - forElement.getLocalPositionY();
            }
            else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
                return parentHeight;
            }
            else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                return forElement.getLocalPositionY();
            }
        }
        else{
            ItemBox itemBox = forElement.getParent();

            int staticHeight = 0;
            int fillElementCount = 0;
            for(UIElement sibling : ((ItemBox) forElement.getParent()).getActiveChildren()){
                if(sibling.getHeightRaw() instanceof FillDimension){
                    fillElementCount++;
                }
                else{
                    staticHeight += sibling.getHeight();
                }
            }

            staticHeight += (((ItemBox) forElement.getParent()).getActiveChildren().size() - 1) * itemBox.getItemSpacing();

            return Math.max((parentHeight - staticHeight) / (float) fillElementCount, 1f);
        }

        return null;
    }

    private boolean canCalculateHeight(UIElement forElement){
        if(!isWithinVerticalBox(forElement)){
            return UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null && (forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER || !forElement.getLocalPositionYRaw().needsRecalculation());
        }
        else{
            for (UIElement sibling : ((ItemBox) forElement.getParent()).getActiveChildren()){
                if(sibling.getHeightRaw() instanceof FillDimension) continue;
                if(!sibling.needsHeightCalculation()) continue;

                return false;
            }

            return UIHelpers.getCalculatedParentHeightInHierarchy(forElement) != null;
        }
    }

    //endregion

    //endregion

    //region Utility Methods

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FillDimension;
    }

    @Override
    public AbstractDimension cpy() {
        FillDimension dimension = new FillDimension();
        dimension.copyFrom(this);
        return dimension;
    }

    @Override
    public String toString() {
        return "fill";
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new FillDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new FillDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //endregion
}
