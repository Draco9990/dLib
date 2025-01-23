package dLib.ui.elements.items.color;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.SimpleHorizontalRangeSelector;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.numericaleditors.IntegerInputBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.helpers.ColorHelpers;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

//TODO fix hex code input not upodating lightness and alpha correctly
//TODO add a 'ping' to the color wheel when the color is changed
public class ColorPickerPopup extends Renderable {
    private Color selectedColor;

    private Color rawColorCache; //Used to store the color before lightness is applied to prevent color loss

    public ColorWheel colorWheel;
    public SimpleHorizontalRangeSelector lightnessBar;
    public SimpleHorizontalRangeSelector alphaBar;

    public Event<Consumer<Color>> onSelectedColorChangedEvent = new Event<>();

    public IntegerInputBox rValInput;
    public IntegerInputBox gValInput;
    public IntegerInputBox bValInput;
    public IntegerInputBox aValInput;
    public Inputfield hexValInput;

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
        addChild(colorWheel);

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
            lightnessBar.addChild(overlay);
            lightnessBar.swapChildren(0, 1); //TODO replace with insert child
        }
        addChild(lightnessBar);

        alphaBar = new SimpleHorizontalRangeSelector(Tex.stat(UICommonResources.alphaBar), Pos.px(30), Pos.px(42), Dim.px(380), Dim.px(28));
        alphaBar.setRenderColor(rawColorCache);
        alphaBar.setSliderFromPercentage(1 - initialColor.a);
        alphaBar.onPercentageChangedEvent.subscribeManaged((percentage) -> {
            selectedColor.a = 1 - percentage;
            onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
        });
        addChild(alphaBar);


        VerticalBox rgbValuesBox = new VerticalBox(Pos.px(210), Pos.px(340-190), Dim.px(198), Dim.px(156));
        rgbValuesBox.setItemSpacing(5);
        {
            HorizontalBox rVal = new HorizontalBox(Dim.fill(), Dim.px(27));
            {
                TextBox rValText = new TextBox("R:", Pos.px(0), Pos.px(0), Dim.px(20), Dim.fill());
                rVal.addChild(rValText);

                rValInput = new IntegerInputBox(Pos.px(23), Pos.px(0), Dim.fill(), Dim.fill());
                rValInput.leftArrow.onLeftClickEvent.subscribe(rValInput, () -> {
                    Integer r = Integer.parseInt(rValInput.inputbox.textBox.getText());
                    r = Math.max(0, r - 1);
                    rValInput.inputbox.textBox.setText(String.valueOf(r));
                });
                rValInput.rightArrow.onLeftClickEvent.subscribe(rValInput, () -> {
                    Integer r = Integer.parseInt(rValInput.inputbox.textBox.getText());
                    r = Math.min(255, r + 1);
                    rValInput.inputbox.textBox.setText(String.valueOf(r));
                });
                rValInput.inputbox.onValueChangedEvent.subscribeManaged((value) -> {
                    Integer r = Integer.parseInt(value);
                    r = Math.min(255, Math.max(0, r));
                    selectedColor.r = (float)r / 255f;
                    onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
                });
                rValInput.inputbox.textBox.setText(String.valueOf((int)(selectedColor.r * 255)));
                rVal.addChild(rValInput);
            }
            rgbValuesBox.addChild(rVal);

            HorizontalBox gVal = new HorizontalBox(Dim.fill(), Dim.px(27));
            {
                TextBox gValText = new TextBox("G:", Pos.px(0), Pos.px(0), Dim.px(20), Dim.fill());
                gVal.addChild(gValText);

                gValInput = new IntegerInputBox(Pos.px(23), Pos.px(0), Dim.fill(), Dim.fill());
                gValInput.leftArrow.onLeftClickEvent.subscribe(gValInput, () -> {
                    Integer g = Integer.parseInt(gValInput.inputbox.textBox.getText());
                    g = Math.max(0, g - 1);
                    gValInput.inputbox.textBox.setText(String.valueOf(g));
                });
                gValInput.rightArrow.onLeftClickEvent.subscribe(gValInput, () -> {
                    Integer g = Integer.parseInt(gValInput.inputbox.textBox.getText());
                    g = Math.min(255, g + 1);
                    gValInput.inputbox.textBox.setText(String.valueOf(g));
                });
                gValInput.inputbox.onValueChangedEvent.subscribeManaged((value) -> {
                    Integer g = Integer.parseInt(value);
                    g = Math.min(255, Math.max(0, g));
                    selectedColor.g = (float)g / 255f;
                    onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
                });
                gValInput.inputbox.textBox.setText(String.valueOf((int)(selectedColor.g * 255)));
                gVal.addChild(gValInput);
            }
            rgbValuesBox.addChild(gVal);

            HorizontalBox bVal = new HorizontalBox(Dim.fill(), Dim.px(27));
            {
                TextBox bValText = new TextBox("B:", Pos.px(0), Pos.px(0), Dim.px(20), Dim.fill());
                bVal.addChild(bValText);

                bValInput = new IntegerInputBox(Pos.px(23), Pos.px(0), Dim.fill(), Dim.fill());
                bValInput.leftArrow.onLeftClickEvent.subscribe(bValInput, () -> {
                    Integer b = Integer.parseInt(bValInput.inputbox.textBox.getText());
                    b = Math.max(0, b - 1);
                    bValInput.inputbox.textBox.setText(String.valueOf(b));
                });
                bValInput.rightArrow.onLeftClickEvent.subscribe(bValInput, () -> {
                    Integer b = Integer.parseInt(bValInput.inputbox.textBox.getText());
                    b = Math.min(255, b + 1);
                    bValInput.inputbox.textBox.setText(String.valueOf(b));
                });
                bValInput.inputbox.onValueChangedEvent.subscribeManaged((value) -> {
                    Integer b = Integer.parseInt(value);
                    b = Math.min(255, Math.max(0, b));
                    selectedColor.b = (float)b / 255f;
                    onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
                });
                bValInput.inputbox.textBox.setText(String.valueOf((int)(selectedColor.b * 255)));
                bVal.addChild(bValInput);
            }
            rgbValuesBox.addChild(bVal);

            HorizontalBox aVal = new HorizontalBox(Dim.fill(), Dim.px(27));
            {
                TextBox aValText = new TextBox("A:", Pos.px(0), Pos.px(0), Dim.px(20), Dim.fill());
                aVal.addChild(aValText);

                aValInput = new IntegerInputBox(Pos.px(23), Pos.px(0), Dim.fill(), Dim.fill());
                aValInput.leftArrow.onLeftClickEvent.subscribe(aValInput, () -> {
                    Integer a = Integer.parseInt(aValInput.inputbox.textBox.getText());
                    a = Math.max(0, a - 1);
                    aValInput.inputbox.textBox.setText(String.valueOf(a));
                });
                aValInput.rightArrow.onLeftClickEvent.subscribe(aValInput, () -> {
                    Integer a = Integer.parseInt(aValInput.inputbox.textBox.getText());
                    a = Math.min(255, a + 1);
                    aValInput.inputbox.textBox.setText(String.valueOf(a));
                });
                aValInput.inputbox.onValueChangedEvent.subscribeManaged((value) -> {
                    Integer a = Integer.parseInt(value);
                    a = Math.min(255, Math.max(0, a));
                    selectedColor.a = (float)a / 255f;
                    onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
                });
                aValInput.inputbox.textBox.setText(String.valueOf((int)(selectedColor.a * 255)));
                aVal.addChild(aValInput);
            }
            rgbValuesBox.addChild(aVal);

            HorizontalBox hexVal = new HorizontalBox(Dim.fill(), Dim.px(27));
            {
                TextBox hexValText = new TextBox("Hex:", Pos.px(0), Pos.px(0), Dim.px(45), Dim.fill());
                hexVal.addChild(hexValText);

                hexValInput = new Inputfield(selectedColor.toString(), Pos.px(48), Pos.px(0), Dim.fill(), Dim.fill());
                hexValInput.setCharacterLimit(6);
                hexValInput.onValueChangedEvent.subscribeManaged((value) -> {
                    if(value.length() == 6){
                        selectedColor = Color.valueOf("#" + value);
                        onSelectedColorChangedEvent.invoke(colorConsumer -> colorConsumer.accept(selectedColor));
                    }
                });
                hexVal.addChild(hexValInput);
            }
            rgbValuesBox.addChild(hexVal);
        }
        addChild(rgbValuesBox);

        Consumer<Color> updateValuesForColor = color -> {
            if(!rValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.r * 255)))) rValInput.inputbox.textBox.setText(String.valueOf((int)(color.r * 255)));
            if(!gValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.g * 255)))) gValInput.inputbox.textBox.setText(String.valueOf((int)(color.g * 255)));
            if(!bValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.b * 255)))) bValInput.inputbox.textBox.setText(String.valueOf((int)(color.b * 255)));
            if(!aValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.a * 255)))) aValInput.inputbox.textBox.setText(String.valueOf((int)(color.a * 255)));
            if(!hexValInput.textBox.getText().equals(color.toString().substring(0, 6))) hexValInput.textBox.setText(color.toString().substring(0, 6));

            float[] hsl = ColorHelpers.toHSL(color);
            if(lightnessBar.getSliderPercentage() != hsl[2]) lightnessBar.setSliderFromPercentage(hsl[2]);
            if(alphaBar.getSliderPercentage() != 1 - color.a) alphaBar.setSliderFromPercentage(1 - color.a);
        };

        onSelectedColorChangedEvent.subscribe(this, updateValuesForColor);
        updateValuesForColor.accept(selectedColor);
    }
}
