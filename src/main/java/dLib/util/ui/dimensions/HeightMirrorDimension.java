package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

public class HeightMirrorDimension extends AbstractDimension{
    @Override
    public int getWidth(UIElement self) {
        return self.getHeight();
    }

    @Override
    public int getHeight(UIElement self) {
        throw new UnsupportedOperationException("Height mirror dimension does not support getHeight");
    }

    @Override
    public AbstractDimension cpy() {
        return new HeightMirrorDimension();
    }
}
