package dLib.util.ui.position;

import dLib.properties.objects.PositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PercentagePositionValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.text.TextBox;
import dLib.util.ui.dimensions.FillDimension;

public class PercentagePosition extends AbstractPosition {
    private float percentage;

    public PercentagePosition(float percentage){
        if(percentage < 0) percentage = 0;
        if(percentage > 1) percentage = 1;

        this.percentage = percentage;
    }

    public float getValueRaw(){
        return percentage;
    }

    @Override
    public int getLocalX(UIElement element) {
        if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || element instanceof TextBox){
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;
            return (int)(parentWidth * percentage);
        }
        else if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

            if(element.getWidthRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentWidth / 2 - (int) (element.getWidth() * percentage);
            }
        }
        else{ //element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT
            int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

            if(element.getWidthRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentWidth - (int) ((parentWidth - element.getWidth()) * percentage);
            }
        }
    }

    @Override
    public int getLocalY(UIElement element) {
        if(element.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || element instanceof TextBox){
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;
            return (int)(parentHeight * percentage);
        }
        else if(element.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

            if(element.getHeightRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentHeight / 2 - (int) (element.getHeight() * percentage);
            }
        }
        else{ //element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP
            int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

            if(element.getHeightRaw() instanceof FillDimension){
                return 0;
            }
            else{
                return parentHeight - (int) ((parentHeight - element.getHeight()) * percentage);
            }
        }
    }

    @Override
    public void offsetHorizontal(UIElement element, int amount) {
        int parentWidth = element.getParent() != null ? element.getParent().getWidth() : 1920;

        if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT || element.getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
            percentage += (float)amount / parentWidth;
        }
        else if(element.getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
            percentage -= (float)amount / parentWidth;
        }
    }

    @Override
    public void offsetVertical(UIElement element, int amount) {
        int parentHeight = element.getParent() != null ? element.getParent().getHeight() : 1080;

        if(element.getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM || element.getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
            percentage += (float)amount / parentHeight;
        }
        else if(element.getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
            percentage -= (float)amount / parentHeight;
        }
    }

    @Override
    public void setValueFromString(String value) {
        percentage = Float.parseFloat(value);
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PercentagePositionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PercentagePositionValueEditor((PositionProperty) property);
    }

    @Override
    public AbstractPosition cpy() {
        return new PercentagePosition(percentage);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentagePosition)) {
            return false;
        }

        return ((PercentagePosition)obj).percentage == percentage;
    }

    @Override
    public String toString() {
        return "%[" + percentage + "]";
    }

    @Override
    public String getDisplayValue() {
        return "%";
    }
}
