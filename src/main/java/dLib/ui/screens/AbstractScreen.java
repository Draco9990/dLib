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
import dLib.ui.elements.ElementGroupManager;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.elements.prefabs.Image;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.util.Help;
import sayTheSpire.Output;

import java.util.ArrayList;
import java.util.UUID;

// Abstract version of a screen
public abstract class AbstractScreen {
    /** Variables */
    private String ID;

    protected Renderable background;
    protected Interactable cancelElement;

    private ArrayList<UIElement> backgroundElements = new ArrayList<>();
    protected ElementGroupManager interactableElements = new ElementGroupManager();
    private ArrayList<UIElement> foregroundElements = new ArrayList<>();

    protected UITheme theme;

    protected static UIStrings globalStrings;
    protected UIStrings localStrings;

    private InputProcessor cachedInputProcessor;

    private boolean pendingRefresh = false;
    private boolean pendingTempPurge = false;

    private AbstractScreen screenToOpenOnClose;

    private boolean hidden = false;

    /** Constructors */
    public AbstractScreen(){
        localStrings = CardCrawlGame.languagePack.getUIString(getModId() + ":" + this.getClass().getSimpleName());
        this.ID = "Screen_" + UUID.randomUUID();
        hidden = false;
        theme = UIThemeManager.getDefaultTheme();
    }

    /** Update & Render */
    public void update(){
        if(hidden) return;

        updateInput();

        foregroundElements.forEach(UIElement::update);
        interactableElements.update();
        backgroundElements.forEach(UIElement::update);
        if(background != null) background.update();

        if(pendingRefresh) {
            refreshScreen();
            pendingRefresh = false;
        }
    }
    public void render(SpriteBatch sb){
        if(hidden) return;

        if(background != null) background.render(sb);
        backgroundElements.forEach(element -> element.render(sb));
        interactableElements.render(sb);
        foregroundElements.forEach(element -> element.render(sb));
    }

    /** ID */
    public void setId(String newID){
        this.ID = newID;
    }
    public String getId(){
        return ID;
    }

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

    /** Show & Hide */
    public void hide(){
        hidden = true;
    }
    public void show(){
        hidden = false;
    }

    /** Elements */
    protected void addElementToBackground(UIElement element){
        backgroundElements.add(element);
    }
    protected void addInteractableElement(UIElement element){
        CompositeUIElement compositeUIElement = new CompositeUIElement(element.getPositionX(), element.getPositionY(), element.getWidth(), element.getHeight());
        compositeUIElement.middle = element;
        interactableElements.addElement(compositeUIElement);
    }
    protected void addInteractableElement(UIElement element, boolean temporary){
        CompositeUIElement compositeUIElement = new CompositeUIElement(element.getPositionX(), element.getPositionY(), element.getWidth(), element.getHeight());
        compositeUIElement.middle = element;
        compositeUIElement.temporary = temporary;
        interactableElements.addElement(compositeUIElement);
    }
    protected void addInteractableElement(CompositeUIElement compositeUIElement){
        interactableElements.addElement(compositeUIElement);
    }
    protected void addElementToForeground(UIElement element){
        foregroundElements.add(element);
    }

    protected void removeElementFromBackground(UIElement element){
        backgroundElements.remove(element);
    }
    protected void removeInteractableElement(UIElement element){
        for(CompositeUIElement interactableElement : interactableElements.getElements()){
            interactableElement.removeUIElement(element);
        }
    }
    protected void removeElementFromForeground(UIElement element){
        foregroundElements.remove(element);
    }

    protected void addGenericBackground(){
        this.background = new Image(theme.background, 0, 0, 1920, 1080);
    }
    protected void registerCancelElement(Interactable interactable){
        this.cancelElement = interactable;
        addInteractableElement(cancelElement);
    }

    /** Input */
    private void updateInput(){
        if(Help.Input.isPressed(CInputActionSet.down, InputActionSet.down)) interactableElements.onDownInteraction(this);
        if(Help.Input.isPressed(CInputActionSet.up, InputActionSet.up)) interactableElements.onUpInteraction(this);
        if(Help.Input.isPressed(CInputActionSet.left, InputActionSet.left)) interactableElements.onLeftInteraction(this);
        if(Help.Input.isPressed(CInputActionSet.right, InputActionSet.right)) interactableElements.onRightInteraction(this);
        if(Help.Input.isPressed(CInputActionSet.proceed, InputActionSet.confirm)) interactableElements.onConfirmInteraction(this);
        if(Help.Input.isPressed(CInputActionSet.cancel, InputActionSet.cancel)) onCancelButtonPressed();
    }

    private void onCancelButtonPressed(){
        cancelElement.clickLeft();
    }

    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(cachedInputProcessor);
    }

    /** Iteration */
    public void onIterationReachedTop(){}
    public void onIterationReachedBottom(){}

    /** Refresh & Temp Purge*/
    public void markForRefresh(){
        pendingRefresh = true;
    }
    public void markForTempPurge(){
        pendingRefresh = true;
        pendingTempPurge = true;
    }

    protected void refreshScreen(){
        if(pendingTempPurge){
            interactableElements.purgeTempElements();
            pendingTempPurge = false;

            onTempPurge();
        }
    }
    protected void onTempPurge(){
    }

    /** Say the Spire */
    public abstract String getModId();
    public String getOnScreenOpenLine(){ return null; }
}
