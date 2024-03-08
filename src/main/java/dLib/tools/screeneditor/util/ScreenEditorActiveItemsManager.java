package dLib.tools.screeneditor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.util.Reflection;

import java.util.ArrayList;

public class ScreenEditorActiveItemsManager {
    //region Variables
    private ScreenEditorBaseScreen screenEditor;

    private ArrayList<ScreenEditorItem> activeItems = new ArrayList<>();


    //endregion

    //region Constructors

    public ScreenEditorActiveItemsManager(ScreenEditorBaseScreen screenEditor){
        this.screenEditor = screenEditor;
    }

    //endregion

    //region Methods

    //region Update & Render

    public void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL)){
            for(ScreenEditorItem item : activeItems){
                screenEditor.getPreviewScreen().deletePreviewItem(item);
            }
            clearActiveItems();
        }
    }

    //endregion

    //region Active Item Management

    public void addActiveItem(ScreenEditorItem item){
        screenEditor.getPropertiesScreen().clearScreen();
        if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            clearActiveItems();
        }

        if(!activeItems.contains(item)) {
            activeItems.add(item);

            if(activeItems.size() == 1){
                screenEditor.getPropertiesScreen().createPropertiesFor(item);
            }
        }
    }
    public void clearActiveItems(){
        screenEditor.getPropertiesScreen().clearScreen();
        screenEditor.getElementListScreen().refreshItemList();
        activeItems.clear();
    }

    public boolean isItemActive(ScreenEditorItem item){
        return activeItems.contains(item);
    }

    //endregion

    //region Group Drag

    public void markAllForDrag(){
        for(ScreenEditorItem item : activeItems){
            item.setProxyDragged(true);
            Reflection.invokeMethod("onLeftClick", item);
            item.setProxyDragged(false);
        }
    }

    public void markAllForDragUpdate(float totalDuration){
        for(ScreenEditorItem item : activeItems){
            item.setProxyDragged(true);
            Reflection.invokeMethod("onLeftClickHeld", item, totalDuration);
            item.setProxyDragged(false);
        }
    }

    //endregion

    //endregion
}
