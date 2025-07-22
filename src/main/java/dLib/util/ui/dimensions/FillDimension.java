package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.FillDimensionValueEditor;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
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

    @Override
    protected Float tryCalculateValue_Width(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;

        return calculateWidth(forElement, calculationPass);
    }

    @Override
    protected Float tryCalculateValue_Height(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;

        return calculateHeight(forElement, calculationPass);
    }


    //region Calculation Methods

    //region Width

    private Float calculateWidth(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass){
        Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
        if(parentWidth.getKey() == null) return null;
        if(parentWidth.getValue() != null) registerDependency(parentWidth.getValue().getWidthRaw());

        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingLeftRaw());

        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingRightRaw());

        UIElement analyzingParent = forElement.getParent();
        if(parentWidth.getValue() != analyzingParent){
            for(UIElement child : parentWidth.getValue().getChildren()){
                if(forElement == child || forElement.isDescendantOf(child)) {
                    analyzingParent = child;
                    break;
                }
            }
        }

        if(!isHorizontalBox(analyzingParent)){
            if(forElement.getLocalPositionXRaw().needsRecalculation()) return null;
            registerDependency(forElement.getLocalPositionXRaw());

            return parentWidth.getKey() - forElement.getPaddingLeft() - forElement.getPaddingRight() - forElement.getLocalPositionX();
        }
        else{
            ItemBox itemBox = (ItemBox) analyzingParent;

            float staticWidth = 0;
            float fillElementCount = 0;
            for(UIElement sibling : itemBox.getActiveChildren()){
                if(sibling.getWidthRaw() instanceof FillDimension || (sibling.getWidthRaw() instanceof AutoDimension && sibling.getWidthRaw().needsRecalculation() && calculationPass == ElementCalculationManager.CalculationPass.THIRD)){
                    fillElementCount++;
                }
                else{
                    if(sibling.getWidthRaw().needsRecalculation()) return null;
                    registerDependency(sibling.getWidthRaw());

                    staticWidth += sibling.getWidth();
                }
            }

            staticWidth += (itemBox.getActiveChildren().size() - 1) * itemBox.getHorizontalItemSpacing();

            return Math.max((parentWidth.getKey() - staticWidth) / fillElementCount, 1f) - forElement.getPaddingLeft() - forElement.getPaddingRight();
        }
    }

    //endregion

    //region Height

    private Float calculateHeight(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass){
        Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement, calculationPass == ElementCalculationManager.CalculationPass.THIRD);
        if(parentHeight.getKey() == null) return null;
        if(parentHeight.getValue() != null) registerDependency(parentHeight.getValue().getHeightRaw());

        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingBottomRaw());

        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingTopRaw());


        UIElement analyzingParent = forElement.getParent();
        if(parentHeight.getValue() != analyzingParent){
            for(UIElement child : parentHeight.getValue().getChildren()){
                if(forElement == child || forElement.isDescendantOf(child)) {
                    analyzingParent = child;
                    break;
                }
            }
        }

        if(!isVerticalBox(analyzingParent)){
            if(forElement.getLocalPositionYRaw().needsRecalculation()) return null;
            registerDependency(forElement.getLocalPositionYRaw());

            return parentHeight.getKey() - forElement.getPaddingBottom() - forElement.getPaddingTop() - forElement.getLocalPositionY();
        }
        else{
            ItemBox itemBox = (ItemBox) analyzingParent;

            float staticHeight = 0;
            float fillElementCount = 0;
            for(UIElement sibling : itemBox.getActiveChildren()){
                if(sibling.getHeightRaw() instanceof FillDimension || (sibling.getHeightRaw() instanceof AutoDimension && sibling.getHeightRaw().needsRecalculation() && calculationPass == ElementCalculationManager.CalculationPass.THIRD)){
                    fillElementCount++;
                }
                else{
                    if(sibling.getHeightRaw().needsRecalculation()) return null;
                    registerDependency(sibling.getHeightRaw());

                    staticHeight += sibling.getHeight();
                }
            }

            staticHeight += (itemBox.getActiveChildren().size() - 1) * itemBox.getVerticalItemSpacing();

            return Math.max((parentHeight.getKey() - staticHeight) / fillElementCount, 1f) - forElement.getPaddingBottom() - forElement.getPaddingTop();
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
