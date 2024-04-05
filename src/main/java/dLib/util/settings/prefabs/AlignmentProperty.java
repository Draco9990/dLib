package dLib.util.settings.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.settings.AbstractPropertyEditor;
import dLib.ui.elements.settings.AlignmentPropertyEditor;
import dLib.util.settings.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class AlignmentProperty extends Property<Alignment> implements Serializable {
    static final long serialVersionUID = 1L;
    //region Variables

    private transient ArrayList<BiConsumer<Alignment.HorizontalAlignment, Alignment.HorizontalAlignment>> onHorizontalAlignmentChangedListeners = new ArrayList<>();
    private transient ArrayList<BiConsumer<Alignment.VerticalAlignment, Alignment.VerticalAlignment>> onVerticalAlignmentChangedListeners = new ArrayList<>();

    //endregion

    //region Constructors

    public AlignmentProperty(Alignment value) {
        super(value);
    }

    //endregion

    //region Methods

    //region Value

    @Override
    public Alignment getValue() {
        return new Alignment(super.getValue());
    }

    public AlignmentProperty setHorizontalAlignment(Alignment.HorizontalAlignment alignment){
        Alignment value = getValue();
        value.horizontalAlignment = alignment;
        setValue(value);
        return this;
    }
    public Alignment.HorizontalAlignment getHorizontalAlignment(){
        return getValue().horizontalAlignment;
    }

    public void onHorizontalAlignmentChanged(Alignment.HorizontalAlignment oldValue, Alignment.HorizontalAlignment newValue){
        if(onHorizontalAlignmentChangedListeners == null) onHorizontalAlignmentChangedListeners = new ArrayList<>();
        for(BiConsumer<Alignment.HorizontalAlignment, Alignment.HorizontalAlignment> listener : onHorizontalAlignmentChangedListeners) listener.accept(oldValue, newValue);
    }
    public AlignmentProperty addOnHorizontalAlignmentChangedListener(BiConsumer<Alignment.HorizontalAlignment, Alignment.HorizontalAlignment> listener){
        if(onHorizontalAlignmentChangedListeners == null) onHorizontalAlignmentChangedListeners = new ArrayList<>();
        onHorizontalAlignmentChangedListeners.add(listener);
        return this;
    }

    public AlignmentProperty setVerticalAlignment(Alignment.VerticalAlignment alignment){
        Alignment value = getValue();
        value.verticalAlignment = alignment;
        setValue(value);
        return this;
    }
    public Alignment.VerticalAlignment getVerticalAlignment(){
        return getValue().verticalAlignment;
    }

    public void onVerticalAlignmentChanged(Alignment.VerticalAlignment oldValue, Alignment.VerticalAlignment newValue){
        if(onVerticalAlignmentChangedListeners == null) onVerticalAlignmentChangedListeners = new ArrayList<>();
        for(BiConsumer<Alignment.VerticalAlignment, Alignment.VerticalAlignment> listener : onVerticalAlignmentChangedListeners) listener.accept(oldValue, newValue);
    }
    public AlignmentProperty addOnVerticalAlignmentChangedListener(BiConsumer<Alignment.VerticalAlignment, Alignment.VerticalAlignment> listener){
        if(onVerticalAlignmentChangedListeners == null) onVerticalAlignmentChangedListeners = new ArrayList<>();
        onVerticalAlignmentChangedListeners.add(listener);
        return this;
    }

    //endregion

    //region Edit UI

    public AbstractPropertyEditor makeEditUI(int xPos, int yPos, int width, int height) {
        return new AlignmentPropertyEditor(this, xPos, yPos, width, height);
    }

    //endregion

    //endregion
}
