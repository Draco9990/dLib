package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;

public abstract class Scrollbar extends Renderable {
    //region Variables

    protected Draggable slider;

    protected int currentPage = 1;

    protected int pageCount = 0;

    //endregion

    //region Constructors

    public Scrollbar(int x, int y, int width, int height){
        super(UIThemeManager.getDefaultTheme().inputfield, x, y, width, height);

        makeSlider();
        currentPage = 1;
        recalculateScrollbar();
    }

    public abstract void makeSlider();

    //endregion

    //region Methods

    //region Update & Render
    @Override
    public void updateSelf() {
        super.updateSelf();
        recalculateScrollbar();
    }
    //endregion

    //region Slider

    public Draggable getSlider(){
        return slider;
    }

    //endregion

    //region Pages

    public int getCurrentPage(){
        return currentPage;
    }
    public abstract int getPageCount();

    public abstract void nextPage();
    public abstract void previousPage();
    public abstract void setFirstPage();

    public void onPageChanged(int newPage){}

    //endregion

    protected abstract void recalculateScrollbar();

    //endregion
}
