package dLib.ui.elements.prefabs;

import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UIThemeManager;

public class ColorPickerPopup extends CompositeUIElement {
    /** Constructors */
    public ColorPickerPopup(int xPos, int yPos, int width, int height) {
        super(xPos, yPos, width, height);

        ColorPickerPopup instance = this;

        this.background.add(new Draggable(UIThemeManager.getDefaultTheme().inputfield, xPos, yPos, width, height){
            @Override
            public Hoverable setPosition(Integer newPosX, Integer newPosY) {
                if(instance.getPositionX() == newPosX && instance.getPositionY() == newPosY){
                    return super.setPosition(newPosX, newPosY);
                }

                instance.setPosition(newPosX, newPosY);
                return this;
            }
        });

        int marginX = (int) (width * 0.1f);
        int marginY = (int) (height * 0.1f);
        int margin = Math.min(marginY, marginX);

        middle = new ColorWheel(xPos, yPos, width - 2*margin, height - 2*margin);
        middle.setCenterPosition((int) (xPos + width *0.5f), (int) (yPos + height * 0.5f));
    }
}
