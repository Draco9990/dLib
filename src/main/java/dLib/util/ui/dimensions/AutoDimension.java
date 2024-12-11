package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;
import dLib.util.Bounds;

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
        Bounds childBounds = self.getFullChildLocalBounds();
        if(childBounds == null) return 1;
        return childBounds.right - childBounds.left;
    }

    @Override
    public int getHeight(UIElement self) {
        Bounds childBounds = self.getFullChildLocalBounds();
        if(childBounds == null) return 1;
        return childBounds.top - childBounds.bottom;
    }

    @Override
    public AbstractDimension cpy() {
        return new AutoDimension();
    }
}
