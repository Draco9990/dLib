package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import dLib.ui.data.UIElementData;
import dLib.util.IntVector2;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class UIElement {
    //region Variables

    protected String ID;

    protected UIElement parent;
    protected List<UIElementChild> children = new ArrayList<>();

    private IntVector2 localPosition = new IntVector2(0, 0);
    private boolean dockedToParent = true;

    private IntegerVe

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    private boolean selected;
    private ArrayList<Consumer<Boolean>> onSelectionStateChangedConsumers = new ArrayList<>();

    //endregion

    /** DEPRECATED */
    protected int x = 0;
    protected int y = 0;

    protected int width = 0; //TODO RF replace with scale
    protected int height = 0;

    private ArrayList<BiConsumer<Integer, Integer>> positionChangedConsumers = new ArrayList<>();

    //region Constructors

    public UIElement(int xPos, int yPos, int width, int height){
        this.ID = getClass().getSimpleName() + "_" + UUID.randomUUID().toString().replace("-", "");
        this.localPosition.x = xPos;
        this.localPosition.y = yPos;
        this.x = xPos;
        this.y = yPos;
        this.width = width;
        this.height = height;
    }

    public UIElement(UIElementData data){
        this.ID = data.ID;
        this.x = data.x;
        this.y = data.y;
        this.width = data.width;
        this.height = data.height;
    }

    //endregion

    //region Methods

    //region Update & Render
    public void update(){
        if(!shouldUpdate()) return;

        for(int i = children.size() - 1; i >= 0; i--){
            children.get(i).element.update();
        }
    }
    public void render(SpriteBatch sb){
        if(!shouldRender()) return;

        for(UIElementChild child : children){
            child.element.render(sb);
        }
    }

    protected boolean shouldUpdate(){
        return isActive() && isEnabled() && isVisible();
    }
    protected boolean shouldRender(){
        return isActive() && isVisible();
    }
    //endregion

    //region Id
    public UIElement setID(String newId){
        this.ID = newId;
        return this;
    }
    public String getId(){
        return ID;
    }
    //endregion

    //region Parent & Children

    //region Parent
    public UIElement setParent(UIElement parent){
        this.parent = parent;
        return this;
    }
    public UIElement getParent(){
        return parent;
    }
    public boolean hasParent(){
        return parent != null;
    }
    //endregion

    //region Children
    public UIElement addChildNCS(UIElement child){
        return addChild(child, false);
    }
    public UIElement addChildCS(UIElement child){
        return addChild(child, true);
    }
    public UIElement addChild(UIElement child, boolean isControllerSelectable){
        return addChild(new UIElementChild(child, isControllerSelectable));
    }
    public UIElement addChild(UIElementChild child){
        this.children.add(child);
        child.element.setParent(this);
        return this;
    }

    public UIElement setChildren(ArrayList<UIElementChild> children){
        clearChildren();
        for(UIElementChild child : children){
            addChild(child);
        }
        return this;
    }

    public UIElement removeChild(UIElement child){
        this.children.remove(child);
        if(Objects.equals(child.getParent(), this)) child.setParent(null);
        return this;
    }
    public UIElement clearChildren(){
        for(UIElementChild child : children){
            if(Objects.equals(child.element.getParent(), this)){
                child.element.setParent(null);
            }
        }
        children.clear();
        return this;
    }

    public UIElementChild getFirstChild(){
        if(children.isEmpty()) return null;
        return children.get(0);
    }
    public UIElementChild getLastChild(){
        if(children.isEmpty()) return null;
        return children.get(children.size() - 1);
    }
    //endregion

    //endregion

    //region Position

    //region Local Position
    public UIElement setLocalPositionX(int newPosition){
        return setLocalPosition(newPosition, y);
    }
    public UIElement setLocalPositionY(int newPosition){
        return setLocalPosition(x, newPosition);
    }
    public UIElement setLocalPosition(int newPositionX, int newPositionY){
        int xDiff = newPositionX - x;
        int yDiff = newPositionY - y;

        this.x = newPositionX;
        this.y = newPositionY;

        if(xDiff != 0 || yDiff != 0){
            onPositionChanged(xDiff, yDiff);
        }

        return this;
    }

    public final int getLocalPositionX(){
        return localPosition.x;
    }
    public final int getLocalPositionY(){
        return localPosition.y;
    }
    public final IntVector2 getLocalPosition(){
        return localPosition.copy();
    }
    //endregion

    //region World Position
    public UIElement setWorldPositionX(int newPos){
        return setWorldPosition(newPos, getWorldPositionY());
    }
    public UIElement setWorldPositionY(int newPos){
        return setWorldPosition(getWorldPositionX(), newPos);
    }
    public UIElement setWorldPosition(int newPosX, int newPosY){
        int xDiff = newPosX - getWorldPositionX();
        int yDiff = newPosY - getWorldPositionY();
        offset(xDiff, yDiff);
        return this;
    }

    public final int getWorldPositionX(){
        return getWorldPosition().x;
    }
    public final int getWorldPositionY(){
        return getWorldPosition().y;
    }
    public final IntVector2 getWorldPosition(){
        if(!hasParent()) return getLocalPosition();
        else{
            IntVector2 parentWorld = parent.getWorldPosition();
            parentWorld.x += getLocalPositionX();
            parentWorld.y += getLocalPositionY();
            return parentWorld;
        }
    }

    public UIElement setWorldPositionCenteredX(int newPos){
        return setWorldPositionCentered(newPos, getWorldPositionCenteredY());
    }
    public UIElement setWorldPositionCenteredY(int newPos){
        return setWorldPositionCentered(getWorldPositionCenteredX(), newPos);
    }
    public UIElement setWorldPositionCentered(int newPosX, int newPosY){
        int wHalf = (int)(width * 0.5f);
        int hHalf = (int)(height * 0.5f);
        return setWorldPosition(newPosX - wHalf, newPosY - hHalf);
    }

    public final int getWorldPositionCenteredX(){
        return getWorldPositionCentered().x;
    }
    public final int getWorldPositionCenteredY(){
        return getWorldPositionCentered().y;
    }
    public final IntVector2 getWorldPositionCentered(){
        IntVector2 worldPosition = getWorldPosition();
        worldPosition.x += (int)(width * 0.5f);
        worldPosition.y += (int)(height * 0.5f);
        return worldPosition;
    }
    //endregion

    //region Offset
    public UIElement offsetX(int xOffset){
        offset(xOffset, 0);
        return this;
    }
    public UIElement offsetY(int yOffset){
        offset(0, yOffset);
        return this;
    }
    public UIElement offset(int xOffset, int yOffset){
        setLocalPosition(getLocalPositionX() + xOffset, getLocalPositionY() + yOffset);
        return this;
    }
    //endregion

    //region Docking
    public UIElement dockToParent(){
        return setDockedToParent(true);
    }
    public UIElement undockFromParent(){
        return setDockedToParent(false);
    }
    public UIElement setDockedToParent(boolean dockedToParent){
        this.dockedToParent = dockedToParent;
        return this;
    }

    public boolean isDockedToParent(){
        return dockedToParent;
    }
    //endregion

    public void onPositionChanged(int diffX, int diffY){
        for(BiConsumer<Integer, Integer> consumer : positionChangedConsumers) consumer.accept(diffX, diffY);

        for(UIElementChild child : children){
            child.element.onParentPositionChanged(diffX, diffY);
        }
    }
    public UIElement addOnPositionChangedConsumer(BiConsumer<Integer, Integer> consumer){
        positionChangedConsumers.add(consumer);
        return this;
    }

    protected void onParentPositionChanged(int diffX, int diffY){
        if(!isDockedToParent()){
            offset(-diffX, -diffY);
        }
    }

    //endregion

    //region Interactions
    public boolean onLeftInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onLeftInteraction();
        return hasInteraction;
    }
    public boolean onRightInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onRightInteraction();
        return hasInteraction;
    }
    public boolean onUpInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onUpInteraction();
        return hasInteraction;
    }
    public boolean onDownInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onDownInteraction();
        return hasInteraction;
    }

    public boolean onConfirmInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onConfirmInteraction();
        return hasInteraction;
    }
    public boolean onCancelInteraction(){
        boolean hasInteraction = false;
        for(UIElementChild child : children) hasInteraction = hasInteraction || child.element.onCancelInteraction();
        return hasInteraction;
    }
    //endregion

    //region Selection
    public UIElement select(){
        return setSelected(true);
    }
    public UIElement deselect(){
        return setSelected(false);
    }
    public UIElement setSelected(boolean selected){
        this.selected = selected;
        onSelectionStateChanged();
        return this;
    }

    public boolean isSelected(){
        return selected;
    }

    public void onSelectionStateChanged(){
        for(Consumer<Boolean> consumer : onSelectionStateChangedConsumers) consumer.accept(selected);
    }
    public UIElement addOnSelectionStateChangedConsumer(Consumer<Boolean> consumer){
        onSelectionStateChangedConsumers.add(consumer);
        return this;
    }

    public final UIElement getInnerMostSelectedChild(){
        for(UIElementChild child : children) {
            if(child.element.isSelected()){
                UIElement selectedChild = child.element.getInnerMostSelectedChild();
                return selectedChild == null ? child.element : selectedChild;
            }
        }
        return null;
    }

    public final boolean hasPreviousChildToSelect(){
        if(children.isEmpty()) return false;

        UIElementChild firstChild = getFirstChild();
        if(firstChild.element.isSelected()){
            return firstChild.element.hasPreviousChildToSelect();
        }

        return children.size() > 1;
    }
    public final void selectPreviousChild(){
        boolean selectedPreviousChild = false;
        for(UIElementChild child : children){
            if(child.element.isSelected()){
                if(child.element.hasPreviousChildToSelect()){
                    selectedPreviousChild = true;
                    child.element.selectPreviousChild();
                }
            }
        }

        if(!selectedPreviousChild){
            for(int i = children.size() - 1; i >= 0; i--){
                if(children.get(i).element.isSelected()){
                    children.get(i).element.deselect();
                }

                if(i - 1 < 0){
                    return;
                }

                children.get(i).element.select();
            }
        }
    }

    public final boolean hasNextChildToSelect(){
        if(children.isEmpty()) return false;

        UIElementChild lastChild = getLastChild();
        if(lastChild.element.isSelected()){
            return lastChild.element.hasNextChildToSelect();
        }

        return children.size() > 1;
    }
    public final void selectNextChild(){
        boolean selectedNextChild = false;
        for(UIElementChild child : children){
            if(child.element.isSelected()){
                if(child.element.hasNextChildToSelect()){
                    selectedNextChild = true;
                    child.element.selectNextChild();
                }
            }
        }

        if(!selectedNextChild){
            for(int i = 0; i < children.size(); i++){
                if(children.get(i).element.isSelected()){
                    children.get(i).element.deselect();
                }

                if(i + 1 >= children.size()){
                    return;
                }

                children.get(i).element.select();
            }
        }
    }
    //endregion

    //region Visible & Enabled States

    //region Visibility

    public final void hide(){
        setVisibility(false);
    }
    public final void show(){
        setVisibility(true);
    }
    protected void setVisibility(boolean visible){
        isVisible = visible;
    }
    public boolean isVisible(){
        if(hasParent() && !parent.isVisible()) return false;
        return isVisible;
    }

    //endregion

    //region Enabled State

    public final void disable(){
        setEnabled(false);
    }
    public final void enable(){
        setEnabled(true);
    }
    protected void setEnabled(boolean enabled){
        isEnabled = enabled;
    }
    public boolean isEnabled(){
        if(hasParent() && !parent.isEnabled()) return false;
        return isEnabled;
    }

    //endregion

    public void hideAndDisable(){
        hide();
        disable();
    }
    public void showAndEnable(){
        show();
        enable();
    }

    public boolean isActive(){
        if(hasParent() && !parent.isActive()) return false;
        return isVisible() || isEnabled();
    }

    //endregion

    //endregion

    /** DEPRECATED */
    /** Position */
    public UIElement setPositionX(int newPosX){
        setPosition(newPosX, y);
        return this;
    }
    public UIElement setPositionY(int newPosY){
        setPosition(x, newPosY);
        return this;
    }
    public UIElement setPosition(Integer newPosX, Integer newPosY){
        boolean positionDifferent = x != newPosX || y != newPosY;

        x = newPosX;
        y = newPosY;

        if(positionDifferent) {
            onPositionChanged(x, y);
        }

        return this;
    }

    public UIElement setCenterPositionX(int newPosX){
        setPositionX(newPosX - ((int)(float)getWidth() / 2));
        return this;
    }
    public UIElement setCenterPositionY(int newPosY){
        setPositionY(newPosY - ((int)(float)getHeight() / 2));
        return this;
    }
    public UIElement setCenterPosition(int newPosX, int newPosY){
        setPosition(newPosX - ((int)(float)getWidth() / 2), newPosY - ((int)(float)getHeight() / 2));
        return this;
    }

    public int getPositionX() { return x; }
    public int getPositionY() { return y; }

    /** Width and height */
    public UIElement setWidth(int newWidth){
        return setDimensions(newWidth, height);
    }
    public UIElement setHeight(int newHeight){
        return setDimensions(width, newHeight);
    }
    public UIElement setDimensions(Integer newWidth, Integer newHeight){
        if(newWidth < 1) newWidth = 1;
        if(newHeight < 1) newHeight = 1;

        this.width = newWidth;
        this.height = newHeight;
        return this;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    /** CLASS DEFINITIONS */
    public static class UIElementChild{
        public UIElement element;
        public boolean isControllerSelectable;

        public UIElementChild(UIElement element, boolean isControllerSelectable){
            this.element = element;
            this.isControllerSelectable = isControllerSelectable;
        }
    }
}
