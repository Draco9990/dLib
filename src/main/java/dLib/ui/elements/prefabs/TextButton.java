package dLib.ui.elements.prefabs;

import dLib.ui.data.prefabs.ButtonData;
import dLib.ui.data.prefabs.TextBoxData;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;

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
        addChildNCS(label);
    }

    public TextButton(TextButtonData data){
        super(data);

        button = data.buttonData.makeLiveInstance();
        addChildCS(button);

        label = data.textBoxData.makeLiveInstance();
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

        public ButtonData buttonData;
        public TextBoxData textBoxData;

        @Override
        public UIElement makeUIElement() {
            return super.makeUIElement();
        }
    }
}