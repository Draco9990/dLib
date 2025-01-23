package dLib.ui.elements.items.text;

import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class TextButton extends Button {
    //region Variables

    public TextBox label;

    //endregion

    //region Constructors

    public TextButton(String text, AbstractDimension width, AbstractDimension height){
        this(text, Pos.px(0), Pos.px(0), width, height);
    }
    public TextButton(String text, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        setImage(Tex.stat(UICommonResources.button01_horizontal));

        label = new TextBox(text, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        label.setFont(Font.stat(FontHelper.buttonLabelFont));
        label.setFontSize(14);
        label.setPadding(Padd.px(10));
        addChild(label);
    }

    public TextButton(TextButtonData data){
        super(data);

        label = (TextBox) findChildById("label");
    }

    //endregion

    public static class TextButtonData extends ButtonData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextBox.TextBoxData textBoxData = new TextBox.TextBoxData();

        public TextButtonData(){
            super();

            width.setValue(Dim.px(244));
            height.setValue(Dim.px(90));

            texture.setValue(Tex.resource(UICommonResources.class, "button01_horizontal"));

            textBoxData.id.setValue("label");
            textBoxData.width.setValue(Dim.fill());
            textBoxData.height.setValue(Dim.fill());
            textBoxData.font.setValue(Font.resource(FontHelper.class, "buttonLabelFont"));
            textBoxData.fontSize.setValue(12f);
            //TODO set textbox font padding
            children.add(textBoxData);
        }

        @Override
        public TextButton makeUIElement_internal() {
            return new TextButton(this);
        }
    }
}