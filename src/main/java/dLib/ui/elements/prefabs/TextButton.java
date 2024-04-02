package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.IntegerVector2;
import dLib.util.bindings.texture.TextureThemeBinding;
import dLib.util.settings.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class TextButton extends UIElement {
    //region Variables

    private Button button;
    private TextBox label;

    //endregion

    //region Constructors

    public TextButton(String text, int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        button = new Button(0, 0, width, height).setImage(UIThemeManager.getDefaultTheme().button_large);
        addChildCS(button);

        label = new TextBox(text, 0, 0, width, height);
        addChildNCS(label);
    }

    public TextButton(TextButtonData data){
        super(data);

        button = data.buttonData.makeUIElement();
        addChildCS(button);

        label = data.textBoxData.makeUIElement();
        addChildCS(label);
    }

    //endregion

    //region Methods

    public Button getButton(){
        return button;
    }

    public TextBox getTextBox(){
        return label;
    }

    //endregion

    public static class TextButtonData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public Button.ButtonData buttonData = new Button.ButtonData();
        public TextBox.TextBoxData textBoxData = new TextBox.TextBoxData();

        public TextButtonData(){
            buttonData.textureBinding.setValue(new TextureThemeBinding("button_small"));

            //add children transient property that manages this and below?
            dimensions.addOnValueChangedListener(new BiConsumer<IntegerVector2, IntegerVector2>() {
                @Override
                public void accept(IntegerVector2 integerVector2, IntegerVector2 integerVector22) {
                    buttonData.dimensions.setValue(dimensions.getValue());
                    textBoxData.dimensions.setValue(dimensions.getValue());
                }
            });
        }

        @Override
        public TextButton makeUIElement() {
            return new TextButton(this);
        }

        @Override
        public ArrayList<Property<?>> getEditableProperties() {
            ArrayList<Property<?>> properties = super.getEditableProperties();

            properties.addAll(buttonData.getEditableProperties());
            buttonData.filterInnerProperties(properties);

            properties.addAll(textBoxData.getEditableProperties());
            textBoxData.filterInnerProperties(properties);
            properties.remove(textBoxData.textureBinding);

            return properties;
        }
    }
}