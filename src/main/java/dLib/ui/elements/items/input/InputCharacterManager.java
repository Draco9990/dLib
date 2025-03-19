package dLib.ui.elements.items.input;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.elements.UIElement;
import dLib.util.IntegerVector2;

import java.util.Objects;

public class InputCharacterManager extends UIElement {
    private IntegerVector2 selectionStart;
    private IntegerVector2 selectionEnd;

    private ESelectionMode selectionMode = ESelectionMode.Standard;

    public InputCharacterManager() {
        super();
    }

    @Override
    public void addChild(UIElement child) {
        super.addChild(child);

        if (child instanceof InputCharacterHB) {
            InputCharacterHB hb = (InputCharacterHB) child;
            hb.onLeftClickEvent.subscribe(this, () -> {
                selectionEnd = null;
                selectionStart = new IntegerVector2(hb.glyphRowIndex, hb.glyphIndex);
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
    }

    public boolean hasSelection(){
        return selectionStart != null && selectionEnd != null && !Objects.equals(selectionStart, selectionEnd);
    }

    private enum ESelectionMode{
        Standard
    }
}
