package dLib.ui.elements.items.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Inputfield extends Button {
    //region Variables

    public TextBox textBox;
    public TextBox previewTextBox;

    private List<Character> characterFilter = new ArrayList<>();
    private int characterLimit = -1;

    private EInputfieldPreset preset;

    public Event<Consumer<String>> onValueChangedEvent = new Event<>();
    public Event<Consumer<String>> onValueCommittedEvent = new Event<>();

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

        setImage(Tex.stat(UICommonResources.inputfield));

        this.textBox = new TextBox(initialValue, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        textBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        textBox.setOnTextChangedLine("Value changed to: " + textBox.getText());
        textBox.setPaddingLeft(Padd.px(20));
        addChild(textBox);

        this.previewTextBox = new TextBox("", Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
            @Override
            protected boolean shouldRender() {
                return super.shouldRender() && textBox.getText().isEmpty();
            }
        };
        previewTextBox.setTextRenderColor(Color.DARK_GRAY);
        previewTextBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        previewTextBox.setPaddingLeft(Padd.px(20));
        addChild(this.previewTextBox);

        textBox.setPadding(Padd.px(10));

        postInitialize();
    }

    public Inputfield(InputfieldData data){
        super(data);

        preInitialize();

        addChild(this.textBox);

        addChild(this.previewTextBox);

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
        onSelectionStateChangedEvent.subscribeManaged(aBoolean -> {
            if(aBoolean){
                Gdx.input.setInputProcessor(inputProcessor);
            }
            else{
                resetInputProcessor();
                onValueCommittedEvent.invoke(stringConsumer -> stringConsumer.accept(textBox.getText()));
            }
        });

        textBox.onTextChangedEvent.subscribeManaged(s -> onValueChangedEvent.invoke(stringConsumer -> stringConsumer.accept(s)));
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

    //region Preset & Filters

    public void setPreset(EInputfieldPreset preset){
        characterFilter.clear();
        this.preset = preset;
    }

    public void filterAddNumerical(){
        for(char c = '0'; c <= '9'; c++){
            characterFilter.add(c);
        }
    }
    public void filterAddLowercase(){
        for(char c = 'a'; c <= 'z'; c++){
            characterFilter.add(c);
        }
    }
    public void filterAddUpercase(){
        for(char c = 'A'; c <= 'Z'; c++){
            characterFilter.add(c);
        }
    }
    public void filterAddAToZ(){
        filterAddLowercase();
        filterAddUpercase();
    }

    //endregion

    //region Blinking Cursor

    private void calculateCursorBlinkPosition(){
        //TODO
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

    public static class InputfieldData extends ButtonData implements Serializable {
        private static final long serialVersionUID = 1L;

        public TextBox.TextBoxData textboxData = new TextBox.TextBoxData();
        public TextBox.TextBoxData previewTextBoxData = new TextBox.TextBoxData();

        public List<Character> characterFilter = new ArrayList<>();
        public IntegerProperty characterLimit = new IntegerProperty(-1).setName("Character Limit");

        Inputfield.EInputfieldPreset inputfieldPreset = Inputfield.EInputfieldPreset.GENERIC;

        public InputfieldData(){
            super();
        }

        @Override
        public Inputfield makeUIElement_internal() {
            return new Inputfield(this);
        }
    }
}
