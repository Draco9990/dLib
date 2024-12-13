package dLib.properties.objects;

import dLib.properties.objects.templates.TPositionProperty;
import dLib.util.ui.position.AbstractPosition;

public class PositionProperty extends TPositionProperty<PositionProperty> {
    public PositionProperty(AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos);
    }
}
