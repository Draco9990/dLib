package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import dLib.modcompat.ModManager;
import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.TextureBindingProperty;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import sayTheSpire.Output;

import java.io.Serializable;

public class Toggle extends Interactable {
    //region Variables

    private boolean toggled = false;

    private AbstractTextureBinding toggledTexture;
    private AbstractTextureBinding toggledHoveredTexture;
    private AbstractTextureBinding toggledDisabledTexture;

    private Color toggledColor = Color.BLACK.cpy();
    private float toggledColorMultiplier = 0.25f;

    private Color toggledHoveredColor = Color.BLACK.cpy();
    private float toggledHoveredColorMultiplier = 0.35f;

    private Color toggledDisabledColor = Color.WHITE.cpy();
    private float toggledDisabledColorMultiplier = 0.25f;

    public ConsumerEvent<Boolean> postToggledEvent = new ConsumerEvent<>();                                             public static BiConsumerEvent<Boolean, Toggle> postToggledEvent_Static = new BiConsumerEvent<>();

    //endregion

    //region Constructors

    public Toggle(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos){
        this(imageBinding, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Toggle(AbstractTextureBinding imageBinding, AbstractDimension width, AbstractDimension height){
        this(imageBinding, Pos.px(0), Pos.px(0), width, height);
    }
    public Toggle(AbstractTextureBinding imageBinding, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        this(imageBinding, null, xPos, yPos, width, height);
    }
    public Toggle(AbstractTextureBinding imageBinding, AbstractTextureBinding toggledTexture, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(imageBinding, xPos, yPos, width, height);
        this.toggledTexture = toggledTexture;
    }

    public Toggle(ToggleData data){
        super(data);

        this.toggled = data.isToggled.getValue();

        this.toggledTexture = data.toggledTexture.getValue();
        this.toggledHoveredTexture = data.toggledHoveredTexture.getValue();
        this.toggledDisabledTexture = data.toggledDisabledTexture.getValue();

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
    protected NinePatch getTextureForRender() {
        if(toggled){
            if(!isEnabled() && toggledDisabledTexture != null) return toggledDisabledTexture.getBoundObject();
            if(isHovered() && toggledHoveredTexture != null) return toggledHoveredTexture.getBoundObject();
            if(toggledTexture != null) return toggledTexture.getBoundObject();
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
                        return colorToRender;
                    }
                }
            }
            else if(isHovered()){
                if(toggledHoveredTexture == null){
                    colorToRender = toggledHoveredColor.cpy();
                    if(toggledHoveredColorMultiplier != 1.0f){
                        colorToRender = colorToRender.lerp(super.getColorForRender(), 1 - toggledHoveredColorMultiplier);
                        return colorToRender;
                    }
                }
            }
            else if(toggledTexture == null){
                return colorToRender.lerp(toggledColor, toggledColorMultiplier);
            }
        }

        return super.getColorForRender();
    }

    public void setToggledColor(Color color){
        this.toggledColor = color;
    }
    public void setToggledColorMultiplier(float multiplier){
        this.toggledColorMultiplier = multiplier;
    }

    public void setToggledHoveredColor(Color color){
        this.toggledHoveredColor = color;
    }
    public void setToggledHoveredColorMultiplier(float multiplier){
        this.toggledHoveredColorMultiplier = multiplier;
    }

    public void setToggledDisabledColor(Color color){
        this.toggledDisabledColor = color;
    }
    public void setToggledDisabledColorMultiplier(float multiplier){
        this.toggledDisabledColorMultiplier = multiplier;
    }

    //endregion

    //region Left Click

    @Override
    public void onLeftClick(boolean byProxy) {
        super.onLeftClick(byProxy);
        toggle(byProxy);
    }

    @Override
    public void deselect() {
        if(isControllerSelected() && isToggled()){
            // If the toggle is selected with a controller, we want to deselect it when clicked
            // This is to prevent the toggle from being stuck in a selected state
            setToggled(false);
        }

        super.deselect();
    }

    //endregion

    //region Toggled State

    public void toggle(boolean byProxy){
        setToggled(!isToggled());

        if(ModManager.SayTheSpire.isActive()){
            if(getOnTriggerLine() != null){
                Output.text(getOnTriggerLine(), true);
            }
        }
    }
    public boolean isToggled(){
        return toggled;
    }


    public void setToggled(boolean toggled){
        if(this.toggled == toggled) return; // No change, do nothing

        this.toggled = toggled;

        postToggledEvent.invoke(toggled);
        postToggledEvent_Static.invoke(toggled, this);
    }
    public void setToggledHoveredTexture(AbstractTextureBinding hoveredTexture){
        this.toggledHoveredTexture = hoveredTexture;
    }
    public void setToggledDisabledTexture(AbstractTextureBinding disabledTexture){
        this.toggledDisabledTexture = disabledTexture;
    }

    //endregion

    //endregion

    public static class ToggleData extends Interactable.InteractableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public BooleanProperty isToggled = new BooleanProperty(false).setName("Toggled");

        public TextureBindingProperty toggledTexture = new TextureBindingProperty(new TextureNoneBinding()).setName("Toggled Image");
        public TextureBindingProperty toggledHoveredTexture = new TextureBindingProperty(new TextureNoneBinding());
        public TextureBindingProperty toggledDisabledTexture = new TextureBindingProperty(new TextureNoneBinding());

        private String toggledColor = Color.BLACK.toString();
        private float toggledColorMultiplier = 0.15f;

        public String toggledHoveredColor = Color.BLACK.toString();
        public float toggledHoveredColorMultiplier = 0.25f;

        public String toggledDisabledColor = Color.WHITE.toString();
        public float toggledDisabledColorMultiplier = 0.25f;

        @Override
        public Toggle makeUIElement_internal() {
            return new Toggle(this);
        }
    }
}
