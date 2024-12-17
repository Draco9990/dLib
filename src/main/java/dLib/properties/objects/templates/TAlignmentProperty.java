package dLib.properties.objects.templates;

import dLib.ui.Alignment;
import dLib.properties.ui.elements.AlignmentValueEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public abstract class TAlignmentProperty<PropertyType extends TAlignmentProperty<PropertyType>> extends TProperty<Alignment, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;
    //region Variables

    private transient ArrayList<BiConsumer<Alignment.HorizontalAlignment, Alignment.HorizontalAlignment>> onHorizontalAlignmentChangedListeners = new ArrayList<>();
    private transient ArrayList<BiConsumer<Alignment.VerticalAlignment, Alignment.VerticalAlignment>> onVerticalAlignmentChangedListeners = new ArrayList<>();

    //endregion

    //region Constructors

    public TAlignmentProperty(Alignment value) {
        super(value);
    }

    //endregion

    //region Methods

    //region Value

    @Override
    public boolean setValueFromString(String value) {
        throw new UnsupportedOperationException("AlignmentProperty does not support setting value from string");
    }

    @Override
    public Alignment getValue() {
        return new Alignment(super.getValue());
    }

    public PropertyType setHorizontalAlignment(Alignment.HorizontalAlignment alignment){
        Alignment value = getValue();
        value.horizontalAlignment = alignment;
        setValue(value);
        return (PropertyType) this;
    }
    public Alignment.HorizontalAlignment getHorizontalAlignment(){
        return getValue().horizontalAlignment;
    }

    public void onHorizontalAlignmentChanged(Alignment.HorizontalAlignment oldValue, Alignment.HorizontalAlignment newValue){
        if(onHorizontalAlignmentChangedListeners == null) onHorizontalAlignmentChangedListeners = new ArrayList<>();
        for(BiConsumer<Alignment.HorizontalAlignment, Alignment.HorizontalAlignment> listener : onHorizontalAlignmentChangedListeners) listener.accept(oldValue, newValue);
    }
    public PropertyType addOnHorizontalAlignmentChangedListener(BiConsumer<Alignment.HorizontalAlignment, Alignment.HorizontalAlignment> listener){
        if(onHorizontalAlignmentChangedListeners == null) onHorizontalAlignmentChangedListeners = new ArrayList<>();
        onHorizontalAlignmentChangedListeners.add(listener);
        return (PropertyType) this;
    }

    public PropertyType setVerticalAlignment(Alignment.VerticalAlignment alignment){
        Alignment value = getValue();
        value.verticalAlignment = alignment;
        setValue(value);
        return (PropertyType) this;
    }
    public Alignment.VerticalAlignment getVerticalAlignment(){
        return getValue().verticalAlignment;
    }

    public void onVerticalAlignmentChanged(Alignment.VerticalAlignment oldValue, Alignment.VerticalAlignment newValue){
        if(onVerticalAlignmentChangedListeners == null) onVerticalAlignmentChangedListeners = new ArrayList<>();
        for(BiConsumer<Alignment.VerticalAlignment, Alignment.VerticalAlignment> listener : onVerticalAlignmentChangedListeners) listener.accept(oldValue, newValue);
    }
    public PropertyType addOnVerticalAlignmentChangedListener(BiConsumer<Alignment.VerticalAlignment, Alignment.VerticalAlignment> listener){
        if(onVerticalAlignmentChangedListeners == null) onVerticalAlignmentChangedListeners = new ArrayList<>();
        onVerticalAlignmentChangedListeners.add(listener);
        return (PropertyType) this;
    }

    //endregion

    //endregion
}
