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
import sayTheSpire.Output;

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

        selectPreviousChild();
        return true;
    }

    @Override
    public boolean onUpInteraction() {
        if(parent != null) return super.onUpInteraction();

        selectNextChild();
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
}
