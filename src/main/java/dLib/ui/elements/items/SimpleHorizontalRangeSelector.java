package dLib.ui.elements.items;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.Alignment;
import dLib.ui.elements.components.UIDraggableComponent;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.bounds.Bound;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class SimpleHorizontalRangeSelector extends Renderable{
    private Image slider;

    public ConsumerEvent<Float> onPercentageChangedEvent = new ConsumerEvent<>();

    public SimpleHorizontalRangeSelector(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(imageBinding, xPos, yPos, width, height);

        slider = new Image(Tex.stat(UICommonResources.arrow_down), Pos.px(-31), Pos.px(-27), Dim.px(64), Dim.px(63));
        slider.setVerticalAlignment(Alignment.VerticalAlignment.TOP);
        slider.setPassthrough(false);
        slider.setContainerBounds(Bound.parent(slider));
        slider.setContainerBoundCalculationType(BoundCalculationType.CONTAINS_HALF);
        {
            UIDraggableComponent draggable = slider.addComponent(new UIDraggableComponent());
            draggable.setCanDragY(false);
            draggable.onDraggedEvent.subscribeManaged(() -> {
                float totalWidth = getWidth();
                float sliderPos = slider.getLocalPositionX();
                float percent = sliderPos / totalWidth;
                onPercentageChangedEvent.invoke(percent);
            });
        }
        addChild(slider);

        onLeftClickEvent.subscribe(this, () -> {
            float mouseWorldX = (InputHelper.mX / Settings.xScale);
            float mouseLocalX = worldToLocal(new Vector2(mouseWorldX, 0)).x;

            float percentage = mouseLocalX / getWidth();
            float finalPercentage = Math.max(0, Math.min(1, percentage));
            onPercentageChangedEvent.invoke(finalPercentage);

            //slider.clickLeft();
        });
    }

    public void setSliderFromPercentage(float percentage){
        slider.getLocalPositionXRaw().overrideCalculatedValue((getWidth() * percentage));
    }

    public float getSliderPercentage(){
        return slider.getLocalPositionX() / getWidth();
    }
}
