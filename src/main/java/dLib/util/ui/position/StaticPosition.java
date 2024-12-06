package dLib.util.ui.position;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIItemBoxElementHolderComponent;
import dLib.ui.elements.prefabs.HorizontalItemBox;
import dLib.ui.elements.prefabs.VerticalItemBox;
import dLib.util.ui.dimensions.FillDimension;

public class StaticPosition extends AbstractPosition {
    private int position;

    public StaticPosition(int position){
        this.position = position;
    }

    public int getVal(){
        return position;
    }

    @Override
    public int getLocalX(UIElement element) {
        if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || (element.getParent().hasComponent(UIItemBoxElementHolderComponent.class) && element.getParent().getComponent(UIItemBoxElementHolderComponent.class).isHorizontal())){
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

    @Override
    public int getLocalY(UIElement element) {
        if(element.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || (element.getParent().hasComponent(UIItemBoxElementHolderComponent.class) && element.getParent().getComponent(UIItemBoxElementHolderComponent.class).isVertical())){
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
                return getLocalYForTopOffsetFill(element, parentHeight - position);
            }
            else{
                return parentHeight - element.getHeight() + position;
            }
        }
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
