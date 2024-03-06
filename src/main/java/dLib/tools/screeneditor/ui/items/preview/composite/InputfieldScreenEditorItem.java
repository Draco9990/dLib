package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.CompositeScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ButtonScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ImageScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.TextBoxScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.InputfieldData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Inputfield;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureThemeBinding;

public class InputfieldScreenEditorItem extends CompositeScreenEditorItem {
    /** Variables */
    private ButtonScreenEditorItem background;
    private TextBoxScreenEditorItem textBox;

    /** Constructors */
    public InputfieldScreenEditorItem(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        background = new ButtonScreenEditorItem(new TextureThemeBinding("inputfield"), xPos, yPos, width, height);
        textBox = new TextBoxScreenEditorItem(new TextureEmptyBinding(), xPos, yPos, width, height);

        addItemToComposite(background);
        addItemToComposite(textBox);
    }

    public InputfieldScreenEditorItem(InputfieldData data){
        super(data);

        this.background = data.buttonData.makeEditorInstance();
        this.textBox = data.textboxData.makeEditorInstance();
    }

    /** Data */
    @Override
    public InputfieldData makeElementData() {
        return new InputfieldData();
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        InputfieldData inputfieldData = (InputfieldData) data;
        inputfieldData.buttonData = background.getElementData();
        inputfieldData.textboxData = textBox.getElementData();
    }

    @Override
    public InputfieldData getElementData() {
        return (InputfieldData) super.getElementData();
    }

    /** Copy */
    public static ScreenEditorItem makeNewInstance(){
        return new InputfieldScreenEditorItem(0, 0, 500, 75);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return Inputfield.class;
    }
}
