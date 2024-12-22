package dLib.ui.elements.items;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.Alignment;
import dLib.ui.resources.UICommonResources;
import dLib.util.IntegerVector2;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public class SimpleHorizontalRangeSelector extends Renderable{
    private Image slider;

    public Event<Consumer<Float>> onPercentageChangedEvent = new Event<>();

    public SimpleHorizontalRangeSelector(TextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(imageBinding, xPos, yPos, width, height);

        slider = new Image(Tex.stat(UICommonResources.arrow_down), Pos.px(-31), Pos.px(-27), Dim.px(64), Dim.px(63));
        slider.setVerticalAlignment(Alignment.VerticalAlignment.TOP);
        slider.setPassthrough(true);
        addChildNCS(slider);

        onLeftClickEvent.subscribe(this, () -> {
            int mouseWorldX = (int) (InputHelper.mX / Settings.xScale);
            int mouseLocalX = slider.worldToLocal(new IntegerVector2(mouseWorldX, 0)).x;

            float percentage = (float) (mouseLocalX - getLocalPositionX()) / getWidth();
            float finalPercentage = Math.max(0, Math.min(1, percentage));
            onPercentageChangedEvent.invoke(floatConsumer -> floatConsumer.accept(finalPercentage));

            slider.setLocalPositionCenteredX(mouseLocalX);
        });
    }

    public void setSliderFromPercentage(float percentage){
        slider.setLocalPositionCenteredX((int) (slider.getLocalPositionX() + getWidth() * percentage));
    }
}
