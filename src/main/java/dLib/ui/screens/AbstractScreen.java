package dLib.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import dLib.modcompat.ModManager;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.ElementManager;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.Help;
import sayTheSpire.Output;

import java.util.UUID;

// Abstract version of a screen
public abstract class AbstractScreen extends UIElement {
    /** Variables */
    protected UITheme theme;

    protected static UIStrings globalStrings;
    protected UIStrings localStrings;

    private InputProcessor cachedInputProcessor;
    private AbstractScreen screenToOpenOnClose;

    private boolean pendingRefresh = false;

    /** Constructors */
    public AbstractScreen(){
        super(0, 0, 1920, 1080);
        localStrings = CardCrawlGame.languagePack.getUIString(getModId() + ":" + this.getClass().getSimpleName());
        theme = UIThemeManager.getDefaultTheme();
    }

    /** Methods */
    //region Update & Render
    public void update(){
        if(!shouldUpdate()) return;
        super.update();

        if(pendingRefresh) {
            refreshScreen();
            pendingRefresh = false;
        }
    }
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

    //region Refresh
    public void markForRefresh(){
        pendingRefresh = true;
    }
    protected void refreshScreen(){}
    //endregion

    /** DEPRECATED */

    /** Theme */
    public AbstractScreen setThemeOverride(UITheme theme){
        this.theme = theme;
        return this;
    }

    /** Open & Close*/
    public void close(){
        ScreenManager.closeScreen();
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

    protected void addGenericBackground(){
        addChildNCS(new Image(theme.background, 0, 0, getWidth(), getHeight()));
    }

    /** Input */
    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(cachedInputProcessor);
    }

    /** Iteration */
    public void onIterationReachedTop(){}
    public void onIterationReachedBottom(){}

    /** Say the Spire */
    public abstract String getModId();
    public String getOnScreenOpenLine(){ return null; }
}
