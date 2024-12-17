package dLib.ui.elements.prefabs;

import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class TextButton extends UIElement {
    //region Variables

    public Button button;
    public TextBox label;

    //endregion

    //region Constructors

    public TextButton(String text, AbstractDimension width, AbstractDimension height){
        this(text, Pos.px(0), Pos.px(0), width, height);
    }
    public TextButton(String text, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        button = new Button(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        button.setImage(Tex.stat(UICommonResources.button01_horizontal));
        addChildCS(button);

        label = new TextBox(text, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
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

    public static class TextButtonData extends UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextBox.TextBoxData textBoxData = new TextBox.TextBoxData();
        public Button.ButtonData buttonData = new Button.ButtonData();

        public TextButtonData(){
            super();
        }

        @Override
        public TextButton makeUIElement() {
            return new TextButton(this);
        }
    }
}