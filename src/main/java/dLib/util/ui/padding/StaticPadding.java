package dLib.util.ui.padding;

import dLib.ui.elements.UIElement;

public class StaticPadding extends AbstractPadding {
    private int val;

    public StaticPadding(int val){
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
        return new StaticPadding(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StaticPadding)) {
            return false;
        }

        StaticPadding other = (StaticPadding) obj;
        return other.val == val;
    }
}
