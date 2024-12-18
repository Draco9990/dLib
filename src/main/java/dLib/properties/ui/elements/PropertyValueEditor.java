package dLib.properties.ui.elements;

import dLib.properties.objects.BooleanProperty;
import dLib.properties.objects.templates.TBooleanProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.HorizontalBox;
import dLib.ui.elements.prefabs.Spacer;
import dLib.ui.elements.prefabs.TextBox;
import dLib.ui.elements.prefabs.VerticalBox;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.util.function.Consumer;

public class PropertyValueEditor<PropertyType extends TProperty> extends AbstractValueEditor<PropertyType, PropertyType> {
    //region Variables

    protected boolean multiline;

    protected UIElement contentEditor;

    public Event<Consumer<TProperty<?, ?>>> onPropertyHovered = new Event<>();
    public Event<Consumer<TProperty<?, ?>>> onPropertyUnhovered = new Event<>();

    //endregion

    //region Constructors

    public PropertyValueEditor(TProperty<?, ?> property) {
        this((PropertyType) property, false);
    }

    public PropertyValueEditor(TProperty<?, ?> property, boolean multiline) {
        super((PropertyType) property);

        this.multiline = multiline && !(property instanceof TBooleanProperty); //Fuck it we hard-code

        if(this.multiline){
            buildMultiline();
        }
        else{
            buildSingleLine();
        }

        property.onValueChangedEvent.subscribe(this, (oldVal, newVal) -> {
            if(oldVal.getClass() == newVal.getClass()){
                return;
            }

            if(this.multiline){
                buildValueContent(Dim.fill(), Dim.auto());
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
                        onPropertyHovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(boundProperty));
                    }

                    @Override
                    public void onUnhovered() {
                        onPropertyUnhovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(boundProperty));
                    }
                };
                hoverable.setPassthrough(true);
                item.addChildNCS(hoverable);

                return item;
            }
        };
        vBox.setPadding(Padd.px(15), Padd.px(0));

        vBox.addItem(new TextBox(boundProperty.getName() + ":", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(50)).setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT));
        buildValueContent(Dim.fill(), Dim.auto());
        vBox.addItem(contentEditor);
        addChildCS(vBox);
    }

    private void buildSingleLine(){
        HorizontalBox hBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto()){
            @Override
            public UIElement wrapUIForItem(UIElement item) {
                UIElement hoverable = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
                    @Override
                    public void onHovered() {
                        onPropertyHovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(boundProperty));
                    }

                    @Override
                    public void onUnhovered() {
                        onPropertyUnhovered.invoke(tPropertyConsumer -> tPropertyConsumer.accept(boundProperty));
                    }
                };
                hoverable.setPassthrough(true);
                item.addChildNCS(hoverable);

                return item;
            }
        };
        hBox.setPadding(Padd.px(15), Padd.px(0));

        hBox.addItem(new TextBox(boundProperty.getName() + ":", Pos.perc(0.5), Pos.px(0), Dim.perc(0.75), Dim.px(50)).setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT));

        buildValueContent(Dim.perc(0.25), Dim.px(50));
        hBox.addItem(contentEditor);
        addChildNCS(hBox);
    }

    protected void buildValueContent(AbstractDimension width, AbstractDimension height){
        UIElement builtContent = ValueEditorManager.makeEditorFor(boundProperty, width, height);
        if(builtContent == null) builtContent = new Spacer(width, Dim.px(1)); //TODO remove Fallback

        if(contentEditor != null){
            contentEditor.getParent().replaceChild(contentEditor, builtContent);
        }

        contentEditor = builtContent;
    }

    //endregion
}
