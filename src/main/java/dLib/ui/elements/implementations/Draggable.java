package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class Draggable extends Interactable{
    /** Variables */
    private boolean canDragX = true;
    private boolean canDragY = true;

    private int xDragOffset;
    private int yDragOffset;

    private Integer lowerXBound;
    private Integer upperXBound;

    private Integer lowerYBound;
    private Integer upperYBound;

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
        lowerXBound = lowerBound;
        upperXBound = upperBound;

        if(lowerXBound != null && x < lowerXBound){
            setPositionX(lowerXBound);
        }
        if(upperXBound != null && x > upperXBound){
            setPositionX(upperXBound);
        }

        return this;
    }
    public Draggable setBoundsY(Integer lowerBound, Integer upperBound){
        lowerYBound = lowerBound;
        upperYBound = upperBound;

        if(lowerYBound != null && y < lowerYBound){
            setPositionY(lowerBound);
        }
        if(upperYBound != null && y > upperYBound){
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

        if(lowerXBound != null && xPos < lowerXBound){
            xPos = lowerXBound;
        }
        if(upperXBound != null && xPos > upperXBound){
            xPos = upperXBound;
        }

        if(lowerYBound != null && yPos < lowerYBound){
            yPos = lowerYBound;
        }
        if(upperYBound != null && yPos > upperYBound){
            yPos = upperYBound;
        }

        setPosition(xPos, yPos);
    }
}
