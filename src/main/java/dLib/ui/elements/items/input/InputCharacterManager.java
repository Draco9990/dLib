package dLib.ui.elements.items.input;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.color.ColorPickerPopup;
import dLib.util.IntegerVector2;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.ui.position.Pos;

import java.util.Objects;

public class InputCharacterManager extends UIElement {
    public IntegerVector2 selectionStart;
    public IntegerVector2 selectionEnd;

    public ESelectionMode selectionMode = ESelectionMode.Standard;

    public RunnableEvent onSelectionChangedEvent = new RunnableEvent();

    public InputCharacterManager() {
        super();

        preLeftClickGlobalEvent.subscribe(this, (element) -> {
            if(element != getParentOfType(Inputfield.class) && !element.isDescendantOf(getParentOfType(Inputfield.class)) && !(element.getTopParent() instanceof ColorPickerPopup)){
                clearSelection();
            }
        });

        preRightClickGlobalEvent.subscribe(this, (element) -> {
            if(element != getParentOfType(Inputfield.class) && !element.isDescendantOf(getParentOfType(Inputfield.class)) && !(element.getTopParent() instanceof ColorPickerPopup)){
                clearSelection();
            }
        });
    }

    @Override
    public void addChild(UIElement child) {
        super.addChild(child);

        if (child instanceof InputCharacterHB) {
            InputCharacterHB hb = (InputCharacterHB) child;
            hb.onLeftClickEvent.subscribe(this, () -> {
                selectionEnd = null;
                selectionStart = new IntegerVector2(hb.glyphRowIndex, hb.glyphIndex);
                onSelectionUpdated();
            });
            hb.onHoveredEvent.subscribe(this, () -> {
                if (selectionStart != null && InputHelper.isMouseDown) {
                    selectionEnd = new IntegerVector2(hb.glyphRowIndex, hb.glyphIndex);
                    onSelectionUpdated();
                }
            });
        }
    }

    public void onSelectionUpdated(){
        if(selectionMode == ESelectionMode.Standard){
            for (InputCharacterHB child : getChildren(InputCharacterHB.class)){
                child.hideInstantly();
                if(selectionStart != null && selectionEnd != null && !Objects.equals(selectionStart, selectionEnd)){
                    boolean forwardSelection = selectionStart.x < selectionEnd.x || (selectionStart.x.equals(selectionEnd.x) && selectionStart.y < selectionEnd.y);

                    if(forwardSelection){
                        if(child.glyphRowIndex > selectionStart.x && child.glyphRowIndex < selectionEnd.x){
                            child.showInstantly();
                        }
                        else if(child.glyphRowIndex == selectionStart.x && child.glyphIndex >= selectionStart.y){
                            if(child.glyphIndex == selectionStart.y && child.side == InputCharacterHB.ECharHbSide.Right){
                                continue;
                            }

                            if(Objects.equals(selectionStart.x, selectionEnd.x)){
                                if(child.glyphIndex > selectionEnd.y){
                                    continue;
                                }
                                else if(child.glyphIndex == selectionEnd.y && child.side == InputCharacterHB.ECharHbSide.Left){
                                    continue;
                                }
                            }

                            child.showInstantly();
                        }
                        else if(child.glyphRowIndex == selectionEnd.x && child.glyphIndex <= selectionEnd.y && !Objects.equals(selectionStart.x, selectionEnd.x)){
                            if(child.glyphIndex == selectionEnd.y && child.side == InputCharacterHB.ECharHbSide.Left){
                                continue;
                            }
                            child.showInstantly();
                        }
                    }
                    else{
                        if(child.glyphRowIndex < selectionStart.x && child.glyphRowIndex > selectionEnd.x){
                            child.showInstantly();
                        }
                        else if(child.glyphRowIndex == selectionStart.x && child.glyphIndex <= selectionStart.y){
                            if(child.glyphIndex == selectionStart.y && child.side == InputCharacterHB.ECharHbSide.Left){
                                continue;
                            }

                            if(Objects.equals(selectionStart.x, selectionEnd.x)){
                                if(child.glyphIndex < selectionEnd.y){
                                    continue;
                                }
                                else if(child.glyphIndex == selectionEnd.y && child.side == InputCharacterHB.ECharHbSide.Right){
                                    continue;
                                }
                            }

                            child.showInstantly();
                        }
                        else if(child.glyphRowIndex == selectionEnd.x && child.glyphIndex >= selectionEnd.y && !Objects.equals(selectionStart.x, selectionEnd.x)){
                            if(child.glyphIndex == selectionEnd.y && child.side == InputCharacterHB.ECharHbSide.Right){
                                continue;
                            }

                            child.showInstantly();
                        }
                    }
                }
            }
        }

        onSelectionChangedEvent.invoke();
    }

    public boolean hasValidUserSelection(){
        return selectionStart != null && selectionEnd != null && !Objects.equals(selectionStart, selectionEnd);
    }

    public boolean userSelectedForward(){
        return selectionStart.x < selectionEnd.x || (selectionStart.x.equals(selectionEnd.x) && selectionStart.y < selectionEnd.y);
    }
    public boolean userSelectedBackward(){
        return selectionStart.x > selectionEnd.x || (selectionStart.x.equals(selectionEnd.x) && selectionStart.y > selectionEnd.y);
    }

    public void clearSelection(){
        selectionStart = null;
        selectionEnd = null;

        onSelectionUpdated();
    }

    public void selectAll(){
        if(children.isEmpty()){
            return;
        }

        InputCharacterHB lastChild = ((InputCharacterHB)children.get(children.size() - 1));

        selectionStart = new IntegerVector2(0, 0);
        selectionEnd = new IntegerVector2(lastChild.glyphRowIndex, lastChild.glyphIndex);

        onSelectionUpdated();
    }

    public enum ESelectionMode{
        Standard
    }
}
