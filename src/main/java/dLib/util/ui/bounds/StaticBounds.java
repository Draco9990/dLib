package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public class StaticBounds extends AbstractBounds{
    public Integer left;
    public Integer top;
    public Integer right;
    public Integer bottom;

    public StaticBounds(Integer left, Integer bottom, Integer right, Integer top) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean clip(AbstractBounds mask){
        if (this.left < mask.getWorldLeft()) {
            this.left = mask.getWorldLeft();
        }
        if (this.right > mask.getWorldRight()) {
            this.right = mask.getWorldRight();
        }
        if (this.top > mask.getWorldTop()) {
            this.top = mask.getWorldTop();
        }
        if (this.bottom < mask.getWorldBottom()) {
            this.bottom = mask.getWorldBottom();
        }
        return this.left < this.right && this.bottom < this.top;
    }

    @Override
    public Integer getWorldLeft(UIElement reference) {
        return left;
    }

    @Override
    public Integer getWorldBottom(UIElement reference) {
        return bottom;
    }

    @Override
    public Integer getWorldRight(UIElement reference) {
        return right;
    }

    @Override
    public Integer getWorldTop(UIElement reference) {
        return top;
    }
}
