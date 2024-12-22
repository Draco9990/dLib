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

    private Color rawColorCache; //Used to store the color before lightness is applied to prevent color loss

    public ColorWheel colorWheel;
    public SimpleHorizontalRangeSelector lightnessBar;
    public SimpleHorizontalRangeSelector alphaBar;

    public Event<Consumer<Color>> onSelectedColorChangedEvent = new Event<>();

    public ColorPickerPopup(Color initialColor, AbstractPosition xPos, AbstractPosition yPos) {
        super(Tex.stat(ImageMaster.OPTION_CONFIRM), xPos, yPos, Dim.px(438), Dim.px(340));

        setContextual(true);

        selectedColor = initialColor;

        float[] rawHSL = ColorHelpers.toHSL(initialColor);
        rawColorCache = ColorHelpers.fromHSL(rawHSL[0], rawHSL[1], 0.5f);
        rawColorCache.a = 1f;

        colorWheel = new ColorWheel(Pos.px(36), Pos.px(149), Dim.px(156), Dim.px(156));
        colorWheel.onColorHoveredEvent.subscribeManaged((color) -> {
            lightnessBar.setRenderColor(color);
            alphaBar.setRenderColor(color);
        });
        colorWheel.onUnhoveredEvent.subscribeManaged(() -> {
            lightnessBar.setRenderColor(rawColorCache);
            alphaBar.setRenderColor(rawColorCache);
        });
        colorWheel.onColorSelectedEvent.subscribeManaged((color) -> {
            lightnessBar.setRenderColor(color);
            alphaBar.setRenderColor(color);

            rawColorCache = color;

            float[] hsl = ColorHelpers.toHSL(rawColorCache);
            selectedColor = ColorHelpers.fromHSL(hsl[0], hsl[1], lightnessBar.getSliderPercentage());

            selectedColor.a = 1 - alphaBar.getSliderPercentage();

            onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
        });
        addChildNCS(colorWheel);

        lightnessBar = new SimpleHorizontalRangeSelector(Tex.stat(UICommonResources.lightnessBar), Pos.px(30), Pos.px(90), Dim.px(380), Dim.px(28));
        lightnessBar.setRenderColor(rawColorCache);
        lightnessBar.setSliderFromPercentage(((initialColor.equals(Color.WHITE) || initialColor.equals(Color.BLACK)) ? 0.5f : ColorHelpers.toHSL(initialColor)[2]));
        lightnessBar.onPercentageChangedEvent.subscribeManaged((percentage) -> {
            float[] hsl = ColorHelpers.toHSL(rawColorCache);
            Color newColor = ColorHelpers.fromHSL(hsl[0], hsl[1], percentage);
            newColor.a = 1 - alphaBar.getSliderPercentage();

            selectedColor = newColor;
            onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
        });
        {
            Image overlay = new Image(Tex.stat(UICommonResources.lightnessBarOverlay), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            overlay.setPassthrough(true);
            lightnessBar.addChildNCS(overlay);
            lightnessBar.swapChildren(0, 1); //TODO replace with insert child
        }
        addChildNCS(lightnessBar);

        alphaBar = new SimpleHorizontalRangeSelector(Tex.stat(UICommonResources.alphaBar), Pos.px(30), Pos.px(42), Dim.px(380), Dim.px(28));
        alphaBar.setRenderColor(rawColorCache);
        alphaBar.setSliderFromPercentage(1 - initialColor.a);
        alphaBar.onPercentageChangedEvent.subscribeManaged((percentage) -> {
            selectedColor.a = 1 - percentage;
            onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
        });
        addChildNCS(alphaBar);
    }
}
