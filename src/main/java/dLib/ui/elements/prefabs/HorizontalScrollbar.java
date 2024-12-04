package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public abstract class HorizontalScrollbar extends Scrollbar {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalScrollbar(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height){
        super(x, y, width, height);

        HorizontalBox elements = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill(), true);
        {
            elements.addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_left, Pos.perc(0), Pos.perc(0), Dim.px(22), Dim.fill()));
            elements.addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_middle, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill()));
            elements.addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_right, Pos.perc(0), Pos.perc(0), Dim.px(22), Dim.fill()));
        }
        addChildNCS(elements);

        addChildNCS(slider);
    }

    @Override
    protected Draggable buildSlider() {
        Draggable slider = new Draggable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_train, Pos.perc(0), Pos.px((int) (5 * 1.29f)), Dim.px(60), Dim.perc(0.7762));
        slider.setCanDragY(false);
        slider.setBoundWithinParent(true);
        slider.addOnPositionChangedConsumer((element) -> {
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
