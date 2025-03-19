package dLib.util.ui.dimensions;

import basemod.Pair;
import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.AutoDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.ElementCalculationManager;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.descriptors.ElementDescriptorCalcOrders;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.layout.ILayoutProvider;
import dLib.util.ui.position.PixelPosition;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "auto")
public class AutoDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public AutoDimension(){
        super();
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    //region Width

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Width(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_AUTO, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, calculateWidth(forElement)),
                () -> canCalculateWidth(forElement)
        ));
    }

    private Integer calculateWidth(UIElement forElement){
        Pair<Integer, Integer> totalWidth = null;
        for (UIElement child : forElement.getChildren()){
            if(child.hasComponent(UITransientElementComponent.class)) continue;

            Pair<Integer, Integer> childWidth = calculateWidthRecursive(child);
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

        if(forElement instanceof ILayoutProvider){
            int width = ((ILayoutProvider) forElement).getContentPaddingLeft() + ((ILayoutProvider) forElement).getContentPaddingRight();

            totalWidth = new Pair<>(
                    totalWidth == null ? 0 : totalWidth.getKey() - ((ILayoutProvider) forElement).getContentPaddingLeft(),
                    totalWidth == null ? width : totalWidth.getValue() + ((ILayoutProvider) forElement).getContentPaddingRight()
            );
        }

        if(forElement instanceof TextBox){
            int localPosX = forElement.getLocalPositionX();
            int width = ((TextBox) forElement).getTextWidth();

            totalWidth = new Pair<>(
                    totalWidth == null ? localPosX : Math.min(localPosX, totalWidth.getKey()),
                    totalWidth == null ? localPosX + width : Math.max(localPosX + width, totalWidth.getValue())
            );
        }

        if(totalWidth == null){
            return 0;
        }
        return totalWidth.getValue() - totalWidth.getKey();
    }
    private Pair<Integer, Integer> calculateWidthRecursive(UIElement forElement){
        Pair<Integer, Integer> totalWidth = null;

        for (UIElement child : forElement.getChildren()) {
            if(child.hasComponent(UITransientElementComponent.class)) continue;

            Pair<Integer, Integer> childWidth = calculateWidthRecursive(child);
            if(totalWidth == null){
                totalWidth = childWidth;
            }
            else{
                if(forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL){
                    if(((ItemBox) forElement).isChildVisible(child)) totalWidth = new Pair<>(totalWidth.getKey(), totalWidth.getValue() + (childWidth.getValue() - childWidth.getKey()) + ((ItemBox) forElement).getItemSpacing());
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

        int calculatedLocalX = forElement.getLocalPositionX();
        int widthRaw = forElement.getWidth();
        int bonusWidth = (forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL) ? ((ItemBox) forElement).getItemSpacing() : 0;

        return new Pair<>(calculatedLocalX, calculatedLocalX + widthRaw + forElement.getPaddingLeft() + forElement.getPaddingRight() + bonusWidth);
    }

    private boolean canCalculateWidth(UIElement forElement){
        for (UIElement child : forElement.getAllChildren()) {
            if(child.hasComponent(UITransientElementComponent.class)) continue;

            if(child.getParent() == forElement){
                if(child.needsWidthCalculation()){
                    return false;
                }

                if(child.getLocalPositionXRaw().needsRecalculation()){
                    if(child.getLocalPositionXRaw() instanceof PixelPosition && child.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
                        return false;
                    }
                }
            }
            else{
                if(child.needsWidthCalculation() || child.getLocalPositionXRaw().needsRecalculation()){
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
        }

        return true;
    }

    //endregion

    //region Height

    @Override
    protected Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationFormula_Height(UIElement forElement) {
        return new Pair<>(ElementDescriptorCalcOrders.DIMENSION_AUTO, new ElementCalculationManager.ElementCalculationInstruction(
                () -> setCalculatedValue(forElement, calculateHeight(forElement)),
                () -> canCalculateHeight(forElement)
        ));
    }

    private Integer calculateHeight(UIElement forElement){
        Pair<Integer, Integer> totalHeight = null;
        for (UIElement child : forElement.getChildren()){
            if(child.hasComponent(UITransientElementComponent.class)) continue;

            Pair<Integer, Integer> childHeight = calculateHeightRecursive(child);
            if(totalHeight == null){
                totalHeight = childHeight;
            }
            else{
                if(forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.VERTICAL){
                    if(((ItemBox) forElement).isChildVisible(child)) totalHeight = new Pair<>(totalHeight.getKey(), totalHeight.getValue() + (childHeight.getValue() - childHeight.getKey()) + ((ItemBox) forElement).getItemSpacing());
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

        if(forElement instanceof ILayoutProvider){
            int height = ((ILayoutProvider) forElement).getContentPaddingBottom() + ((ILayoutProvider) forElement).getContentPaddingTop();

            totalHeight = new Pair<>(
                    totalHeight == null ? 0 : totalHeight.getKey() - ((ILayoutProvider) forElement).getContentPaddingBottom(),
                    totalHeight == null ? height : totalHeight.getValue() + ((ILayoutProvider) forElement).getContentPaddingTop()
            );
        }

        if(forElement instanceof TextBox){
            int localPosY = forElement.getLocalPositionY();
            int height = ((TextBox) forElement).getTextHeight();

            totalHeight = new Pair<>(
                    totalHeight == null ? localPosY : Math.min(localPosY, totalHeight.getKey()),
                    totalHeight == null ? localPosY + height : Math.max(localPosY + height, totalHeight.getValue())
            );
        }

        if(totalHeight == null){
            return 0;
        }
        return totalHeight.getValue() - totalHeight.getKey();
    }
    private Pair<Integer, Integer> calculateHeightRecursive(UIElement forElement){
        Pair<Integer, Integer> totalHeight = null;

        for (UIElement child : forElement.getChildren()) {
            if(child.hasComponent(UITransientElementComponent.class)) continue;

            Pair<Integer, Integer> childHeight = calculateHeightRecursive(child);
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

        int calculatedLocalY = forElement.getLocalPositionY();
        int heightRaw = forElement.getHeight();
        int bonusHeight = (forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.VERTICAL) ? ((ItemBox) forElement).getItemSpacing() : 0;

        return new Pair<>(calculatedLocalY, calculatedLocalY + heightRaw + forElement.getPaddingTop() + forElement.getPaddingBottom() + bonusHeight);
    }

    private boolean canCalculateHeight(UIElement forElement){
        for (UIElement child : forElement.getAllChildren()) {
            if(child.hasComponent(UITransientElementComponent.class)) continue;

            if(child.getParent() == forElement){
                if(child.needsHeightCalculation()){
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
                if(child.needsHeightCalculation() || child.getLocalPositionYRaw().needsRecalculation()){
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
