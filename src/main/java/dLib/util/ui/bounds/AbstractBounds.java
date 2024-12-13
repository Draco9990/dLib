package dLib.util.ui.bounds;

import dLib.ui.elements.UIElement;

public abstract class AbstractBounds {
    public boolean overlaps(AbstractBounds other){
        if (this.getWorldRight() < other.getWorldLeft() || this.getWorldLeft() > other.getWorldRight()) {
            return false;
        }

        if (this.getWorldTop() < other.getWorldBottom() || this.getWorldBottom() > other.getWorldTop()) {
            return false;
        }

        return true;
    }

    public boolean within(AbstractBounds other){
        return this.getWorldLeft() >= other.getWorldLeft() && this.getWorldRight() <= other.getWorldRight() && this.getWorldTop() <= other.getWorldTop() && this.getWorldBottom() >= other.getWorldBottom();
    }

    public final Integer getWorldLeft(){
        return getWorldLeft(null);
    }
    public final Integer getWorldBottom(){
        return getWorldBottom(null);
    }
    public final Integer getWorldRight(){
        return getWorldRight(null);
    }
    public final Integer getWorldTop(){
        return getWorldTop(null);
    }

    public abstract Integer getWorldLeft(UIElement reference);
    public abstract Integer getWorldBottom(UIElement reference);
    public abstract Integer getWorldRight(UIElement reference);
    public abstract Integer getWorldTop(UIElement reference);
}
