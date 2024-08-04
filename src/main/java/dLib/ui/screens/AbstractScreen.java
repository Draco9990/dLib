package dLib.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import dLib.modcompat.ModManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.DLibLogger;
import dLib.util.IntegerVector2;
import sayTheSpire.Output;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

// Abstract version of a screen
public abstract class AbstractScreen extends UIElement {
    //region Variables

    protected UITheme theme;

    protected static UIStrings globalStrings;
    protected UIStrings localStrings;

    private InputProcessor cachedInputProcessor;
    private AbstractScreen screenToOpenOnClose;

    //endregion

    //region Constructors

    public AbstractScreen(){
        this(0, 0, 1920, 1080);
    }
    public AbstractScreen(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);
        theme = UIThemeManager.getDefaultTheme();
        localStrings = CardCrawlGame.languagePack.getUIString(getModId() + ":" + this.getClass().getSimpleName());
    }

    public AbstractScreen(AbstractScreenData data){
        this();
        ArrayList<UIElement> makeLiveItems = data.makeLiveItems();
        for (int i = 0; i < makeLiveItems.size(); i++) {
            UIElement liveElement = makeLiveItems.get(i);
            addChild(liveElement, data.data.get(i).isSelectable);
        }
    }

    //endregion

    //region Methods

    //region Update & Render

    //endregion

    //region Open & Close

    public void close(){
        if(!hasParent()){
            ScreenManager.closeScreen();
        }
    }

    public void onOpen(){
        cachedInputProcessor = Gdx.input.getInputProcessor();

        if(ModManager.SayTheSpire.isActive()){
            String onScreenOpenLine = getOnScreenOpenLine();
            if(onScreenOpenLine != null){
                Output.text(onScreenOpenLine, true);
            }
        }
    }
    public void onClose(){
        resetInputProcessor();
        dispose();
    }

    public void setScreenToOpenOnClose(AbstractScreen screen){
        this.screenToOpenOnClose = screen;
    }
    public AbstractScreen getScreenToOpenOnClose(){ return this.screenToOpenOnClose;}

    //endregion

    //region Interactions

    @Override
    public boolean onDownInteraction() {
        if(parent != null) return super.onDownInteraction();

        selectNextChild();
        return true;
    }

    @Override
    public boolean onUpInteraction() {
        if(parent != null) return super.onUpInteraction();

        selectPreviousChild();
        return true;
    }

    //endregion

    //region Theme

    public AbstractScreen setThemeOverride(UITheme theme){
        this.theme = theme;
        return this;
    }

    //endregion

    protected void addGenericBackground(){
        addChildNCS(new Image(theme.background, 0, 0, getWidth(), getHeight()));
    }

    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(cachedInputProcessor);
    }

    public abstract String getModId();
    public String getOnScreenOpenLine(){ return null; }

    //endregion

    public static class AbstractScreenData extends UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public int referenceWidth = 1490;
        public int referenceHeight = 840;

        public ArrayList<UIElementData> data = new ArrayList<>();

        public String modID;

        @Override
        public UIElement makeUIElement() {
            return new AbstractScreen() {
                @Override
                public String getModId() {
                    return modID;
                }
            };
        }

        public ArrayList<UIElement> makeLiveItems(){
            ArrayList<UIElement> elements = new ArrayList<>();
            for(UIElementData elementData : data){
                UIElement liveInstance = elementData.makeUIElement();
                if(liveInstance == null){
                    DLibLogger.log("Failed to create a live instance of an element!");
                    continue;
                }

                rescaleElement(liveInstance, new IntegerVector2(1920, 1080));
                elements.add(liveInstance);
            }

            return elements;
        }

        private void rescaleElement(UIElement element, IntegerVector2 targetResolution){
            float scaleMultX = (float)targetResolution.x / referenceWidth;
            float scaleMultY = (float)targetResolution.y / referenceHeight;

            element.setWorldPosition((int) (element.getWorldPositionX() * scaleMultX), (int) (element.getWorldPositionY() * scaleMultY));
            element.setDimensions((int) (element.getWidth() * scaleMultX), (int) (element.getHeight() * scaleMultY));
        }
    }
}
