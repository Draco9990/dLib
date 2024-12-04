package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

public class StaticDimension extends AbstractDimension {
    private int size;

    public StaticDimension(int size){
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StaticDimension)) {
            return false;
        }

        StaticDimension other = (StaticDimension) obj;
        return other.size == size;
    }

    @Override
    public int getWidth(UIElement self) {
        return size;
    }

    @Override
    public int getHeight(UIElement self) {
        return size;
    }

    @Override
    public AbstractDimension cpy() {
        return new StaticDimension(size);
    }
}
