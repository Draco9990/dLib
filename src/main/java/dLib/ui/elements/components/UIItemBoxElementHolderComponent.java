package dLib.ui.elements.components;

import dLib.ui.Orientation;

public class UIItemBoxElementHolderComponent extends UIElementComponent {
    private Orientation.OrientationType orientation;

    public UIItemBoxElementHolderComponent(Orientation.OrientationType orientation) {
        this.orientation = orientation;
    }

    public Boolean isHorizontal() {
        return orientation == Orientation.OrientationType.HORIZONTAL;
    }

    public Boolean isVertical() {
        return orientation == Orientation.OrientationType.VERTICAL;
    }
}
