package dLib.ui.elements.items.color;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.SimpleHorizontalRangeSelector;
import dLib.ui.resources.UICommonResources;
import dLib.util.ColorHelpers;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public class ColorPickerPopup extends Renderable {
    private Color selectedColor;

    public ColorWheel colorWheel;
    public SimpleHorizontalRangeSelector lightnessBar;
    public SimpleHorizontalRangeSelector alphaBar;

    public Event<Consumer<Color>> onSelectedColorChangedEvent = new Event<>();

    public ColorPickerPopup(Color initialColor, AbstractPosition xPos, AbstractPosition yPos) {
        super(Tex.stat(ImageMaster.OPTION_CONFIRM), xPos, yPos, Dim.px(438), Dim.px(340));

        setContextual(true);

        selectedColor = initialColor;

        colorWheel = new ColorWheel(Pos.px(36), Pos.px(149), Dim.px(156), Dim.px(156));
        colorWheel.onColorHoveredEvent.subscribeManaged((color) -> {
            lightnessBar.setRenderColor(color);
            alphaBar.setRenderColor(color);
        });
        colorWheel.onUnhoveredEvent.subscribeManaged(() -> {
            lightnessBar.setRenderColor(selectedColor);
            alphaBar.setRenderColor(selectedColor);
        });
        colorWheel.onColorSelectedEvent.subscribeManaged((color) -> {
            lightnessBar.setRenderColor(color);
            alphaBar.setRenderColor(color);

            selectedColor.r = color.r;
            selectedColor.g = color.g;
            selectedColor.b = color.b;
            onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
        });
        addChildNCS(colorWheel);

        lightnessBar = new SimpleHorizontalRangeSelector(Tex.stat(UICommonResources.lightnessBar), Pos.px(30), Pos.px(90), Dim.px(380), Dim.px(28));
        lightnessBar.setRenderColor(initialColor);
        lightnessBar.setSliderFromPercentage(1f);
        lightnessBar.onPercentageChangedEvent.subscribeManaged((percentage) -> {
            float[] hsl = ColorHelpers.toHSL(selectedColor);
            selectedColor = ColorHelpers.fromHSL(hsl[0], hsl[1], percentage);
            onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
        });
        {
            Image overlay = new Image(Tex.stat(UICommonResources.lightnessBarOverlay), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            overlay.setPassthrough(true);
            lightnessBar.addChildNCS(overlay);
            lightnessBar.swapChildren(0, 1); //TODO replace with insert child
        }
        addChildNCS(lightnessBar);

        alphaBar = new SimpleHorizontalRangeSelector(Tex.stat(UICommonResources.alphaBar), Pos.px(30), Pos.px(32), Dim.px(380), Dim.px(28));
        alphaBar.setRenderColor(initialColor);
        alphaBar.onPercentageChangedEvent.subscribeManaged((percentage) -> {
            selectedColor.a = 1 - percentage;
            onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
        });
        addChildNCS(alphaBar);
    }
}
