package dLib.ui.elements.prefabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.HorizontalAlignment;
import dLib.ui.elements.ElementGroup;

import java.util.ArrayList;
import java.util.List;

public class Inputfield extends ElementGroup {
    private Button background;
    private TextBox textBox;

    private InputProcessor cachedInputProcessor;
    private InputProcessor inputProcessor;

    private List<Character> characterFilter = new ArrayList<>();
    private int characterLimit = -1;

    private boolean holdingDelete = false;
    private float deleteTimerCount = 0;

    public Inputfield(Texture background, int posX, int posY, int width, int height){
        cachedInputProcessor = Gdx.input.getInputProcessor();
        Inputfield self = this;
        this.textBox = new TextBox("", posX, posY, width, height){
            @Override
            public String getOnTextChangedLine(String newText) {
                return self.getOnTextChangedLine(newText);
            }
        }.setHorizontalAlignment(HorizontalAlignment.LEFT);
        this.other.add(textBox);

        this.background = new Button(background, posX, posY, width, height){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                Gdx.input.setInputProcessor(inputProcessor);
            }

            @Override
            public void select() {
                super.select();
                self.onHovered();
            }

            @Override
            public void deselect() {
                super.deselect();
                self.deselect();
                resetInputProcessor();
            }
        };
        this.middle = this.background;

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

    /** Builder methods */

    public Inputfield setText(String text){
        this.textBox.setText(text);
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

    public Inputfield setOnTextChangedLine(String newLine){
        textBox.setOnTextChangedLine(newLine);
        return this;
    }
    public String getOnTextChangedLine(String newText){return "";}

    /** Update and render */

    @Override
    public void update() {
        this.background.update();
        this.textBox.update();

        if(holdingDelete){
            float delta = Gdx.graphics.getDeltaTime();
            deleteTimerCount += delta;
            if(deleteTimerCount > 1){
                removeLastCharacter();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.background.render(sb);
        this.textBox.render(sb);
    }

    /** Misc methods */

    private void addCharacter(char character){
        this.textBox.setText(this.textBox.getText() + character);
        onTextChanged(this.textBox.getText());
    }
    private void removeLastCharacter(){
        if(this.textBox.getText().isEmpty()) return;

        this.textBox.setText(this.textBox.getText().substring(0, this.textBox.getText().length()-1));
        onTextChanged(this.textBox.getText());
    }

    private void onHovered(){}
    private void onUnhovered(){}

    public String getText() { return textBox.getText(); }
    protected void onTextChanged(String newText){}
    public void setTextColor(Color color){
        this.textBox.setRenderColor(color);
    }

    protected void setVisibility(boolean visible) {
        background.setVisibility(visible);
        textBox.setVisibility(visible);
    }
    public boolean isVisible() {
        return background.isVisible() || textBox.isVisible();
    }

    protected void setEnabled(boolean enabled) {
        background.setEnabled(enabled);
        textBox.setEnabled(enabled);
    }
    public boolean isEnabled() {
        return background.isEnabled() || textBox.isEnabled();
    }

    public boolean isActive() {
        return isEnabled() && isActive();
    }

    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(cachedInputProcessor);
    }
}
