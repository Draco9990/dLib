package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.FillDimensionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ItemboxChildComponent;
import dLib.ui.elements.items.itembox.HorizontalListBox;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.elements.items.itembox.GridItemBox;
import dLib.ui.elements.items.itembox.VerticalListBox;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.StaticPosition;

public class FillDimension extends AbstractDimension {
    public FillDimension(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FillDimension;
    }

    @Override
    public int getWidth(UIElement self) {
        if(self.getParent() == null) return 1920;

        if((self.getParent() instanceof HorizontalListBox) && ((ItemBox) self.getParent()).containsRenderItem(self)){
            ItemBox itemBox = self.getParent();

            int staticWidth = 0;
            int fillElementCount = 0;
            for(UIElement sibling : self.getParent().getChildren()){
                if(!sibling.hasComponent(ItemboxChildComponent.class)){
                    continue;
                }

                if(!(sibling.getWidthRaw() instanceof FillDimension)){ //* Implies sibling != self
                    staticWidth += sibling.getWidth();
                }
                else{
                    fillElementCount++;
                }

                staticWidth += sibling.getPaddingLeft() + sibling.getPaddingRight();
            }

            staticWidth += (self.getParent().getChildren().size() -1 ) * itemBox.getItemSpacing();

            return Math.max((int) ((self.getParent().getWidth() - staticWidth) / (float) fillElementCount), 1);
        }
        else{
            int maxWidth = self.getParent().getWidth() - self.getLocalPositionX();
            if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
                if(self.getLocalPositionXRaw() instanceof StaticPosition){
                    maxWidth -= ((StaticPosition) self.getLocalPositionXRaw()).getValueRaw();
                }
                else if(self.getLocalPositionXRaw() instanceof PercentagePosition){
                    maxWidth -= (int) (maxWidth * ((PercentagePosition) self.getLocalPositionXRaw()).getValueRaw());
                }
            }

            return maxWidth;
        }
    }

    @Override
    public int getHeight(UIElement self) {
        if(self.getParent() == null) return 1080;

        if((self.getParent() instanceof VerticalListBox || self.getParent() instanceof GridItemBox) && ((ItemBox) self.getParent()).containsRenderItem(self)){
            ItemBox itemBox = self.getParent();

            int staticHeight = 0;
            int fillElementCount = 0;
            for(UIElement sibling : self.getParent().getChildren()){
                if(!sibling.hasComponent(ItemboxChildComponent.class)){
                    continue;
                }

                if(!(sibling.getHeightRaw() instanceof FillDimension)){ //* Implies sibling != self
                    staticHeight += sibling.getHeight();
                }
                else{
                    fillElementCount++;
                }

                staticHeight += sibling.getPaddingTop() + sibling.getPaddingBottom();
            }

            staticHeight += (self.getParent().getChildren().size() -1 ) * itemBox.getItemSpacing();

            return Math.max((int) ((self.getParent().getHeight() - staticHeight) / (float) fillElementCount), 1);
        }
        else{
            int maxHeight = self.getParent().getHeight() - self.getLocalPositionY();
            if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                if(self.getLocalPositionYRaw() instanceof StaticPosition){
                    maxHeight -= ((StaticPosition) self.getLocalPositionYRaw()).getValueRaw();
                }
                else if(self.getLocalPositionYRaw() instanceof PercentagePosition){
                    maxHeight -= (int) (maxHeight * ((PercentagePosition) self.getLocalPositionYRaw()).getValueRaw());
                }
            }

            return maxHeight;
        }
    }

    @Override
    public void setValueFromString(String value) {

    }

    @Override
    public void resizeWidthBy(UIElement self, int amount) {

    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {

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
        return new FillDimension();
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
