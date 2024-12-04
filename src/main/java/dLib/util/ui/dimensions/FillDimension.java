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
        if(self.getParent() == null) return 1920;

        int parentWidth = self.getParent().getWidth();
        int fillChildCount = 0;
        for(UIElement child : self.getParent().getChildren()){
            if(!(child.getWidthRaw() instanceof FillDimension)){
                parentWidth -= child.getWidth();
            }
            else{
                fillChildCount++;
            }
        }

        return (int) ((float) parentWidth / fillChildCount);
    }

    @Override
    public int getHeight(UIElement self) {
        if(self.getParent() == null) return 1080;

        int parentHeight = self.getParent().getHeight();
        int fillChildCount = 0;
        for(UIElement child : self.getParent().getChildren()){
            if(!(child.getHeightRaw() instanceof FillDimension)){
                parentHeight -= child.getHeight();
            }
            else{
                fillChildCount++;
            }
        }

        return (int) ((float) parentHeight / fillChildCount);
    }

    @Override
    public AbstractDimension cpy() {
        return new FillDimension();
    }
}
