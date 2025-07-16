package dLib.ui.descriptors;

import basemod.Pair;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;
import dLib.util.events.localevents.RunnableEvent;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class ElementDescriptor<TReferenceEnum, ElementDescriptorType extends ElementDescriptor> implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    protected TReferenceEnum reference;

    private float calculatedValue = 0;
    protected boolean needsRecalculation = true;

    private ElementDescriptorType minValue = null;
    private ElementDescriptorType maxValue = null;

    private Float value = null;

    public ArrayList<ElementDescriptor> dependencies = new ArrayList<>();

    //endregion

    //region Constructors

    public ElementDescriptor(){
    }

    //endregion

    //region Class Methods

    //region Calculation

    public boolean calculateValue(UIElement forElement){
        Float value = tryCalculateValue(forElement);
        if(value != null){
            calculatedValue = value;
            needsRecalculation = false;
            return true;
        }

        return false;
    }
    protected abstract Float tryCalculateValue(UIElement forElement);

    public boolean needsRecalculation(){
        return needsRecalculation;
    }
    public void requestRecalculation(){
        needsRecalculation = true;
    }

    public float getCalculatedValue(){
        return calculatedValue;
    }
    public void overrideCalculatedValue(float value){
        calculatedValue = value;
    }

    public void setValueFromString(String value){
        throw new UnsupportedOperationException("setValueFromString not implemented for this class");
    }

    //endregion

    //region Reference

    public void setReference(TReferenceEnum reference){
        this.reference = reference;
    }

    //endregion

    public abstract ElementDescriptor<TReferenceEnum, ElementDescriptorType> cpy();

    public void copyFrom(ElementDescriptor<TReferenceEnum, ElementDescriptorType> other){
        this.reference = other.reference;

        this.calculatedValue = other.calculatedValue;
        this.needsRecalculation = other.needsRecalculation;
    }

    //endregion
}
