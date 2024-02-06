package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.modcompat.ModManager;
import dLib.ui.data.prefabs.ToggleData;
import dLib.ui.elements.implementations.Interactable;
import dLib.util.TextureManager;
import sayTheSpire.Output;

public class Toggle extends Interactable {
    private boolean toggled = false;

    private Texture toggledTexture;
    public Texture toggledHoveredTexture;
    public Texture toggledDisabledTexture;

    public Toggle(Texture image, Texture toggledTexture) {
        super(image);
        this.toggledTexture = toggledTexture;
    }

    public Toggle(Texture image, Texture toggledTexture, int xPos, int yPos) {
        super(image, xPos, yPos);
        this.toggledTexture = toggledTexture;
    }

    public Toggle(Texture image, Texture toggledTexture, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
        this.toggledTexture = toggledTexture;
    }

    public Toggle(ToggleData data){
        super(data);

        this.toggledTexture = TextureManager.getTexture(data.toggledTexturePath);
        this.toggledHoveredTexture = TextureManager.getTexture(data.toggledHoveredTexturePath);
        this.toggledDisabledTexture = TextureManager.getTexture(data.toggledDisabledTexturePath);

        this.toggled = data.isToggled;
    }

    /** Builders */

    public Toggle setToggled(boolean toggled){
        this.toggled = toggled;

        return this;
    }
    public Interactable setToggledHoveredTexture(Texture hoveredTexture){
        this.toggledHoveredTexture = hoveredTexture;
        return this;
    }
    public Interactable setToggledDisabledTexture(Texture disabledTexture){
        this.toggledDisabledTexture = disabledTexture;
        return this;
    }


    /** Update and render */

    @Override
    protected Texture getTextureForRender() {
        if(toggled){
            if(!isEnabled() && toggledDisabledTexture != null) return toggledDisabledTexture;
            if(isHovered() && toggledHoveredTexture != null) return toggledHoveredTexture;
            return toggledTexture;
        }
        return super.getTextureForRender();
    }
    @Override
    protected Color getColorForRender() {
        if(toggled){
            if(!isEnabled()){
                if(toggledDisabledTexture == null){
                    return Color.LIGHT_GRAY;
                }
            }
            else if(isHovered()){
                if(toggledHoveredTexture == null){
                    return Color.LIGHT_GRAY;
                }
            }
        }
        return super.getColorForRender();
    }

    /** Misc methods */

    @Override
    public void clickLeft() {
        toggle();
    }
    public void toggle(){
        this.toggled = !toggled;

        if(ModManager.SayTheSpire.isActive()){
            if(onTriggeredLine != null){
                Output.text(onTriggeredLine, true);
            }
        }
    }
    public boolean isToggled(){
        return toggled;
    }
}
