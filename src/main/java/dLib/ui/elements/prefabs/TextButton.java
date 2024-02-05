package dLib.ui.elements.prefabs;

import dLib.ui.elements.CompositeUIElement;
import dLib.ui.themes.UIThemeManager;

public class TextButton extends CompositeUIElement {
    /** Class variables */
    private TextBox label;

    /** Constructors */
    public TextButton(String text, int xPos, int yPos, int width, int height){
        super(xPos, yPos);

        middle = new Button(xPos, yPos, width, height).setImage(UIThemeManager.getDefaultTheme().button_large);
        label = new TextBox(text, xPos, yPos, width, height);

        other.add(label);
    }

    /** Button */
    public Button getButton(){
        return (Button) middle;
    }

    /** Label */
    public TextBox getLabel(){
        return label;
    }
}
