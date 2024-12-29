package dLib.ui.elements.items.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Button;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;

import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Inputfield extends UIElement {
    //region Variables

    private Button background;
    private TextBox textBox;

    private TextBox previewTextBox;

    private List<Character> characterFilter = new ArrayList<>();
    private int characterLimit = -1;

    private EInputfieldPreset preset;

    //Temps

    private ArrayList<Consumer<String>> onValueChangedListeners = new ArrayList<>();
    private ArrayList<Consumer<String>> onValueCommittedListeners = new ArrayList<>();

    private InputProcessor cachedInputProcessor;
    private InputProcessor inputProcessor;

    private boolean holdingDelete = false;
    private float deleteTimerCount = 0;

    private float blinkingCursorPosX = 0;

    //endregion

    //region Constructors

    public Inputfield(String initialValue, AbstractDimension width, AbstractDimension height){
        this(initialValue, Pos.px(0), Pos.px(0), width, height);
    }

    public Inputfield(String initialValue, AbstractPosition posX, AbstractPosition posY, AbstractDimension width, AbstractDimension height){
        super(posX, posY, width, height);

        preInitialize();

        this.background = new Button(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        this.background.setImage(Tex.stat(UICommonResources.inputfield));
        addChildNCS(this.background);

        this.textBox = (TextBox) new TextBox(initialValue, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()).setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        textBox.setOnTextChangedLine("Value changed to: " + textBox.getText());
        textBox.setPaddingLeft(Padd.px(20));
        addChildNCS(textBox);

        this.previewTextBox = (TextBox) new TextBox("", Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
            @Override
            protected boolean shouldRender() {
                return super.shouldRender() && textBox.getText().isEmpty();
            }
        }.setTextRenderColor(Color.DARK_GRAY).setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        previewTextBox.setPaddingLeft(Padd.px(20));
        addChildNCS(this.previewTextBox);

        textBox.setPadding(Padd.px(10));

        postInitialize();
    }

    public Inputfield(InputfieldData data){
        super(data);

        preInitialize();

        this.background = data.buttonData.makeUIElement();
        addChildNCS(this.background);

        this.textBox = data.textboxData.makeUIElement();
        addChildCS(this.textBox);

        this.previewTextBox = data.previewTextBoxData.makeUIElement();
        addChildCS(this.previewTextBox);

        characterFilter = data.characterFilter;
        characterLimit = data.characterLimit.getValue();

        setPreset(data.inputfieldPreset);

        postInitialize();
    }

    private void preInitialize(){
        cachedInputProcessor = Gdx.input.getInputProcessor();

        inputProcessor = new InputAdapter(){
            @Override
            public boolean keyTyped(char character) {
                char[] charsToAdd = new char[]{character};

                if(InputHelper.isPasteJustPressed()){
                    charsToAdd = Gdx.app.getClipboard().getContents().toCharArray();
                }

                boolean success = true;

                for(char c : charsToAdd){
                    if(characterLimit >= 0 && textBox.getText().length() >= characterLimit) {
                        success = false;
                        continue;
                    }

                    if(Character.isISOControl(c)) {
                        success = false;
                        continue;
                    }

                    boolean respectsPreset = preset != EInputfieldPreset.GENERIC;
                    if(preset == EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE || preset == EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE
                            || preset == EInputfieldPreset.NUMERICAL_DECIMAL || preset == EInputfieldPreset.NUMERICAL_WHOLE){
                        if (c < '0' || c > '9') {
                            if(c == '.'){
                                if(textBox.getText().contains(".")){
                                    respectsPreset = false;
                                }
                                if(preset != EInputfieldPreset.NUMERICAL_DECIMAL && preset != EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE){
                                    respectsPreset = false;
                                }
                            }
                            else if(c == '-'){
                                respectsPreset = false;

                                if(preset == EInputfieldPreset.NUMERICAL_WHOLE || preset == EInputfieldPreset.NUMERICAL_DECIMAL){
                                    if(textBox.getText().startsWith("-")){
                                        textBox.setText(textBox.getText().substring(1));
                                    }
                                    else{
                                        textBox.setText("-" + textBox.getText());
                                    }
                                }
                            }
                            else{
                                respectsPreset = false;
                            }
                        }
                    }

                    if(!characterFilter.isEmpty() && !characterFilter.contains(c)) {
                        if(!respectsPreset){
                            success = false;
                            continue;
                        }
                    }
                    else if(!respectsPreset && preset != EInputfieldPreset.GENERIC){
                        success = false;
                        continue;
                    }

                    addCharacter(c);
                }

                return success;
            }

            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACKSPACE){
                    removeLastCharacter();
                    holdingDelete = true;
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if(keycode == Input.Keys.BACKSPACE){
                    holdingDelete = false;
                    deleteTimerCount = 0;
                    return true;
                }
                return false;
            }
        };
    }

    private void postInitialize(){
        background.addOnSelectionStateChangedConsumer(aBoolean -> {
            if(aBoolean){
                Gdx.input.setInputProcessor(inputProcessor);
            }
            else{
                resetInputProcessor();
                onValueCommitted();
            }
        });

        textBox.addOnTextChangedConsumer(s -> onValueChanged());
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(holdingDelete){
            float delta = Gdx.graphics.getDeltaTime();
            deleteTimerCount += delta;
            if(deleteTimerCount > 0.5){
                removeLastCharacter();
            }
        }
    }

    //endregion

    //region Button

    public Button getButton(){
        return background;
    }

    //endregion

    //region TextBox

    public TextBox getTextBox(){
        return textBox;
    }

    //endregion

    //region Preview Text

    public Inputfield setPreviewText(String text){
        previewTextBox.setText(text);
        return this;
    }

    //endregion Preview Text

    //region Value

    public void onValueChanged(){
        for(Consumer<String> listener : onValueChangedListeners){
            listener.accept(textBox.getText());
        }
    }
    public void addOnValueChangedListener(Consumer<String> listener){
        onValueChangedListeners.add(listener);
    }

    public void onValueCommitted(){
        for(Consumer<String> listener : onValueCommittedListeners){
            listener.accept(textBox.getText());
        }
    }
    public void addOnValueCommittedListener(Consumer<String> listener){
        onValueCommittedListeners.add(listener);
    }

    //endregion

    //region Preset & Filters

    public Inputfield setPreset(EInputfieldPreset preset){
        characterFilter.clear();
        this.preset = preset;

        return this;
    }

    public Inputfield filterAddNumerical(){
        for(char c = '0'; c <= '9'; c++){
            characterFilter.add(c);
        }
        return this;
    }
    public Inputfield filterAddLowercase(){
        for(char c = 'a'; c <= 'z'; c++){
            characterFilter.add(c);
        }
        return this;
    }
    public Inputfield filterAddUpercase(){
        for(char c = 'A'; c <= 'Z'; c++){
            characterFilter.add(c);
        }
        return this;
    }
    public Inputfield filterAddAToZ(){
        filterAddLowercase();
        filterAddUpercase();

        return this;
    }

    //endregion

    //region Blinking Cursor

    private void calculateCursorBlinkPosition(){

    }

    //endregion

    //region Input Processing

    private void addCharacter(char character){
        this.textBox.setText(this.textBox.getText() + character);
    }
    private void removeLastCharacter(){
        if(this.textBox.getText().isEmpty()) return;

        String newText = this.textBox.getText().substring(0, this.textBox.getText().length()-1);
        if(newText.isEmpty() && (preset == EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE || preset == EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE)){
            newText = "0";
        }
        this.textBox.setText(newText);
    }

    public void resetInputProcessor(){
        if(Gdx.input.getInputProcessor() == inputProcessor){
            Gdx.input.setInputProcessor(cachedInputProcessor);
        }
    }

    //endregion

    //region Character Limit

    public Inputfield setCharacterLimit(int characterLimit){
        this.characterLimit = characterLimit;
        return this;
    }

    public int getCharacterLimit(){
        return characterLimit;
    }

    //endregion Character Limit

    //endregion

    public enum EInputfieldPreset {
        GENERIC,
        NUMERICAL_WHOLE,
        NUMERICAL_DECIMAL,
        NUMERICAL_WHOLE_POSITIVE,
        NUMERICAL_DECIMAL_POSITIVE
    }

    public static class InputfieldData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextBox.TextBoxData textboxData = new TextBox.TextBoxData();
        public Button.ButtonData buttonData = new Button.ButtonData();

        public TextBox.TextBoxData previewTextBoxData = new TextBox.TextBoxData();

        public List<Character> characterFilter = new ArrayList<>();
        public IntegerProperty characterLimit = new IntegerProperty(-1).setName("Character Limit");

        Inputfield.EInputfieldPreset inputfieldPreset = Inputfield.EInputfieldPreset.GENERIC;

        public InputfieldData(){
            super();
        }

        @Override
        public UIElement makeUIElement() {
            return new Inputfield(this);
        }
    }
}
