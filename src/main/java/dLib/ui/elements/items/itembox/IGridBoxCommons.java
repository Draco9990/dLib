package dLib.ui.elements.items.itembox;

import basemod.Pair;
import dLib.ui.elements.UIElement;
import dLib.ui.layout.ILayoutProvider;

public interface IGridBoxCommons extends ILayoutProvider {
    @Override
    default boolean providesWidth(){
        return false;
    }
    @Override
    default Pair<Integer, Integer> calculateContentWidth(){
        return new Pair<>(0, 0);
    }

    @Override
    default boolean providesHeight(){
        return true;
    }
    @Override
    default boolean canCalculateContentHeight() {
        return !((ItemBox)this).needsWidthCalculation();
    }
    @Override
    default Pair<Integer, Integer> calculateContentHeight(){
        ItemBox itemBox = (ItemBox) this;

        int height = 0;
        int currentXPos = 0;

        for(UIElement child : itemBox.filteredChildren){
            if(itemBox.filteredChildren.get(0) == child){
                height += child.getPaddingTop();
                height += child.getHeight();
                height += itemBox.itemSpacing;
                height += child.getPaddingBottom();
            }

            if(!child.isActive()){
                continue;
            }

            if(currentXPos + child.getWidth() + itemBox.itemSpacing > itemBox.getWidth()){
                currentXPos = 0;

                height += child.getPaddingTop();
                height += child.getHeight();
                height += itemBox.itemSpacing;
                height += child.getPaddingBottom();
            }

            currentXPos += child.getWidth() + itemBox.itemSpacing + child.getPaddingRight();
        }

        return new Pair<>(itemBox.getLocalPositionY(), itemBox.getLocalPositionY() + height);
    }
}
