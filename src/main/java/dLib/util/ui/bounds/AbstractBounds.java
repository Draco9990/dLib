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

    public final Float getWorldLeft(){
        return getWorldLeft(null);
    }
    public final Float getWorldBottom(){
        return getWorldBottom(null);
    }
    public final Float getWorldRight(){
        return getWorldRight(null);
    }
    public final Float getWorldTop(){
        return getWorldTop(null);
    }

    public abstract Float getWorldLeft(UIElement reference);
    public abstract Float getWorldBottom(UIElement reference);
    public abstract Float getWorldRight(UIElement reference);
    public abstract Float getWorldTop(UIElement reference);
}
