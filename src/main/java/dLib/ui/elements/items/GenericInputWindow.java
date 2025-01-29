package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.buttons.CancelButtonSmall;
import dLib.ui.elements.items.buttons.ConfirmButtonSmall;
import dLib.ui.elements.items.input.Inputfield;
import dLib.ui.elements.items.input.PasswordBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public class GenericInputWindow extends UIElement {
    public DarkenLayer darkenLayer;
    public InternalPasswordWindow popup;

    public Event<Consumer<String>> onConfirmEvent = new Event<>();
    public Event<Consumer<String>> onCancelEvent = new Event<>();

    public GenericInputWindow(String title, String confirmButtonText) {
        this(title, confirmButtonText, new ElementProperties());
    }
    public GenericInputWindow(String title, String confirmButtonText, ElementProperties properties){
        super();

        darkenLayer = new DarkenLayer();
        addChild(darkenLayer);

        popup = new InternalPasswordWindow(title, confirmButtonText, properties);
        addChild(popup);

        setModal(true);
        setDrawFocusOnOpen(true);
    }

    public void reset(){
        if(popup.inputBox != null) popup.inputBox.textBox.setText("");
        if(popup.passwordBox != null) popup.passwordBox.inputfield.textBox.setText("");
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

            TextBox titleBox =new TextBox(title, Pos.px(35), Pos.px(286), Dim.px(643), Dim.px(49));
            titleBox.setFont(Font.stat(FontHelper.buttonLabelFont));
            titleBox.setTextRenderColor(Color.GOLD);
            titleBox.setFontSize(20);
            addChild(titleBox);

            if(properties.canCancel){
                cancelButton = new CancelButtonSmall(Pos.px(-6), Pos.px(18));
                cancelButton.onLeftClickEvent.subscribe(this, () -> {
                    getParentOfType(GenericInputWindow.class).onCancelEvent.invoke(consumer -> consumer.accept(""));
                    getParentOfType(GenericInputWindow.class).dispose();
                });
                addChild(cancelButton);
            }

            confirmButton = new ConfirmButtonSmall(Pos.px(536), Pos.px(18));
            confirmButton.label.setText(confirmButtonText);
            confirmButton.onLeftClickEvent.subscribe(this, () -> {
                getParentOfType(GenericInputWindow.class).onConfirmEvent.invoke(consumer -> {
                    if(properties.isPassword){
                        consumer.accept(passwordBox.inputfield.textBox.getText());
                    }
                    else{
                        consumer.accept(inputBox.textBox.getText());
                    }
                });
            });
            addChild(confirmButton);

            if(properties.isPassword){
                passwordBox = new PasswordBox(Pos.px(24), Pos.px(155), Dim.px(658), Dim.px(61));
                addChild(passwordBox);
            }
            else{
                inputBox = new Inputfield("", Pos.px(24), Pos.px(155), Dim.px(658), Dim.px(61));
                inputBox.textBox.setFont(Font.stat(FontHelper.buttonLabelFont));
                addChild(inputBox);
            }
        }
    }

    public static class ElementProperties{
        public boolean isPassword = false;
        public boolean canCancel = true;
    }
}