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
    private Texture toggledHoveredTexture;
    private Texture toggledDisabledTexture;

    private Color toggledColor = Color.BLACK.cpy();
    private float toggledColorMultiplier = 0.15f;

    private Color toggledHoveredColor = Color.BLACK.cpy();
    private float toggledHoveredColorMultiplier = 0.25f;

    private Color toggledDisabledColor = Color.WHITE.cpy();
    private float toggledDisabledColorMultiplier = 0.25f;

    //endregion

    //region Constructors

    public Toggle(Texture image, int xPos, int yPos, int width, int height){
        this(image, null, xPos, yPos, width, height);
    }

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

        this.toggledColor = Color.valueOf(data.toggledColor);
        this.toggledColorMultiplier = data.toggledColorMultiplier;

        this.toggledHoveredColor = Color.valueOf(data.toggledHoveredColor);
        this.toggledHoveredColorMultiplier = data.toggledHoveredColorMultiplier;

        this.toggledDisabledColor = Color.valueOf(data.toggledDisabledColor);
        this.toggledDisabledColorMultiplier = data.toggledDisabledColorMultiplier;
    }


    //endregion

    //region Methods

    //region Render Texture & Color

    @Override
    protected Texture getTextureForRender() {
        if(toggled){
            if(!isEnabled() && toggledDisabledTexture != null) return toggledDisabledTexture;
            if(isHovered() && toggledHoveredTexture != null) return toggledHoveredTexture;
            if(toggledTexture != null) return toggledTexture;
        }
        return super.getTextureForRender();
    }
    @Override
    protected Color getColorForRender() {
        if(isToggled()){
            Color colorToRender = super.getColorForRender().cpy();
            if(!isEnabled()){
                if(toggledDisabledTexture == null){
                    colorToRender = toggledDisabledColor.cpy();
                    if(toggledDisabledColorMultiplier != 1.0f){
                        colorToRender = colorToRender.lerp(super.getColorForRender(), 1 - toggledDisabledColorMultiplier);
                    }
                }
            }
            else if(isHovered()){
                if(toggledHoveredTexture == null){
                    colorToRender = toggledHoveredColor.cpy();
                    if(toggledHoveredColorMultiplier != 1.0f){
                        colorToRender = colorToRender.lerp(super.getColorForRender(), 1 - toggledHoveredColorMultiplier);
                    }
                }
            }
            return colorToRender.lerp(toggledColor, toggledColorMultiplier);
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

        private String toggledColor = Color.BLACK.toString();
        private float toggledColorMultiplier = 0.15f;

        public String toggledHoveredColor = Color.BLACK.toString();
        public float toggledHoveredColorMultiplier = 0.25f;

        public String toggledDisabledColor = Color.WHITE.toString();
        public float toggledDisabledColorMultiplier = 0.25f;

        @Override
        public Toggle makeUIElement() {
            return new Toggle(this);
        }
    }
}
