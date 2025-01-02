package dLib.ui.elements.items.scroll;

import basemod.Pair;
import com.badlogic.gdx.math.MathUtils;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.items.Button;
import dLib.ui.elements.items.Interactable;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.resources.UICommonResources;

import dLib.util.bindings.texture.Tex;
import dLib.util.ui.bounds.AbstractBounds;
import dLib.util.ui.bounds.Bound;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class VerticalScrollbar extends Scrollbar {
    //region Variables

    private Integer scrollbarTargetY = null;

    //endregion

    //region Constructors

    public VerticalScrollbar(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height){
        super(x, y, width, height);

        VerticalBox elements = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        {
            elements.addItem(new Renderable(Tex.stat(UICommonResources.scrollbar_vertical_top), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(22)));
            elements.addItem(new Renderable(Tex.stat(UICommonResources.scrollbar_vertical_mid), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
            elements.addItem(new Renderable(Tex.stat(UICommonResources.scrollbar_vertical_bottom), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(22)));
        }
        addChild(elements);

        addChild(slider);

        setScrollbarScrollPercentageForExternalChange(100);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(boundElement != null){
            if(boundElement.hasVerticalChildrenOOB()){
                slider.showAndEnableInstantly();

                if(scrollbarTargetY != null){
                    if(Math.abs(scrollbarTargetY - slider.getLocalPositionY()) > 0.5f){
                        slider.setLocalPositionY((int) MathUtils.lerp(slider.getLocalPositionY(), scrollbarTargetY, scrollSpeed));
                    }
                    else{
                        slider.setLocalPositionY(scrollbarTargetY);
                        scrollbarTargetY = null;
                    }
                }
            }
            else{
                onScrollbarScrolled(0);
                slider.hideAndDisableInstantly();
                scrollbarTargetY = null;
            }
        }
        else{
            scrollbarTargetY = null;
        }
    }

    @Override
    protected Interactable buildSlider() {
        Button slider = new Button(Pos.px((int) (5 * 1.29f)), Pos.px(0), Dim.perc(0.7762), Dim.px(60));
        slider.setImage(Tex.stat(UICommonResources.scrollbar_vertical_train));
        slider.setContainerBounds(Bound.parent(slider));
        slider.onPositionChangedEvent.subscribeManaged((element) -> {
            onScrollbarScrolled((float) slider.getLocalPositionY() / (getHeight() - slider.getHeight()));
        });

        UIDraggableComponent dragComp = slider.addComponent(new UIDraggableComponent());
        dragComp.setCanDragX(false);
        return slider;
    }

    //endregion

    //region Methods

    @Override
    public void onScrollbarScrolled(float percentage) {
        if(boundElement != null){
            Pair<Integer, Integer> oobAmounts = boundElement.getVerticalChildrenOOBAmount();

            int scrollableArea = oobAmounts.getKey() + oobAmounts.getValue();
            int offset = (int) (scrollableArea - (scrollableArea * percentage)) - oobAmounts.getValue();

            boundElement.setLocalChildOffsetY(offset);
        }

        super.onScrollbarScrolled(percentage);
    }

    @Override
    public void setScrollbarScrollPercentageForExternalChange(float percentage) {
        slider.setLocalPositionY((int) ((getHeight() - slider.getHeight()) * percentage));
    }

    public void reset(){
        slider.setLocalPositionY(getHeight() - slider.getHeight());
    }

    @Override
    public void onScrolledDown() {
        AbstractBounds sliderBounds = slider.getContainerBounds();
        Integer maxDiff = null;
        if(sliderBounds != null){
            maxDiff = slider.getWorldPositionY() - sliderBounds.getWorldBottom();
        }

        scrollbarTargetY = slider.getLocalPositionY() - (maxDiff != null ? MathUtils.clamp(scrollAmount, 0, maxDiff) : scrollAmount);
    }

    @Override
    public void onScrolledUp() {
        AbstractBounds sliderBounds = slider.getContainerBounds();
        Integer maxDiff = null;
        if(sliderBounds != null){
            maxDiff = sliderBounds.getWorldTop() - slider.getWorldPositionY();
        }

        scrollbarTargetY = slider.getLocalPositionY() + (maxDiff != null ? MathUtils.clamp(scrollAmount, 0, maxDiff) : scrollAmount);
    }

    //endregion
}
