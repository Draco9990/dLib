package dLib.tools.screeneditor.ui.items.preview.composite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.tools.screeneditor.ui.items.preview.CompositeScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.ImageScreenEditorItem;
import dLib.ui.data.UIElementData;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.prefabs.ListBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;

/// No need to inherit from CompositeScreenEditor item, we aren't treating ListBoxes as regular composites
public class ListBoxScreenEditorItem extends ScreenEditorItem {
    /** Variables */



    /** Constructors */
    public ListBoxScreenEditorItem(int xPos, int yPos, int width, int height) {
        super(UIThemeManager.getDefaultTheme().listbox, xPos, yPos, width, height);
    }

    public ListBoxScreenEditorItem(ListBoxData<?> data){
        super(data);
    }

    /** Data */
    @Override
    public ListBoxData<Object> makeElementData() {
        return new ListBoxData<Object>();
    }

    @Override
    public ListBoxData<Object> getElementData() {
        return (ListBoxData<Object>) super.getElementData();
    }

    @Override
    public void initializeElementData(UIElementData data) {
        super.initializeElementData(data);
        ListBoxData<Object> listBoxData = (ListBoxData<Object>) data;
    }

    /** Copy */
    public static ScreenEditorItem makeNewInstance(){
        return (ScreenEditorItem) new ListBoxScreenEditorItem(0, 0, 500, 500).setRenderColor(Color.LIGHT_GRAY);
    }

    @Override
    public Class<? extends UIElement> getLiveInstanceType() {
        return ListBox.class;
    }
}
