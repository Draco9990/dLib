package dLib.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import dLib.modcompat.ModManager;
import dLib.ui.elements.ElementGroup;
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

    private String ID;

    protected static UIStrings globalStrings;
    protected UIStrings localStrings;

    private InputProcessor cachedInputProcessor;

    protected Renderable background;
    protected Interactable cancelElement;

    private ArrayList<UIElement> backgroundElements = new ArrayList<>();
    protected ElementGroupManager interactableElements = new ElementGroupManager();
    private ArrayList<UIElement> foregroundElements = new ArrayList<>();

    protected UITheme theme;

    private boolean pendingRefresh = false;
    private boolean pendingTempPurge = false;

    private AbstractScreen screenToOpenOnClose;

    public AbstractScreen(){
        localStrings = CardCrawlGame.languagePack.getUIString(getModId() + ":" + this.getClass().getSimpleName());
        this.ID = "Screen_" + UUID.randomUUID();
        theme = UIThemeManager.getDefaultTheme();
    }

    public void setId(String newID){
        this.ID = newID;
    }
    public String getId(){
        return ID;
    }

    public AbstractScreen setThemeOverride(UITheme theme){
        this.theme = theme;
        return this;
    }

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

    public void update(){
        updateInput();

        if(background != null) background.update();
        backgroundElements.forEach(UIElement::update);
        interactableElements.update();
        foregroundElements.forEach(UIElement::update);

        if(pendingRefresh) {
            refreshScreen();
            pendingRefresh = false;
        }
    }
    public void render(SpriteBatch sb){
        if(background != null) background.render(sb);
        backgroundElements.forEach(element -> element.render(sb));
        interactableElements.render(sb);
        foregroundElements.forEach(element -> element.render(sb));
    }

    protected void addElementToBackground(UIElement element){
        backgroundElements.add(element);
    }
    protected void addInteractableElement(UIElement element){
        ElementGroup elementGroup = new ElementGroup();
        elementGroup.middle = element;
        interactableElements.addElement(elementGroup);
    }
    protected void addInteractableElement(UIElement element, boolean temporary){
        ElementGroup elementGroup = new ElementGroup();
        elementGroup.middle = element;
        elementGroup.temporary = temporary;
        interactableElements.addElement(elementGroup);
    }
    protected void addInteractableElement(ElementGroup elementGroup){
        interactableElements.addElement(elementGroup);
    }
    protected void addElementToForeground(UIElement element){
        foregroundElements.add(element);
    }

    protected void registerGenericBackground(){
        this.background = new Image(theme.background, 0, 0, 1920, 1080);
    }
    protected void registerCancelElement(Interactable interactable){
        this.cancelElement = interactable;
        addInteractableElement(cancelElement);
    }

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

    public void onIterationReachedTop(){}
    public void onIterationReachedBottom(){}

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

    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(cachedInputProcessor);
    }

    public void setScreenToOpenOnClose(AbstractScreen screen){
        this.screenToOpenOnClose = screen;
    }
    public AbstractScreen getScreenToOpenOnClose(){ return this.screenToOpenOnClose;}

    public abstract String getModId();
    public String getOnScreenOpenLine(){ return null; }
}
