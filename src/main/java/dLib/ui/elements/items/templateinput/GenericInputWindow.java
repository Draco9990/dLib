package dLib.ui.elements.items.templateinput;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.DarkenLayer;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.buttons.CancelButtonSmall;
import dLib.ui.elements.items.buttons.ConfirmButtonSmall;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.input.PasswordBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class GenericInputWindow extends UIElement {
    public DarkenLayer darkenLayer;
    public InternalInputWindow popup;

    public ConsumerEvent<String> onConfirmEvent = new ConsumerEvent<>();
    public ConsumerEvent<String> onCancelEvent = new ConsumerEvent<>();

    public GenericInputWindow(String title, String confirmButtonText) {
        this(title, confirmButtonText, new ElementProperties());
    }
    public GenericInputWindow(String title, String confirmButtonText, ElementProperties properties){
        super();

        darkenLayer = new DarkenLayer();
        addChild(darkenLayer);

        popup = new InternalInputWindow(title, confirmButtonText, properties);
        addChild(popup);

        setModal(true);
        setDrawFocusOnOpen(true);
    }

    public void reset(){
        if(popup.inputBox != null) popup.inputBox.textBox.setText("");
        if(popup.passwordBox != null) popup.passwordBox.inputfield.textBox.setText("");
    }

    public static class InternalInputWindow extends Renderable {
        public TextButton cancelButton;
        public TextButton confirmButton;

        public Inputfield inputBox;
        public PasswordBox passwordBox;

        public InternalInputWindow(String title, String confirmButtonText, ElementProperties properties){
            super(Tex.stat("dLibResources/images/ui/common/GenericInputWindowBackground.png"), Pos.px(603), Pos.px(323), Dim.px(699), Dim.px(369));

            setEntryAnimation(new UIAnimation_SlideInUp(this));
            setExitAnimation(new UIAnimation_SlideOutDown(this));

            TextBox titleBox =new TextBox(title, Pos.px(35), Pos.px(286), Dim.px(643), Dim.px(49));
            titleBox.setFont(Font.stat(FontHelper.buttonLabelFont));
            titleBox.setTextRenderColor(Color.GOLD);
            titleBox.setFontSize(20);
            addChild(titleBox);

            if(properties.canCancel){
                cancelButton = new CancelButtonSmall(Pos.px(-6), Pos.px(18));
                cancelButton.onLeftClickEvent.subscribe(this, () -> {
                    getParentOfType(GenericInputWindow.class).onCancelEvent.invoke("");
                    getParentOfType(GenericInputWindow.class).dispose();
                });
                addChild(cancelButton);
            }

            confirmButton = new ConfirmButtonSmall(Pos.px(536), Pos.px(18));
            confirmButton.label.setText(confirmButtonText);
            confirmButton.onLeftClickEvent.subscribe(this, () -> {
                if(properties.isPassword){
                    getParentOfType(GenericInputWindow.class).onConfirmEvent.invoke(passwordBox.inputfield.textBox.getText());
                }
                else{
                    getParentOfType(GenericInputWindow.class).onConfirmEvent.invoke(inputBox.textBox.getText());
                }
            });
            addChild(confirmButton);

            if(properties.isPassword){
                passwordBox = new PasswordBox(Pos.px(24), Pos.px(155), Dim.px(658), Dim.px(61));
                passwordBox.inputfield.setSayTheSpireElementName(Str.stat(title));
                addChild(passwordBox);
            }
            else{
                inputBox = new Inputfield("", Pos.px(24), Pos.px(155), Dim.px(658), Dim.px(61));
                inputBox.textBox.setFont(Font.stat(FontHelper.buttonLabelFont));
                inputBox.setSayTheSpireElementName(Str.stat(title));
                addChild(inputBox);
            }
        }
    }

    public static class ElementProperties{
        public boolean isPassword = false;
        public boolean canCancel = true;
    }
}