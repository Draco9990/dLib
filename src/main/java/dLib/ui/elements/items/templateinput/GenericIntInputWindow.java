package dLib.ui.elements.items.templateinput;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.buttons.CancelButtonSmall;
import dLib.ui.elements.items.buttons.ConfirmButtonSmall;
import dLib.ui.elements.items.popup.GenericPopupHolder;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class GenericIntInputWindow extends GenericPopupHolder {
    public InternalInputWindow popup;

    public IntegerProperty property = new IntegerProperty(0);

    public ConsumerEvent<Integer> onConfirmEvent = new ConsumerEvent<>();
    public RunnableEvent onCancelEvent = new RunnableEvent();

    public GenericIntInputWindow(String title, String confirmButtonText) {
        this(title, confirmButtonText, true);
    }
    public GenericIntInputWindow(String title, String confirmButtonText, boolean canCancel){
        super();

        popup = new InternalInputWindow(title, confirmButtonText, canCancel, property);
        addChild(popup);

        setModal(true);
        setDrawFocusOnOpen(true);
    }

    public static class InternalInputWindow extends Renderable {
        public TextButton cancelButton;
        public TextButton confirmButton;


        public InternalInputWindow(String title, String confirmButtonText, boolean canCancel, IntegerProperty editingProperty){
            super(Tex.stat("dLibResources/images/ui/common/GenericInputWindowBackground.png"), Pos.px(603), Pos.px(323), Dim.px(699), Dim.px(369));

            setEntryAnimation(new UIAnimation_SlideInUp(this));
            setExitAnimation(new UIAnimation_SlideOutDown(this));

            TextBox titleBox =new TextBox(title, Pos.px(35), Pos.px(286), Dim.px(643), Dim.px(49));
            titleBox.setFont(Font.stat(FontHelper.buttonLabelFont));
            titleBox.setTextRenderColor(Color.GOLD);
            titleBox.setFontSize(20);
            addChild(titleBox);

            if(canCancel){
                cancelButton = new CancelButtonSmall(Pos.px(-6), Pos.px(18));
                cancelButton.postLeftClickEvent.subscribe(this, () -> {
                    getParentOfType(GenericIntInputWindow.class).onCancelEvent.invoke();
                    getParentOfType(GenericIntInputWindow.class).dispose();
                });
                addChild(cancelButton);
            }

            confirmButton = new ConfirmButtonSmall(Pos.px(536), Pos.px(18));
            confirmButton.label.setText(confirmButtonText);
            confirmButton.postLeftClickEvent.subscribe(this, () -> getParentOfType(GenericIntInputWindow.class).onConfirmEvent.invoke(getParentOfType(GenericIntInputWindow.class).property.getValue()));
            addChild(confirmButton);

            UIElement editor = editingProperty.makeEditorFor();
            editor.setLocalPosition(24, 155);
            editor.setWidth(658);
            addChild(editor);
        }
    }
}