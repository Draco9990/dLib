package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.FillDimensionValueEditor;
import dLib.ui.Alignment;
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
    protected Float tryCalculateValue_Width(UIElement forElement) {
        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;

        return calculateWidth(forElement);
    }

    @Override
    protected Float tryCalculateValue_Height(UIElement forElement) {
        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;

        return calculateHeight(forElement);
    }


    //region Calculation Methods

    //region Width

    private Float calculateWidth(UIElement forElement){
        Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement);
        if(parentWidth.getKey() == null) return null;
        if(parentWidth.getValue() != null) registerDependency(parentWidth.getValue().getWidthRaw());

        if(!isHorizontalBox(parentWidth.getValue())){
            if(forElement.getLocalPositionXRaw().needsRecalculation()) return null;
            registerDependency(forElement.getLocalPositionXRaw());

            if(forElement.getPaddingRightRaw().needsRecalculation()) return null;
            registerDependency(forElement.getPaddingRightRaw());

            return parentWidth.getKey() - forElement.getLocalPositionX() - forElement.getPaddingRight();
        }
        else{
            ItemBox itemBox = (ItemBox) parentWidth.getValue();

            float staticWidth = 0;
            float fillElementCount = 0;
            for(UIElement sibling : itemBox.getActiveChildren()){
                if(sibling.getWidthRaw() instanceof FillDimension || sibling.getWidthRaw().needsRecalculation()){ // Cases where sibling is parent of child but has something like auto dim
                    fillElementCount++;
                }
                else{
                    if(sibling.getWidthRaw().needsRecalculation()) return null;
                    registerDependency(sibling.getWidthRaw());

                    staticWidth += sibling.getWidth();
                }
            }

            staticWidth += (itemBox.getActiveChildren().size() - 1) * itemBox.getItemSpacing();

            return Math.max((parentWidth.getKey() - staticWidth) / fillElementCount, 1f);
        }
    }

    //endregion

    //region Height

    private Float calculateHeight(UIElement forElement){
        Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement);
        if(parentHeight.getKey() == null) return null;
        if(parentHeight.getValue() != null) registerDependency(parentHeight.getValue().getHeightRaw());

        if(!isVerticalBox(parentHeight.getValue())){
            if(forElement.getLocalPositionYRaw().needsRecalculation()) return null;
            registerDependency(forElement.getLocalPositionYRaw());

            if(forElement.getPaddingTopRaw().needsRecalculation()) return null;
            registerDependency(forElement.getPaddingTopRaw());

            return parentHeight.getKey() - forElement.getLocalPositionY() - forElement.getPaddingTop();
        }
        else{
            ItemBox itemBox = (ItemBox) parentHeight.getValue();

            float staticHeight = 0;
            float fillElementCount = 0;
            for(UIElement sibling : itemBox.getActiveChildren()){
                if(sibling.getHeightRaw() instanceof FillDimension || sibling.getHeightRaw().needsRecalculation()){ // Cases where sibling is parent of child but has something like auto dim
                    fillElementCount++;
                }
                else{
                    if(sibling.getHeightRaw().needsRecalculation()) return null;
                    registerDependency(sibling.getHeightRaw());

                    staticHeight += sibling.getHeight();
                }
            }

            staticHeight += (itemBox.getActiveChildren().size() - 1) * itemBox.getItemSpacing();

            return Math.max((parentHeight.getKey() - staticHeight) / fillElementCount, 1f);
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
