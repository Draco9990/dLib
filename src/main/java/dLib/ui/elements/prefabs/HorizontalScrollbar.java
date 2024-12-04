package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;

public abstract class HorizontalScrollbar extends Scrollbar {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalScrollbar(int x, int y, int width, int height){
        super(x, y, width, height);

        float mult = height / 49f;
        int leftRightWidth = (int) Math.min(22 * mult, (float) (width - 1) / 2);

        addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_left, 0, 0, leftRightWidth, height));
        addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_middle, leftRightWidth, 0, width - leftRightWidth * 2, height));
        addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_right, width - leftRightWidth, 0, leftRightWidth, height));

        addChildNCS(slider);
    }

    @Override
    protected Draggable buildSlider(int containerWidth, int containerHeight) {
        Draggable slider = new Draggable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_train, 0, (int) (5 * 1.29f), 60, (int) (containerHeight / 1.29f));
        slider.setCanDragY(false);
        slider.setBoundWithinParent(true);
        slider.addOnPositionChangedConsumer((diffX, diffY) -> {
            onScrollbarScrolled((float) slider.getLocalPositionX() / (getWidth() - slider.getWidth()));
        });
        return slider;
    }

    //endregion

    //region Methods

    @Override
    public void setScrollbarScrollPercentageForExternalChange(float percentage) {
        slider.setLocalPositionX((int) ((getWidth() - slider.getWidth()) * percentage));
    }

    public void reset(){
        slider.setLocalPositionX(0);
    }

    //endregion
}
