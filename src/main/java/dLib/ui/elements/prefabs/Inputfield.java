package dLib.ui.elements.prefabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;

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

    public Inputfield(String initialValue, int posX, int posY, int width, int height){
        super(posX, posY, width, height);

        preInitialize();

        this.background = new Button(0, 0, width, height).setImage(UIThemeManager.getDefaultTheme().inputfield);
        addChildNCS(this.background);

        this.textBox = new TextBox(initialValue, 0, 0, width, height).setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        textBox.setOnTextChangedLine("Value changed to: " + textBox.getText());
        textBox.setPadding(20, 0, 0, 0);
        addChildCS(textBox);

        this.previewTextBox = new TextBox("", 0, 0, width, height){
            @Override
            protected boolean shouldRender() {
                return super.shouldRender() && textBox.getText().isEmpty();
            }
        }.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT).setTextRenderColor(Color.DARK_GRAY);
        previewTextBox.setPadding(20, 0, 0, 0);
        addChildNCS(this.previewTextBox);

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

                if(character == '\u0016'){
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
                    if(preset == EInputfieldPreset.NUMERICAL_DECIMAL || preset == EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE){
                        if (c < '0' || c > '9') {
                            if(c != '.' || preset != EInputfieldPreset.NUMERICAL_DECIMAL || textBox.getText().contains(".")){
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

        this.textBox.setText(this.textBox.getText().substring(0, this.textBox.getText().length()-1));
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
        NUMERICAL_WHOLE_POSITIVE,
        NUMERICAL_DECIMAL
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
            buttonData.textureBinding.setValue(new TextureThemeBinding("inputfield"));

            //add children transient property that manages this and below?
            dimensions.addOnValueChangedListener((integerVector2, integerVector22) -> {
                buttonData.dimensions.setValue(dimensions.getValue());
                textboxData.dimensions.setValue(dimensions.getValue());
            });
        }

        @Override
        public UIElement makeUIElement() {
            return new Inputfield(this);
        }
    }
}
