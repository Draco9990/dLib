package dLib.ui.elements.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.elements.implementations.Resizeable;
import dLib.ui.themes.UITheme;

public class ResizeNode extends Draggable {
    /** Variables  */
    private Resizeable resizeableElement;

    /** Constructors */
    public ResizeNode(Resizeable resizeableElement, int xPos, int yPos) {
        super(UITheme.whitePixel, xPos, yPos, 20, 20);

        this.resizeableElement = resizeableElement;
        this.renderColor = Color.RED;
    }
}
