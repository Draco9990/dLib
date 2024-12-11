package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TPositionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.*;
import dLib.util.IntegerVector2;
import dLib.util.Reflection;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.PercentagePosition;
import dLib.util.ui.position.StaticPosition;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;

public class PositionPropertyEditor extends AbstractPropertyEditor<TPositionProperty<?>> {

    public PositionPropertyEditor(TPositionProperty<?> property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline) {
        super(property, xPos, yPos, width, multiline);
    }

    @Override
    protected UIElement buildContent(TPositionProperty<?> property, AbstractDimension width, AbstractDimension height) {
        ArrayList<AbstractPosition> positionOptions = new ArrayList<>();
        positionOptions.add(new StaticPosition(0));
        positionOptions.add(new PercentagePosition(0));

        HorizontalBox horizontalBox = new HorizontalBox(width, height);
        {
            TextBox xLabel = new TextBox("X:", Dim.perc(0.1), Dim.fill());
            horizontalBox.addItem(xLabel);

            horizontalBox.addItem(buildPositionValueBox(property, property.getXPosition()));

            ComboBox<AbstractPosition> xValueChanged = new ComboBox<AbstractPosition>(property.getXPosition(), positionOptions, Dim.fill(), Dim.px(15)){
                @Override
                public String itemToString(AbstractPosition item) {
                    return item.getSimpleDisplayName();
                }
            };
            xValueChanged.addOnSelectedItemChangedEvent((classComboBox, option) -> {
                property.setXPosition(option.cpy());
            });
            xValueChanged.getTextBox().setFontScaleOverride(0.5f);
            horizontalBox.addItem(xValueChanged);

            horizontalBox.addItem(new Spacer(Dim.perc(0.1), Dim.fill()));

            TextBox yLabel = new TextBox("Y:", Dim.perc(0.1), Dim.fill());
            horizontalBox.addItem(yLabel);

            horizontalBox.addItem(buildPositionValueBox(property, property.getYPosition()));

            ComboBox<AbstractPosition> yValueChanged = new ComboBox<AbstractPosition>(property.getYPosition(), positionOptions, Dim.fill(), Dim.px(15)){
                @Override
                public String itemToString(AbstractPosition item) {
                    return item.getSimpleDisplayName();
                }
            };
            yValueChanged.addOnSelectedItemChangedEvent((classComboBox, option) -> {
                property.setYPosition(option.cpy());
            });
            yValueChanged.getTextBox().setFontScaleOverride(0.5f);
            horizontalBox.addItem(yValueChanged);
        }

        return horizontalBox;
    }

    private UIElement buildPositionValueBox(TPositionProperty<?> property, AbstractPosition positionValue){
        return new Spacer(Dim.perc(0.25f), Dim.fill());
    }
}
