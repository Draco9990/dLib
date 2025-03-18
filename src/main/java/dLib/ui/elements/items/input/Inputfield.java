package dLib.ui.elements.items.input;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.text.InputCaret;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.IntegerVector2;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.AutoDimension;
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
    public ConsumerEvent<String> onValueConfirmedEvent = new ConsumerEvent<>();

    private InputCaret caret;
    private int caretOffset = 0; //! Offsets from the END of the text instead of the start

    //Temps

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

        setTexture(Tex.stat(UICommonResources.inputfield));

        this.textBox = new TextBox(initialValue, Pos.px(0), Pos.px(0), width instanceof AutoDimension ? Dim.auto() : Dim.fill(), height instanceof AutoDimension ? Dim.auto() : Dim.fill());
        textBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
        textBox.setOnTextChangedLine("Value changed to: " + textBox.getText());
        textBox.setPaddingLeft(Padd.px(20));
        addChild(textBox);

        this.previewTextBox = new TextBox("", Pos.px(0), Pos.px(0), width instanceof AutoDimension ? Dim.auto() : Dim.fill(), height instanceof AutoDimension ? Dim.auto() : Dim.fill()){
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

        caret = new InputCaret(Dim.px((int) Math.ceil(textBox.getFontSizeRaw())));
        caret.addComponent(new UITransientElementComponent());
        recalculateCaretPosition();
        textBox.addChild(caret);

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

        caret = new InputCaret(Dim.px((int) Math.ceil(textBox.getFontSizeRaw())));
        caret.addComponent(new UITransientElementComponent());
        recalculateCaretPosition();
        textBox.addChild(caret);

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
                    backwardErase();
                    holdingDelete = true;
                    return true;
                }
                else if(keycode == Input.Keys.ENTER){
                    onValueConfirmedEvent.invoke(textBox.getText());
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
        caret.hideAndDisableInstantly();

        onSelectionStateChangedEvent.subscribeManaged(aBoolean -> {
            if(aBoolean){
                Gdx.input.setInputProcessor(inputProcessor);
                caret.showAndEnableInstantly();
            }
            else{
                resetInputProcessor();
                onValueCommittedEvent.invoke(stringConsumer -> stringConsumer.accept(textBox.getText()));
                caret.hideAndDisableInstantly();
            }
        });

        textBox.onTextChangedEvent.subscribeManaged(s -> {
            onValueChangedEvent.invoke(stringConsumer -> stringConsumer.accept(s));
            recalculateCaretPosition();
        });

        textBox.onFontSizeChangedEvent.subscribe(this, (aFloat, aFloat2) -> {
            caret.setHeight(Dim.px((int) Math.ceil(aFloat)));
            recalculateCaretPosition();
        });
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
                backwardErase();
            }
        }
    }

    //endregion

    //region Caret

    private void recalculateCaretPosition(){
        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        int glyphCount = getTotalGlyphCount(layout.getValue());
        if(glyphCount == 0){
            caret.setWorldPosition(0, 0);
            return;
        }

        Pair<Integer, Integer> currentCaretPosition = getCurrentCaretPosition();

        GlyphLayout.GlyphRun currentRun = layout.getValue().runs.get(currentCaretPosition.getKey());
        Integer runGlyphIndex = currentCaretPosition.getValue();

        if(currentCaretPosition.getValue() == currentRun.glyphs.size && layout.getValue().runs.size > currentCaretPosition.getKey() + 1){
            currentRun = layout.getValue().runs.get(currentCaretPosition.getKey() + 1);
            runGlyphIndex = 0;
        }

        float x = layout.getKey().x + currentRun.x;
        float y = layout.getKey().y + currentRun.y - textBox.getFontSizeRaw();

        for (int i = 0; i < Math.min(currentRun.glyphs.size + 1, runGlyphIndex + 1); i++) {
            x += currentRun.xAdvances.get(i);
        }

        caret.setWorldPosition((int) (x / Settings.xScale), (int) (y / Settings.yScale));
        caret.playAnimation(caret.getIdleAnimation());
    }

    private Pair<Integer, Integer> getCurrentCaretPosition(){
        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        int glyphCount = getTotalGlyphCount(layout.getValue());
        if(glyphCount == 0){
            return new Pair<>(0, 0);
        }

        int caretOffsetFromStart = glyphCount - caretOffset;

        int currentRun = 0;
        GlyphLayout.GlyphRun charRun = layout.getValue().runs.get(currentRun);
        while(caretOffsetFromStart > charRun.glyphs.size){
            caretOffsetFromStart -= charRun.glyphs.size;
            charRun = layout.getValue().runs.get(++currentRun);
        }

        if(caretOffsetFromStart == charRun.glyphs.size && layout.getValue().runs.size > currentRun + 1){
            caretOffsetFromStart = 0;
            currentRun++;
        }

        return new Pair<>(currentRun, caretOffsetFromStart);
    }

    private int getTotalGlyphCount(GlyphLayout layout){
        int count = 0;
        for (GlyphLayout.GlyphRun run : layout.runs) {
            count += run.glyphs.size;
        }
        return count;
    }

    private String getGlyphText(GlyphLayout layout){
        StringBuilder builder = new StringBuilder();
        for (GlyphLayout.GlyphRun run : layout.runs) {
            for (int i = 0; i < run.glyphs.size; i++) {
                builder.append(run.glyphs.get(i));
            }
        }
        return builder.toString();
    }

    private int getCaretOffsetFromRealText(GlyphLayout layout, boolean withHesitancy){
        String glyphText = getGlyphText(layout);
        String realText = textBox.getText();
        if(glyphText.isEmpty() || realText.isEmpty()){
            return 0;
        }

        int hesitancy = 0;
        int caretOffsetRemaining = caretOffset;
        int realIndex = realText.length() - 1;
        int bracketDepth = 0;
        for(int glyphIndex = glyphText.length() - 1; glyphIndex >= 0; glyphIndex--, realIndex--, caretOffsetRemaining--){
            while (glyphText.charAt(glyphIndex) != realText.charAt(realIndex) || bracketDepth > 0){
                if(realText.charAt(realIndex) == ']'){
                    bracketDepth++;
                }
                else if(realText.charAt(realIndex) == '['){
                    bracketDepth--;
                }

                realIndex--;
                hesitancy++;
            }

            if(caretOffsetRemaining == 0){
                return (realText.length() - 1) - (realIndex + (withHesitancy ? hesitancy : 0));
            }

            hesitancy = 0;
        }

        return 0;
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

    //region Input Processing

    private void addCharacter(char character){
        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();

        String currentText = this.textBox.getText();
        int insertPos = currentText.length() - getCaretOffsetFromRealText(layout.getValue(), true);

        // Ensure insertPos is within bounds
        insertPos = Math.max(0, Math.min(insertPos, currentText.length()));

        String newText = currentText.substring(0, insertPos) + character + currentText.substring(insertPos);
        this.textBox.setText(newText);
    }

    private void backwardErase(){
        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        if(getTotalGlyphCount(layout.getValue()) == 0){
            return;
        }

        String currentText = this.textBox.getText();
        int deletePos = currentText.length() - getCaretOffsetFromRealText(layout.getValue(), false);

        // Ensure deletePos is valid and there's something to delete
        if (deletePos <= 0 || deletePos > currentText.length()) {
            return;
        }

        String newText = currentText.substring(0, deletePos - 1) + currentText.substring(deletePos);

        if(newText.isEmpty() && (preset == EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE || preset == EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE || preset == EInputfieldPreset.NUMERICAL_WHOLE || preset == EInputfieldPreset.NUMERICAL_DECIMAL)){
            newText = "0";
        }

        if(newText.equals("-")){
            newText = "-0";
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

    //region KYB controls

    @Override
    public boolean onLeftInteraction() {
        if(caretOffset == textBox.getText().length()) return true;

        caretOffset++;
        recalculateCaretPosition();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        if(caretOffset == 0) return true;

        caretOffset--;
        recalculateCaretPosition();
        return true;
    }

    @Override
    public boolean onUpInteraction() {
        if(textBox.getText().isEmpty()) return super.onUpInteraction();

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        if(layout.getValue().runs.size == 1) return super.onUpInteraction();

        Pair<Integer, Integer> currentCaretPosition = getCurrentCaretPosition();
        if(currentCaretPosition.getKey() == 0) return super.onUpInteraction();

        GlyphLayout.GlyphRun currentRun = layout.getValue().runs.get(currentCaretPosition.getKey());
        GlyphLayout.GlyphRun prevRun = layout.getValue().runs.get(currentCaretPosition.getKey() - 1);

        float x = currentRun.x;
        for (int i = 0; i < Math.min(currentRun.glyphs.size + 1, currentCaretPosition.getValue() + 1); i++) {
            x += currentRun.xAdvances.get(i);
        }

        int newCaretOffset = 0;
        x -= prevRun.x;
        for (int i = 0; i < prevRun.glyphs.size; i++) {
            x -= prevRun.xAdvances.get(i);
            if(x < prevRun.xAdvances.get(i + 1) * 0.5f){
                newCaretOffset = i;
                break;
            }
        }

        caretOffset += currentCaretPosition.getValue() + (prevRun.glyphs.size - newCaretOffset);
        recalculateCaretPosition();
        return true;
    }

    @Override
    public boolean onDownInteraction() {
        if(textBox.getText().isEmpty()) return super.onDownInteraction();

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        if(layout.getValue().runs.size == 1) return super.onDownInteraction();

        Pair<Integer, Integer> currentCaretPosition = getCurrentCaretPosition();
        if(currentCaretPosition.getKey() == layout.getValue().runs.size - 1) return super.onDownInteraction();

        GlyphLayout.GlyphRun currentRun = layout.getValue().runs.get(currentCaretPosition.getKey());
        GlyphLayout.GlyphRun nextRun = layout.getValue().runs.get(currentCaretPosition.getKey() + 1);

        float x = currentRun.x;
        for (int i = 0; i < Math.min(currentRun.glyphs.size + 1, currentCaretPosition.getValue() + 1); i++) {
            x += currentRun.xAdvances.get(i);
        }

        int newCaretOffset = 0;
        x -= nextRun.x;
        for (int i = 0; i < nextRun.glyphs.size; i++) {
            x -= nextRun.xAdvances.get(i);
            if(x < nextRun.xAdvances.get(i + 1) * 0.5f){
                newCaretOffset = i;
                break;
            }
        }

        caretOffset -= (currentRun.glyphs.size - currentCaretPosition.getValue()) + newCaretOffset;
        recalculateCaretPosition();
        return true;
    }

    //endregion

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
