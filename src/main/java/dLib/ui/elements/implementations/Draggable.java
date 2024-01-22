package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class Draggable extends Interactable{
    /** Variables */
    private int xDragOffset;
    private int yDragOffset;

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

    /** Dragging */
    @Override
    protected void onLeftClick() {
        super.onLeftClick();
        xDragOffset = (int) (InputHelper.mX - this.x * Settings.xScale);
        yDragOffset = (int) (InputHelper.mY - this.y * Settings.yScale);
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        super.onLeftClickHeld(totalDuration);
        setPosition((int) ((InputHelper.mX - xDragOffset) / Settings.xScale), (int) ((InputHelper.mY - yDragOffset) / Settings.yScale));
    }
}
