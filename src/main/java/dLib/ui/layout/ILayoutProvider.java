package dLib.ui.layout;

import basemod.Pair;
import dLib.util.ui.padding.AbstractPadding;

public interface ILayoutProvider {

    // Content Width and Height

    boolean providesWidth();
    default boolean canCalculateContentWidth() { return true; }
    Pair<Integer, Integer> calculateContentWidth();

    boolean providesHeight();
    default boolean canCalculateContentHeight() { return true; }
    Pair<Integer, Integer> calculateContentHeight();

    //endregion Content Width and Height

    //region Content Padding

    default int getContentPaddingLeft(){
        return getContentPaddingLeftRaw().getCalculatedValue();
    }
    default int getContentPaddingRight(){
        return getContentPaddingRightRaw().getCalculatedValue();
    }
    default int getContentPaddingTop(){
        return getContentPaddingTopRaw().getCalculatedValue();
    }
    default int getContentPaddingBottom(){
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
}
