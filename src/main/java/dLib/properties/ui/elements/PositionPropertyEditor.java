package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TPositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.util.IntegerVector2;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

import java.util.Objects;

public class PositionPropertyEditor extends AbstractPropertyEditor<TPositionProperty<?>> {

    public PositionPropertyEditor(TPositionProperty<?> property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(property, xPos, yPos, width, multiline);
    }

    @Override
    protected UIElement buildContent(TPositionProperty<?> property, AbstractDimension width, AbstractDimension height) {
        HorizontalBox horizontalBox = new HorizontalBox(width, height);
        {
            TextBox xLabel = new TextBox("X:", Dim.perc(0.2), Dim.fill());
            horizontalBox.addItem(xLabel);

            VerticalBox xValueBox = new VerticalBox(Dim.perc(0.25f), Dim.fill());
            {
                xValueBox.addItem(buildPositionValueBox(property, property.getXPosition(), Dim.perc(0.8f)));
                xValueBox.addItem(new Spacer(Dim.fill(), Dim.perc(0.2f))); //TODO
            }
            horizontalBox.addItem(xValueBox);

            horizontalBox.addItem(new Spacer(Dim.perc(0.1), Dim.fill()));

            TextBox yLabel = new TextBox("Y:", Dim.perc(0.2), Dim.fill());
            horizontalBox.addItem(yLabel);

            VerticalBox yValueBox = new VerticalBox(Dim.perc(0.25f), Dim.fill());
            {
                yValueBox.addItem(buildPositionValueBox(property, property.getYPosition(), Dim.perc(0.8f)));
                yValueBox.addItem(new Spacer(Dim.fill(), Dim.perc(0.2f))); //TODO
            }
        }

        return horizontalBox;
    }

    private UIElement buildPositionValueBox(TPositionProperty<?> property, AbstractPosition positionValue, AbstractDimension boxHeight){
        return new Spacer(Dim.fill(), boxHeight);
    }
}
