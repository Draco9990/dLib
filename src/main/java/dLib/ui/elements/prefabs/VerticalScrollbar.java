package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public abstract class VerticalScrollbar extends Scrollbar {
    //region Variables

    //endregion

    //region Constructors

    public VerticalScrollbar(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height){
        super(x, y, width, height);

        VerticalBox elements = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill(), true);
        {
            elements.addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_vertical_top, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.px(22)));
            elements.addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_vertical_middle, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.fill()));
            elements.addChildNCS(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_vertical_bottom, Pos.perc(0), Pos.perc(0), Dim.fill(), Dim.px(22)));
        }
        addChildNCS(elements);

        addChildNCS(slider);
    }

    @Override
    protected Draggable buildSlider() {
        Draggable slider = new Draggable(UIThemeManager.getDefaultTheme().scrollbar_vertical_train, Pos.px((int) (5 * 1.29f)), Pos.perc(0), Dim.perc(0.7762), Dim.px(60));
        slider.setCanDragX(false);
        slider.setBoundWithinParent(true);
        slider.addOnPositionChangedConsumer((element) -> {
            onScrollbarScrolled((float) slider.getLocalPositionY() / (getHeight() - slider.getHeight()));
        });
        return slider;
    }

    //endregion

    //region Methods

    @Override
    public void setScrollbarScrollPercentageForExternalChange(float percentage) {
        slider.setLocalPositionY((int) ((getHeight() - slider.getHeight()) * percentage));
    }

    public void reset(){
        slider.setLocalPositionY(getHeight() - slider.getHeight());
    }

    //endregion
}
