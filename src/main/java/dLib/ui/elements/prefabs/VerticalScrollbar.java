package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.themes.UIThemeManager;

public abstract class VerticalScrollbar extends Scrollbar {
    //region Variables

    private int heightPerState = 1;

    //endregion

    //region Constructors

    public VerticalScrollbar(int x, int y, int width, int height){
        super(x, y, width, height);
    }

    @Override
    public void makeSlider() {
        slider = new Draggable(UIThemeManager.getDefaultTheme().scroll_button, 0, 0, getWidth(), getHeight()){
            @Override
            public void onPositionChanged(int diffX, int diffY) {
                super.onPositionChanged(diffX, diffY);

                setPageForSliderHeight(slider.getLocalPositionY());
            }
        }.setCanDragX(false);
        slider.setBoundWithinParent(true);
        addChildNCS(slider);
    }

    //endregion

    //region Methods

    //region Pages

    private void setPageForSliderHeight(int sliderHeight){
        int state = 0;
        if(heightPerState == 0) heightPerState = 1;
        while(sliderHeight > heightPerState * state){
            state++;
        }

        currentPage = getPageCount() - state;
        if(currentPage < 1) currentPage = 1;
        onPageChanged(currentPage);
    }

    public void nextPage(){
        if(currentPage < pageCount){
            slider.setLocalPositionY(slider.getLocalPositionY() - heightPerState);
        }
    }
    public void previousPage(){
        if(currentPage > 0){
            slider.setLocalPositionY(slider.getLocalPositionY() + heightPerState);
        }
    }
    public void setFirstPage(){
        slider.setLocalPositionY(getHeight() - slider.getHeight());
    }

    //endregion

    protected void recalculateScrollbar(){
        pageCount = getPageCount();
        if(pageCount == 0) pageCount = 1;
        heightPerState = (int)((float)getHeight() / pageCount);

        if(slider != null){
            slider.setHeight(heightPerState);
        }
    }

    //endregion
}
