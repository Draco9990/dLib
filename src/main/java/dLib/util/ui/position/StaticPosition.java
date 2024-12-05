package dLib.util.ui.position;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalItemBox;
import dLib.ui.elements.prefabs.VerticalItemBox;
import dLib.util.ui.dimensions.FillDimension;

public class StaticPosition extends AbstractPosition {
    private int position;

    public StaticPosition(int position){
        this.position = position;
    }

    @Override
    public int getLocalX(UIElement element) {
        if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || element.getParent().getParent() instanceof HorizontalItemBox){
            return position;
        }
        else if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

            if(element.getWidthRaw() instanceof FillDimension){
                return getLocalXForRightOffsetFill(element, (int) (parentWidth / 2f));
            }
            else{
                return position + (parentWidth - element.getWidth()) / 2;
            }
        }
        else{ //element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

            if(element.getWidthRaw() instanceof FillDimension){
                return getLocalXForRightOffsetFill(element, parentWidth);
            }
            else{
                return parentWidth - element.getWidth() + position;
            }
        }
    }

    private int getLocalXForRightOffsetFill(UIElement element, int pivotPoint){
        //One day this will cause issues in non vertical boxes not expanding correctly
        //For future Marino, this is the issue:
        //Sibling elements are not being checked for their allignments, especially when checking if they are FillDimensions.
        //That means that elements potentially expanding in the opposite direction will still be flagged as intruding on our space and requireing sharing
        //I didnt fix it now cause im lazy and its alot of edge cases.

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

    @Override
    public int getLocalY(UIElement element) {
        if(element.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || element.getParent().getParent() instanceof VerticalItemBox){
            return position;
        }
        else if(element.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

            if(element.getHeightRaw() instanceof FillDimension){
                return getLocalYForTopOffsetFill(element, (int) (parentHeight / 2f));
            }
            else{
                return position + (parentHeight - element.getHeight()) / 2;
            }
        }
        else{ //element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

            if(element.getHeightRaw() instanceof FillDimension){
                return getLocalYForTopOffsetFill(element, parentHeight);
            }
            else{
                return parentHeight - element.getHeight() + position;
            }
        }
    }

    private int getLocalYForTopOffsetFill(UIElement element, int pivotPoint){
        //if this continues to cause issues insta return if parent is a itembox, and just fill all space if not. who cares.

        int yResult = 0;
        for(UIElement sibling : element.getParent().getChildren()){
            if(sibling == element){
                continue;
            }

            if ((sibling.getLocalPositionX() >= element.getLocalPositionX() || sibling.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() + sibling.getWidth() < element.getLocalPositionX()) &&
                (sibling.getLocalPositionX() < element.getLocalPositionX() || element.getWidthRaw() instanceof FillDimension || sibling.getLocalPositionX() > element.getLocalPositionX() + element.getWidth())) {
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

    @Override
    public AbstractPosition cpy() {
        return new StaticPosition(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StaticPosition)) {
            return false;
        }

        return ((StaticPosition)obj).position == position;
    }
}
