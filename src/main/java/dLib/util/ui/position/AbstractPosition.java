package dLib.util.ui.position;

import dLib.ui.elements.UIElement;

public abstract class AbstractPosition {
    public AbstractPosition(){

    }

    public abstract int getLocalX(UIElement element);
    public abstract int getLocalY(UIElement element);

    public int getWorldX(UIElement element){
        int parentWorldX = element.getParent() != null ? element.getParent().getWorldPositionX() : 0;
        return parentWorldX + getLocalX(element);
    }
    public int getWorldY(UIElement element){
        int parentWorldY = element.getParent() != null ? element.getParent().getWorldPositionY() : 0;
        return parentWorldY + getLocalY(element);
    }

    public abstract AbstractPosition cpy();
}
