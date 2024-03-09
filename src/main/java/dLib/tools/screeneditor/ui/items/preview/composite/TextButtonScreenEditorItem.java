package dLib.tools.screeneditor.ui.items.preview.composite;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.CompositeScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ButtonScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.TextBoxScreenEditorItem;
import dLib.ui.data.CompositeUIElementData;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.TextButtonData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.ui.elements.prefabs.TextButton;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

public class TextButtonScreenEditorItem extends CompositeScreenEditorItem {
    /** Variables */
    private ButtonScreenEditorItem button;
    private TextBoxScreenEditorItem textBox;

    /** Constructors */
    public TextButtonScreenEditorItem(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        button = new ButtonScreenEditorItem(new TextureThemeBinding("button_small"), xPos, yPos, width, height);
        textBox = new TextBoxScreenEditorItem(new TextureEmptyBinding(), xPos, yPos, width, height);
        textBox.setText("");

        addItemToComposite(button);
        addItemToComposite(textBox);
    }

    public TextButtonScreenEditorItem(TextButtonData data){
        super(null); //TODO RF fix

        this.button = data.buttonData.makeEditorInstance();
        this.textBox = data.textBoxData.makeEditorInstance();
    }

    /** Data */
    @Override
    public CompositeUIElementData makeElementData() {
        return null; //TODO RF fix
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        TextButtonData tData = (TextButtonData) data;
        tData.buttonData = button.getElementData();
        tData.textBoxData = textBox.getElementData();
    }

    @Override
    public CompositeUIElementData getElementData() {
        return null; //TODO RF fix
    }

    /** Instancing */
    public static TextButtonScreenEditorItem makeNewInstance(ScreenEditorBaseScreen screenEditor){
        return new TextButtonScreenEditorItem(0, 0, 75, 75);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return TextButton.class;
    }
}
