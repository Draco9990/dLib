package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;

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
        int leftmostX = Integer.MAX_VALUE;
        int rightmostX = Integer.MIN_VALUE;

        for(UIElement child : self.getChildren()){
            int childX = child.getLocalPositionX();
            if(childX < leftmostX){
                leftmostX = childX;
            }
            if(childX + child.getWidth() > rightmostX){
                rightmostX = childX + child.getWidth();
            }
        }

        return rightmostX - leftmostX;
    }

    @Override
    public int getHeight(UIElement self) {
        int topmostY = Integer.MAX_VALUE;
        int bottommostY = Integer.MIN_VALUE;

        for(UIElement child : self.getChildren()){
            int childY = child.getLocalPositionY();
            if(childY < topmostY){
                topmostY = childY;
            }
            if(childY + child.getHeight() > bottommostY){
                bottommostY = childY + child.getHeight();
            }
        }

        return topmostY - bottommostY;
    }

    @Override
    public AbstractDimension cpy() {
        return new AutoDimension();
    }
}
