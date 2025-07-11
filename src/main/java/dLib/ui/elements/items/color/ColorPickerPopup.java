package dLib.ui.elements.items.color;

import com.badlogic.gdx.graphics.Color;
import dLib.magiccolor.MagicColor;
import dLib.magiccolor.MagicColorManager;
import dLib.ui.elements.items.*;
import dLib.ui.elements.items.buttons.Toggle;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.itembox.GridBox;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.numericaleditors.IntegerInputBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.Reflection;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;
import dLib.util.helpers.ColorHelpers;
import dLib.util.helpers.UIHelpers;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.util.HashMap;
import java.util.function.BiConsumer;

//TODO fix hex code input not upodating lightness and alpha correctly
//TODO add a 'ping' to the color wheel when the color is changed
public class ColorPickerPopup extends VerticalBox {
    private Color selectedColor;

    public BiConsumerEvent<Color, Boolean> onSelectedColorChangedEvent = new BiConsumerEvent<>();                       public TriConsumerEvent<Color, Boolean, ColorPickerPopup> onSelectedColorChangedEvent_Static = new TriConsumerEvent<>();

    public ColorPickerPopup(Color initialColor,
                            boolean allowAlpha,
                            boolean allowPresets,
                            boolean allowMagicColors,
                            boolean allowClear) {
        this(initialColor, Pos.px(UIHelpers.getMouseWorldPositionX()), Pos.px(UIHelpers.getMouseWorldPositionY()),
                allowAlpha,
                allowPresets,
                allowMagicColors,
                allowClear);
    }

    public ColorPickerPopup(Color initialColor, AbstractPosition xPos, AbstractPosition yPos,
                            boolean allowAlpha,
                            boolean allowPresets,
                            boolean allowMagicColors,
                            boolean allowClear) {
        super(xPos, yPos, Dim.px(400), Dim.auto());

        setContextual(true);

        setItemSpacing(10);

        selectedColor = initialColor;

        if(allowPresets){
            ColorPickerStaticColorSelector colorSelector = new ColorPickerStaticColorSelector(this, allowMagicColors);
            addChild(colorSelector);
        }

        ColorPickerDynamicColorSelector colorSelector = new ColorPickerDynamicColorSelector(this, initialColor, allowAlpha);
        onSelectedColorChangedEvent.subscribe(colorSelector, (color, isStatic) -> {
            if(isStatic){
                colorSelector.disable();
            }
            else {
                colorSelector.enable();
            }
        });
        addChild(colorSelector);

        setSelectedColor(initialColor, initialColor instanceof MagicColor);
    }

    private void setSelectedColor(Color color, boolean isStatic){
        selectedColor = color;

        onSelectedColorChangedEvent.invoke(selectedColor, isStatic);
        onSelectedColorChangedEvent_Static.invoke(selectedColor, isStatic, this);
    }

    private static class ColorPickerStaticColorSelector extends VerticalBox{
        private HashMap<Toggle, Color> colorBoxOutlines = new HashMap<>();

        public ColorPickerStaticColorSelector(ColorPickerPopup parent, boolean allowMagicColors) {
            super(Dim.fill(), Dim.auto());

            setTexture(UICommonResources.tooltipBg);
            setRenderColor(Color.WHITE);
            setHueShiftAmount(180);

            setItemSpacing(20);

            setContentPadding(Padd.px(20));

            VerticalBox colorPresetsBox = new VerticalBox(Dim.fill(), Dim.auto());

            if(allowMagicColors){
                GridBox magicColorPresets = new GridBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
                magicColorPresets.setItemSpacing(5);
                {
                    for (MagicColor magicColor : MagicColorManager.magicColors.values()){
                        Image colorBox = new Image(Tex.stat(magicColor.getSquareImage()), Pos.px(0), Pos.px(0), Dim.px(25), Dim.px(25));
                        {
                            Toggle colorBoxOutline = new Toggle(Tex.stat(UICommonResources.color_outline), Tex.stat(UICommonResources.color_outline_selected), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                            colorBoxOutline.postToggledEvent.subscribe(colorBox, (toggleState) -> {
                                if(toggleState){
                                    parent.setSelectedColor(magicColor, true);
                                }
                            });
                            colorBox.addChild(colorBoxOutline);
                            colorBoxOutlines.put(colorBoxOutline, magicColor);
                        }
                        magicColorPresets.addChild(colorBox);
                    }
                }
                colorPresetsBox.addChild(magicColorPresets);
            }

            GridBox staticColorPresets = new GridBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
            staticColorPresets.setItemSpacing(5);
            {
                for (Color staticColor : Reflection.getFieldValuesByClass(Color.class, Color.class)) {
                    Image colorBox = new Image(Tex.stat(UICommonResources.color_fill), Pos.px(0), Pos.px(0), Dim.px(25), Dim.px(25));
                    colorBox.setRenderColor(staticColor);
                    {
                        Toggle colorBoxOutline = new Toggle(Tex.stat(UICommonResources.color_outline), Tex.stat(UICommonResources.color_outline_selected), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                        colorBoxOutline.postToggledEvent.subscribe(colorBox, (toggleState) -> {
                            if(toggleState){
                                parent.setSelectedColor(staticColor, true);
                            }
                        });
                        colorBox.addChild(colorBoxOutline);
                        colorBoxOutlines.put(colorBoxOutline, staticColor);
                    }
                    staticColorPresets.addChild(colorBox);
                }
            }
            colorPresetsBox.addChild(staticColorPresets);

            parent.onSelectedColorChangedEvent.subscribe(this, (color, isStatic) -> colorBoxOutlines.forEach((outline, staticColor) -> {
                outline.setToggled(isStatic && (color == staticColor || (color instanceof MagicColor && color.getClass() == staticColor.getClass())));
            }));

            addChild(colorPresetsBox);
        }
    }

    private static class ColorPickerDynamicColorSelector extends VerticalBox {
        private Color preLightnessColorCache; //Used to store the color before lightness is applied to prevent color loss

        public ColorWheel colorWheel;
        public SimpleHorizontalRangeSelector lightnessBar;
        public SimpleHorizontalRangeSelector alphaBar;

        public IntegerInputBox rValInput;
        public IntegerInputBox gValInput;
        public IntegerInputBox bValInput;
        public IntegerInputBox aValInput;
        public Inputfield hexValInput;

        public ColorPickerDynamicColorSelector(ColorPickerPopup parent, Color initialColor, boolean allowAlpha) {
            super(Dim.fill(), Dim.auto());

            setTexture(UICommonResources.tooltipBg);
            setRenderColor(Color.WHITE);

            float[] rawHSL = ColorHelpers.toHSL(initialColor);
            preLightnessColorCache = ColorHelpers.fromHSL(rawHSL[0], rawHSL[1], 0.5f);
            preLightnessColorCache.a = 1f;

            setItemSpacing(20);

            setContentPadding(Padd.px(20));

            HorizontalBox colorPickerBox = new HorizontalBox(Dim.fill(), Dim.px(156));
            {
                colorWheel = new ColorWheel(Pos.px(0), Pos.px(0), Dim.mirror(), Dim.fill());
                colorWheel.onColorHoveredEvent.subscribeManaged((color) -> {
                    lightnessBar.setRenderColor(color);
                    alphaBar.setRenderColor(color);
                });
                colorWheel.onUnhoveredEvent.subscribeManaged(() -> {
                    lightnessBar.setRenderColor(preLightnessColorCache);
                    alphaBar.setRenderColor(preLightnessColorCache);
                });
                colorWheel.onColorSelectedEvent.subscribeManaged((color) -> {
                    lightnessBar.setRenderColor(color);
                    alphaBar.setRenderColor(color);

                    preLightnessColorCache = color;

                    float[] hsl = ColorHelpers.toHSL(preLightnessColorCache);
                    Color newColor = ColorHelpers.fromHSL(hsl[0], hsl[1], lightnessBar.getSliderPercentage());

                    if(allowAlpha) newColor.a = 1 - alphaBar.getSliderPercentage();

                    parent.setSelectedColor(newColor, false);
                });
                colorPickerBox.addChild(colorWheel);

                colorPickerBox.addChild(new Spacer(Dim.fill(), Dim.fill()));

                VerticalBox rgbValuesBox = new VerticalBox(Dim.px(198), Dim.fill());
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

                            Color newColor = parent.selectedColor.cpy();
                            newColor.r = (float)r / 255f;
                            parent.setSelectedColor(newColor, false);
                        });
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

                            Color newColor = parent.selectedColor.cpy();
                            newColor.g = (float)g / 255f;
                            parent.setSelectedColor(newColor, false);
                        });
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

                            Color newColor = parent.selectedColor.cpy();
                            newColor.b = (float)b / 255f;
                            parent.setSelectedColor(newColor, false);
                        });
                        bVal.addChild(bValInput);
                    }
                    rgbValuesBox.addChild(bVal);

                    if(allowAlpha){
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

                                Color newColor = parent.selectedColor.cpy();
                                newColor.a = (float)a / 255f;
                                parent.setSelectedColor(newColor, false);
                            });
                            aVal.addChild(aValInput);
                        }
                        rgbValuesBox.addChild(aVal);
                    }

                    HorizontalBox hexVal = new HorizontalBox(Dim.fill(), Dim.px(27));
                    {
                        TextBox hexValText = new TextBox("Hex:", Pos.px(0), Pos.px(0), Dim.px(45), Dim.fill());
                        hexVal.addChild(hexValText);

                        hexValInput = new Inputfield(initialColor.toString(), Pos.px(48), Pos.px(0), Dim.fill(), Dim.fill());
                        hexValInput.setCharacterLimit(6);
                        hexValInput.onValueChangedEvent.subscribeManaged((value) -> {
                            if(value.length() == 6){

                                Color newColor = Color.valueOf("#" + value);
                                parent.setSelectedColor(newColor, false);
                            }
                        });
                        hexVal.addChild(hexValInput);
                    }
                    rgbValuesBox.addChild(hexVal);
                }
                colorPickerBox.addChild(rgbValuesBox);
            }
            addChild(colorPickerBox);

            lightnessBar = new SimpleHorizontalRangeSelector(Tex.stat(UICommonResources.lightnessBar), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(28));
            lightnessBar.setRenderColor(preLightnessColorCache);
            lightnessBar.setSliderFromPercentage(((initialColor.equals(Color.WHITE) || initialColor.equals(Color.BLACK)) ? 0.5f : ColorHelpers.toHSL(initialColor)[2]));
            lightnessBar.onPercentageChangedEvent.subscribeManaged((percentage) -> {
                float[] hsl = ColorHelpers.toHSL(preLightnessColorCache);
                Color newColor = ColorHelpers.fromHSL(hsl[0], hsl[1], percentage);
                if(allowAlpha) newColor.a = 1 - alphaBar.getSliderPercentage();

                parent.setSelectedColor(newColor, false);
            });
            {
                Image overlay = new Image(Tex.stat(UICommonResources.lightnessBarOverlay), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                overlay.setPassthrough(true);
                lightnessBar.addChild(overlay);
                lightnessBar.swapChildren(0, 1); //TODO replace with insert child
            }
            addChild(lightnessBar);

            alphaBar = new SimpleHorizontalRangeSelector(Tex.stat(UICommonResources.alphaBar), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(28));
            alphaBar.setRenderColor(preLightnessColorCache);
            alphaBar.setSliderFromPercentage(1 - initialColor.a);
            alphaBar.onPercentageChangedEvent.subscribeManaged((percentage) -> {
                Color newColor = parent.selectedColor.cpy();
                newColor.a = 1 - percentage;
                parent.setSelectedColor(newColor, false);
            });
            if(allowAlpha) addChild(alphaBar);

            BiConsumer<Color, Boolean> updateValuesForColor = (color, isStatic) -> {
                if(!(color instanceof MagicColor)){
                    if(!rValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.r * 255)))) rValInput.inputbox.textBox.setText(String.valueOf((int)(color.r * 255)));
                    if(!gValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.g * 255)))) gValInput.inputbox.textBox.setText(String.valueOf((int)(color.g * 255)));
                    if(!bValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.b * 255)))) bValInput.inputbox.textBox.setText(String.valueOf((int)(color.b * 255)));
                    if(allowAlpha && !aValInput.inputbox.textBox.getText().equals(String.valueOf((int)(color.a * 255)))) aValInput.inputbox.textBox.setText(String.valueOf((int)(color.a * 255)));
                    if(!hexValInput.textBox.getText().equals(color.toString().substring(0, 6))) hexValInput.textBox.setText(color.toString().substring(0, 6));

                    float[] hsl = ColorHelpers.toHSL(color);
                    if(lightnessBar.getSliderPercentage() != hsl[2]) lightnessBar.setSliderFromPercentage(hsl[2]);
                    if(alphaBar.getSliderPercentage() != 1 - color.a) alphaBar.setSliderFromPercentage(1 - color.a);
                }

                if(isStatic && !(color instanceof MagicColor)){
                    preLightnessColorCache = ColorHelpers.getSaturatedColor(color);
                    lightnessBar.setRenderColor(preLightnessColorCache);
                    alphaBar.setRenderColor(preLightnessColorCache);
                }
                else if(isStatic){
                    lightnessBar.setRenderColor(color);
                    alphaBar.setRenderColor(color);
                }
            };

            parent.onSelectedColorChangedEvent.subscribe(this, updateValuesForColor);
            updateValuesForColor.accept(initialColor, initialColor instanceof MagicColor);
        }
    }
}
