package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public class ElementBounds extends AbstractBounds{
    private UIElement element;

    public ElementBounds(UIElement element) {
        this.element = element;
    }

    protected UIElement getBoundElement(){
        return element;
    }

    @Override
    public Integer getWorldLeft(UIElement reference) {
        if(getBoundElement() == null){
            return 0;
        }

        return getBoundElement().getWorldPositionX();
    }
    @Override
    public Integer getWorldBottom(UIElement reference) {
        if(getBoundElement() == null){
            return 0;
        }

        return getBoundElement().getWorldPositionY();
    }
    @Override
    public Integer getWorldRight(UIElement reference) {
        if(getBoundElement() == null){
            return 1920;
        }

        return getBoundElement().getWorldPositionX() + getBoundElement().getWidth();
    }
    @Override
    public Integer getWorldTop(UIElement reference) {
        if(getBoundElement() == null){
            return 1080;
        }

        return getBoundElement().getWorldPositionY() + getBoundElement().getHeight();
    }
}
