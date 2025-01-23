package dLib.ui.elements.items.itembox;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ItemboxChildComponent;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class UIItemBox extends ItemBox {
    //region Constructors

    public UIItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);
        setImage(new TextureNoneBinding());
    }

    public UIItemBox(UIItemBoxData data){
        super(data);
    }

    //endregion

    //region Methods

    //region Item Management

    public void updateChildren(ArrayList<UIElement> items){
        ArrayList<UIElement> itemsToAdd = new ArrayList<>(items);
        ArrayList<UIElement> originalChildren = new ArrayList<>(children);

        Iterator<UIElement> existingChildIt = originalChildren.iterator();
        while(existingChildIt.hasNext()){
            UIElement existingChild = existingChildIt.next();
            if(itemsToAdd.contains(existingChild)){
                itemsToAdd.remove(existingChild);
            }
            else {
                removeChild(existingChild);
            }
        }

        for (int i = 0; i < itemsToAdd.size(); i++) {
            UIElement item = itemsToAdd.get(i);
            insertChild(i, item);
        }
    }

    //endregion

    //region Item Management Overrides

    @Override
    public void replaceChild(UIElement original, UIElement replacement) {
        super.replaceChild(original, replacement);

        if(!replacement.hasComponent(ItemboxChildComponent.class)){
            replacement.addComponent(new ItemboxChildComponent());
        }
    }


    //endregion

    //endregion

    public static class UIItemBoxData extends ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public UIItemBoxData(){
            super();

            texture.setValue(new TextureNoneBinding());
        }
    }
}
