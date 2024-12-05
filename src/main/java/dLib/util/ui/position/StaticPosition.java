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
                return getLocalXForCenterFill(element);
            }
            else{
                return position + (parentWidth - element.getWidth()) / 2;
            }
        }
        else{ //element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

            if(element.getWidthRaw() instanceof FillDimension){
                return parentWidth;
            }
            else{
                return parentWidth - element.getWidth() + position;
            }
        }
    }

    private int getLocalXForCenterFill(UIElement element){
        int parentWidth = element.getParent().getWidth();
        int lowestX = parentWidth / 2;

        for(UIElement child : element.getParent().getChildren()){
            if(child == element){
                continue;
            }

            if ((child.getLocalPositionY() >= element.getLocalPositionY() || child.getHeightRaw() instanceof FillDimension || child.getLocalPositionY() + child.getHeight() < element.getLocalPositionY()) &&
                (child.getLocalPositionY() < element.getLocalPositionY() || element.getHeightRaw() instanceof FillDimension || child.getLocalPositionY() > element.getLocalPositionY() + element.getHeight())) {
                continue;
            }

            if (child.getLocalPositionX() >= element.getLocalPositionX()) {
                continue;
            }

            if(child.getLocalPositionX() + child.getWidth() >= element.getLocalPositionX()){
                return (int) (parentWidth / 2f);
            }
            else if(child.getLocalPositionX() < lowestX){
                lowestX = child.getLocalPositionX();
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
                return parentHeight / 2;
            }
            else{
                return position + (parentHeight - element.getHeight()) / 2;
            }
        }
        else{ //element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

            if(element.getHeightRaw() instanceof FillDimension){
                return parentHeight;
            }
            else{
                return parentHeight - element.getHeight() + position;
            }
        }
    }

    private int getLocalYForCenterFill(UIElement element){
        int parentHeight = element.getParent().getHeight();
        int lowestY = parentHeight / 2;

        for(UIElement child : element.getParent().getChildren()){
            if(child == element){
                continue;
            }

            if ((child.getLocalPositionX() >= element.getLocalPositionX() || child.getWidthRaw() instanceof FillDimension || child.getLocalPositionX() + child.getWidth() < element.getLocalPositionX()) &&
                (child.getLocalPositionX() < element.getLocalPositionX() || element.getWidthRaw() instanceof FillDimension || child.getLocalPositionX() > element.getLocalPositionX() + element.getWidth())) {
                continue;
            }

            if (child.getLocalPositionY() >= element.getLocalPositionY()) {
                continue;
            }

            if(child.getLocalPositionY() + child.getHeight() >= element.getLocalPositionY()){
                return (int) (parentHeight / 2f);
            }
            else if(child.getLocalPositionY() < lowestY){
                lowestY = child.getLocalPositionY();
            }
        }
        return lowestY;
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
