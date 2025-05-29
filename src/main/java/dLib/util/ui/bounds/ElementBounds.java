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
    public Float getWorldLeft(UIElement reference) {
        if(getBoundElement() == null){
            return 0f;
        }

        return getBoundElement().getWorldPositionX();
    }
    @Override
    public Float getWorldBottom(UIElement reference) {
        if(getBoundElement() == null){
            return 0f;
        }

        return getBoundElement().getWorldPositionY();
    }
    @Override
    public Float getWorldRight(UIElement reference) {
        if(getBoundElement() == null){
            return 1920f;
        }

        return getBoundElement().getWorldPositionX() + getBoundElement().getWidth();
    }
    @Override
    public Float getWorldTop(UIElement reference) {
        if(getBoundElement() == null){
            return 1080f;
        }

        return getBoundElement().getWorldPositionY() + getBoundElement().getHeight();
    }
}
