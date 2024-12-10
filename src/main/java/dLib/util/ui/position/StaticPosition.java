package dLib.util.ui.position;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalListBox;
import dLib.ui.elements.prefabs.ItemBox;
import dLib.ui.elements.prefabs.VerticalGridBox;
import dLib.ui.elements.prefabs.VerticalListBox;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.FillDimension;

public class StaticPosition extends AbstractPosition {
    private int position;

    public StaticPosition(int position){
        this.position = position;
    }

    public int getVal(){
        return position;
    }

    @Override
    public int getLocalX(UIElement self) {
        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT ||
                (self.getParent() instanceof HorizontalListBox) && ((ItemBox) self.getParent()).containsRenderItem(self)){
            return position;
        }
        else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            int parentWidth = self.getParent() != null ? self.getParent().getWidth() : 1920;

            if(self.getWidthRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return position + (parentWidth - self.getWidth()) / 2;
            }
        }
        else{ //element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT
            int parentWidth = self.getParent() != null ? self.getParent().getWidth() : 1920;

            if(self.getWidthRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentWidth - self.getWidth() + position;
            }
        }
    }

    @Override
    public int getLocalY(UIElement self) {
        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM ||
                (self.getParent() instanceof VerticalListBox || self.getParent() instanceof VerticalGridBox) && ((ItemBox) self.getParent()).containsRenderItem(self)){
            return position;
        }
        else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            int parentHeight = self.getParent() != null ? self.getParent().getHeight() : 1080;

            if(self.getHeightRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return position + (parentHeight - self.getHeight()) / 2;
            }
        }
        else{ //element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP
            int parentHeight = self.getParent() != null ? self.getParent().getHeight() : 1080;

            if(self.getHeightRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentHeight - self.getHeight() + position;
            }
        }
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
