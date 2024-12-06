package dLib.util.ui.dimensions;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIItemBoxElementHolderComponent;
import dLib.ui.elements.prefabs.HorizontalItemBox;
import dLib.ui.elements.prefabs.ItemBox;
import dLib.ui.elements.prefabs.VerticalItemBox;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.StaticPosition;

import java.util.ArrayList;

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
                    maxWidth = maxWidth - ((StaticPosition) self.getLocalPositionXRaw()).getVal();
                }
                else if(self.getLocalPositionXRaw() instanceof PercentagePosition){
                    maxWidth = maxWidth - (int) (self.getParent().getWidth() * ((PercentagePosition) self.getLocalPositionXRaw()).getVal());
                }
            }

            for(UIElement sibling : self.getParent().getChildren()){
                if(sibling == self){
                    continue;
                }

                if ((sibling.getLocalPositionY() >= self.getLocalPositionY() || sibling.getHeightRaw() instanceof FillDimension || sibling.getLocalPositionY() + sibling.getHeight() < self.getLocalPositionY()) &&
                        (sibling.getLocalPositionY() < self.getLocalPositionY() || self.getHeightRaw() instanceof FillDimension || sibling.getLocalPositionY() > self.getLocalPositionY() + self.getHeight())) {
                    continue;
                }

                if(sibling.getLocalPositionX() < self.getLocalPositionX()){
                    if(sibling.getLocalPositionX() + sibling.getWidth() >= self.getLocalPositionX()){
                        return 0;
                    }
                }
                else{
                    maxWidth = Math.min(maxWidth, sibling.getLocalPositionX() - self.getLocalPositionX());
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
            int resultingHeight = self.getParent().getHeight() - self.getLocalPositionY();
            if(self.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                if(self.getLocalPositionYRaw() instanceof StaticPosition){
                    resultingHeight = resultingHeight - ((StaticPosition) self.getLocalPositionYRaw()).getVal();
                }
                else if(self.getLocalPositionYRaw() instanceof PercentagePosition){
                    resultingHeight = resultingHeight - (int) (self.getParent().getHeight() * ((PercentagePosition) self.getLocalPositionYRaw()).getVal());
                }
            }

            for(UIElement sibling : self.getParent().getChildren()){
                if(sibling == self){
                    continue;
                }

                if ((sibling.getLocalPositionX() >= self.getLocalPositionX() || sibling.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() + sibling.getWidth() < self.getLocalPositionX()) &&
                        (sibling.getLocalPositionX() < self.getLocalPositionX() || self.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() > self.getLocalPositionX() + self.getWidth())) {
                    continue;
                }

                try{
                    if(sibling.getLocalPositionY() < self.getLocalPositionY()){
                        if(sibling.getLocalPositionY() + sibling.getHeight() >= self.getLocalPositionY()){
                            return 0;
                        }
                    }
                    else{
                        resultingHeight = Math.min(resultingHeight, sibling.getLocalPositionY() - self.getLocalPositionY());
                    }
                }
                catch (StackOverflowError e) {
                    System.out.println("Error");
                }
            }
            return resultingHeight;
        }
    }

    @Override
    public AbstractDimension cpy() {
        return new FillDimension();
    }
}
