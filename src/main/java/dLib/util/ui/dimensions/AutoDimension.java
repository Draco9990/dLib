package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AutoDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIOverlayElementComponent;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.layout.ILayoutProvider;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "auto")
public class AutoDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    //! Child dynamic calculations shouldn't account for padding
    private float calculatedValueForChildren = 0;

    //endregion

    //region Constructors

    public AutoDimension(){
        super();
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    @Override
    protected Float tryCalculateValue_Width(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        Float childVal = calculateWidth(forElement, calculationPass, true);
        if(childVal == null) return null;
        calculatedValueForChildren = childVal;

        return calculateWidth(forElement, calculationPass, true);
    }

    @Override
    protected Float tryCalculateValue_Height(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass) {
        Float childVal = calculateHeight(forElement, calculationPass, true);
        if(childVal == null) return null;
        calculatedValueForChildren = childVal;

        return calculateHeight(forElement, calculationPass, true);
    }


    //region Width

    private Float calculateWidth(UIElement forElement, ElementCalculationManager.CalculationPass pass, boolean includePadding){
        Pair<Float, Float> totalWidth = null;

        if(forElement instanceof ILayoutProvider && ((ILayoutProvider) forElement).providesWidth()){
            if(!((ILayoutProvider) forElement).canCalculateContentWidth(this)) return null;

            Float contentWidth = ((ILayoutProvider) forElement).calculateContentWidth(); // TODO register dependencies
            if(contentWidth == null) return null;

            if(includePadding){
                if(((ILayoutProvider) forElement).getContentPaddingLeftRaw().needsRecalculation()) return null;
                registerDependency(((ILayoutProvider) forElement).getContentPaddingLeftRaw());

                if(((ILayoutProvider) forElement).getContentPaddingRightRaw().needsRecalculation()) return null;
                registerDependency(((ILayoutProvider) forElement).getContentPaddingRightRaw());

                contentWidth += ((ILayoutProvider) forElement).getContentPaddingLeft() + ((ILayoutProvider) forElement).getContentPaddingRight();
            }

            totalWidth = new Pair<>(0f, contentWidth);
        }
        else{
            for (UIElement child : forElement.getChildren()){
                if(child.hasComponent(UIOverlayElementComponent.class)) continue;

                Pair<Float, Float> childWidth = calculateChildWidth(child, pass != ElementCalculationManager.CalculationPass.FIRST, includePadding);
                if(childWidth == null) {
                    if(pass == ElementCalculationManager.CalculationPass.FIRST) return null;
                    else continue;
                }

                if(totalWidth == null){
                    totalWidth = childWidth;
                }
                else{
                    if(childWidth.getKey() < totalWidth.getKey()){
                        totalWidth = new Pair<>(childWidth.getKey(), totalWidth.getValue());
                    }
                    if(childWidth.getValue() > totalWidth.getValue()){
                        totalWidth = new Pair<>(totalWidth.getKey(), childWidth.getValue());
                    }
                }
            }

            if(totalWidth == null){
                return null;
            }
        }

        if(forElement instanceof TextBox){
            if(forElement.getLocalPositionXRaw().needsRecalculation()) return null;
            registerDependency(forElement.getLocalPositionXRaw());
            float localPosX = forElement.getLocalPositionX();

            float width = ((TextBox) forElement).getTextWidth();

            totalWidth = new Pair<>(
                    totalWidth == null ? localPosX : Math.min(localPosX, totalWidth.getKey()),
                    totalWidth == null ? localPosX + width : Math.max(localPosX + width, totalWidth.getValue())
            );
        }

        if(totalWidth == null){
            return 0f;
        }
        return totalWidth.getValue() - totalWidth.getKey();
    }
    private Pair<Float, Float> calculateChildWidth(UIElement forElement, boolean canIgnoreMissingPos, boolean includePadding){
        float calculatedLocalX = 0;
        if(forElement.getLocalPositionXRaw().needsRecalculation()) {
            if(!canIgnoreMissingPos) return null;
        }
        else{
            calculatedLocalX = forElement.getLocalPositionX();
            registerDependency(forElement.getLocalPositionXRaw());
        }

        if(forElement.getWidthRaw().needsRecalculation()) return null;
        registerDependency(forElement.getWidthRaw());
        float widthRaw = forElement.getWidth();

        float bonusWidth = 0;
        if(forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL){
            bonusWidth = ((ItemBox) forElement).getItemSpacing();
        }
        if(includePadding){
            if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
            registerDependency(forElement.getPaddingLeftRaw());
            bonusWidth += forElement.getPaddingLeft();

            if(forElement.getPaddingRightRaw().needsRecalculation()) return null;
            registerDependency(forElement.getPaddingRightRaw());
            bonusWidth += forElement.getPaddingLeft() + forElement.getPaddingRight();
        }

        return new Pair<>(
                calculatedLocalX,
                calculatedLocalX + widthRaw + bonusWidth
        );
    }

    //endregion

    public float getCalculatedValueForChildren() {
        return calculatedValueForChildren;
    }

    //region Height

    private Float calculateHeight(UIElement forElement, ElementCalculationManager.CalculationPass pass, boolean includePadding){
        Pair<Float, Float> totalHeight = null;

        if(forElement instanceof ILayoutProvider && ((ILayoutProvider) forElement).providesHeight()){
            if(!((ILayoutProvider) forElement).canCalculateContentHeight(this)) return null;

            Float contentHeight = ((ILayoutProvider) forElement).calculateContentHeight(); // TODO register dependencies
            if(contentHeight == null) return null;

            if(includePadding){
                if(((ILayoutProvider) forElement).getContentPaddingBottomRaw().needsRecalculation()) return null;
                registerDependency(((ILayoutProvider) forElement).getContentPaddingBottomRaw());

                if(((ILayoutProvider) forElement).getContentPaddingTopRaw().needsRecalculation()) return null;
                registerDependency(((ILayoutProvider) forElement).getContentPaddingTopRaw());

                contentHeight += ((ILayoutProvider) forElement).getContentPaddingBottom() + ((ILayoutProvider) forElement).getContentPaddingTop();
            }

            totalHeight = new Pair<>(0f, contentHeight);
        }
        else{
            for (UIElement child : forElement.getChildren()){
                if(child.hasComponent(UIOverlayElementComponent.class)) continue;

                Pair<Float, Float> childHeight = calculateChildHeight(child, pass != ElementCalculationManager.CalculationPass.FIRST, includePadding);
                if(childHeight == null) {
                    if(pass == ElementCalculationManager.CalculationPass.FIRST) return null;
                    else continue;
                }

                if(totalHeight == null){
                    totalHeight = childHeight;
                }
                else{
                    if(childHeight.getKey() < totalHeight.getKey()){
                        totalHeight = new Pair<>(childHeight.getKey(), totalHeight.getValue());
                    }
                    if(childHeight.getValue() > totalHeight.getValue()){
                        totalHeight = new Pair<>(totalHeight.getKey(), childHeight.getValue());
                    }
                }
            }

            if(totalHeight == null){
                return null;
            }
        }

        if(forElement instanceof TextBox){
            if(forElement.getLocalPositionYRaw().needsRecalculation()) return null;
            registerDependency(forElement.getLocalPositionYRaw());
            float localPosY = forElement.getLocalPositionY();

            float height = ((TextBox) forElement).getTextHeight();

            totalHeight = new Pair<>(
                    totalHeight == null ? localPosY : Math.min(localPosY, totalHeight.getKey()),
                    totalHeight == null ? localPosY + height : Math.max(localPosY + height, totalHeight.getValue())
            );
        }

        if(totalHeight == null){
            return 0f;
        }
        return totalHeight.getValue() - totalHeight.getKey();
    }
    private Pair<Float, Float> calculateChildHeight(UIElement forElement, boolean canIgnoreMissingPos, boolean includePadding){
        float calculatedLocalY = 0;
        if(forElement.getLocalPositionYRaw().needsRecalculation()) {
            if(!canIgnoreMissingPos) return null;
        }
        else{
            calculatedLocalY = forElement.getLocalPositionY();
            registerDependency(forElement.getLocalPositionYRaw());
        }

        if(forElement.getHeightRaw().needsRecalculation()) return null;
        registerDependency(forElement.getHeightRaw());
        float heightRaw = forElement.getHeight();

        float bonusHeight = 0;
        if(forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.VERTICAL){
            bonusHeight = ((ItemBox) forElement).getItemSpacing();
        }
        if(includePadding){
            if(forElement.getPaddingTopRaw().needsRecalculation()) return null;
            registerDependency(forElement.getPaddingTopRaw());
            bonusHeight += forElement.getPaddingTop();

            if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;
            registerDependency(forElement.getPaddingBottomRaw());
            bonusHeight += forElement.getPaddingTop() + forElement.getPaddingBottom();
        }

        return new Pair<>(
                calculatedLocalY,
                calculatedLocalY + heightRaw + bonusHeight
        );
    }

    //endregion

    //endregion

    //region Utility Methods

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AutoDimension;
    }

    @Override
    public AbstractDimension cpy() {
        AutoDimension cpy = new AutoDimension();
        cpy.copyFrom(this);
        return cpy;
    }

    @Override
    public String toString() {
        return "auto";
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new AutoDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new AutoDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //endregion
}
