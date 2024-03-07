package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.data.CompositeUIElementData;
import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.DLibLogger;
import dLib.util.IntVector2;
import dLib.util.settings.Setting;

import java.util.ArrayList;

public abstract class CompositeScreenEditorItem extends ScreenEditorItem {
    /** Variables */
    private ArrayList<ScreenEditorItem> items = new ArrayList<>();

    /** Constructors */
    public CompositeScreenEditorItem(int xPos, int yPos, int width, int height) {
        super(null, xPos, yPos, width, height);
    }

    public CompositeScreenEditorItem(CompositeUIElementData data){
        super(data);
    }

    /** Update & Render */
    @Override
    public void update() {
        super.update();

        for(int i = items.size() - 1; i >= 0; i--){
            items.get(i).update();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        for(ScreenEditorItem item : items) item.render(sb);

        super.render(sb);
    }

    /** Position & Dimensions */
    @Override
    public CompositeScreenEditorItem setPosition(Integer newPosX, Integer newPosY) {
        int oldX = x;
        int oldY = y;

        super.setPosition(newPosX, newPosY);

        int diffX = x - oldX;
        int diffY = y - oldY;

        for (int i = 0; i < items.size(); i++) {
            ScreenEditorItem item = items.get(i);
            //item.offset(diffX, diffY); //TODO: IDFK know what the problem is here, it should work but y axis doesn't. Fuck this. I'll just set it manually for now and come back to this when I have more will to do it.
            item.setPosition(x, y);
        }

        return this;
    }

    @Override
    public CompositeScreenEditorItem setDimensions(Integer newWidth, Integer newHeight) {
        int oldWidth = width;
        int oldHeight = height;

        super.setDimensions(newWidth, newHeight);

        float diffXPerc = (float)width / oldWidth;
        float diffYPerc = (float)height / oldHeight;

        for(int i = 0; i < items.size(); i++){
            ScreenEditorItem item = items.get(i);
            item.setDimensions((int) (item.getWidth() * diffXPerc), (int) (item.getHeight() * diffYPerc));
        }

        return this;
    }

    /** Bounds */
    @Override
    public Draggable setCanDragX(boolean canDragX) {
        super.setCanDragX(canDragX);
        for(ScreenEditorItem item : items) item.setCanDragX(canDragX);
        return this;
    }

    @Override
    public Draggable setCanDragY(boolean canDragY) {
        super.setCanDragY(canDragY);
        for(ScreenEditorItem item : items) item.setCanDragY(canDragY);
        return this;
    }

    @Override
    public Draggable setBoundsX(Integer lowerBound, Integer upperBound) {
        super.setBoundsX(lowerBound, upperBound);
        for(ScreenEditorItem item : items) item.setBoundsX(lowerBound, upperBound);
        return this;
    }

    @Override
    public Draggable setBoundsY(Integer lowerBound, Integer upperBound) {
        super.setBoundsY(lowerBound, upperBound);
        for(ScreenEditorItem item : items) item.setBoundsY(lowerBound, upperBound);
        return this;
    }

    /** Image */
    @Override
    public final Renderable setImage(Texture image) {
        return this; // Composites should NOT have images. It will only lead to visual errors in the editor.
    }

    /** ID */
    @Override
    public ScreenEditorItem setID(String newId) {
        super.setID(newId);
        for (int i = 0; i < items.size(); i++) {
            ScreenEditorItem item = items.get(i);
            item.setID(getId() + "_" + item.getClass().getSimpleName() + "_" + i);
        }

        return this;
    }

    /** Items */
    public void addItemToComposite(ScreenEditorItem item){
        items.add(item);
    }

    /** Properties */
    @Override
    public ArrayList<Setting<?>> getPropertiesForItem() {
        ArrayList<Setting<?>> settings = super.getPropertiesForItem();
        for(ScreenEditorItem editorItem : items){
            ArrayList<Setting<?>> itemSettings = editorItem.getPropertiesForItem();
            itemSettings.removeIf(element -> element.getTitle().equals("ID:")); //Remove all ID fields
            settings.addAll(itemSettings);
        }
        return settings;
    }

    /** Data */
    @Override
    public CompositeUIElementData makeElementData() {
        return new CompositeUIElementData();
    }

    @Override
    public CompositeUIElementData getElementData() {
        return (CompositeUIElementData) super.getElementData();
    }

    /** Settings */
    @Override
    public void initializeSettingsData() {
        super.initializeSettingsData();
        for(ScreenEditorItem item : items) item.initializeSettingsData();
    }
}
