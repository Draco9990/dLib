package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.TextureManager;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public class GenericInputWindow extends UIElement {
    protected DarkenLayer darkenLayer;
    protected InternalPasswordWindow popup;

    public Event<Consumer<String>> onConfirm = new Event<>();

    public GenericInputWindow(String title, String confirmButtonText) {
        this(title, confirmButtonText, new ElementProperties());
    }

    public GenericInputWindow(String title, String confirmButtonText, ElementProperties properties){
        super();

        darkenLayer = new DarkenLayer();
        addChildNCS(darkenLayer);

        popup = new InternalPasswordWindow(title, confirmButtonText, properties);
        addChildNCS(popup);

        setModal(true);
        setDrawFocusOnOpen(true);
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

    public static class InternalPasswordWindow extends Renderable{
        public TextButton cancelButton;
        public TextButton confirmButton;

        public Inputfield inputBox;
        public PasswordBox passwordBox;

        public InternalPasswordWindow(String title, String confirmButtonText, ElementProperties properties){
            super(Tex.stat("dLibResources/images/ui/common/GenericInputWindowBackground.png"), Pos.px(603), Pos.px(323), Dim.px(699), Dim.px(369));

            setEntryAnimation(new UIAnimation_SlideInUp(this));
            setExitAnimation(new UIAnimation_SlideOutDown(this));

            addChildNCS(new TextBox(title, Pos.px(35), Pos.px(286), Dim.px(643), Dim.px(49)).setFont(FontHelper.buttonLabelFont).setTextRenderColor(Color.GOLD).setFontScaleOverride(1f));

            if(properties.canCancel){
                cancelButton = new TextButton("Cancel", Pos.px(-6), Pos.px(18), Dim.px(161), Dim.px(74));
                cancelButton.getButton().setImage(Tex.stat("dLibResources/images/ui/common/CancelButtonSmall.png"));
                cancelButton.getTextBox().setFontScaleOverride(0.9f).setTextRenderColor(Color.WHITE);
                cancelButton.onLeftClickEvent.subscribe(this, () -> {
                    getParentOfType(GenericInputWindow.class).hideAndDisable();
                });
                addChildCS(cancelButton);
            }

            confirmButton = new TextButton(confirmButtonText, Pos.px(536), Pos.px(18), Dim.px(173), Dim.px(74));
            confirmButton.getButton().setImage(Tex.stat("dLibResources/images/ui/common/ConfirmButtonSmall.png"));
            confirmButton.getTextBox().setFontScaleOverride(0.9f).setTextRenderColor(Color.WHITE);
            confirmButton.onLeftClickEvent.subscribe(this, () -> {
                getParentOfType(GenericInputWindow.class).onConfirm.invoke(consumer -> {
                    if(properties.isPassword){
                        consumer.accept(passwordBox.inputfield.getTextBox().getText());
                    }
                    else{
                        consumer.accept(inputBox.getTextBox().getText());
                    }
                });
            });
            addChildCS(confirmButton);

            if(properties.isPassword){
                passwordBox = new PasswordBox(Pos.px(24), Pos.px(155), Dim.px(658), Dim.px(61));
                addChildCS(passwordBox);
            }
            else{
                inputBox = new Inputfield("", Pos.px(24), Pos.px(155), Dim.px(658), Dim.px(61));
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