package dLib.ui.data;

import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.tools.screeneditor.ui.items.preview.ScreenEditorItem;
import dLib.ui.elements.UIElement;
import dLib.util.DLibLogger;
import dLib.util.IntVector2;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public class AbstractScreenData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String screenClass;

    public int offsetX;
    public int offsetY;

    public int referenceWidth;
    public int referenceHeight;

    public ArrayList<UIElementData> data = new ArrayList<>();

    public AbstractScreenData(ScreenEditorBaseScreen instance){
        offsetX = ScreenEditorPreview.xOffset;
        offsetY = ScreenEditorPreview.yOffset;
        referenceWidth = ScreenEditorPreview.width;
        referenceHeight = ScreenEditorPreview.height;

        screenClass = instance.getEditingScreen();

        for(ScreenEditorItem item : instance.getPreviewScreen().getPreviewItems()){
            data.add(item.getElementData());
        }
    }

    /** Converters */
    public ArrayList<UIElement> makeLiveItems(){
        ArrayList<UIElement> elements = new ArrayList<>();
        for(UIElementData elementData : data){
            UIElement liveInstance = elementData.makeLiveInstance();
            if(liveInstance == null){
                DLibLogger.log("Failed to create a live instance of an element!");
                continue;
            }

            repositionElement(liveInstance, new IntVector2(0, 0));
            rescaleElement(liveInstance, new IntVector2(1920, 1080));
            elements.add(liveInstance);
        }

        return elements;
    }

    public ArrayList<ScreenEditorItem> getEditorItems(){
        ArrayList<ScreenEditorItem> items = new ArrayList<>();
        for(UIElementData itemData : data){
            ScreenEditorItem editorInstance = itemData.makeEditorInstance();
            if(editorInstance == null){
                DLibLogger.log("Failed to create an editor instance of element data.");
                continue;
            }

            repositionElement(editorInstance, new IntVector2(ScreenEditorPreview.xOffset, ScreenEditorPreview.yOffset));
            rescaleElement(editorInstance, new IntVector2(ScreenEditorPreview.width, ScreenEditorPreview.height));

            items.add(editorInstance);
        }

        return items;
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
    // Method to serialize the object to a string
    public String serializeToString(){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(this);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
        catch (Exception e){
            DLibLogger.logError("Failed to serialize AbstractScreenData due to "+ e.getLocalizedMessage());
            e.printStackTrace();
        }

        return "";
    }

    // Method to deserialize the object from a string
    public static AbstractScreenData deserializeFromString(String s){
        byte[] data = Base64.getDecoder().decode(s);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (AbstractScreenData) ois.readObject();
        }catch (Exception e){
            DLibLogger.log("Failed to deserialize AbstractScreenData due to " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }
}
