package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

public class WidthMirrorDimension extends AbstractDimension{
    @Override
    public int getWidth(UIElement self) {
        throw new UnsupportedOperationException("Width mirror dimension does not support getWidth");
    }

    @Override
    public int getHeight(UIElement self) {
        return self.getWidth();
    }

    @Override
    public AbstractDimension cpy() {
        return new WidthMirrorDimension();
    }
}
