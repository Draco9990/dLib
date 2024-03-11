package dLib.ui.elements.prefabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.bindings.texture.TextureThemeBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inputfield extends UIElement {
    //region Variables

    private Button background;
    private TextBox textBox;

    private List<Character> characterFilter = new ArrayList<>();
    private int characterLimit = -1;

    private EInputfieldPreset preset;

    //Temps

    private InputProcessor cachedInputProcessor;
    private InputProcessor inputProcessor;

    private boolean holdingDelete = false;
    private float deleteTimerCount = 0;

    //endregion

    //region Constructors

    public Inputfield(String initialValue, int posX, int posY, int width, int height){
        super(posX, posY, width, height);

        preInitialize();

        this.background = new Button(0, 0, width, height){
            @Override
            public void onSelectionStateChanged() {
                super.onSelectionStateChanged();
                if(isSelected()){
                    Gdx.input.setInputProcessor(inputProcessor);
                }
                else{
                    resetInputProcessor();
                }
            }
        }.setImage(UIThemeManager.getDefaultTheme().inputfield);
        addChildCS(this.background);

        this.textBox = new TextBox(initialValue, 0, 0, width, height, 0.025f, 0.025f){
            @Override
            public void setText(String text) {
                if(preset == EInputfieldPreset.NUMERICAL_DECIMAL || preset == EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE){
                    if(Objects.equals(this.getText(), "1") && text.length() > 1){
                        text = text.substring(1);
                    }
                }
                super.setText(text);
            }
        }.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        textBox.setOnTextChangedLine("Value changed to: " + textBox.getText());
        addChildNCS(textBox);
    }

    public Inputfield(InputfieldData data){
        super(data);

        preInitialize();

        this.background = data.buttonData.makeUIElement();
        addChildCS(this.background);

        this.textBox = data.textboxData.makeUIElement();
        addChildNCS(this.textBox);

        characterFilter = data.characterFilter;
        characterLimit = data.characterLimit;

        setPreset(data.inputfieldPreset);
    }

    public void preInitialize(){
        cachedInputProcessor = Gdx.input.getInputProcessor();

        inputProcessor = new InputAdapter(){
            @Override
            public boolean keyTyped(char character) {
                if(characterLimit >= 0 && textBox.getText().length() >= characterLimit) return false;
                if(Character.isISOControl(character)) return false;
                if(!characterFilter.isEmpty() && !characterFilter.contains(character)) return false;

                addCharacter(character);
                return true;
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

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(holdingDelete){
            float delta = Gdx.graphics.getDeltaTime();
            deleteTimerCount += delta;
            if(deleteTimerCount > 1){
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

    //region Preset & Filters

    public Inputfield setPreset(EInputfieldPreset preset){
        characterFilter.clear();
        this.preset = preset;

        if(preset == EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE || preset == EInputfieldPreset.NUMERICAL_DECIMAL){
            filterAddNumerical();
        }

        if(preset == EInputfieldPreset.NUMERICAL_DECIMAL){
            characterFilter.add('.');
        }

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

    //endregion

    public enum EInputfieldPreset {
        GENERIC,
        NUMERICAL_WHOLE_POSITIVE,
        NUMERICAL_DECIMAL
    }

    public static class InputfieldData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public Button.ButtonData buttonData = new Button.ButtonData();
        public TextBox.TextBoxData textboxData = new TextBox.TextBoxData();

        public List<Character> characterFilter = new ArrayList<>();
        public int characterLimit = -1;

        Inputfield.EInputfieldPreset inputfieldPreset = Inputfield.EInputfieldPreset.GENERIC;

        public InputfieldData(){
            buttonData.textureBinding = new TextureThemeBinding("inputfield");
        }

        @Override
        public UIElement makeUIElement() {
            return new Inputfield(this);
        }
    }
}
