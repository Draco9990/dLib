package dLib.properties.ui.elements;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.Property;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.VerticalBox;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import org.apache.logging.log4j.util.BiConsumer;

import java.util.Objects;
import java.util.function.Consumer;

public class PropertyValueEditor extends AbstractValueEditor<TProperty<?, ?>> {
    //region Variables

    private TProperty<?, ?> property;

    private boolean multiline;

    private Class<? extends AbstractValueEditor> contentEditorClass;
    private AbstractValueEditor contentEditor;

    public Event<Consumer<TProperty<?, ?>>> onPropertyHovered = new Event<>();
    public Event<Consumer<TProperty<?, ?>>> onPropertyUnhovered = new Event<>();

    //endregion

    //region Constructors


    public PropertyValueEditor(TProperty<?, ?> property, AbstractDimension width, AbstractDimension height) {
        this(property, Pos.px(0), Pos.px(0), width, false);
    }

    public PropertyValueEditor(TProperty<?, ?> property, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, boolean multiline){
        super(xPos, yPos, width, Dim.auto());

        this.property = property;
        this.multiline = multiline;

        if(multiline){
            buildMultiline();
        }
        else{
            buildSingleLine();
        }

        property.onValueChangedPureEvent.subscribe(this, () -> {
            if(property.getPreviousValue().getClass() == property.getValue().getClass()){
                contentEditor.onValueChangedEvent.invoke(o -> ((Consumer)(o)).accept(property.getValue()));
                return;
            }

            if(multiline){
                buildValueContent(Dim.fill(), Dim.fill());
            }
            else{
                buildValueContent(Dim.fill(), Dim.px(50));
            }
        });
    }

    //endregion

    //region Methods

    private void buildMultiline(){
        VerticalBox vBox = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto()){
            @Override
            public UIElement wrapUIForItem(UIElement item) {
                UIElement hoverable = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
                    @Override
                    public void onHovered() {
                        onPropertyHovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(property));
                    }

                    @Override
                    public void onUnhovered() {
                        onPropertyUnhovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(property));
                    }
                };
                hoverable.setPassthrough(true);
                item.addChildNCS(hoverable);

                return item;
            }
        };
        vBox.setPadding(Padd.px(15), Padd.px(0));

        vBox.addItem(new TextBox(property.getName() + ":", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(50)).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT));
        vBox.addItem(contentEditor);
        addChildCS(vBox);
    }

    private void buildSingleLine(){
        HorizontalBox hBox = new HorizontalBox(Pos.px(15), Pos.px(0), Dim.fill(), Dim.auto()){
            @Override
            public UIElement wrapUIForItem(UIElement item) {
                UIElement hoverable = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
                    @Override
                    public void onHovered() {
                        onPropertyHovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(property));
                    }

                    @Override
                    public void onUnhovered() {
                        onPropertyUnhovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(property));
                    }
                };
                hoverable.setPassthrough(true);
                item.addChildNCS(hoverable);

                return item;
            }
        };
        hBox.setPadding(Padd.px(15), Padd.px(0));

        hBox.addItem(new TextBox(property.getName() + ":", Pos.px(0), Pos.px(0), Dim.perc(0.5), Dim.px(50)).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT));

        buildValueContent(Dim.perc(0.5), Dim.px(50));
        hBox.addItem(contentEditor);
        addChildNCS(hBox);
    }

    private void buildValueContent(AbstractDimension width, AbstractDimension height){
        AbstractValueEditor builtContent = ValueEditorManager.makeEditorFor(property.getValue(), width, height);
        builtContent.boundProperty = property;

        if(contentEditor != null){
            contentEditor.getParent().replaceChild(contentEditor, builtContent);
        }

        contentEditor = builtContent;

        builtContent.setValueEvent.subscribe(this, (Consumer<Object>) o -> {
            if(!property.setValueFromObject(o)){
                onValueChangedEvent.invoke(o1 -> ((Consumer)(o1)).accept(property.getValue()));
            }
        });
    }

    //endregion
}
