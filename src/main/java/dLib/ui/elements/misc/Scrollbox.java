package dLib.ui.elements.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;

public abstract class Scrollbox extends Renderable {
    /** Class Variables */
    private Draggable slider;

    private int currentPage = 0;

    private int pageCount = 0;
    private int heightPerState = 0;

    /** Constructors */
    public Scrollbox(int x, int y, int width, int height){
        super(UIThemeManager.getDefaultTheme().inputfield, x, y, width, height);

        slider = new Draggable(UIThemeManager.getDefaultTheme().button_small, x, y + height - heightPerState, width, heightPerState){
            @Override
            public void onPositionChanged(int newXPos, int newYPos) {
                super.onPositionChanged(newXPos, newYPos);

                setPageForSliderHeight(newYPos);
            }
        }.setCanDragX(false);

        currentPage = 0;
        recalculateScrollbar();
    }

    /** Update and render */
    @Override
    public void update() {
        super.update();

        recalculateScrollbar();
        slider.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        slider.render(sb);
    }

    /** Helper methods */
    private void recalculateScrollbar(){
        pageCount = getPageCount();
        if(pageCount == 0) pageCount = 1;
        heightPerState = (int)((float)height / pageCount);

        if(slider != null){
            slider.setHeight(heightPerState);
            slider.setBoundsY(y, y + height - heightPerState);
        }
    }

    /** Pages */
    public abstract int getPageCount();

    public int getCurrentPage(){
        return currentPage;
    }

    private void setPageForSliderHeight(int sliderHeight){
        int state = 0;
        while(sliderHeight > y + heightPerState * state){
            state++;
        }

        currentPage = getPageCount() - state;
        onPageChanged(currentPage);
    }

    public void onPageChanged(int newPage){}
}
