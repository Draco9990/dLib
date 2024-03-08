package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.data.UIElementData;
import dLib.util.IntVector2;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class UIElement {
    /** Variables */
    //region Id
    protected String ID;
    //endregion

    //region Parent & Children
    protected UIElement parent;
    protected UIElement[] children;
    //endregion

    //region Position
    protected IntVector2 localPosition = new IntVector2(0, 0);
    //endregion

    //region Dimensions
    protected int width = 0;
    protected int height = 0;
    //endregion

    /** DEPRECATED */
    protected int x = 0;
    protected int y = 0;

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    private ArrayList<BiConsumer<Integer, Integer>> positionChangedConsumers = new ArrayList<>();

    /** Constructors */
    public UIElement(int xPos, int yPos, int width, int height){
        this.ID = getClass().getSimpleName() + "_" + UUID.randomUUID().toString().replace("-", "");
        this.x = xPos;
        this.y = yPos;
        this.width = width;
        this.height = height;
    }

    public UIElement(UIElementData data){
        this.ID = data.ID;
        this.x = data.x;
        this.y = data.y;
        this.width = data.width;
        this.height = data.height;
    }

    /** Class Methods */
    //region Update & Render
    public abstract void update();
    public abstract void render(SpriteBatch sb);

    protected boolean shouldUpdate(){
        return isActive() && isEnabled() && isVisible();
    }
    protected boolean shouldRender(){
        return isActive() && isVisible();
    }
    //endregion

    //region Id
    public UIElement setID(String newId){
        this.ID = newId;
        return this;
    }
    public String getId(){
        return ID;
    }
    //endregion

    //region Position

    //region Local Position
    public UIElement setLocalPositionX(int newPosition){
        return setLocalPosition(newPosition, y);
    }
    public UIElement setLocalPositionY(int newPosition){
        return setLocalPosition(x, newPosition);
    }
    public UIElement setLocalPosition(int newPositionX, int newPositionY){
        int xDiff = newPositionX - x;
        int yDiff = newPositionY - y;

        this.x = newPositionX;
        this.y = newPositionY;

        if(xDiff != 0 || yDiff != 0){
            onPositionChanged(xDiff, yDiff);
        }

        return this;
    }

    public final int getLocalPositionX(){
        return localPosition.x;
    }
    public final int getLocalPositionY(){
        return localPosition.y;
    }
    public final IntVector2 getLocalPosition(){
        return localPosition.copy();
    }
    //endregion

    //region World Position
    public UIElement setWorldPositionX(int newPos){
        return setWorldPosition(newPos, getWorldPositionY());
    }
    public UIElement setWorldPositionY(int newPos){
        return setWorldPosition(getWorldPositionX(), newPos);
    }
    public UIElement setWorldPosition(int newPosX, int newPosY){
        int xDiff = newPosX - getWorldPositionX();
        int yDiff = newPosY - getWorldPositionY();
        offset(xDiff, yDiff);
        return this;
    }

    public final int getWorldPositionX(){
        return getWorldPosition().x;
    }
    public final int getWorldPositionY(){
        return getWorldPosition().y;
    }
    public final IntVector2 getWorldPosition(){
        if(parent == null) return getLocalPosition();
        else{
            IntVector2 parentWorld = parent.getWorldPosition();
            parentWorld.x += getLocalPositionX();
            parentWorld.y += getLocalPositionY();
            return parentWorld;
        }
    }

    public UIElement setWorldPositionCenteredX(int newPos){
        return setWorldPositionCentered(newPos, getWorldPositionCenteredY());
    }
    public UIElement setWorldPositionCenteredY(int newPos){
        return setWorldPositionCentered(getWorldPositionCenteredX(), newPos);
    }
    public UIElement setWorldPositionCentered(int newPosX, int newPosY){
        int wHalf = (int)(width * 0.5f);
        int hHalf = (int)(height * 0.5f);
        return setWorldPosition(newPosX - wHalf, newPosY - hHalf);
    }

    public final int getWorldPositionCenteredX(){
        return getWorldPositionCentered().x;
    }
    public final int getWorldPositionCenteredY(){
        return getWorldPositionCentered().y;
    }
    public final IntVector2 getWorldPositionCentered(){
        IntVector2 worldPosition = getWorldPosition();
        worldPosition.x += (int)(width * 0.5f);
        worldPosition.y += (int)(height * 0.5f);
        return worldPosition;
    }
    //endregion

    //region Offset
    public UIElement offsetX(int xOffset){
        offset(xOffset, 0);
        return this;
    }
    public UIElement offsetY(int yOffset){
        offset(0, yOffset);
        return this;
    }
    public UIElement offset(int xOffset, int yOffset){
        setLocalPosition(getLocalPositionX() + xOffset, getLocalPositionY() + yOffset);
        return this;
    }
    //endregion

    //endregion

    /** Position */
    public UIElement setPositionX(int newPosX){
        setPosition(newPosX, y);
        return this;
    }
    public UIElement setPositionY(int newPosY){
        setPosition(x, newPosY);
        return this;
    }
    public UIElement setPosition(Integer newPosX, Integer newPosY){
        boolean positionDifferent = x != newPosX || y != newPosY;

        x = newPosX;
        y = newPosY;

        if(positionDifferent) {
            onPositionChanged(x, y);
        }

        return this;
    }

    public UIElement setCenterPositionX(int newPosX){
        setPositionX(newPosX - ((int)(float)getWidth() / 2));
        return this;
    }
    public UIElement setCenterPositionY(int newPosY){
        setPositionY(newPosY - ((int)(float)getHeight() / 2));
        return this;
    }
    public UIElement setCenterPosition(int newPosX, int newPosY){
        setPosition(newPosX - ((int)(float)getWidth() / 2), newPosY - ((int)(float)getHeight() / 2));
        return this;
    }

    public int getPositionX() { return x; }
    public int getPositionY() { return y; }

    public void onPositionChanged(int newPosX, int newPosY){
        for(BiConsumer<Integer, Integer> consumer : positionChangedConsumers) consumer.accept(newPosX, newPosY);
    } //* Callback
    public UIElement addOnPositionChangedConsumer(BiConsumer<Integer, Integer> consumer){
        positionChangedConsumers.add(consumer);
        return this;
    }

    /** Width and height */
    public UIElement setWidth(int newWidth){
        return setDimensions(newWidth, height);
    }
    public UIElement setHeight(int newHeight){
        return setDimensions(width, newHeight);
    }
    public UIElement setDimensions(Integer newWidth, Integer newHeight){
        if(newWidth < 1) newWidth = 1;
        if(newHeight < 1) newHeight = 1;

        this.width = newWidth;
        this.height = newHeight;
        return this;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    /** Visibility */
    public void hide(){
        setVisibility(false);
    }
    public void show(){
        setVisibility(true);
    }
    protected void setVisibility(boolean visible){
        isVisible = visible;
    }
    public boolean isVisible(){
        return isVisible;
    }

    /** Enabled */
    public void disable(){
        setEnabled(false);
    }
    public void enable(){
        setEnabled(true);
    }
    protected void setEnabled(boolean enabled){
        isEnabled = enabled;
    }
    public boolean isEnabled(){
        return isEnabled;
    }

    /** Active */
    public void hideAndDisable(){
        hide();
        disable();
    }
    public void showAndEnable(){
        show();
        enable();
    }
    public boolean isActive(){
        return isVisible() || isEnabled();
    }

    /** Children */
}
