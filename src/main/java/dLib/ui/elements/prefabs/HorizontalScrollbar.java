package dLib.ui.elements.prefabs;

import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.Bounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class HorizontalScrollbar extends Scrollbar {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalScrollbar(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height){
        super(x, y, width, height);

        HorizontalBox elements = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        {
            elements.addItem(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_left, Pos.px(0), Pos.px(0), Dim.px(22), Dim.fill()));
            elements.addItem(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_middle, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
            elements.addItem(new Renderable(UIThemeManager.getDefaultTheme().scrollbar_horizontal_right, Pos.px(0), Pos.px(0), Dim.px(22), Dim.fill()));
        }
        addChildNCS(elements);

        addChildNCS(slider);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(boundElement != null){
            Bounds childBounds = boundElement.getChildUnscrolledBounds();
            if(childBounds.right > boundElement.getWorldPositionX() + getWidth()){
                slider.showAndEnableInstantly();
            }
            else if(childBounds.left < boundElement.getWorldPositionX()){
                slider.showAndEnableInstantly();
            }
            else{
                slider.hideAndDisableInstantly();
            }
        }
    }

    @Override
    protected Interactable buildSlider() {
        Button slider = new Button(Pos.px(0), Pos.px((int) (5 * 1.29f)), Dim.px(60), Dim.perc(0.7762));
        {
            slider.setImage(UIThemeManager.getDefaultTheme().scrollbar_horizontal_train);
            slider.setBoundWithinParent(true);
            slider.addOnPositionChangedConsumer((element) -> {
                onScrollbarScrolled((float) slider.getLocalPositionX() / (getWidth() - slider.getWidth()));
            });

            UIDraggableComponent component = slider.addComponent(new UIDraggableComponent());
            component.setCanDragY(false);
        }
        return slider;
    }

    //endregion

    //region Methods


    @Override
    public void onScrollbarScrolled(float percentage) {
        if(boundElement != null){
            Bounds bounds = boundElement.getChildUnscrolledBounds();

            int boundElementRightX = boundElement.getWorldPositionX() + boundElement.getWidth();
            int correctionAmount = Math.max(0, bounds.right - boundElementRightX);

            int totalWidth = bounds.right - bounds.left;
            int overlapAmount = Math.max(0, bounds.right - boundElement.getWorldPositionX());
            int scrollableArea = totalWidth - overlapAmount + correctionAmount;
            int offset = (int) (scrollableArea - (scrollableArea * percentage)) - correctionAmount;

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

    //endregion
}
