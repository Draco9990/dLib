package dLib.tools.uicreator.ui.elements;

import com.badlogic.gdx.Gdx;
import dLib.ui.elements.UIElement;
import dLib.util.SerializationHelpers;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class GeneratedUIElement extends UIElement {
    public GeneratedUIElement() {
        this(Dim.fill(), Dim.fill());
    }
    public GeneratedUIElement(AbstractPosition xPos, AbstractPosition yPos) {
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public GeneratedUIElement(AbstractDimension width, AbstractDimension height) {
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public GeneratedUIElement(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        loadGeneratedData();
    }

    private void loadGeneratedData() {
        String generatedObjectData = Gdx.files.internal("dLibResources/generated/ui/" + getClass().getName() + ".gen").readString();

        RootElement.RootElementData data = SerializationHelpers.fromString(generatedObjectData);
        data.inEditor = false;
        addChild(data.makeUIElement());
    }
}
