package dLib.ui.layout;

import basemod.Pair;
import dLib.ui.elements.UIElement;
import dLib.util.ui.padding.AbstractPadding;

public interface ILayoutProvider {

    // Content Width and Height

    boolean providesWidth();
    default boolean canCalculateContentWidth() { return true; }
    Pair<Float, Float> calculateContentWidth();

    boolean providesHeight();
    default boolean canCalculateContentHeight() { return true; }
    Pair<Float, Float> calculateContentHeight();

    //endregion Content Width and Height

    //region Content Padding

    default float getContentPaddingLeft(){
        return getContentPaddingLeftRaw().getCalculatedValue();
    }
    default float getContentPaddingRight(){
        return getContentPaddingRightRaw().getCalculatedValue();
    }
    default float getContentPaddingTop(){
        return getContentPaddingTopRaw().getCalculatedValue();
    }
    default float getContentPaddingBottom(){
        return getContentPaddingBottomRaw().getCalculatedValue();
    }

    AbstractPadding getContentPaddingLeftRaw();
    AbstractPadding getContentPaddingRightRaw();
    AbstractPadding getContentPaddingTopRaw();
    AbstractPadding getContentPaddingBottomRaw();

    void setLeftContentPadding(AbstractPadding padding);
    void setRightContentPadding(AbstractPadding padding);
    void setTopContentPadding(AbstractPadding padding);
    void setBottomContentPadding(AbstractPadding padding);

    default void setHorizontalContentPadding(AbstractPadding padding){
        setLeftContentPadding(padding.cpy());
        setRightContentPadding(padding.cpy());
    }
    default void setVerticalContentPadding(AbstractPadding padding){
        setTopContentPadding(padding.cpy());
        setBottomContentPadding(padding.cpy());
    }

    default void setContentPadding(AbstractPadding padding){
        setLeftContentPadding(padding.cpy());
        setRightContentPadding(padding.cpy());
        setTopContentPadding(padding.cpy());
        setBottomContentPadding(padding.cpy());
    }

    //endregion

    //region Child Manipulation

    default boolean isChildEnabled(UIElement child){
        return true;
    }

    default boolean isChildVisible(UIElement child){
        return true;
    }

    //endregion Child Manipulation
}
