package dLib.util.ui.position;

import dLib.ui.elements.UIElement;

public class StaticPosition extends AbstractPosition {
    private int position;

    public StaticPosition(int position){
        this.position = position;
    }

    @Override
    public int getLocalX(UIElement element) {
        return position;
    }

    @Override
    public int getLocalY(UIElement element) {
        return position;
    }

    @Override
    public AbstractPosition cpy() {
        return new StaticPosition(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StaticPosition)) {
            return false;
        }

        return ((StaticPosition)obj).position == position;
    }
}
