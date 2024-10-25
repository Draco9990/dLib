package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.elements.UIElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HorizontalScrollBox extends ScrollBox {
    //region Variables

    private int scrollbarHeight = 49;

    private int currentWidthOffset = 0;

    private int lowestX = Integer.MAX_VALUE;
    private int highestX = Integer.MIN_VALUE;

    //endregion

    public HorizontalScrollBox(int x, int y, int width, int height) {
        super(x, y, width, height);

        initialize();
    }

    public HorizontalScrollBox(HorizontalScrollBoxData data) {
        super(data);

        scrollbarHeight = data.scrollbarHeight.getValue();
    }

    @Override
    protected Scrollbar buildScrollbar() {
        Scrollbar scrollbar = new HorizontalScrollbar(0, 0, getWidthUnscaled(), scrollbarHeight) {
            @Override
            public boolean isActive() {
                return isScrollbarVisible();
            }

            @Override
            public void onScrollbarScrolled(float percentage) {
                super.onScrollbarScrolled(percentage);

                currentWidthOffset = lowestX + (int) ((highestX - lowestX) * percentage);
            }
        };

        return scrollbar;
    }

    private boolean isScrollbarVisible() {
        return lowestX < 0 || highestX > getWidth();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();

        recalculateChildPositions();

        if(currentWidthOffset > highestX - getWidth()){
            currentWidthOffset = highestX - getWidth();
        }

        recalculateScrollBarPosition();
    }

    @Override
    protected void updateChildren() {
        Map<UIElement, Integer> originalPositions = new HashMap<>();

        for(UIElement child : getChildren()){
            child.setLocalPositionX(child.getLocalPosition().x + currentWidthOffset);
            originalPositions.put(child, child.getLocalPosition().x);
        }

        super.updateChildren();

        for(UIElement child : getChildren()){
            child.setLocalPositionX(originalPositions.get(child));
        }
    }

    @Override
    protected void renderChildren(SpriteBatch sb) {
        Map<UIElement, Integer> originalPositions = new HashMap<>();

        for(UIElement child : getChildren()){
            child.setLocalPositionX(child.getLocalPosition().x + currentWidthOffset);
            originalPositions.put(child, child.getLocalPosition().x);
        }

        super.renderChildren(sb);

        for(UIElement child : getChildren()){
            child.setLocalPositionX(originalPositions.get(child));
        }
    }

    private void recalculateChildPositions(){
        lowestX = Integer.MAX_VALUE;
        highestX = Integer.MIN_VALUE;

        for(UIElement child : getChildren()){
            if(child.getLocalPosition().x < lowestX){
                lowestX = child.getLocalPosition().x;
            }

            if(child.getLocalPosition().x + child.getWidth() > highestX){
                highestX = child.getLocalPosition().x + child.getWidth();
            }
        }
    }

    private void recalculateScrollBarPosition(){
        float totalWidth = highestX - lowestX;
        float percentage = (currentWidthOffset - lowestX) / totalWidth;


        scrollbar.getSlider().setLocalPositionX((int) ((scrollbar.getWidth() - scrollbar.getSlider().getWidth()) * percentage));
    }

    public static class HorizontalScrollBoxData extends ScrollBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public IntegerProperty scrollbarHeight = (IntegerProperty) new IntegerProperty(50).setMinimumValue(0).setName("Scrollbar Width");
    }
}
