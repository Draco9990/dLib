package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.themes.UIThemeManager;

public abstract class HorizontalScrollbar extends Scrollbar {
    //region Variables

    private int widthPerState = 0;

    //endregion

    //region Constructors

    public HorizontalScrollbar(int x, int y, int width, int height){
        super(x, y, width, height);
    }

    @Override
    public void makeSlider() {
        slider = new Draggable(UIThemeManager.getDefaultTheme().scroll_button, 0, 0, getWidth(), getHeight()){
            @Override
            public void onPositionChanged(int diffX, int diffY) {
                super.onPositionChanged(diffX, diffY);

                setPageForSliderWidth(slider.getLocalPositionX());
            }
        }.setCanDragY(false);
        slider.setBoundWithinParent(true);
        addChildNCS(slider);
    }

    //endregion

    //region Methods

    //region Pages

    private void setPageForSliderWidth(int sliderHeight){
        int state = 0;
        while(sliderHeight > widthPerState * state){
            state++;
        }

        currentPage = getPageCount() - state;
        if(currentPage < 1) currentPage = 1;
        onPageChanged(currentPage);
    }

    public void nextPage(){
        if(currentPage < pageCount){
            slider.setLocalPositionX(slider.getLocalPositionX() + widthPerState);
        }
    }
    public void previousPage(){
        if(currentPage > 0){
            slider.setLocalPositionX(slider.getLocalPositionX() - widthPerState);
        }
    }
    public void setFirstPage(){
        slider.setLocalPositionX(0);
    }

    //endregion

    protected void recalculateScrollbar(){
        pageCount = getPageCount();
        if(pageCount == 0) pageCount = 1;
        widthPerState = (int)((float)getWidth() / pageCount);

        if(slider != null){
            slider.setWidth(widthPerState);
        }
    }

    //endregion
}
