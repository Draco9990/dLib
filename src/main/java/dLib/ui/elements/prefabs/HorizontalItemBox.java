package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.IntegerProperty;
import dLib.properties.objects.templates.TIntegerProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import org.lwjgl.input.Mouse;

import java.io.Serializable;

public abstract class HorizontalItemBox<ItemType> extends ItemBox<ItemType> {
    //region Variables

    private int scrollbarHeight = 49;

    //endregion

    //region Constructors

    public HorizontalItemBox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public HorizontalItemBox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public HorizontalItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        this(xPos, yPos, width, height, false);
    }
    public HorizontalItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height, boolean noInitScrollbar) {
        super(xPos, yPos, width, height, noInitScrollbar);
    }

    public HorizontalItemBox(HorizontalItemBoxData data) {
        super(data);

        scrollbarHeight = data.scrollbarHeight.getValue();
    }

    //endregion

    //region Methods


    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(trackScrollWheelScroll && scrollbar != null && scrollbar.getSlider().isVisible()){
            int scrollDelta = (int)(Math.signum((float) Mouse.getDWheel()));
            scrollbar.getSlider().setLocalPositionY(scrollbar.getSlider().getLocalPositionX() + scrollDelta * 10);
        }
    }

    @Override
    protected UIElement buildItemBox() {
        Color bgColor = Color.BLACK.cpy();
        bgColor.a = 0.4f;
        Hoverable itemBoxBackground = new Hoverable(UIThemeManager.getDefaultTheme().listbox, Pos.px(0), Pos.px(49), Dim.fill(), Dim.fill()){
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
            if(getTotalItemWidth() > itemBox.getWidth()){
                scrollbar.getSlider().showInstantly();
            }
            else{
                scrollbar.getSlider().hideInstantly();
            }
        }
    }

    @Override
    protected Scrollbar buildScrollBar() {
        Scrollbar scrollbar = new HorizontalScrollbar(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(scrollbarHeight)) {
            @Override
            public void onScrollbarScrolled(float percentage) {
                super.onScrollbarScrolled(percentage);

                currentScrollbarOffset = recalculateScrollOffset(percentage);
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
            if(getTotalItemWidth() <= itemBox.getWidth()){
                scrollbar.reset();
            }
        }
    }

    protected abstract int getTotalItemWidth();

    //endregion

    public static class HorizontalItemBoxData extends ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public IntegerProperty scrollbarHeight = new IntegerProperty(50).setMinimumValue(0).setName("Scrollbar Height");
    }
}
