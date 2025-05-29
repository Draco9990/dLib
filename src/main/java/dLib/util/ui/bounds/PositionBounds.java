package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public class PositionBounds extends AbstractBounds{
    public float left;
    public float top;
    public float right;
    public float bottom;

    public PositionBounds(float left, float bottom, float right, float top) {
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
    public Float getWorldLeft(UIElement reference) {
        return left;
    }

    @Override
    public Float getWorldBottom(UIElement reference) {
        return bottom;
    }

    @Override
    public Float getWorldRight(UIElement reference) {
        return right;
    }

    @Override
    public Float getWorldTop(UIElement reference) {
        return top;
    }
}
