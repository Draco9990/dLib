package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.elements.implementations.Renderable;

import java.util.UUID;

public abstract class UIElement {
    /** Variables */
    protected String ID;

    protected int x = 0;
    protected int y = 0;

    /** Constructors */
    public UIElement(int xPos, int yPos){
        this.ID = "UIElement_" + UUID.randomUUID();
        this.x = xPos;
        this.y = yPos;
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

    public void onPositionChanged(int newPosX, int newPosY){ } //* Callback

    /** ID */
    public void setID(String newId){
        this.ID = newId;
    }
    public String getId(){
        return ID;
    }

    /** Width and height */
    public abstract int getWidth();
    public abstract int getHeight();

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
