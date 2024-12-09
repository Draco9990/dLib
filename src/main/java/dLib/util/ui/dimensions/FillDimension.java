package dLib.util.ui.dimensions;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIItemBoxElementHolderComponent;
import dLib.ui.elements.prefabs.HorizontalItemBox;
import dLib.ui.elements.prefabs.VerticalItemBox;
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

        if(self.getParent().hasComponent(UIItemBoxElementHolderComponent.class) && self.getParent().getComponent(UIItemBoxElementHolderComponent.class).isHorizontal()){
            HorizontalItemBox itemBox = self.getParent().getParent();

            int staticWidth = 0;
            int fillElementCount = 0;
            for(UIElement sibling : self.getParent().getChildren()){
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
                    maxWidth -= ((StaticPosition) self.getLocalPositionXRaw()).getVal();
                }
                else if(self.getLocalPositionXRaw() instanceof PercentagePosition){
                    maxWidth -= (int) (maxWidth * ((PercentagePosition) self.getLocalPositionXRaw()).getVal());
                }
            }

            return maxWidth;
        }
    }

    @Override
    public int getHeight(UIElement self) {
        if(self.getParent() == null) return 1080;

        if(self.getParent().hasComponent(UIItemBoxElementHolderComponent.class) && self.getParent().getComponent(UIItemBoxElementHolderComponent.class).isVertical()){
            VerticalItemBox itemBox = self.getParent().getParent();

            int staticHeight = 0;
            int fillElementCount = 0;
            for(UIElement sibling : self.getParent().getChildren()){
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
                    maxHeight -= ((StaticPosition) self.getLocalPositionYRaw()).getVal();
                }
                else if(self.getLocalPositionYRaw() instanceof PercentagePosition){
                    maxHeight -= (int) (maxHeight * ((PercentagePosition) self.getLocalPositionYRaw()).getVal());
                }
            }

            return maxHeight;
        }
    }

    @Override
    public AbstractDimension cpy() {
        return new FillDimension();
    }
}
