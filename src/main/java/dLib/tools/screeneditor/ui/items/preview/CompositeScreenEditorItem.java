package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.data.CompositeUIElementData;
import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Renderable;
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
        //Set position can get called within set position, so we have to do this.
        ArrayList<IntVector2> itemPositions = new ArrayList<>();
        for(ScreenEditorItem item : items){
            itemPositions.add(new IntVector2(item.getPositionX(), item.getPositionY()));
        }

        int oldX = x;
        int oldY = y;

        super.setPosition(newPosX, newPosY);

        int diffX = x - oldX;
        int diffY = y - oldY;

        for (int i = 0; i < items.size(); i++) {
            ScreenEditorItem item = items.get(i);
            item.setPosition(itemPositions.get(i).x + diffX, itemPositions.get(i).y + diffY);
        }

        return this;
    }

    @Override
    public CompositeScreenEditorItem setDimensions(Integer newWidth, Integer newHeight) {
        super.setDimensions(newWidth, newHeight);
        // if composite elements are ever available for user adding, implement this with properties
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

    /** Items */
    public void addItemToComposite(ScreenEditorItem item){
        items.add(item);
    }

    /** Properties */
    @Override
    public ArrayList<Setting<?>> getPropertiesForItem() {
        ArrayList<Setting<?>> settings = super.getPropertiesForItem();
        for(ScreenEditorItem editorItem : items){
            settings.addAll(editorItem.getPropertiesForItem());
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
