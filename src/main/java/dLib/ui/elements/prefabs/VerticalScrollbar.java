package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;

public abstract class VerticalScrollbar extends Scrollbar {
    //region Variables

    //endregion

    //region Constructors

    public VerticalScrollbar(int x, int y, int width, int height){
        super(x, y, width, height);

        float mult = width / 49f;
        int topBottomHeight = (int) Math.min(22 * mult, (float) (height - 1) / 2);

        addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_vertical_top, 0, height - topBottomHeight, width, topBottomHeight));
        addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_vertical_middle, 0, topBottomHeight, width, height - topBottomHeight * 2));
        addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_vertical_bottom, 0, 0, width, topBottomHeight));

        addChildNCS(slider);
    }

    @Override
    protected Draggable buildSlider(int containerWidth, int containerHeight) {
        Draggable slider = new Draggable(UIThemeManager.getDefaultTheme().scrollbar_vertical_train, (int) (5 * 1.29f), 0, (int) (containerWidth / 1.29f), 60);
        slider.setCanDragX(false);
        slider.setBoundWithinParent(true);
        slider.addOnPositionChangedConsumer((diffX, diffY) -> {
            onScrollbarScrolled((float) slider.getLocalPositionY() / (getHeight() - slider.getHeight()));
        });
        return slider;
    }

    //endregion

    //region Methods

    @Override
    public void onScrollbarScrolled(float percentage) {
        super.onScrollbarScrolled(percentage);
    }

    public void reset(){
        slider.setLocalPositionY(getHeight() - slider.getHeight());
    }

    //endregion
}
