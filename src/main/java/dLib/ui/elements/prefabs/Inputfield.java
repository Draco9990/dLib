package dLib.ui.elements.prefabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import dLib.ui.Alignment;
import dLib.ui.data.prefabs.InputfieldData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.themes.UIThemeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inputfield extends CompositeUIElement {
    /** Variables */
    private Button background;
    private TextBox textBox;

    private InputProcessor cachedInputProcessor;
    private InputProcessor inputProcessor;

    private List<Character> characterFilter = new ArrayList<>();
    private int characterLimit = -1;

    private boolean holdingDelete = false;
    private float deleteTimerCount = 0;

    private EInputfieldType type;

    /** Constructor */
    public Inputfield(String initialValue, int posX, int posY, int width, int height){
        super(posX, posY, width, height);

        preInitialize();

        this.textBox = new TextBox(initialValue, posX, posY, width, height, 0.025f, 0.025f){
            @Override
            public void setText(String text) {
                if(type == EInputfieldType.NUMERICAL_DECIMAL || type == EInputfieldType.NUMERICAL_WHOLE_POSITIVE){
                    if(Objects.equals(this.getText(), "1") && text.length() > 1){
                        text = text.substring(1);
                    }
                }
                super.setText(text);
            }
        }.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        textBox.setOnTextChangedLine("Value changed to: " + textBox.getText());
        this.foreground.add(textBox);

        this.background = new Button(posX, posY, width, height){
            @Override
            protected void onSelected() {
                super.onSelected();
                Gdx.input.setInputProcessor(inputProcessor);
            }

            @Override
            protected void onDeselected() {
                super.onDeselected();
                resetInputProcessor();
            }
        }.setImage(UIThemeManager.getDefaultTheme().inputfield);
        this.middle = this.background;
    }

    public Inputfield(InputfieldData data){
        super(data);

        preInitialize();

        this.textBox = data.textboxData.makeLiveInstance();
        this.foreground.add(textBox);

        this.background = data.buttonData.makeLiveInstance();
        this.middle = background;

        characterFilter = data.characterFilter;
        characterLimit = data.characterLimit;
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

    /** Update and render */
    @Override
    public void update() {
        super.update();

        if(holdingDelete){
            float delta = Gdx.graphics.getDeltaTime();
            deleteTimerCount += delta;
            if(deleteTimerCount > 1){
                removeLastCharacter();
            }
        }
    }

    /** Button */
    public Button getButton(){
        return background;
    }

    /** Label */
    public TextBox getTextBox(){
        return textBox;
    }

    /** Typing */
    public Inputfield setType(EInputfieldType type){
        characterFilter.clear();
        this.type = type;

        if(type == EInputfieldType.NUMERICAL_WHOLE_POSITIVE || type == EInputfieldType.NUMERICAL_DECIMAL){
            filterAddNumerical();
        }

        if(type == EInputfieldType.NUMERICAL_DECIMAL){
            characterFilter.add('.');
        }

        return this;
    }

    /** Filters */
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



    /** Misc methods */
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

    public enum EInputfieldType{
        GENERIC,
        NUMERICAL_WHOLE_POSITIVE,
        NUMERICAL_DECIMAL
    }
}
