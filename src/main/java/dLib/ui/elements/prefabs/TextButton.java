package dLib.ui.elements.prefabs;

import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;

import java.io.Serializable;

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
        label.setFont(FontHelper.buttonLabelFont);
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

        public TextBox.TextBoxData textBoxData = new TextBox.TextBoxData();
        public Button.ButtonData buttonData = new Button.ButtonData();

        public TextButtonData(){
            buttonData.textureBinding.setValue(new TextureThemeBinding("button_small"));
        }

        @Override
        public TextButton makeUIElement() {
            return new TextButton(this);
        }
    }
}