package dLib.tools.screeneditor.screens;

import com.badlogic.gdx.graphics.Color;
import dLib.DLib;
import dLib.tools.screeneditor.ui.items.editoritems.BackgroundScreenEditorItem;
import dLib.tools.screeneditor.ui.items.implementations.menu.ScreenEditorMenu;
import dLib.tools.screeneditor.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.tools.screeneditor.ui.items.implementations.toolbar.ScreenEditorElementList;
import dLib.tools.screeneditor.ui.items.implementations.toolbar.ScreenEditorElementProperties;
import dLib.tools.screeneditor.ui.items.implementations.toolbar.ScreenEditorToolbox;
import dLib.tools.screeneditor.ui.items.editoritems.ScreenEditorItem;
import dLib.tools.screeneditor.util.ScreenEditorActiveItemsManager;
import dLib.tools.screeneditor.util.ScreenEditorSaveManager;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.screens.GeneratedAbstractScreen;
import dLib.ui.themes.UITheme;

public class ScreenEditorBaseScreen extends AbstractScreen {
    //region Variables

    private String editingScreen;

    private ScreenEditorMenu menu;

    private ScreenEditorPreview preview;

    private ScreenEditorToolbox toolbar;
    private ScreenEditorElementProperties properties;
    private ScreenEditorElementList elementListScreen;

    private ScreenEditorSaveManager saveManager;
    private ScreenEditorActiveItemsManager activeItemsManager;

    //endregion

    //region Constructors

    public ScreenEditorBaseScreen(String editingClass){
        super();
        initialize(editingClass);
    }
    public ScreenEditorBaseScreen(GeneratedAbstractScreen.GeneratedScreenData initialData){
        super();
        initialize(initialData.screenClass);

        /*for(ScreenEditorItem item : initialData.getEditorItems()){
            preview.addPreviewItem(item);
        }*/

        activeItemsManager.clearActiveItems();
    }

    private void initialize(String editingClass){
        this.editingScreen = editingClass;

        addChildNCS(new Renderable(UITheme.whitePixel, 0, 0, getWidth(), getHeight()).setRenderColor(Color.valueOf("#151515FF")));

        menu = new ScreenEditorMenu();
        addChildNCS(menu);

        preview = new ScreenEditorPreview();
        addChildNCS(preview);

        toolbar = new ScreenEditorToolbox();
        addChildNCS(toolbar);
        properties = new ScreenEditorElementProperties();
        addChildNCS(properties);
        elementListScreen = new ScreenEditorElementList();
        addChildNCS(elementListScreen);

        saveManager = new ScreenEditorSaveManager(this);
        activeItemsManager = new ScreenEditorActiveItemsManager(this);

        preview.makeNewPreviewItem(BackgroundScreenEditorItem.class).setID("Background");
    }


    //endregion

    //region Methods
    //endregion

    /** Variables */

    /** Constructors */

    /** Update and Render */
    @Override
    public void updateSelf() {
        super.updateSelf();
        activeItemsManager.update();
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
    public ScreenEditorPreview getPreviewScreen(){
        return preview;
    }

    /** Toolbar */
    public ScreenEditorToolbox getToolbarScreen(){
        return toolbar;
    }
    public ScreenEditorElementProperties getPropertiesScreen() { return properties; }
    public ScreenEditorElementList getElementListScreen(){
        return elementListScreen;
    }

    /** Management */
    public void hideAllToolbarItems(){
        toolbar.hideAndDisable();
        properties.hideAndDisable();
        elementListScreen.hideAndDisable();
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
