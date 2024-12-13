package dLib.util.ui.position;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.ItemBox;
import dLib.util.ui.dimensions.FillDimension;

public abstract class AbstractPosition {
    public AbstractPosition(){

    }

    public abstract int getLocalX(UIElement element);
    public abstract int getLocalY(UIElement element);

    protected int getLocalXForRightOffsetFill(UIElement element, int pivotPoint){
        int lowestX = 0;
        for(UIElement sibling : element.getParent().getChildren()){
            if(sibling == element){
                continue;
            }

            if ((sibling.getLocalPositionY() >= element.getLocalPositionY() || sibling.getHeightRaw() instanceof FillDimension || sibling.getLocalPositionY() + sibling.getHeight() < element.getLocalPositionY()) &&
                    (sibling.getLocalPositionY() < element.getLocalPositionY() || element.getHeightRaw() instanceof FillDimension || sibling.getLocalPositionY() > element.getLocalPositionY() + element.getHeight())) {
                continue;
            }

            if (sibling.getLocalPositionX() >= pivotPoint) {
                continue;
            }

            if(sibling.getWidthRaw() instanceof FillDimension){
                lowestX = sibling.getLocalPositionX() + 1;
                continue;
            }

            if(sibling.getLocalPositionX() + sibling.getWidth() >= pivotPoint){
                return pivotPoint;
            }
            else if(sibling.getLocalPositionX() < lowestX){
                lowestX = sibling.getLocalPositionX();
            }
        }
        return lowestX;
    }
    protected int getLocalYForTopOffsetFill(UIElement element, int pivotPoint){
        //if this continues to cause issues insta return if parent is a itembox, and just fill all space if not. who cares.

        int yResult = 0;
        for(UIElement sibling : element.getParent().getChildren()){
            if(sibling == element){
                continue;
            }

            if ((sibling.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() >= element.getLocalPositionX() || sibling.getLocalPositionX() + sibling.getWidth() < element.getLocalPositionX()) &&
                    (element.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() < element.getLocalPositionX() || sibling.getLocalPositionX() > element.getLocalPositionX() + element.getWidth())) {
                continue;
            }

            if (sibling.getLocalPositionY() >= pivotPoint) {
                continue;
            }

            if(sibling.getHeightRaw() instanceof FillDimension){
                yResult = sibling.getLocalPositionY() + 1;
                continue;
            }

            if(sibling.getLocalPositionY() + sibling.getHeight() >= pivotPoint){
                return pivotPoint;
            }
            else if(sibling.getLocalPositionY() > yResult){
                yResult = sibling.getLocalPositionY();
            }
        }
        return yResult;
    }

    public abstract AbstractPosition cpy();

    public abstract String getSimpleDisplayName();

    public abstract void offsetHorizontal(UIElement element, int amount);
    public abstract void offsetVertical(UIElement element, int amount);
}
