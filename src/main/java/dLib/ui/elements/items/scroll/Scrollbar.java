package dLib.ui.elements.items.scroll;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Interactable;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.util.function.Consumer;

public abstract class Scrollbar extends UIElement {
    //region Variables

    protected Interactable slider;

    public Event<Consumer<Float>> onScrollbarScrolledEvent = new Event<>();

    protected UIElement boundElement;

    protected float currentScrollPercentageCache = 0;

    protected int scrollAmount = 60;
    protected float scrollSpeed = 0.1f;

    //endregion

    //region Constructors

    public Scrollbar(AbstractPosition x, AbstractPosition y, AbstractDimension width, AbstractDimension height){
        super(x, y, width, height);
    }

    @Override
    public void postConstruct() {
        super.postConstruct();

        slider = buildSlider();
        addChild(slider);
        setScrollbarScrollPercentageForExternalChange(100);
    }

    protected abstract Interactable buildSlider();

    //endregion

    //region Methods


    @Override
    protected void updateSelf() {
        super.updateSelf();

        if(boundElement != null){
            if(boundElement.isHoveredOrChildHovered() || isHovered() || slider.isHovered()){
                if(InputHelper.scrolledUp){
                    onScrolledUp();
                }
                else if(InputHelper.scrolledDown){
                    onScrolledDown();
                }
            }
        }
    }

    public abstract void onScrolledDown();
    public abstract void onScrolledUp();

    public void onScrollbarScrolled(float percentage){
        currentScrollPercentageCache = percentage;
        onScrollbarScrolledEvent.invoke(floatConsumer -> floatConsumer.accept(percentage));
    }

    public abstract void setScrollbarScrollPercentageForExternalChange(float percentage);

    public abstract void reset();

    //region Slider

    public Interactable getSlider(){
        return slider;
    }

    //endregion

    //region Bound Element

    public void setBoundElement(UIElement element){
        boundElement = element;
    }

    //endregion

    //endregion
}
