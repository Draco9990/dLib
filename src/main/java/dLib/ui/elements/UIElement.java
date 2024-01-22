package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.UUID;

public abstract class UIElement {

    protected String ID;

    public UIElement(){
        this.ID = "UIElement_" + UUID.randomUUID();
    }

    public abstract void update();
    public abstract void render(SpriteBatch sb);

    public void setID(String newId){
        this.ID = newId;
    }

    public String getId(){
        return ID;
    }

    public void hide(){
        setVisibility(false);
    }
    public void show(){
        setVisibility(true);
    }
    protected abstract void setVisibility(boolean visible);
    public abstract boolean isVisible();

    public void disable(){
        setEnabled(false);
    }
    public void enable(){
        setEnabled(true);
    }
    protected abstract void setEnabled(boolean enabled);
    public abstract boolean isEnabled();

    public void hideAndDisable(){
        hide();
        disable();
    }
    public void showAndEnable(){
        show();
        enable();
    }
    public abstract boolean isActive();

    protected boolean shouldUpdate(){
        return isActive() && isEnabled() && isVisible();
    }
    protected boolean shouldRender(){
        return isActive() && isVisible();
    }
}
