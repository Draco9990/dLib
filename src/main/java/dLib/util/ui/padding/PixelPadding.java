package dLib.util.ui.padding;

import dLib.ui.elements.UIElement;

public class PixelPadding extends AbstractPadding {
    private int val;

    public PixelPadding(int val){
        this.val = val;
    }

    @Override
    public int getHorizontal(UIElement owner) {
        return val;
    }

    @Override
    public int getVertical(UIElement owner) {
        return val;
    }

    @Override
    public AbstractPadding cpy() {
        return new PixelPadding(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PixelPadding)) {
            return false;
        }

        PixelPadding other = (PixelPadding) obj;
        return other.val == val;
    }
}
