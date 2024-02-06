package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.data.implementations.DraggableData;

public class Draggable extends Interactable{
    /** Variables */
    private boolean canDragX = true;
    private boolean canDragY = true;

    private int xDragOffset;
    private int yDragOffset;

    private Integer lowerBoundX;
    private Integer upperBoundX;

    private Integer lowerBoundY;
    private Integer upperBoundY;

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

        if(lowerBoundX != null && xPos < lowerBoundX){
            xPos = lowerBoundX;
        }
        if(upperBoundX != null && xPos > upperBoundX){
            xPos = upperBoundX;
        }

        if(lowerBoundY != null && yPos < lowerBoundY){
            yPos = lowerBoundY;
        }
        if(upperBoundY != null && yPos > upperBoundY){
            yPos = upperBoundY;
        }

        setPosition(xPos, yPos);
    }
}
