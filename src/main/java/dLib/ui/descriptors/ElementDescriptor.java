package dLib.ui.descriptors;

import basemod.Pair;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

public abstract class ElementDescriptor<TReferenceEnum> implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    protected TReferenceEnum reference;

    private int calculatedValue = 0;
    protected boolean needsRecalculation = true;

    //endregion

    //region Constructors

    public ElementDescriptor(){
    }

    //endregion

    //region Class Methods

    //region Calculation

    public abstract Pair<Integer, ElementCalculationManager.ElementCalculationInstruction> getCalculationInstruction(UIElement forElement);

    public boolean needsRecalculation(){
        return needsRecalculation;
    }
    public void requestRecalculation(){
        needsRecalculation = true;
    }

    public int getCalculatedValue(){
        return calculatedValue;
    }
    public void overrideCalculatedValue(int value){
        calculatedValue = value;
    }
    protected void setCalculatedValue(UIElement forElement, int value){
        calculatedValue = value;
        needsRecalculation = false;
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

    public abstract ElementDescriptor<TReferenceEnum> cpy();

    public void copyFrom(ElementDescriptor<TReferenceEnum> other){
        this.reference = other.reference;

        this.calculatedValue = other.calculatedValue;
        this.needsRecalculation = other.needsRecalculation;
    }

    //endregion
}
