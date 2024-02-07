package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.data.UIElementData;

import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class UIElement {
    /** Variables */
    protected String ID;

    protected int x = 0;
    protected int y = 0;

    protected int width = 0;
    protected int height = 0;

    private BiConsumer<Integer, Integer> positionChangedConsumer;

    /** Constructors */
    public UIElement(int xPos, int yPos, int width, int height){
        this.ID = "UIElement_" + UUID.randomUUID();
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

    /** Update and render */
    public abstract void update();
    public abstract void render(SpriteBatch sb);

    protected boolean shouldUpdate(){
        return isActive() && isEnabled() && isVisible();
    }
    protected boolean shouldRender(){
        return isActive() && isVisible();
    }

    /** Position */
    public UIElement setPositionX(int newPosX){
        setPosition(newPosX, y);
        return this;
    }
    public UIElement setPositionY(int newPosY){
        setPosition(x, newPosY);
        return this;
    }
    public UIElement setPosition(int newPosX, int newPosY){
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

    public UIElement offsetX(int xOffset){
        offset(xOffset, 0);
        return this;
    }
    public UIElement offsetY(int yOffset){
        offset(0, yOffset);
        return this;
    }
    public UIElement offset(int xOffset, int yOffset){
        setPosition(x + xOffset, y+yOffset);
        return this;
    }

    public void onPositionChanged(int newPosX, int newPosY){
        if(positionChangedConsumer != null){
            positionChangedConsumer.accept(newPosX, newPosY);
        }
    } //* Callback
    public UIElement setOnPositionChangedConsumer(BiConsumer<Integer, Integer> consumer){
        positionChangedConsumer = consumer;
        return this;
    }

    /** ID */
    public void setID(String newId){
        this.ID = newId;
    }
    public String getId(){
        return ID;
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
    protected abstract void setVisibility(boolean visible);
    public abstract boolean isVisible();

    /** Enabled */
    public void disable(){
        setEnabled(false);
    }
    public void enable(){
        setEnabled(true);
    }
    protected abstract void setEnabled(boolean enabled);
    public abstract boolean isEnabled();

    /** Active */
    public void hideAndDisable(){
        hide();
        disable();
    }
    public void showAndEnable(){
        show();
        enable();
    }
    public abstract boolean isActive();
}
