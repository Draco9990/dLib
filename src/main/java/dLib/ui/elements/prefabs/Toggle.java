package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.modcompat.ModManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import sayTheSpire.Output;

import java.io.Serializable;

public class Toggle extends Interactable {
    //region Variables

    private boolean toggled = false;

    private Texture toggledTexture;
    public Texture toggledHoveredTexture;
    public Texture toggledDisabledTexture;

    //endregion

    //region Constructors

    public Toggle(Texture image, Texture toggledTexture, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
        this.toggledTexture = toggledTexture;
    }

    public Toggle(ToggleData data){
        super(data);

        this.toggled = data.isToggled;

        this.toggledTexture = data.toggledTexture.getBoundTexture();
        this.toggledHoveredTexture = data.toggledTexture.getBoundTexture();
        this.toggledDisabledTexture = data.toggledTexture.getBoundTexture();
    }


    //endregion

    //region Methods

    //region Render Texture & Color

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

    //endregion

    //region Left Click

    @Override
    public void clickLeft() {
        toggle();
    }

    //endregion

    //region Toggled State

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

    //endregion

    //endregion

    public static class ToggleData extends Interactable.InteractableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public boolean isToggled;

        public TextureBinding toggledTexture = new TextureEmptyBinding();
        public TextureBinding toggledHoveredTexture = new TextureEmptyBinding();
        public TextureBinding toggledDisabledTexture = new TextureEmptyBinding();

        @Override
        public Toggle makeUIElement() {
            return new Toggle(this);
        }
    }
}
