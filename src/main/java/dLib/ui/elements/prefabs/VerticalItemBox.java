package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.*;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UIThemeManager;
import org.lwjgl.input.Mouse;

import java.io.Serializable;

public abstract class VerticalItemBox<ItemType> extends ItemBox<ItemType> {
    //region Variables

    private int scrollbarWidth = 49;

    //endregion

    //region Constructors

    public VerticalItemBox(int xPos, int yPos, int width, int height) {
        this(xPos, yPos, width, height, false);
    }

    public VerticalItemBox(int xPos, int yPos, int width, int height, boolean noInitScrollbar) {
        super(xPos, yPos, width, height, noInitScrollbar);
    }

    public VerticalItemBox(VerticalItemBoxData data) {
        super(data);

        scrollbarWidth = data.scrollbarWidth.getValue();
    }

    //endregion

    //region Methods

    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(trackScrollWheelScroll && scrollbar != null && scrollbar.getSlider().isVisible()){
            int scrollDelta = (int)(Math.signum((float) Mouse.getDWheel()));
            scrollbar.getSlider().setLocalPositionY(scrollbar.getSlider().getLocalPositionY() + scrollDelta * 10);
        }
    }

    @Override
    protected UIElement buildItemBox() {
        Color bgColor = Color.BLACK.cpy();
        bgColor.a = 0.4f;
        Hoverable itemBoxBackground = new Hoverable(UIThemeManager.getDefaultTheme().listbox, 0, 0, getWidthUnscaled() - (noInitScrollbar ? 0 : scrollbarWidth), getHeightUnscaled() - (titleBox != null ? titleBox.getHeight() : 0)){
            @Override
            protected void onHovered() {
                super.onHovered();
                trackScrollWheelScroll = true;
            }

            @Override
            protected void onUnhovered() {
                super.onUnhovered();
                trackScrollWheelScroll = false;
            }
        };
        itemBoxBackground.setRenderColor(bgColor);
        return itemBoxBackground;
    }

    @Override
    public void onItemsChanged() {
        super.onItemsChanged();

        if (scrollbar != null) {
            if(getTotalItemHeight() > itemBox.getHeight()){
                scrollbar.getSlider().showInstantly();
            }
            else{
                scrollbar.getSlider().hideInstantly();
            }
        }
    }

    @Override
    protected Scrollbar buildScrollBar() {
        Scrollbar scrollbar = new VerticalScrollbar(getWidthUnscaled() - scrollbarWidth, 0, scrollbarWidth, getHeightUnscaled() - (titleBox != null ? titleBox.getHeight() : 0)) {
            @Override
            public void onScrollbarScrolled(float percentage) {
                super.onScrollbarScrolled(percentage);

                currentScrollbarOffset = recalculateScrollOffset(1 - percentage);
            }
        };

        return scrollbar;
    }

    @Override
    public Hoverable getBackground() {
        return (Hoverable) super.getBackground();
    }

    @Override
    public void refilterItems() {
        super.refilterItems();

        if(scrollbar != null){
            if(getTotalItemHeight() <= itemBox.getHeight()){
                scrollbar.reset();
            }
        }
    }

    protected abstract int getTotalItemHeight();

    //endregion

    public static class VerticalItemBoxData extends ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public IntegerProperty scrollbarWidth = (IntegerProperty) new IntegerProperty(50).setMinimumValue(0).setName("Scrollbar Width");
    }
}
