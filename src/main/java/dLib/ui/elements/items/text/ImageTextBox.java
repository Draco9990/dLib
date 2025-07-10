package dLib.ui.elements.items.text;

import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.string.interfaces.ITextProvider;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class ImageTextBox extends Renderable implements ITextProvider {
    public TextBox textBox;

    public ImageTextBox(String text) {
        super(Tex.stat(UICommonResources.inputfield));
        init(text);
    }
    public ImageTextBox(String text, AbstractPosition xPos, AbstractPosition yPos) {
        super(Tex.stat(UICommonResources.inputfield), xPos, yPos);
        init(text);
    }
    public ImageTextBox(String text, AbstractDimension width, AbstractDimension height) {
        super(Tex.stat(UICommonResources.inputfield), width, height);
        init(text);
    }
    public ImageTextBox(String text, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(Tex.stat(UICommonResources.inputfield), xPos, yPos, width, height);
        init(text);
    }

    private void init(String text){
        textBox = new TextBox(text, Pos.px(0), Pos.px(0), getWidthRaw() instanceof AutoDimension ? Dim.auto() : Dim.fill(), getHeightRaw() instanceof AutoDimension ? Dim.auto() : Dim.fill());
        textBox.setPadding(Padd.px(10));
        addChild(textBox);

        setOnHoverLine(Str.src(textBox));
    }

    //region Methods

    //region ITextProvider implementation

    @Override
    public String getText() {
        return textBox.getText();
    }

    //endregion

    //endregion
}
