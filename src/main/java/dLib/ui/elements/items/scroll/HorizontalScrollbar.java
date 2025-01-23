package dLib.ui.elements.items.scroll;

import basemod.Pair;
import com.badlogic.gdx.math.MathUtils;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.items.Interactable;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.bounds.Bound;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class HorizontalScrollbar extends Scrollbar {
    //region Variables

    private Integer scrollbarTargetX = null;

    //endregion

    //region Constructors

    public HorizontalScrollbar(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height){
        super(x, y, width, height);

        HorizontalBox elements = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        elements.setItemSpacing(0);
        {
            elements.addItem(new Renderable(Tex.stat(UICommonResources.scrollbar_horizontal_left), Pos.px(0), Pos.px(0), Dim.px(22), Dim.fill()));
            elements.addItem(new Renderable(Tex.stat(UICommonResources.scrollbar_horizontal_mid), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
            elements.addItem(new Renderable(Tex.stat(UICommonResources.scrollbar_horizontal_right), Pos.px(0), Pos.px(0), Dim.px(22), Dim.fill()));
        }
        addChild(elements);
    }

    public HorizontalScrollbar(HorizontalScrollbarData data) {
        super(data);
    }

    @Override
    public void postConstruct() {
        super.postConstruct();

        slider.onPositionChangedEvent.subscribeManaged((element) -> onScrollbarScrolled((float) slider.getLocalPositionX() / (getWidth() - slider.getWidth())));

        UIDraggableComponent component = slider.addComponent(new UIDraggableComponent());
        component.setCanDragY(false);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(boundElement != null){
            if(boundElement.hasHorizontalChildrenOOB()){
                slider.showAndEnableInstantly();

                if(scrollbarTargetX != null){
                    if(Math.abs(scrollbarTargetX - slider.getLocalPositionX()) > 0.5f){
                        slider.setLocalPositionX((int) MathUtils.lerp(slider.getLocalPositionX(), scrollbarTargetX, scrollSpeed));
                    }
                    else{
                        slider.setLocalPositionX(scrollbarTargetX);
                        scrollbarTargetX = null;
                    }
                }
            }
            else{
                onScrollbarScrolled(0);
                slider.hideAndDisableInstantly();
                scrollbarTargetX = null;
            }
        }
        else{
            scrollbarTargetX = null;
        }
    }

    @Override
    protected Interactable buildSlider() {
        Button slider = new Button(Pos.px(0), Pos.px((int) (5 * 1.29f)), Dim.px(60), Dim.perc(0.7762));
        slider.setImage(Tex.stat(UICommonResources.scrollbar_horizontal_train));
        slider.setContainerBounds(Bound.parent(slider));
        return slider;
    }

    //endregion

    //region Methods


    @Override
    public void onScrollbarScrolled(float percentage) {
        if(boundElement != null){
            Pair<Integer, Integer> oobAmounts = boundElement.getHorizontalChildrenOOBAmount();

            int scrollableArea = oobAmounts.getKey() + oobAmounts.getValue();
            int offset = (int) (scrollableArea - (scrollableArea * percentage)) - oobAmounts.getValue();

            boundElement.setLocalChildOffsetX(offset);
        }

        super.onScrollbarScrolled(percentage);
    }

    @Override
    public void setScrollbarScrollPercentageForExternalChange(float percentage) {
        slider.setLocalPositionX((int) ((getWidth() - slider.getWidth()) * percentage));
    }

    public void reset(){
        slider.setLocalPositionX(0);
    }

    @Override
    public void onScrolledDown() {
        scrollbarTargetX = slider.getLocalPositionX() + scrollAmount;
    }

    @Override
    public void onScrolledUp() {
        scrollbarTargetX = slider.getLocalPositionX() - scrollAmount;
    }

    //endregion

    public static class HorizontalScrollbarData extends ScrollbarData implements Serializable {
        public static float serialVersionUID = 1L;

        @Override
        public void postConstruct() {
            height.setValue(Dim.px(50));

            sliderData.localPositionX.setValue(Pos.px(0));
            sliderData.localPositionY.setValue(Pos.px((int) (5 * 1.29f)));
            sliderData.width.setValue(Dim.px(60));
            sliderData.height.setValue(Dim.perc(0.7762));
            sliderData.texture.setValue(Tex.resource(UICommonResources.class, "scrollbar_horizontal_train"));
            //TODO bounds
            //todo make it draggable

            super.postConstruct();
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new HorizontalScrollbar(this);
        }
    }
}
