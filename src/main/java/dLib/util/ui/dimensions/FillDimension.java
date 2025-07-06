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
                () -> {
                    boolean test = canCalculateWidth(forElement);
                    return test;
                },
                () -> !forElement.getPaddingRightRaw().needsRecalculation()
        ));
    }

    private Float calculateWidth(UIElement forElement){
        Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement);

        if(!isHorizontalBox(parentWidth.getValue())){
            if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
                return parentWidth.getKey() - forElement.getLocalPositionX();
            }
            else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
                return parentWidth.getKey();
            }
            else if(forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
                return forElement.getLocalPositionX();
            }
        }
        else{
            ItemBox itemBox = (ItemBox) parentWidth.getValue();

            float staticWidth = 0;
            float fillElementCount = 0;
            for(UIElement sibling : itemBox.getActiveChildren()){
                if(sibling.getWidthRaw() instanceof FillDimension || sibling.needsWidthCalculation()){ // Cases where sibling is parent of child but has something like auto dim
                    fillElementCount++;
                }
                else{
                    staticWidth += sibling.getWidth();
                }
            }

            staticWidth += (itemBox.getActiveChildren().size() - 1) * itemBox.getItemSpacing();

            return Math.max((parentWidth.getKey() - staticWidth) / fillElementCount, 1f);
        }

        return null;
    }

    private boolean canCalculateWidth(UIElement forElement){
        Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement);
        if(parentWidth.getKey() == null) return false;

        if(!isHorizontalBox(parentWidth.getValue())){
            return (forElement.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER || !forElement.getLocalPositionXRaw().needsRecalculation());
        }
        else{
            for (UIElement sibling : ((ItemBox) parentWidth.getValue()).getActiveChildren()){
                if(sibling.getWidthRaw() instanceof FillDimension) continue;
                if(!sibling.needsWidthCalculation()) continue;
                if(forElement.isDescendantOf(sibling)) continue;

                return false;
            }

            return true;
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
        Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement);

        if(!isVerticalBox(parentHeight.getValue())){
            if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
                return parentHeight.getKey() - forElement.getLocalPositionY();
            }
            else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
                return parentHeight.getKey();
            }
            else if(forElement.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                return forElement.getLocalPositionY();
            }
        }
        else{
            ItemBox itemBox = (ItemBox) parentHeight.getValue();

            float staticHeight = 0;
            int fillElementCount = 0;
            for(UIElement sibling : itemBox.getActiveChildren()){
                if(sibling.getHeightRaw() instanceof FillDimension || sibling.needsHeightCalculation()){ // Cases where sibling is parent of child but has something like auto dim
                    fillElementCount++;
                }
                else{
                    staticHeight += sibling.getHeight();
                }
            }

            staticHeight += (itemBox.getActiveChildren().size() - 1) * itemBox.getItemSpacing();

            return Math.max((parentHeight.getKey() - staticHeight) / (float) fillElementCount, 1f);
        }

        return null;
    }

    private boolean canCalculateHeight(UIElement forElement){
        Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement);
        if(parentHeight.getKey() == null) return false;

        if(!isVerticalBox(parentHeight.getValue())){
            return (forElement.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER || !forElement.getLocalPositionYRaw().needsRecalculation());
        }
        else{
            for (UIElement sibling : ((ItemBox) parentHeight.getValue()).getActiveChildren()){
                if(sibling.getHeightRaw() instanceof FillDimension) continue;
                if(!sibling.needsHeightCalculation()) continue;
                if(forElement.isDescendantOf(sibling)) continue;

                return false;
            }

            return true;
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
