package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AutoDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIOverlayElementComponent;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.layout.ILayoutProvider;
import dLib.util.ui.position.PixelPosition;

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
    protected Float tryCalculateValue_Width(UIElement forElement) {
        if(!canCalculateWidth(forElement)) return null;

        calculatedValueForChildren = calculateWidth(forElement, false);
        return calculateWidth(forElement, true);
    }

    @Override
    protected Float tryCalculateValue_Height(UIElement forElement) {
        if(!canCalculateHeight(forElement)) return null;

        calculatedValueForChildren = calculateHeight(forElement, false);
        return calculateHeight(forElement, true);
    }


    //region Width

    private Float calculateWidth(UIElement forElement, boolean includePadding){
        Pair<Float, Float> totalWidth = null;

        if(forElement instanceof ILayoutProvider && ((ILayoutProvider) forElement).providesWidth()){
            totalWidth = ((ILayoutProvider) forElement).calculateContentWidth();
        }
        else{
            for (UIElement child : forElement.getChildren()){
                if(child.hasComponent(UIOverlayElementComponent.class)) continue;

                Pair<Float, Float> childWidth = calculateWidthRecursive(child, includePadding);
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
        }

        if(forElement instanceof ILayoutProvider && includePadding){
            float width = ((ILayoutProvider) forElement).getContentPaddingLeft() + ((ILayoutProvider) forElement).getContentPaddingRight();

            totalWidth = new Pair<>(
                    totalWidth == null ? 0 : totalWidth.getKey() - ((ILayoutProvider) forElement).getContentPaddingLeft(),
                    totalWidth == null ? width : totalWidth.getValue() + ((ILayoutProvider) forElement).getContentPaddingRight()
            );
        }

        if(forElement instanceof TextBox){
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
    private Pair<Float, Float> calculateWidthRecursive(UIElement forElement, boolean includePadding){
        float calculatedLocalX = forElement.getLocalPositionX();
        float widthRaw = forElement.getWidth();
        float bonusWidth = (forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL) ? ((ItemBox) forElement).getItemSpacing() : 0;

        return new Pair<>(calculatedLocalX, calculatedLocalX + widthRaw + (includePadding ? forElement.getPaddingLeft() + forElement.getPaddingRight() : 0) + bonusWidth);
    }

    private boolean canCalculateWidth(UIElement forElement){
        for (UIElement child : forElement.getAllChildren()) {
            if(child.hasComponent(UIOverlayElementComponent.class)) continue;

            if(child.getParent() == forElement){
                if(child.getWidthRaw().needsRecalculation()){
                    return false;
                }

                if(child.getLocalPositionXRaw().needsRecalculation()){
                    if(child.getLocalPositionXRaw() instanceof PixelPosition && child.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
                        return false;
                    }
                }
            }
            else{
                if(child.getWidthRaw().needsRecalculation() || child.getLocalPositionXRaw().needsRecalculation()){
                    return false;
                }
            }

            if(child.getPaddingLeftRaw().needsRecalculation() || child.getPaddingRightRaw().needsRecalculation()){
                return false;
            }
        }

        if(forElement instanceof ILayoutProvider){
            if(((ILayoutProvider) forElement).getContentPaddingLeftRaw().needsRecalculation() || ((ILayoutProvider) forElement).getContentPaddingRightRaw().needsRecalculation()){
                return false;
            }

            if(((ILayoutProvider) forElement).providesWidth() && !((ILayoutProvider) forElement).canCalculateContentWidth()){
                return false;
            }
        }

        return true;
    }

    //endregion

    public float getCalculatedValueForChildren() {
        return calculatedValueForChildren;
    }

    //region Height

    private Float calculateHeight(UIElement forElement, boolean includePadding){
        Pair<Float, Float> totalHeight = null;

        if(forElement instanceof ILayoutProvider && ((ILayoutProvider) forElement).providesHeight()){
            totalHeight = ((ILayoutProvider) forElement).calculateContentHeight();
        }
        else{
            for (UIElement child : forElement.getChildren()){
                if(child.hasComponent(UIOverlayElementComponent.class)) continue;

                Pair<Float, Float> childHeight = calculateHeightRecursive(child, includePadding);
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
        }

        if(forElement instanceof ILayoutProvider && includePadding){
            float height = ((ILayoutProvider) forElement).getContentPaddingBottom() + ((ILayoutProvider) forElement).getContentPaddingTop();

            totalHeight = new Pair<>(
                    totalHeight == null ? 0 : totalHeight.getKey() - ((ILayoutProvider) forElement).getContentPaddingBottom(),
                    totalHeight == null ? height : totalHeight.getValue() + ((ILayoutProvider) forElement).getContentPaddingTop()
            );
        }

        if(forElement instanceof TextBox){
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
    private Pair<Float, Float> calculateHeightRecursive(UIElement forElement, boolean includePadding){
        float calculatedLocalY = forElement.getLocalPositionY();
        float heightRaw = forElement.getHeight();
        float bonusHeight = (forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.VERTICAL) ? ((ItemBox) forElement).getItemSpacing() : 0;

        return new Pair<>(calculatedLocalY, calculatedLocalY + heightRaw + (includePadding ? forElement.getPaddingTop() + forElement.getPaddingBottom() : 0) + bonusHeight);
    }

    private boolean canCalculateHeight(UIElement forElement){
        for (UIElement child : forElement.getAllChildren()) {
            if(child.hasComponent(UIOverlayElementComponent.class)) continue;

            if(child.getParent() == forElement){
                if(child.getHeightRaw().needsRecalculation()){
                    if(!(child.getHeightRaw() instanceof FillDimension) && !(child.getHeightRaw() instanceof PercentageDimension)){
                        return false;
                    }
                }

                if(child.getLocalPositionYRaw().needsRecalculation()){
                    if(child.getLocalPositionYRaw() instanceof PixelPosition && child.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
                        return false;
                    }
                }
            }
            else{
                if(child.getHeightRaw().needsRecalculation() || child.getLocalPositionYRaw().needsRecalculation()){
                    return false;
                }
            }

            if(child.getPaddingTopRaw().needsRecalculation() || child.getPaddingBottomRaw().needsRecalculation()){
                return false;
            }
        }

        if(forElement instanceof ILayoutProvider){
            if(((ILayoutProvider) forElement).getContentPaddingTopRaw().needsRecalculation() || ((ILayoutProvider) forElement).getContentPaddingBottomRaw().needsRecalculation()){
                return false;
            }

            if(((ILayoutProvider) forElement).providesHeight() && !((ILayoutProvider) forElement).canCalculateContentHeight()){
                return false;
            }
        }

        if(forElement instanceof TextBox){
            if(forElement.getWidthRaw().needsRecalculation()){
                return false;
            }
        }

        return true;
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
