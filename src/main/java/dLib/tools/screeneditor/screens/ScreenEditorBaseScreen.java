package dLib.tools.screeneditor.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.DLib;
import dLib.tools.screeneditor.screens.menu.ScreenEditorMenuScreen;
import dLib.tools.screeneditor.screens.preview.ScreenEditorPreviewScreen;
import dLib.tools.screeneditor.screens.toolbar.ScreenEditorElementListScreen;
import dLib.tools.screeneditor.screens.toolbar.ScreenEditorPropertiesScreen;
import dLib.tools.screeneditor.screens.toolbar.ScreenEditorToolbarScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.tools.screeneditor.ui.items.preview.renderable.BackgroundScreenEditorItem;
import dLib.tools.screeneditor.util.ScreenEditorActiveItemsManager;
import dLib.tools.screeneditor.util.ScreenEditorSaveManager;
import dLib.ui.data.AbstractScreenData;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UITheme;
import dLib.util.bindings.texture.TextureEmptyBinding;

import java.util.ArrayList;

public class ScreenEditorBaseScreen extends AbstractScreen {
    /** Singleton */
    public static ScreenEditorBaseScreen instance;

    /** Variables */
    private String editingScreen;

    private ScreenEditorMenuScreen menu;

    private ScreenEditorPreviewScreen preview;

    private ScreenEditorToolbarScreen toolbar;
    private ScreenEditorPropertiesScreen properties;
    private ScreenEditorElementListScreen elementListScreen;

    private ScreenEditorSaveManager saveManager;
    private ScreenEditorActiveItemsManager activeItemsManager;

    /** Constructors */
    public ScreenEditorBaseScreen(String editingClass){
        initialize(editingClass);
    }

    public ScreenEditorBaseScreen(AbstractScreenData initialData){
        initialize(editingScreen);

        for(ScreenEditorItem item : initialData.getEditorItems()){
            preview.addPreviewItem(item);
        }

        activeItemsManager.clearActiveItems();
    }

    private void initialize(String editingClass){
        this.editingScreen = editingClass;

        addChildNCS(new Renderable(UITheme.whitePixel, 0, 0, getWidth(), getHeight()).setRenderColor(Color.valueOf("#151515FF")));

        menu = new ScreenEditorMenuScreen();

        preview = new ScreenEditorPreviewScreen();

        toolbar = new ScreenEditorToolbarScreen(){
            @Override
            public void onElementToAddChosen(Class<? extends ScreenEditorItem> previewItem) {
                super.onElementToAddChosen(previewItem);
                preview.makeNewPreviewItem(previewItem);
            }
        };
        properties = new ScreenEditorPropertiesScreen();
        elementListScreen = new ScreenEditorElementListScreen();

        saveManager = new ScreenEditorSaveManager();
        activeItemsManager = new ScreenEditorActiveItemsManager();

        instance = this;

        preview.makeNewPreviewItem(BackgroundScreenEditorItem.class).setID("Background");
    }

    /** Update and Render */
    @Override
    public void update() {
        menu.update();

        preview.update();

        toolbar.update();
        properties.update();
        elementListScreen.update();

        activeItemsManager.update();

        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        preview.render(sb);

        menu.render(sb);

        toolbar.render(sb);
        properties.render(sb);
        elementListScreen.render(sb);
    }

    /** Editing Screen cache */
    public ScreenEditorBaseScreen setEditingScreen(String screen){
        editingScreen = screen;
        return this;
    }

    public String getEditingScreen(){
        return editingScreen;
    }

    /** Managers */
    public ScreenEditorSaveManager getSaveManager(){
        return saveManager;
    }
    public ScreenEditorActiveItemsManager getActiveItemsManager(){
        return activeItemsManager;
    }

    /** Preview */
    public ScreenEditorPreviewScreen getPreviewScreen(){
        return preview;
    }

    /** Toolbar */
    public ScreenEditorToolbarScreen getToolbarScreen(){
        return toolbar;
    }
    public ScreenEditorPropertiesScreen getPropertiesScreen() { return properties; }
    public ScreenEditorElementListScreen getElementListScreen(){
        return elementListScreen;
    }

    /** Management */
    public void hideAllToolbarItems(){
        toolbar.hide();
        properties.hide();
        elementListScreen.hide();
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
