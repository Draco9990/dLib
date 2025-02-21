package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.FillDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.GridItemBox;
import dLib.ui.elements.items.itembox.HorizontalDataBox;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.PixelPosition;

import java.io.Serializable;

public class FillDimension extends AbstractDynamicDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    public FillDimension(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FillDimension;
    }

    @Override
    public int calculateDimension(UIElement self) {
        if(refDimension == AbstractDimension.ReferenceDimension.WIDTH){
            return calculateWidth(self);
        }
        else if (refDimension == AbstractDimension.ReferenceDimension.HEIGHT){
            return calculateHeight(self);
        }

        return 1;
    }

    public int calculateWidth(UIElement self) {
        if(self.getParent() == null) return 1920;

        int parentWidth = 1920;
        UIElement parent = self.getParent();
        while(parent.getWidthRaw() instanceof AutoDimension){
            parent = parent.getParent();
            if(parent == null){
                break;
            }
        }

        if(parent != null){
            parentWidth = parent.getWidth();
        }

        if(self.getParent() instanceof ItemBox && ((ItemBox)self.getParent()).getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL){
            ItemBox itemBox = self.getParent();

            parentWidth -= itemBox.getContentPaddingLeft() + itemBox.getContentPaddingRight();

            int staticWidth = 0;
            int fillElementCount = 0;
            for(UIElement sibling : ((ItemBox) self.getParent()).getActiveChildren()){
                if(!(sibling.getWidthRaw() instanceof FillDimension)){ //* Implies sibling != self
                    staticWidth += sibling.getWidth();
                }
                else{
                    fillElementCount++;
                }

                staticWidth += sibling.getPaddingLeft() + sibling.getPaddingRight();
            }

            staticWidth += (((ItemBox) self.getParent()).getActiveChildren().size() -1 ) * itemBox.getItemSpacing();

            return Math.max((int) ((parentWidth - staticWidth) / (float) fillElementCount), 1);
        }
        else{
            int maxWidth = parentWidth - self.getLocalPositionX();
            if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
                if(self.getLocalPositionXRaw() instanceof PixelPosition){
                    maxWidth -= ((PixelPosition) self.getLocalPositionXRaw()).getValueRaw();
                }
                else if(self.getLocalPositionXRaw() instanceof PercentagePosition){
                    maxWidth -= (int) (maxWidth * ((PercentagePosition) self.getLocalPositionXRaw()).getValueRaw());
                }
            }

            return maxWidth;
        }
    }

    public int calculateHeight(UIElement self) {
        if(self.getParent() == null) return 1080;

        int parentHeight = 1080;
        UIElement parent = self.getParent();
        while(parent.getHeightRaw() instanceof AutoDimension){
            parent = parent.getParent();
            if(parent == null){
                break;
            }
        }

        if(parent != null){
            parentHeight = parent.getHeight();
        }

        if(self.getParent() instanceof ItemBox && ((ItemBox)self.getParent()).getContentAlignmentType() == Alignment.AlignmentType.VERTICAL){
            ItemBox itemBox = self.getParent();

            parentHeight -= itemBox.getContentPaddingTop() + itemBox.getContentPaddingBottom();

            int staticHeight = 0;
            int fillElementCount = 0;
            for(UIElement sibling : ((ItemBox) self.getParent()).getActiveChildren()){
                if(!(sibling.getHeightRaw() instanceof FillDimension)){ //* Implies sibling != self
                    staticHeight += sibling.getHeight();
                }
                else{
                    fillElementCount++;
                }

                staticHeight += sibling.getPaddingTop() + sibling.getPaddingBottom();
            }

            staticHeight += (((ItemBox) self.getParent()).getActiveChildren().size() -1 ) * itemBox.getItemSpacing();

            return Math.max((int) ((parentHeight - staticHeight) / (float) fillElementCount), 1);
        }
        else{
            int maxHeight = parentHeight - self.getLocalPositionY();
            if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                if(self.getLocalPositionYRaw() instanceof PixelPosition){
                    maxHeight -= ((PixelPosition) self.getLocalPositionYRaw()).getValueRaw();
                }
                else if(self.getLocalPositionYRaw() instanceof PercentagePosition){
                    maxHeight -= (int) (maxHeight * ((PercentagePosition) self.getLocalPositionYRaw()).getValueRaw());
                }
            }

            return maxHeight;
        }
    }

    @Override
    public void resizeBy(UIElement self, int amount) {
    }

    @Override
    public void setValueFromString(String value) {

    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new FillDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new FillDimensionValueEditor((DimensionProperty) property);
    }

    @Override
    public AbstractDimension cpy() {
        FillDimension dimension = new FillDimension();
        dimension.setReferenceDimension(this.refDimension);
        return dimension;
    }

    @Override
    public String getSimpleDisplayName() {
        return "fill";
    }

    @Override
    public String toString() {
        return "fill";
    }
}
