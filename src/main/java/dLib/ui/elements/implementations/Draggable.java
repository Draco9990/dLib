package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.data.implementations.DraggableData;
import dLib.util.DLibLogger;

public class Draggable extends Interactable{
    /** Variables */
    private boolean canDragX = true;
    private boolean canDragY = true;

    private int xDragOffset;
    private int yDragOffset;

    protected Integer lowerBoundX;
    protected Integer upperBoundX;

    protected Integer lowerBoundY;
    protected Integer upperBoundY;

    /** Constructor */
    public Draggable(Texture image) {
        super(image);
    }

    public Draggable(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public Draggable(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public Draggable(DraggableData data){
        super(data);

        this.canDragX = data.canDragX;
        this.canDragY = data.canDragY;

        this.lowerBoundX = data.lowerBoundX;
        this.upperBoundX = data.upperBoundX;

        this.lowerBoundY = data.lowerBoundY;
        this.upperBoundY = data.upperBoundY;
    }

    /** Getters and Setters */
    public Draggable setCanDragX(boolean canDragX){
        this.canDragX = canDragX;
        return this;
    }
    public Draggable setCanDragY(boolean canDragY){
        this.canDragY = canDragY;
        return this;
    }

    public Draggable setBoundsX(Integer lowerBound, Integer upperBound){
        lowerBoundX = lowerBound;
        upperBoundX = upperBound;

        if(lowerBoundX != null && x < lowerBoundX){
            setPositionX(lowerBoundX);
        }
        if(upperBoundX != null && x > upperBoundX){
            setPositionX(upperBoundX);
        }

        return this;
    }
    public Draggable setBoundsY(Integer lowerBound, Integer upperBound){
        lowerBoundY = lowerBound;
        upperBoundY = upperBound;

        if(lowerBoundY != null && y < lowerBoundY){
            setPositionY(lowerBound);
        }
        if(upperBoundY != null && y > upperBoundY){
            setPositionY(upperBound);
        }

        return this;
    }

    /** Position & Width/Height */
    @Override
    public Hoverable setPosition(Integer newPosX, Integer newPosY) {
        if(lowerBoundX != null && newPosX < lowerBoundX){
            newPosX = lowerBoundX;
        }
        if(upperBoundX != null && newPosX + width > upperBoundX){
            newPosX = upperBoundX - width;
        }

        if(lowerBoundY != null && newPosY < lowerBoundY){
            newPosY = lowerBoundY;
        }
        if(upperBoundY != null && newPosY + height > upperBoundY){
            newPosY = upperBoundY - height;
        }

        return super.setPosition(newPosX, newPosY);
    }

    /** Dragging */
    @Override
    protected void onLeftClick() {
        super.onLeftClick();
        if(canDragX) xDragOffset = (int) (InputHelper.mX - this.x * Settings.xScale);
        if(canDragY) yDragOffset = (int) (InputHelper.mY - this.y * Settings.yScale);
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        super.onLeftClickHeld(totalDuration);

        int xPos = canDragX ? (int) ((InputHelper.mX - xDragOffset) / Settings.xScale) : x;
        int yPos = canDragY ? (int) ((InputHelper.mY - yDragOffset) / Settings.yScale) : y;

        setPosition(xPos, yPos);
    }
}
