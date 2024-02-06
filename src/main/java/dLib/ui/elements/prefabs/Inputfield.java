package dLib.ui.elements.prefabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import dLib.ui.HorizontalAlignment;
import dLib.ui.data.prefabs.InputfieldData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.themes.UIThemeManager;

import java.util.ArrayList;
import java.util.List;

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

    /** Constructor */
    public Inputfield(int posX, int posY, int width, int height){
        super(posX, posY);

        preInitialize();

        this.textBox = new TextBox("", posX, posY, width, height).setHorizontalAlignment(HorizontalAlignment.LEFT);
        textBox.setOnTextChangedLine("Value changed to: " + textBox.getText());
        this.other.add(textBox);

        this.background = new Button(posX, posY, width, height){
            @Override
            protected void onSelected() {
                super.onSelected();
                Gdx.input.setInputProcessor(inputProcessor);
            }

            @Override
            protected void onUnselected() {
                super.onUnselected();
                resetInputProcessor();
            }
        }.setImage(UIThemeManager.getDefaultTheme().inputfield);
        this.middle = this.background;
    }

    public Inputfield(InputfieldData data){
        super(data);

        preInitialize();

        this.textBox = data.textboxData.makeLiveInstance();
        this.other.add(textBox);

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

    /** Button */
    public Button getButton(){
        return background;
    }

    /** Label */
    public TextBox getTextBox(){
        return textBox;
    }

    /** Builder methods */
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

    /** Misc methods */
    private void addCharacter(char character){
        this.textBox.setText(this.textBox.getText() + character);
    }
    private void removeLastCharacter(){
        if(this.textBox.getText().isEmpty()) return;

        this.textBox.setText(this.textBox.getText().substring(0, this.textBox.getText().length()-1));
    }

    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(cachedInputProcessor);
    }
}
