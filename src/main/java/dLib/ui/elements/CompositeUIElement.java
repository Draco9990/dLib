package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.modcompat.ModManager;
import dLib.ui.data.CompositeUIElementData;
import dLib.ui.data.UIElementData;
import dLib.ui.elements.implementations.Interactable;
import sayTheSpire.Output;

import java.util.ArrayList;
import java.util.Objects;

public class CompositeUIElement extends UIElement {
    public UIElement left;
    public UIElement right;
    public UIElement middle;
    public ArrayList<UIElement> other = new ArrayList<>();

    private boolean isVisible = true;
    private boolean isEnabled = true;

    public String onSelectedLine = ""; // Say the Spire mod compatibility
    public String onTriggeredLine = ""; // Say the Spire mod compatibility

    public boolean temporary = false;

    /** Constructors */
    public CompositeUIElement(int xPos, int yPos){
        super(xPos, yPos, 9999, 9999);
    }

    public CompositeUIElement(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);
    }

    public CompositeUIElement(CompositeUIElementData data){
        super(data);

        if(data.left != null) left = data.left.makeLiveInstance();
        if(data.middle != null) middle = data.middle.makeLiveInstance();
        if(data.right != null) right = data.right.makeLiveInstance();
        for(UIElementData otherData : data.other){
            other.add(otherData.makeLiveInstance());
        }

        temporary = data.isTemporary;
    }

    /** Update */
    public void update(){
        if(left != null) left.update();
        if(right != null) right.update();
        if(middle != null) middle.update();
        for(UIElement u : other) u.update();
    }

    /** Render */
    public void render(SpriteBatch sb){
        if(left != null) left.render(sb);
        if(right != null) right.render(sb);
        if(middle != null) middle.render(sb);
        for(UIElement u : other) u.render(sb);
    }

    /** Getters and Setters */
    public CompositeUIElement setOnSelectedLine(String newLine){
        this.onSelectedLine = newLine;
        return this;
    }
    public String getOnSelectedLine(){ return this.onSelectedLine; }

    public CompositeUIElement setOnTriggerLine(String newLine){
        this.onTriggeredLine = newLine;
        return this;
    }
    public String getOnTriggeredLine(){ return this.onTriggeredLine; }

    /** Bindings */
    public void removeUIElement(UIElement elementToRemove){
        if(Objects.equals(left, elementToRemove)) left = null;
        if(Objects.equals(middle, elementToRemove)) middle = null;
        if(Objects.equals(right, elementToRemove)) right = null;
        other.remove(elementToRemove);
    }
    public boolean isEmpty(){
        return left == null && middle == null && right == null && other.isEmpty();
    }

    /** Position */
    @Override
    public UIElement setPosition(Integer newPosX, Integer newPosY) {
        int xOffset = newPosX - x;
        int yOffset = newPosY - y;

        if(left != null) left.offset(xOffset, yOffset);
        if(middle != null) middle.offset(xOffset, yOffset);
        if(right != null) right.offset(xOffset, yOffset);
        for(UIElement otherElement : other) otherElement.offset(xOffset, yOffset);

        return super.setPosition(newPosX, newPosY);
    }

    public int getBoundX() {
        int xPos = -1;

        if(left != null) xPos = left.getPositionX();
        if(middle != null && (middle.getPositionX() < xPos || xPos == -1)) xPos = middle.getPositionX();
        if(right != null && (right.getPositionX() < xPos || xPos == -1)) xPos = right.getPositionX();
        for(UIElement otherElement : other){
            if((otherElement.getPositionX() < xPos || xPos == -1)) xPos = otherElement.getPositionX();
        }

        return xPos;
    }
    public int getBoundY() {
        int yPos = -1;

        if(left != null) yPos = left.getPositionY();
        if(middle != null && (middle.getPositionY() < yPos || yPos == -1)) yPos = middle.getPositionY();
        if(right != null && (right.getPositionY() < yPos || yPos == -1)) yPos = right.getPositionY();
        for(UIElement otherElement : other){
            if((otherElement.getPositionY() < yPos || yPos == -1)) yPos = otherElement.getPositionY();
        }

        return yPos;
    }


    /** Width and Height */
    @Override
    public UIElement setDimensions(Integer newWidth, Integer newHeight) {
        float diffXPerc = (float)newWidth / width;
        float diffYPerc = (float)newHeight / height;

        if(left != null) {
            shiftItemDimensions(left, diffXPerc, diffYPerc);
        }
        if(middle != null) {
            shiftItemDimensions(middle, diffXPerc, diffYPerc);
        }
        if(right != null){
            shiftItemDimensions(right, diffXPerc, diffYPerc);
        }
        for(UIElement otherElement : other){
            shiftItemDimensions(otherElement, diffXPerc, diffYPerc);
        }

        return super.setDimensions(newWidth, newHeight);
    }

    private void shiftItemDimensions(UIElement item, float diffXPerc, float diffYPerc){
        item.setDimensions((int) (item.getWidth() * diffXPerc), (int) (item.getHeight() * diffYPerc));
        int diffPosX = item.getPositionX() - getPositionX();
        if(diffPosX != 0){
            item.setPositionX(getPositionX() + (int)(diffPosX * diffXPerc));
        }
        int diffPosY = item.getPositionY() - getPositionY();
        if(diffPosY != 0){
            item.setPositionY(getPositionY() + (int)(diffPosY * diffYPerc));
        }
    }

    public int getBoundingWidth() {
        int leftMostX = -1;
        int rightMostX = -1;

        if(left != null){
            leftMostX = left.getPositionX();
            rightMostX += leftMostX + left.getWidth();
        }
        if(middle != null){
            if(middle.getPositionX() < leftMostX || leftMostX == -1){
                leftMostX = middle.getPositionX();
            }
            if(middle.getPositionX() + middle.getWidth() > rightMostX){
                rightMostX = middle.getPositionX() + middle.getWidth();
            }
        }
        if(right != null){
            if(right.getPositionX() < leftMostX || leftMostX == -1){
                leftMostX = right.getPositionX();
            }
            if(right.getPositionX() + right.getWidth() > rightMostX){
                rightMostX = right.getPositionX() + right.getWidth();
            }
        }
        for(UIElement otherElement : other){
            if(otherElement.getPositionX() < leftMostX || leftMostX == -1){
                leftMostX = otherElement.getPositionX();
            }
            if(otherElement.getPositionX() + otherElement.getWidth() > rightMostX){
                rightMostX = otherElement.getPositionX() + otherElement.getWidth();
            }
        }

        return rightMostX - leftMostX;
    }
    public int getBoundingHeight() {
        int bottomY = -1;
        int topY = -1;

        if(left != null){
            bottomY = left.getPositionY();
            topY += bottomY + left.getHeight();
        }
        if(middle != null){
            if(middle.getPositionY() < bottomY || bottomY == -1){
                bottomY = middle.getPositionY();
            }
            if(middle.getPositionY() + middle.getHeight() > topY){
                topY = middle.getPositionY() + middle.getHeight();
            }
        }
        if(right != null){
            if(right.getPositionY() < bottomY || bottomY == -1){
                bottomY = right.getPositionY();
            }
            if(right.getPositionY() + right.getHeight() > topY){
                topY = right.getPositionY() + right.getHeight();
            }
        }
        for(UIElement otherElement : other){
            if(otherElement.getPositionY() < bottomY || bottomY == -1){
                bottomY = otherElement.getPositionY();
            }
            if(otherElement.getPositionY() + otherElement.getHeight() > topY){
                topY = otherElement.getPositionY() + otherElement.getHeight();
            }
        }

        return topY - bottomY;
    }

    /** Visible & Enabled*/
    @Override
    protected void setVisibility(boolean visible) {
        left.setVisibility(visible);
        right.setVisibility(visible);
        middle.setVisibility(visible);
        for(UIElement otherElement : other){
            otherElement.setVisibility(visible);
        }

        isVisible = visible;
    }
    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    protected void setEnabled(boolean enabled) {
        left.setEnabled(enabled);
        right.setEnabled(enabled);
        middle.setEnabled(enabled);
        for(UIElement otherElement : other){
            otherElement.setEnabled(enabled);
        }

        isVisible = enabled;
    }
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean isActive() {
        return isVisible || isEnabled;
    }

    /** Fired whenever the current element group comes into focus */
    public void select(){
        if(ModManager.SayTheSpire.isActive()){
            if(getOnSelectedLine() != null){
                Output.text(getOnSelectedLine(), true);
            }
        }

        if(left != null){
            if(left instanceof Interactable){
                ((Interactable) left).select();
            }
        }
        if(middle != null){
            if(middle instanceof Interactable){
                ((Interactable) middle).select();
            }
        }
        if(right != null){
            if(right instanceof Interactable){
                ((Interactable) right).select();
            }
        }
        for(UIElement otherElement : other) {
            if(otherElement instanceof Interactable){
                ((Interactable) otherElement).select();
            }
        }
    }
    public void deselect(){
        if(left != null){
            if(left instanceof Interactable){
                ((Interactable) left).deselect();
            }
        }
        if(middle != null){
            if(middle instanceof Interactable){
                ((Interactable) middle).deselect();
            }
        }
        if(right != null){
            if(right instanceof Interactable){
                ((Interactable) right).deselect();
            }
        }
        for(UIElement otherElement : other) {
            if(otherElement instanceof Interactable){
                ((Interactable) otherElement).deselect();
            }
        }
    }

    public void triggerLeft(){
        if(left != null){
            if(left instanceof Interactable){
                ((Interactable) left).clickLeft();
            }
        }

        if(getOnTriggeredLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggeredLine(), true);
            }
        }
    }
    public void triggerRight(){
        if(right != null){
            if(right instanceof Interactable){
                ((Interactable) right).clickLeft();
            }
        }

        if(getOnTriggeredLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggeredLine(), true);
            }
        }
    }
    public void triggerMiddle(){
        if(middle != null){
            if(middle instanceof Interactable){
                ((Interactable) middle).clickLeft();
            }
        }

        for(UIElement element : other){
            if(element instanceof Interactable){
                ((Interactable) element).clickLeft();
            }
        }

        if(getOnTriggeredLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggeredLine(), true);
            }
        }
    }
}
