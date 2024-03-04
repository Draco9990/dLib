package dLib.ui.data;

import dLib.DLib;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.screens.preview.ScreenEditorPreviewScreen;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.data.prefabs.BackgroundData;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.screens.AbstractScreen;
import dLib.util.DLibLogger;
import dLib.util.IntVector2;
import dLib.util.Reflection;

import java.io.*;
import java.util.ArrayList;

public class AbstractScreenData implements Serializable {
    private static final long serialVersionUID = 1L;

    public int offsetX;
    public int offsetY;

    public int referenceWidth;
    public int referenceHeight;

    public ArrayList<UIElementData> data = new ArrayList<>();

    public AbstractScreenData(){
        offsetX = ScreenEditorPreviewScreen.xOffset;
        offsetY = ScreenEditorPreviewScreen.yOffset;
        referenceWidth = ScreenEditorPreviewScreen.width;
        referenceHeight = ScreenEditorPreviewScreen.height;
    }

    /** Converters */
    public AbstractScreen makeLiveInstance(){
        //TODO this should use generated screen instead once IntelliJ integration is up
        AbstractScreen newScreen = new AbstractScreen() {
            @Override
            public String getModId() {
                return DLib.getModID();
            }
        };

        ArrayList<UIElement> elements = new ArrayList<>();
        for(UIElementData elementData : data){
            UIElement liveInstance = elementData.makeLiveInstance();
            if(liveInstance == null){
                DLibLogger.log("Failed to create a live instance of an element!");
                continue;
            }

            if(elementData instanceof BackgroundData){
                newScreen.background = (Renderable) liveInstance;
            }

            repositionElement(liveInstance, new IntVector2(0, 0));
            rescaleElement(liveInstance, new IntVector2(1920, 1080));
            elements.add(liveInstance);
        }

        for(UIElement element : elements){
            newScreen.addElement(element);
        }

        return newScreen;
    }
    public ScreenEditorBaseScreen makeEditorInstance(){
        ArrayList<ScreenEditorItem> items = new ArrayList<>();
        for(UIElementData itemData : data){
            ScreenEditorItem editorInstance = itemData.makeEditorInstance();
            if(editorInstance == null){
                DLibLogger.log("Failed to create an editor instance of element data.");
                continue;
            }

            repositionElement(editorInstance, new IntVector2(ScreenEditorPreviewScreen.xOffset, ScreenEditorPreviewScreen.yOffset));
            rescaleElement(editorInstance, new IntVector2(ScreenEditorPreviewScreen.width, ScreenEditorPreviewScreen.height));
            items.add(editorInstance);
        }

        return new ScreenEditorBaseScreen(items);
    }

    private void repositionElement(UIElement element, IntVector2 targetOffset){
        int xOffset = targetOffset.x - offsetX;
        int yOffset = targetOffset.y - offsetY;

        element.offset(xOffset, yOffset);
    }

    private void rescaleElement(UIElement element, IntVector2 targetResolution){
        float scaleMultX = (float)targetResolution.x / referenceWidth;
        float scaleMultY = (float)targetResolution.y / referenceHeight;

        element.setPosition((int) (element.getPositionX() * scaleMultX), (int) (element.getPositionY() * scaleMultY));
        element.setDimensions((int) (element.getWidth() * scaleMultX), (int) (element.getHeight() * scaleMultY));
    }

    /** Serialization */
    public void serialize(String filePath){
        try (FileOutputStream file = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(this);
        }catch (Exception e){
            DLibLogger.log("Failed to serialize screen data due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    public static AbstractScreenData deserialize(String filePath) {
        try (FileInputStream file = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(file)) {
            return (AbstractScreenData) in.readObject();
        }catch (Exception e){
            DLibLogger.log("Failed to deserialize screen data due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }
}
