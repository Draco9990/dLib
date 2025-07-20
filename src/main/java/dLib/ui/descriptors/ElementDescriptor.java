package dLib.ui.descriptors;

import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;

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
    public ArrayList<ElementDescriptor> dependsOn = new ArrayList<>();

    //endregion

    //region Constructors

    public ElementDescriptor(){
    }

    //endregion

    //region Class Methods

    //region Calculation

    public boolean calculateValue(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass){
        flushDependencies();

        Float minValueCalcd = Float.NEGATIVE_INFINITY;
        if(minValue != null && minValue.needsRecalculation()){
            if(!minValue.calculateValue(forElement, calculationPass)){
                return false;
            }
            minValueCalcd = minValue.getCalculatedValue();
        }

        Float maxValueCalcd = Float.POSITIVE_INFINITY;
        if(maxValue != null && maxValue.needsRecalculation()){
            if(!maxValue.calculateValue(forElement, calculationPass)){
                return false;
            }
            maxValueCalcd = maxValue.getCalculatedValue();
        }

        Float value = tryCalculateValue(forElement, calculationPass);
        if(value != null){
            calculatedValue = Math.max(minValueCalcd, Math.min(maxValueCalcd, value));
            needsRecalculation = false;
            return true;
        }

        return false;
    }
    protected abstract Float tryCalculateValue(UIElement forElement, ElementCalculationManager.CalculationPass calculationPass);

    public boolean needsRecalculation(){
        return needsRecalculation;
    }
    public void requestRecalculation(){
        needsRecalculation = true;

        for (ElementDescriptor dependency : dependencies) {
            if (dependency.needsRecalculation()) {
                continue;
            }

            dependency.requestRecalculation();
        }
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

    //region Dependencies

    public void registerDependency(ElementDescriptor descriptor){
        if (descriptor == null || dependsOn.contains(descriptor)) {
            return;
        }

        dependsOn.add(descriptor);
        descriptor.dependencies.add(this);
    }

    public void flushDependencies() {
        for (ElementDescriptor dependency : new ArrayList<>(dependsOn)) {
            unregisterDependency(dependency);
        }
    }

    protected void unregisterDependency(ElementDescriptor descriptor){
        dependsOn.remove(descriptor);
        descriptor.dependencies.remove(this);
    }

    //endregion Dependencies

    //region Min Max Value

    public void setMinValue(ElementDescriptorType minValue) {
        this.minValue = minValue;
        this.minValue.setReference(reference);
        requestRecalculation();
    }
    public Float getMinValue() {
        return minValue.getCalculatedValue();
    }

    public void setMaxValue(ElementDescriptorType maxValue) {
        this.maxValue = maxValue;
        this.maxValue.setReference(reference);
        requestRecalculation();
    }
    public Float getMaxValue() {
        return maxValue.getCalculatedValue();
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

        this.minValue = other.minValue;
        this.maxValue = other.maxValue;

        this.dependencies = new ArrayList<>(other.dependencies);
        this.dependsOn = new ArrayList<>(other.dependsOn);
    }

    //endregion
}
