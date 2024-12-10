package dLib.util.ui.dimensions;

import dLib.ui.elements.UIElement;
import dLib.util.Bounds;

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
        try
        {
            Bounds childBounds = self.getChildBounds();
            return childBounds.top - childBounds.bottom;
        }catch (StackOverflowError e)
        {
            System.out.println("StackOverflowError");
        }

        return 0;
    }

    @Override
    public AbstractDimension cpy() {
        return new AutoDimension();
    }
}
