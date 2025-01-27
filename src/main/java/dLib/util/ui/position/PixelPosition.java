package dLib.util.ui.position;

import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PixelPositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.GridItemBox;
import dLib.ui.elements.items.itembox.HorizontalDataBox;
import dLib.ui.elements.items.itembox.ItemBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.util.IntegerVector2;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.FillDimension;

import java.io.Serializable;

public class PixelPosition extends AbstractStaticPosition implements Serializable {
    private static final long serialVersionUID = 1L;

    private int position;

    public PixelPosition(int position){
        this.position = position;
    }

    public int getValueRaw(){
        return position;
    }

    @Override
    public int getLocalX(UIElement self) {
        if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT ||
                (self.getParent() instanceof HorizontalDataBox) && ((ItemBox) self.getParent()).hasChild(self)){
            return position;
        }
        else if(self.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            int parentWidth = 1920;
            if(self.hasParent()){
                if(self.getParent().getWidthRaw() instanceof AutoDimension && self.getParent().getWidthCache() == null){
                    return 0;
                }
                else{
                    parentWidth = self.getParent().getWidthUnscaled();
                }
            }

            if(self.getWidthRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return position + (parentWidth - self.getWidthLocalScaled()) / 2;
            }
        }
        else{ //element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT
            int parentWidth = 1920;
            if(self.hasParent()){
                if(self.getParent().getWidthRaw() instanceof AutoDimension && self.getParent().getWidthCache() == null){
                    return 0;
                }
                else{
                    parentWidth = self.getParent().getWidthUnscaled();
                }
            }

            if(self.getWidthRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentWidth - self.getWidthLocalScaled() - position;
            }
        }
    }

    @Override
    public int getLocalY(UIElement self) {
        if(self.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM ||
                (self.getParent() instanceof VerticalDataBox || self.getParent() instanceof GridItemBox) && ((ItemBox) self.getParent()).hasChild(self)){
            return position;
        }
        else if(self.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            int parentHeight = 1080;
            if(self.hasParent()){
                if(self.getParent().getHeightRaw() instanceof AutoDimension && self.getParent().getHeightCache() == null){
                    return 0;
                }
                else{
                    parentHeight = self.getParent().getHeightUnscaled();
                }
            }

            if(self.getHeightRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return position + (parentHeight - self.getHeightLocalScaled()) / 2;
            }
        }
        else{ //element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP
            int parentHeight = 1080;
            if(self.hasParent()){
                if(self.getParent().getHeightRaw() instanceof AutoDimension && self.getParent().getHeightCache() == null){
                    return 0;
                }
                else{
                    parentHeight = self.getParent().getHeightUnscaled();
                }
            }

            if(self.getHeightRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentHeight - self.getHeightLocalScaled() - position;
            }
        }
    }

    @Override
    public void offsetHorizontal(UIElement element, int amount) {
        if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || element.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            position += amount;
        }
        else if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            position -= amount;
        }
    }

    @Override
    public void offsetVertical(UIElement element, int amount) {
        if(element.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || element.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            position += amount;
        }
        else if(element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            position -= amount;
        }
    }

    @Override
    public void setForBounds(UIElement element, PositionBounds bounds) {
        IntegerVector2 bl = element.worldToLocal(new IntegerVector2(bounds.left, bounds.bottom));
        IntegerVector2 tr = element.worldToLocal(new IntegerVector2(bounds.right, bounds.top));

    }

    @Override
    public void setValueFromString(String value) {
        position = Integer.parseInt(value);
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PixelPositionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PixelPositionValueEditor((PositionProperty) property);
    }

    @Override
    public AbstractPosition cpy() {
        return new PixelPosition(position);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PixelPosition)) {
            return false;
        }

        return ((PixelPosition)obj).position == position;
    }

    @Override
    public String toString() {
        return "Px[" + position + "]";
    }

    @Override
    public String getDisplayValue() {
        return "px";
    }
}
