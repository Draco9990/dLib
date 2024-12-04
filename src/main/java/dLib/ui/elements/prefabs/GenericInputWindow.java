package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.TextureManager;

//NREDO

/*
public class GenericInputWindow extends UIElement {
    public DarkenLayer darkenLayer;

    public InternalPasswordWindow popup;

    public GenericInputWindow(String title, String confirmButtonText) {
        this(title, confirmButtonText, new ElementProperties());
    }

    public GenericInputWindow(String title, String confirmButtonText, ElementProperties properties){
        super(0, 0, 1920, 1080);

        darkenLayer = new DarkenLayer();
        addChildNCS(darkenLayer);

        popup = new InternalPasswordWindow(title, confirmButtonText, properties);
        addChildCS(popup);
    }

    @Override
    public void hide() {
        darkenLayer.lighten();
        popup.hide();
    }

    @Override
    public void show() {
        darkenLayer.darken();
        popup.show();
    }

    @Override
    protected void setVisibility(boolean visible) {
        if(visible){
            darkenLayer.darkenInstantly();
            popup.showInstantly();
        }
        else{
            darkenLayer.lightenInstantly();
            popup.hideInstantly();
        }
    }

    public void reset(){
        if(popup.inputBox != null) popup.inputBox.getTextBox().setText("");
        if(popup.passwordBox != null) popup.passwordBox.inputfield.getTextBox().setText("");
    }

    public static class InternalPasswordWindow extends UIElement{
        public TextButton cancelButton;
        public TextButton confirmButton;

        public Inputfield inputBox;
        public PasswordBox passwordBox;

        public InternalPasswordWindow(String title, String confirmButtonText, ElementProperties properties){
            super(0, 0, 1920, 1080);

            setEntryAnimation(new UIAnimation_SlideInUp(this));
            setExitAnimation(new UIAnimation_SlideOutDown(this));

            addChildNCS(new Renderable(TextureManager.getTexture("dLibResources/images/ui/common/GenericInputWindowBackground.png"), 603, 1080-757));

            addChildNCS(new TextBox(title, 637, 1080-471, 643, 49).setFont(FontHelper.buttonLabelFont).setTextRenderColor(Color.GOLD).setFontScaleOverride(1f).setMarginPercX(0).setMarginPercY(0));

            if(properties.canCancel){
                cancelButton = new TextButton("Cancel", 598, 1080-739, 161, 74);
                cancelButton.getButton().setImage(TextureManager.getTexture("dLibResources/images/ui/common/CancelButtonSmall.png"));
                cancelButton.getTextBox().setFontScaleOverride(0.9f).setTextRenderColor(Color.WHITE);
                addChildCS(cancelButton);
            }

            confirmButton = new TextButton(confirmButtonText, 1139, 1080-739, 173, 74);
            confirmButton.getButton().setImage(TextureManager.getTexture("dLibResources/images/ui/common/ConfirmButtonSmall.png"));
            confirmButton.getTextBox().setFontScaleOverride(0.9f).setTextRenderColor(Color.WHITE);
            addChildCS(confirmButton);

            if(properties.isPassword){
                passwordBox = new PasswordBox(627, 1080-602, 658, 61);
                addChildCS(passwordBox);
            }
            else{
                inputBox = new Inputfield("", 627, 1080-602, 658, 61);
                inputBox.getTextBox().setFont(FontHelper.buttonLabelFont).setTextRenderColor(Color.WHITE).setMaxFontScale(0.8f);
                addChildCS(inputBox);
            }
        }
    }

    public static class ElementProperties{
        public boolean isPassword = false;
        public boolean canCancel = true;
    }
}
*/