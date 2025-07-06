package dLib.ui.elements.items.itembox;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.util.bindings.texture.TextureNoneBinding;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;
import java.util.*;

public abstract class UIItemBox extends ItemBox {
    //region Constructors

    public UIItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);
        setTexture(new TextureNoneBinding());
    }

    public UIItemBox(UIItemBoxData data){
        super(data);
    }

    //endregion

    //region Methods

    //region Item Management

    @Override
    public void addChild(UIElement child) {
        super.addChild(child);

        child.setElementMask(this);
    }

    @Override
    public void insertChild(int index, UIElement child) {
        super.insertChild(index, child);

        child.setElementMask(this);
    }

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
                removeChildByInstance(existingChild);
            }
        }

        for (int i = 0; i < itemsToAdd.size(); i++) {
            UIElement item = itemsToAdd.get(i);
            insertChild(i, item);
        }
    }

    //endregion

    //region Item Management Overrides

    //endregion

    //region Filters

    @Override
    public void refilterItems(){
        filteredChildren.clear();

        for(UIElement child : children){
            if(!filterCheck(filterText, child)){
                continue;
            }

            if(!child.isActiveRaw()){
                continue;
            }

            filteredChildren.add(child);
        }

        if(invertedItemOrder){
            Collections.reverse(filteredChildren);
        }
    }

    protected boolean filterCheck(String filterText, UIElement item){
        return item.toString().toLowerCase(Locale.ROOT).contains(filterText.toLowerCase(Locale.ROOT)) && !item.hasComponent(UITransientElementComponent.class);
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
