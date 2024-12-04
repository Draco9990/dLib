package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

public class FillDimension extends AbstractDimension {
    public FillDimension(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FillDimension;
    }

    @Override
    public int getWidth(UIElement self) {
        return self.getParent() != null ? self.getParent().getWidth() : 1920;
    }

    @Override
    public int getHeight(UIElement self) {
        return self.getParent() != null ? self.getParent().getHeight() : 1080;
    }

    @Override
    public AbstractDimension cpy() {
        return new FillDimension();
    }
}
