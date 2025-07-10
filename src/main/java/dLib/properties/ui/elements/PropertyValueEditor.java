package dLib.properties.ui.elements;

import dLib.properties.objects.templates.TBooleanProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Spacer;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

public class PropertyValueEditor<PropertyType extends TProperty> extends AbstractValueEditor<PropertyType, PropertyType> {
    //region Variables

    protected boolean multiline;

    protected UIElement contentEditor;

    public ConsumerEvent<TProperty<?, ?>> onPropertyHovered = new ConsumerEvent<>();
    public ConsumerEvent<TProperty<?, ?>> onPropertyUnhovered = new ConsumerEvent<>();

    //endregion

    //region Constructors

    public PropertyValueEditor(TProperty<?, ?> property) {
        this(property, false);
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
        VerticalBox vBox = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
        vBox.setPadding(Padd.px(15), Padd.px(0));

        TextBox boundPropertyNameBox = new TextBox(boundProperty.getName() + ":", Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(50));
        boundPropertyNameBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        vBox.addChild(boundPropertyNameBox);

        buildValueContent(Dim.fill(), Dim.auto());
        vBox.addChild(contentEditor);
        addChild(vBox);
    }

    private void buildSingleLine(){
        HorizontalBox hBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
        hBox.setPadding(Padd.px(15), Padd.px(0));

        TextBox boundPropertyNameBox = new TextBox(boundProperty.getName() + ":", Pos.perc(0.5), Pos.px(0), Dim.perc(0.75), Dim.px(50));
        boundPropertyNameBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        hBox.addChild(boundPropertyNameBox);

        buildValueContent(Dim.perc(0.25), Dim.px(50));
        hBox.addChild(contentEditor);
        addChild(hBox);
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
