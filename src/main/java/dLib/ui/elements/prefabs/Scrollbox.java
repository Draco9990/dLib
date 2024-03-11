package dLib.ui.elements.prefabs;

import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;

public abstract class Scrollbox extends Renderable {
    //region Variables

    private Draggable slider;

    private int currentPage = 1;

    private int pageCount = 0;
    private int heightPerState = 0;

    //endregion

    //region Constructors

    public Scrollbox(int x, int y, int width, int height){
        super(UIThemeManager.getDefaultTheme().inputfield, x, y, width, height);

        slider = new Draggable(UIThemeManager.getDefaultTheme().scroll_button, 0, height, width, 0){
            @Override
            public void onPositionChanged(int diffX, int diffY) {
                super.onPositionChanged(diffX, diffY);

                setPageForSliderHeight(slider.getLocalPositionY());
            }
        }.setCanDragX(false);
        slider.setBoundWithinParent(true);
        addChildNCS(slider);

        initialize();
    }

    public void initialize(){
        currentPage = 1;
        recalculateScrollbar();
    }

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

    private void setPageForSliderHeight(int sliderHeight){
        int state = 0;
        while(sliderHeight > heightPerState * state){
            state++;
        }

        currentPage = getPageCount() - state;
        if(currentPage < 1) currentPage = 1;
        onPageChanged(currentPage);
    }

    public int getCurrentPage(){
        return currentPage;
    }
    public abstract int getPageCount();

    public void nextPage(){
        if(currentPage < pageCount){
            slider.setPositionY(slider.getPositionY() - heightPerState);
        }
    }
    public void previousPage(){
        if(currentPage > 0){
            slider.setPositionY(slider.getPositionY() + heightPerState);
        }
    }
    public void setFirstPage(){
        slider.setPositionY(slider.getPositionY() + height - slider.getHeight());
    }

    public void onPageChanged(int newPage){}

    //endregion

    private void recalculateScrollbar(){
        pageCount = getPageCount();
        if(pageCount == 0) pageCount = 1;
        heightPerState = (int)((float)height / pageCount);

        if(slider != null){
            slider.setHeight(heightPerState);
        }
    }

    //endregion
}
