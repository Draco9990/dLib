package dLib.ui.elements.prefabs;

import dLib.ui.data.prefabs.TextButtonData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;

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
}
