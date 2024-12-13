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
    public void resizeWidthBy(UIElement self, int amount) {

    }

    @Override
    public void resizeHeightBy(UIElement self, int amount) {

    }

    @Override
    public AbstractDimension cpy() {
        return new HeightMirrorDimension();
    }
}
