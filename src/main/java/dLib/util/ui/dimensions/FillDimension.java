package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalItemBox;
import dLib.ui.elements.prefabs.VerticalItemBox;

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

        if(!(self.getParent().getParent() instanceof HorizontalItemBox)){
            Integer resultingWidth = self.getParent().getWidth() - self.getLocalPositionX();
            for(UIElement child : self.getParent().getChildren()){
                if(child == self){
                    continue;
                }

                if((child.getLocalPositionY() < self.getLocalPositionY() && !(child.getHeightRaw() instanceof FillDimension) && child.getLocalPositionY() + child.getHeight() >= self.getLocalPositionY()) ||
                    (child.getLocalPositionY() >= self.getLocalPositionY() && !(self.getHeightRaw() instanceof FillDimension) && child.getLocalPositionY() <= self.getLocalPositionY() + self.getHeight())){
                    if(child.getLocalPositionX() < self.getLocalPositionX()){
                        if(child.getLocalPositionX() + child.getWidth() >= self.getLocalPositionX()){
                            return 0;
                        }
                    }
                    else{
                        resultingWidth = Math.min(resultingWidth, child.getLocalPositionX() - self.getLocalPositionX());
                    }
                }
            }
            return resultingWidth;
        }
        else{
            UIElement fillChainStart = null;
            int fillChainCount = 0;
            boolean isPartOfCurrentFillChain = false;

            for (UIElement child : self.getParent().getChildren()){
                if(child.getWidthRaw() instanceof FillDimension){
                    if(child == self){
                        isPartOfCurrentFillChain = true;
                    }

                    if(fillChainStart == null){
                        fillChainStart = child;
                    }
                    fillChainCount++;
                }
                else if(isPartOfCurrentFillChain){
                    return (int) ((child.getLocalPositionX() - fillChainStart.getLocalPositionX()) / (float) fillChainCount);
                }
                else{
                    fillChainStart = null;
                    fillChainCount = 0;
                }
            }

            return (int) ((self.getParent().getWidth() - fillChainStart.getLocalPositionX()) / (float) fillChainCount);
        }
    }

    @Override
    public int getHeight(UIElement self) {
        if(self.getParent() == null) return 1080;

        if(!(self.getParent().getParent() instanceof VerticalItemBox)){
            Integer resultingHeight = self.getParent().getHeight() - self.getLocalPositionY();
            for(UIElement child : self.getParent().getChildren()){
                if(child == self){
                    continue;
                }

                if((child.getLocalPositionX() < self.getLocalPositionX() && !(child.getWidthRaw() instanceof FillDimension) && child.getLocalPositionX() + child.getWidth() >= self.getLocalPositionX()) ||
                        (child.getLocalPositionX() >= self.getLocalPositionX() && !(self.getWidthRaw() instanceof FillDimension) && child.getLocalPositionX() <= self.getLocalPositionX() + self.getWidth())){
                    if(child.getLocalPositionY() < self.getLocalPositionY()){
                        if(child.getLocalPositionY() + child.getHeight() >= self.getLocalPositionY()){
                            return 0;
                        }
                    }
                    else{
                        resultingHeight = Math.min(resultingHeight, child.getLocalPositionY() - self.getLocalPositionY());
                    }
                }
            }
            return resultingHeight;
        }
        else{
            UIElement fillChainStart = null;
            int fillChainCount = 0;
            boolean isPartOfCurrentFillChain = false;

            for (UIElement child : self.getParent().getChildren()){
                if(child.getHeightRaw() instanceof FillDimension){
                    if(child == self){
                        isPartOfCurrentFillChain = true;
                    }

                    if(fillChainStart == null){
                        fillChainStart = child;
                    }
                    fillChainCount++;
                }
                else if(isPartOfCurrentFillChain){
                    return (int) ((child.getLocalPositionY() - fillChainStart.getLocalPositionY()) / (float) fillChainCount);
                }
                else{
                    fillChainStart = null;
                    fillChainCount = 0;
                }
            }

            return (int) ((self.getParent().getHeight() - fillChainStart.getLocalPositionY()) / (float) fillChainCount);
        }
    }

    @Override
    public AbstractDimension cpy() {
        return new FillDimension();
    }
}
