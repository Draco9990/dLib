package dLib.ui.elements.items.scroll;

import basemod.Pair;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.MethodBindingProperty;
import dLib.properties.objects.Property;
import dLib.ui.bindings.AbstractUIElementBinding;
import dLib.ui.bindings.UIElementUndefinedRelativePathBinding;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Interactable;
import dLib.ui.elements.items.buttons.Button;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;
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

    protected abstract Interactable buildSlider();

    public Scrollbar(ScrollbarData data) {
        super(data);

        slider = findChildById(data.sliderData.id.getValue());
        boundElement = data.boundElement.getValue().getBoundObject();

        onScrollbarScrolledEvent.subscribeManaged(aFloat -> data.onScrollbarScrolledEvent.getValue().executeBinding(this, aFloat));
    }

    @Override
    public void postConstruct() {
        super.postConstruct();

        if(slider == null){
            slider = buildSlider();
            addChild(slider);
        }

        setScrollbarScrollPercentageForExternalChange(100);
    }

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

    public abstract static class ScrollbarData extends UIElementData implements Serializable {
        public static float serialVersionUID = 1L;

        public Button.ButtonData sliderData = new Button.ButtonData();

        public MethodBindingProperty onScrollbarScrolledEvent = new MethodBindingProperty()
                .setName("On Scrollbar Scrolled")
                .setDescription("Event that is triggered when the scrollbar is scrolled.")
                .setCategory("Scrollbar")
                .setDynamicCreationParameters(new Pair<>("newScrolledPercentage", Float.class));

        public Property<AbstractUIElementBinding> boundElement = new Property<AbstractUIElementBinding>(new UIElementUndefinedRelativePathBinding())
                .setName("Bound Element")
                .setDescription("The element that the scrollbar is bound to. Can be null.")
                .setCategory("Scrollbar");

        public ScrollbarData() {
            sliderData.id.setValue("slider");
        }

        @Override
        public void postConstruct() {
            super.postConstruct();

            children.add(sliderData);
        }
    }
}
