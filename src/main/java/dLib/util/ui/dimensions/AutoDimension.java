package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;
import dLib.util.ui.bounds.StaticBounds;

public class AutoDimension extends AbstractDimension {
    public AutoDimension(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AutoDimension;
    }

    @Override
    public int getWidth(UIElement self) {
        StaticBounds childBounds = self.getFullChildLocalBounds();
        if(childBounds == null) return 1;
        return childBounds.right - childBounds.left;
    }

    @Override
    public int getHeight(UIElement self) {
        StaticBounds childBounds = self.getFullChildLocalBounds();
        if(childBounds == null) return 1;
        return childBounds.top - childBounds.bottom;
    }

    @Override
    public void resizeWidthBy(UIElement self, int amount) {

    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {

    }

    @Override
    public AbstractDimension cpy() {
        return new AutoDimension();
    }

    @Override
    public String toString() {
        return "auto";
    }
}
