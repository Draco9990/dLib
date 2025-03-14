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
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.elements.items.text.TextBox;
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
        if(forElement.getChildren().isEmpty()) return 0;

        Pair<Integer, Integer> totalWidth = null;
        for (UIElement child : forElement.getChildren()){
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
        if(totalWidth == null){
            return null;
        }

        if(forElement instanceof TextBox){
            int localPosX = forElement.getLocalPositionX();

            int width = ((TextBox) forElement).getTextWidth();
            if(totalWidth.getKey() < localPosX){
                totalWidth = new Pair<>(localPosX, totalWidth.getValue());
            }
            if(totalWidth.getValue() < localPosX + width){
                totalWidth = new Pair<>(totalWidth.getKey(), localPosX + width);
            }
        }

        return totalWidth.getValue() - totalWidth.getKey();
    }
    private Pair<Integer, Integer> calculateWidthRecursive(UIElement forElement){
        Pair<Integer, Integer> totalWidth = null;

        for (UIElement child : forElement.getChildren()) {
            Pair<Integer, Integer> childWidth = calculateWidthRecursive(child);
            if(totalWidth == null){
                totalWidth = childWidth;
            }
            else{
                if(forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL){
                    totalWidth = new Pair<>(totalWidth.getKey(), totalWidth.getValue() + (childWidth.getValue() - childWidth.getKey()));
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

        return new Pair<>(calculatedLocalX, calculatedLocalX + widthRaw + forElement.getPaddingLeft() + forElement.getPaddingRight());
    }

    private boolean canCalculateWidth(UIElement forElement){
        for (UIElement child : forElement.getAllChildren()) {
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

        if(forElement instanceof TextBox){
            if(forElement.getLocalPositionXRaw().needsRecalculation()){
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
        if(forElement.getChildren().isEmpty()) return 0;

        Pair<Integer, Integer> totalHeight = null;
        for (UIElement child : forElement.getChildren()){
            Pair<Integer, Integer> childHeight = calculateHeightRecursive(child);
            if(totalHeight == null){
                totalHeight = childHeight;
            }
            else{
                if(forElement instanceof ItemBox && ((ItemBox) forElement).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL){
                    totalHeight = new Pair<>(totalHeight.getKey(), totalHeight.getValue() + (childHeight.getValue() - childHeight.getKey()));
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
        if(totalHeight == null){
            return null;
        }

        if(forElement instanceof TextBox){
            int localPosY = forElement.getLocalPositionY();
            int height = ((TextBox) forElement).getTextHeight();
            if(totalHeight.getKey() < localPosY){
                totalHeight = new Pair<>(localPosY, totalHeight.getValue());
            }
            if(totalHeight.getValue() < localPosY + height){
                totalHeight = new Pair<>(totalHeight.getKey(), localPosY + height);
            }
        }

        return totalHeight.getValue() - totalHeight.getKey();
    }
    private Pair<Integer, Integer> calculateHeightRecursive(UIElement forElement){
        Pair<Integer, Integer> totalHeight = null;

        for (UIElement child : forElement.getChildren()) {
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

        return new Pair<>(calculatedLocalY, calculatedLocalY + heightRaw + forElement.getPaddingTop() + forElement.getPaddingBottom());
    }

    private boolean canCalculateHeight(UIElement forElement){
        for (UIElement child : forElement.getAllChildren()) {
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

        if(forElement instanceof TextBox){
            if(forElement.getLocalPositionYRaw().needsRecalculation()){
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
