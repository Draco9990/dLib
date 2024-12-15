package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.modcompat.ModManager;
import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.TextureBindingProperty;
import dLib.util.bindings.texture.TextureBinding;
import dLib.util.bindings.texture.TextureEmptyBinding;
import dLib.util.bindings.texture.TextureNullBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import sayTheSpire.Output;

import java.io.Serializable;

public class Toggle extends Interactable {
    //region Variables

    private boolean toggled = false;

    private TextureRegion toggledTexture;
    private TextureRegion toggledHoveredTexture;
    private TextureRegion toggledDisabledTexture;

    private Color toggledColor = Color.BLACK.cpy();
    private float toggledColorMultiplier = 0.15f;

    private Color toggledHoveredColor = Color.BLACK.cpy();
    private float toggledHoveredColorMultiplier = 0.25f;

    private Color toggledDisabledColor = Color.WHITE.cpy();
    private float toggledDisabledColorMultiplier = 0.25f;

    //endregion

    //region Constructors

    public Toggle(Texture image, AbstractPosition xPos, AbstractPosition yPos){
        this(image, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Toggle(Texture image, AbstractDimension width, AbstractDimension height){
        this(image, Pos.px(0), Pos.px(0), width, height);
    }
    public Toggle(Texture image, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        this(image, null, xPos, yPos, width, height);
    }
    public Toggle(Texture image, Texture toggledTexture, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(image, xPos, yPos, width, height);
        this.toggledTexture = toggledTexture != null ? new TextureRegion(toggledTexture) : null;
    }

    public Toggle(ToggleData data){
        super(data);

        this.toggled = data.isToggled.getValue();

        Texture toggledTextureT = data.toggledTexture.getValue().getBoundObject();
        Texture toggledHoveredTextureT = data.toggledHoveredTexture.getBoundObject();
        Texture toggledDisabledTextureT = data.toggledDisabledTexture.getBoundObject();
        this.toggledTexture = toggledTextureT == null ? null : new TextureRegion(toggledTextureT);
        this.toggledHoveredTexture = toggledHoveredTextureT == null ? null : new TextureRegion(toggledHoveredTextureT);
        this.toggledDisabledTexture = toggledDisabledTextureT == null ? null : new TextureRegion(toggledDisabledTextureT);

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
    protected TextureRegion getTextureForRender() {
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
            else if(toggledTexture == null){
                return colorToRender.lerp(toggledColor, toggledColorMultiplier);
            }
        }

        return super.getColorForRender();
    }

    //endregion

    //region Left Click

    @Override
    public void clickLeft() {
        toggle();

        super.clickLeft();
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
        if(hoveredTexture == null) this.toggledHoveredTexture = null;
        else this.toggledHoveredTexture = new TextureRegion(hoveredTexture);
        return this;
    }
    public Interactable setToggledDisabledTexture(Texture disabledTexture){
        if(disabledTexture == null) this.toggledDisabledTexture = null;
        else this.toggledDisabledTexture = new TextureRegion(disabledTexture);
        return this;
    }

    //endregion

    //endregion

    public static class ToggleData extends Interactable.InteractableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public BooleanProperty isToggled = new BooleanProperty(false).setName("Toggled");

        public TextureBindingProperty toggledTexture = new TextureBindingProperty(new TextureNullBinding()).setName("Toggled Image");
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
