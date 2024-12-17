package dLib.ui.elements.prefabs;

import basemod.Pair;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.resources.UICommonResources;

import dLib.util.ui.bounds.Bound;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class VerticalScrollbar extends Scrollbar {
    //region Variables

    //endregion

    //region Constructors

    public VerticalScrollbar(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height){
        super(x, y, width, height);

        VerticalBox elements = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        {
            elements.addItem(new Renderable(UICommonResources.scrollbar_vertical_top, Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(22)));
            elements.addItem(new Renderable(UICommonResources.scrollbar_vertical_mid, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
            elements.addItem(new Renderable(UICommonResources.scrollbar_vertical_bottom, Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(22)));
        }
        addChildNCS(elements);

        addChildNCS(slider);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(boundElement != null){
            if(boundElement.hasVerticalChildrenOOB()){
                slider.showAndEnableInstantly();
            }
            else{
                slider.hideAndDisableInstantly();
            }
        }
    }

    @Override
    protected Interactable buildSlider() {
        Button slider = new Button(Pos.px((int) (5 * 1.29f)), Pos.px(0), Dim.perc(0.7762), Dim.px(60)){
            @Override
            public int getLocalPositionX() {
                int res = super.getLocalPositionX();
                return res;
            }

            @Override
            public int getLocalPositionY() {
                int res = super.getLocalPositionY();
                return res;
            }

            @Override
            public int getWidth() {
                int res = super.getWidth();
                return res;
            }

            @Override
            public int getHeight() {
                int res = super.getHeight();
                return res;
            }
        };
        slider.setImage(UICommonResources.scrollbar_vertical_train);
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

    //endregion
}
