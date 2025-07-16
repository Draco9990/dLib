package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.PixelDimensionValueEditor;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "px")
public class PixelDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private float size;

    //endregion

    //region Constructors

    public PixelDimension(float size){
        this.size = size;
    }

    //endregion

    //region Methods

    //region Calculations


    @Override
    protected Float tryCalculateValue_Width(UIElement forElement) {
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingLeftRaw());
        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingRightRaw());

        return size - forElement.getPaddingLeft() - forElement.getPaddingRight();
    }

    @Override
    protected Float tryCalculateValue_Height(UIElement forElement) {
        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingTopRaw());
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingBottomRaw());

        return size - forElement.getPaddingTop() - forElement.getPaddingBottom();
    }

    //endregion

    //region Value

    public float getValueRaw(){
        return size;
    }

    @Override
    public void setValueFromString(String value) {
        size = Float.parseFloat(value);
    }

    //endregion

    //region Utility

    @Override
    public AbstractDimension cpy() {
        PixelDimension px = Dim.px(size);
        px.copyFrom(this);
        return px;
    }

    @Override
    public String toString() {
        return "Px[" + size + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PixelDimension)) {
            return false;
        }

        PixelDimension other = (PixelDimension) obj;
        return other.size == size;
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PixelDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PixelDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //region Resizing

    @Override
    protected boolean canResize() {
        return true;
    }

    @Override
    public void resizeBy(UIElement self, float amount) {
        size += amount;

        super.resizeBy(self, amount);
    }

    //endregion

    //endregion
}
