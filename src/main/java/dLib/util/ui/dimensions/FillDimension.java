package dLib.util.ui.dimensions;

import dLib.ui.Alignment;
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

        if(self.getParent().getParent() instanceof HorizontalItemBox){
            UIElement fillChainStart = null;
            int fillChainCount = 0;
            boolean isPartOfCurrentFillChain = false;

            for (UIElement sibling : self.getParent().getChildren()){
                if(sibling.getWidthRaw() instanceof FillDimension){
                    if(sibling == self){
                        isPartOfCurrentFillChain = true;
                    }

                    if(fillChainStart == null){
                        fillChainStart = sibling;
                    }
                    fillChainCount++;
                }
                else if(isPartOfCurrentFillChain){
                    return (int) ((sibling.getLocalPositionX() - fillChainStart.getLocalPositionX()) / (float) fillChainCount);
                }
                else{
                    fillChainStart = null;
                    fillChainCount = 0;
                }
            }

            return (int) ((self.getParent().getWidth() - fillChainStart.getLocalPositionX()) / (float) fillChainCount);
        }
        else{
            if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
                return calculateLeftToRightFillWidth(self);
            }
            else{
                return calculateRightToLeftFillWidth(self);
            }
        }
    }

    private int calculateLeftToRightFillWidth(UIElement self){
        Integer resultingWidth = self.getParent().getWidth() - self.getLocalPositionX();
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
                resultingWidth = Math.min(resultingWidth, sibling.getLocalPositionX() - self.getLocalPositionX());
            }
        }
        return resultingWidth;
    }

    private int calculateRightToLeftFillWidth(UIElement self){
        Integer resultingWidth = self.getParent().getWidth() - self.getLocalPositionX();
        for(UIElement sibling : self.getParent().getChildren()){
            if(sibling == self){
                continue;
            }

            if ((sibling.getLocalPositionY() >= self.getLocalPositionY() || sibling.getHeightRaw() instanceof FillDimension || sibling.getLocalPositionY() + sibling.getHeight() < self.getLocalPositionY()) &&
                (sibling.getLocalPositionY() < self.getLocalPositionY() || self.getHeightRaw() instanceof FillDimension || sibling.getLocalPositionY() > self.getLocalPositionY() + self.getHeight())) {
                continue;
            }

            if(sibling.getLocalPositionX() > self.getLocalPositionX()){
                if(sibling.getLocalPositionX() - sibling.getWidth() <= self.getLocalPositionX()){
                    return 0;
                }
            }
            else{
                resultingWidth = Math.min(resultingWidth,  self.getLocalPositionX() - sibling.getLocalPositionX());
            }
        }
        return resultingWidth;
    }

    @Override
    public int getHeight(UIElement self) {
        if(self.getParent() == null) return 1080;

        if(self.getParent().getParent() instanceof VerticalItemBox){
            UIElement fillChainStart = null;
            int fillChainCount = 0;
            boolean isPartOfCurrentFillChain = false;

            for (UIElement sibling : self.getParent().getChildren()){
                if(sibling.getHeightRaw() instanceof FillDimension){
                    if(sibling == self){
                        isPartOfCurrentFillChain = true;
                    }

                    if(fillChainStart == null){
                        fillChainStart = sibling;
                    }
                    fillChainCount++;
                }
                else if(isPartOfCurrentFillChain){
                    return (int) ((sibling.getLocalPositionY() - fillChainStart.getLocalPositionY()) / (float) fillChainCount);
                }
                else{
                    fillChainStart = null;
                    fillChainCount = 0;
                }
            }

            return (int) ((self.getParent().getHeight() - fillChainStart.getLocalPositionY()) / (float) fillChainCount);
        }
        else{
            if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
                return calculateDownToTopFillHeight(self);
            }
            else{
                return calculateTopToBottomFillHeight(self);
            }
        }
    }

    private int calculateDownToTopFillHeight(UIElement self){
        Integer resultingHeight = self.getParent().getHeight() - self.getLocalPositionY();
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

    private int calculateTopToBottomFillHeight(UIElement self){
        Integer resultingHeight = self.getParent().getHeight() - self.getLocalPositionY();
        for(UIElement sibling : self.getParent().getChildren()){
            if(sibling == self){
                continue;
            }

            if ((sibling.getLocalPositionX() >= self.getLocalPositionX() || sibling.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() + sibling.getWidth() < self.getLocalPositionX()) &&
                (sibling.getLocalPositionX() < self.getLocalPositionX() || self.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() > self.getLocalPositionX() + self.getWidth())) {
                continue;
            }

            if(sibling.getLocalPositionY() > self.getLocalPositionY()){
                if(sibling.getLocalPositionY() - sibling.getHeight() <= self.getLocalPositionY()){
                    return 0;
                }
            }
            else{
                resultingHeight = Math.min(resultingHeight, self.getLocalPositionY() - sibling.getLocalPositionY());
            }
        }
        return resultingHeight;
    }

    @Override
    public AbstractDimension cpy() {
        return new FillDimension();
    }
}
