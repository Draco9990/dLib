package dLib.ui.elements.items.input;

import basemod.Pair;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.properties.objects.IntegerProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.buttons.Button;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private InputCharacterManager characterHbManager = new InputCharacterManager();
    private InputfieldToolbar toolbar;

    //Temps

    private InputProcessor cachedInputProcessor;
    private InputProcessor inputProcessor;

    private boolean holdingDelete = false;
    private boolean holdingForwardDelete = false;
    private float deleteTimerCount = 0;

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

        characterHbManager = new InputCharacterManager();
        textBox.addChild(characterHbManager);
        reinitializeCharacterHBs();

        toolbar = new InputfieldToolbar(Pos.px(0), Pos.perc(1));
        toolbar.setAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.BOTTOM);
        addChild(toolbar);

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

        characterHbManager = new InputCharacterManager();
        textBox.addChild(characterHbManager);
        reinitializeCharacterHBs();

        toolbar = new InputfieldToolbar(Pos.px(0), Pos.perc(1));
        toolbar.setAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.TOP);
        addChild(toolbar);

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

                if(InputHelper.isShortcutModifierKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.A)){
                    characterHbManager.selectAll();
                    return true;
                }

                if(InputHelper.isShortcutModifierKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.X)){
                    Gdx.app.getClipboard().setContents(getSelectionAsText());
                    eraseSelection();
                    return true;
                }

                if(InputHelper.isShortcutModifierKeyPressed() && Gdx.input.isKeyJustPressed(Input.Keys.C)){
                    Gdx.app.getClipboard().setContents(getSelectionAsText());
                    return true;
                }

                boolean success = true;

                List<Character> charsToAddList = new ArrayList<>();
                for(char c : charsToAdd){
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

                    charsToAddList.add(c);
                }

                success &= addCharacters(charsToAddList);

                return success;
            }

            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.BACKSPACE){
                    backwardErase();
                    holdingDelete = true;
                    return true;
                }
                else if(keycode == Input.Keys.FORWARD_DEL){
                    forwardErase();
                    holdingForwardDelete = true;
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
                else if(keycode == Input.Keys.FORWARD_DEL){
                    holdingForwardDelete = false;
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
            reinitializeCharacterHBs();
        });

        textBox.onFontSizeChangedEvent.subscribe(this, (aFloat, aFloat2) -> {
            caret.setHeight(Dim.px((int) Math.ceil(aFloat)));
            recalculateCaretPosition();
        });

        textBox.onDimensionsChangedEvent.subscribe(this, (element) -> {
            recalculateCaretPosition();
        });

        characterHbManager.onSelectionChangedEvent.subscribe(this, () -> {
            if(characterHbManager.hasValidUserSelection()){
                toolbar.showAndEnable();
            }
            else{
                toolbar.hideAndDisable();
            }
        });
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(holdingDelete || holdingForwardDelete){
            float delta = Gdx.graphics.getDeltaTime();
            deleteTimerCount += delta;
            if(deleteTimerCount > 0.75){
                if(holdingDelete) backwardErase();
                if(holdingForwardDelete) forwardErase();

                deleteTimerCount = 0.71f;
            }
        }
    }

    //endregion

    //region Caret

    private void recalculateCaretPosition(){
        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        int glyphCount = getTotalGlyphCount(layout.getValue());
        if(glyphCount == 0){
            caret.setWorldPosition((int) (layout.getKey().x / Settings.xScale), (int) (layout.getKey().y / Settings.yScale));
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

    private int getRealtextOffsetForCaretOffset(GlyphLayout layout, boolean withHesitancy){
        return getRealtextOffsetForGlyphOffset(layout, caretOffset, withHesitancy);
    }

    private int getRealtextOffsetForGlyphOffset(GlyphLayout layout, int offset, boolean withHesitancy){
        String glyphText = getGlyphText(layout);
        String realText = textBox.getText();
        if(glyphText.isEmpty() || realText.isEmpty()){
            return 0;
        }

        int hesitancy = 0;
        int realIndex = realText.length() - 1;
        for(int glyphIndex = glyphText.length() - 1; glyphIndex >= 0; glyphIndex--, realIndex--, offset--){
            while(true){
                if (realText.charAt(realIndex) != ']') {
                    break;
                }

                int bracketStart = realText.lastIndexOf('[', realIndex);
                int potentialNewBracketEnd = realText.indexOf(']', realIndex);
                if(potentialNewBracketEnd <= bracketStart){
                    break;
                }

                String markup = realText.substring(bracketStart + 1, potentialNewBracketEnd);
                if(!isValidMarkup(markup)){
                    break;
                }

                hesitancy += (realIndex - bracketStart) + 1;
                realIndex = bracketStart - 1;
                if(realIndex < 0){
                    break;
                }
            }

            if(offset == 0){
                return (realText.length() - 1) - (realIndex + (withHesitancy ? hesitancy : 0));
            }

            hesitancy = 0;
        }

        return (realText.length() - 1) - (realIndex + (withHesitancy ? hesitancy : 0));
    }

    //endregion

    //region Character Selection

    public void reinitializeCharacterHBs(){
        characterHbManager.clearChildren();

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();

        for (int runIndex = 0; runIndex < layout.getValue().runs.size; runIndex++){
            GlyphLayout.GlyphRun run = layout.getValue().runs.get(runIndex);
            float runX = layout.getKey().x + run.x + run.xAdvances.get(0);
            float runY = layout.getKey().y + run.y - textBox.getFontSizeRaw();

            for(int glyphIndex = 0; glyphIndex < run.glyphs.size; glyphIndex++){
                BitmapFont.Glyph glyph = run.glyphs.get(glyphIndex);

                InputCharacterHB glyphHbLeft = new InputCharacterHB(
                        Pos.px(0),
                        Pos.px(0),
                        Dim.px((int) Math.floor((run.xAdvances.get(glyphIndex + 1) * 0.5f) / Settings.xScale)),
                        Dim.px((int) textBox.getFontSizeRaw()),
                        runIndex, glyphIndex, InputCharacterHB.ECharHbSide.Left);
                textBox.addChild(glyphHbLeft);
                characterHbManager.addChild(glyphHbLeft);
                glyphHbLeft.onLeftClickEvent.subscribe(this, () -> {
                    int totalCharsUpTo = 0;
                    Pair<IntegerVector2, GlyphLayout> layout1 = textBox.prepareForRender();
                    for (int i = 0; i < glyphHbLeft.glyphRowIndex; i++){
                        totalCharsUpTo += layout1.getValue().runs.get(i).glyphs.size;
                    }
                    totalCharsUpTo += glyphHbLeft.glyphIndex;

                    caretOffset = getTotalGlyphCount(layout1.getValue()) - totalCharsUpTo;
                    recalculateCaretPosition();
                });
                glyphHbLeft.setWorldPosition((int) Math.floor(runX / Settings.xScale), (int) (runY / Settings.yScale));

                InputCharacterHB glyphHbRight = new InputCharacterHB(
                        Pos.px(0),
                        Pos.px(0),
                        Dim.px((int) Math.ceil((run.xAdvances.get(glyphIndex + 1) * 0.5f) / Settings.xScale)),
                        Dim.px((int) textBox.getFontSizeRaw()),
                        runIndex, glyphIndex + 1, InputCharacterHB.ECharHbSide.Right);
                textBox.addChild(glyphHbRight);
                characterHbManager.addChild(glyphHbRight);
                glyphHbRight.onLeftClickEvent.subscribe(this, () -> {
                    int totalCharsUpTo = 0;
                    Pair<IntegerVector2, GlyphLayout> layout1 = textBox.prepareForRender();
                    for (int i = 0; i < glyphHbRight.glyphRowIndex; i++){
                        totalCharsUpTo += layout1.getValue().runs.get(i).glyphs.size;
                    }
                    totalCharsUpTo += glyphHbRight.glyphIndex;

                    caretOffset = getTotalGlyphCount(layout1.getValue()) - totalCharsUpTo;
                    recalculateCaretPosition();
                });
                glyphHbRight.setWorldPosition((int) Math.floor((runX + run.xAdvances.get(glyphIndex + 1) * 0.5f) / Settings.xScale) , (int) (runY / Settings.yScale));

                runX += run.xAdvances.get(glyphIndex + 1);
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

    //region Input Processing

    private boolean addCharacters(List<Character> characters){
        if(characters.isEmpty()){
            return false;
        }

        if(characterHbManager.hasValidUserSelection()){
            eraseSelection();
        }

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();

        if(characterLimit >= 0) {
            int currentGlyphCount = getTotalGlyphCount(layout.getValue());
            while (currentGlyphCount + characters.size() > characterLimit) {
                characters.remove(characters.size() - 1);

                if(characters.isEmpty()){
                    return false;
                }
            }
        }

        String currentText = this.textBox.getText();
        int insertPos = currentText.length() - getRealtextOffsetForCaretOffset(layout.getValue(), true);

        // Ensure insertPos is within bounds
        insertPos = Math.max(0, Math.min(insertPos, currentText.length()));

        String newText = currentText.substring(0, insertPos) + characters.stream().map(String::valueOf).collect(Collectors.joining()) + currentText.substring(insertPos);
        newText = removeNullifiedMarkup(newText);
        this.textBox.setText(newText);

        return true;
    }

    private void backwardErase(){
        if(characterHbManager.hasValidUserSelection()){
            eraseSelection();
            return;
        }

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        int glyphCount = getTotalGlyphCount(layout.getValue());
        if(glyphCount == 0 || caretOffset == glyphCount){
            return;
        }

        String currentText = this.textBox.getText();
        int deletePos = currentText.length() - getRealtextOffsetForCaretOffset(layout.getValue(), false);

        // Ensure deletePos is valid and there's something to delete
        if (deletePos <= 0 || deletePos > currentText.length()) {
            return;
        }

        String newText = currentText.substring(0, deletePos - 1) + currentText.substring(deletePos);
        newText = removalTextVerification(newText);

        this.textBox.setText(newText);
    }

    private void forwardErase(){
        if(characterHbManager.hasValidUserSelection()){
            eraseSelection();
            return;
        }

        if(caretOffset == 0){
            return;
        }

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        if(getTotalGlyphCount(layout.getValue()) == 0){
            return;
        }

        String currentText = this.textBox.getText();
        int deletePos = currentText.length() - getRealtextOffsetForCaretOffset(layout.getValue(), true) + 1;

        // Ensure deletePos is valid and there's something to delete
        if (deletePos <= 0 || deletePos > currentText.length()) {
            return;
        }

        String newText = currentText.substring(0, deletePos - 1) + currentText.substring(deletePos);
        newText = removalTextVerification(newText);

        this.textBox.setText(newText);

        caretOffset--;
        recalculateCaretPosition();
    }

    private String getSelectionAsText(){
        if(!characterHbManager.hasValidUserSelection()){
            return "";
        }

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        if(getTotalGlyphCount(layout.getValue()) == 0){
            return "";
        }

        if(characterHbManager.selectionMode == InputCharacterManager.ESelectionMode.Standard){
            String currentText = this.textBox.getText();

            int targetCaretOffset;
            int startCaretOffset;
            if(characterHbManager.userSelectedForward()){
                startCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionEnd.x, characterHbManager.selectionEnd.y);
                targetCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionStart.x, characterHbManager.selectionStart.y);
            }
            else{
                startCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionStart.x, characterHbManager.selectionStart.y);
                targetCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionEnd.x, characterHbManager.selectionEnd.y);
            }

            int readPosStart = currentText.length() - getRealtextOffsetForGlyphOffset(layout.getValue(), startCaretOffset, true);
            int readPosEnd = currentText.length() - getRealtextOffsetForGlyphOffset(layout.getValue(), targetCaretOffset, false);

            return currentText.substring(readPosEnd, readPosStart);
        }

        return "";
    }

    private void eraseSelection(){
        if(!characterHbManager.hasValidUserSelection()){
            return;
        }

        Pair<IntegerVector2, GlyphLayout> layout = textBox.prepareForRender();
        if(getTotalGlyphCount(layout.getValue()) == 0){
            return;
        }

        if(characterHbManager.selectionMode == InputCharacterManager.ESelectionMode.Standard){
            String currentText = this.textBox.getText();

            int targetCaretOffset;
            int startCaretOffset;
            if(characterHbManager.userSelectedForward()){
                startCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionEnd.x, characterHbManager.selectionEnd.y);
                targetCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionStart.x, characterHbManager.selectionStart.y);
            }
            else{
                startCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionStart.x, characterHbManager.selectionStart.y);
                targetCaretOffset = getCaretOffsetForIndex(layout.getValue(), characterHbManager.selectionEnd.x, characterHbManager.selectionEnd.y);
            }

            int deletePosStart = currentText.length() - getRealtextOffsetForGlyphOffset(layout.getValue(), startCaretOffset, true);
            int deletePosEnd = currentText.length() - getRealtextOffsetForGlyphOffset(layout.getValue(), targetCaretOffset, false);

            String newText = currentText.substring(0, deletePosEnd) + currentText.substring(deletePosStart);
            newText = removalTextVerification(newText);

            this.textBox.setText(newText);

            caretOffset = startCaretOffset;
            recalculateCaretPosition();
        }

        characterHbManager.clearSelection();
    }

    private String removalTextVerification(String newText){
        String fixed = removeNullifiedMarkup(newText);

        if(fixed.isEmpty() && (preset == EInputfieldPreset.NUMERICAL_DECIMAL_POSITIVE || preset == EInputfieldPreset.NUMERICAL_WHOLE_POSITIVE || preset == EInputfieldPreset.NUMERICAL_WHOLE || preset == EInputfieldPreset.NUMERICAL_DECIMAL)){
            fixed = "0";
        }

        if(fixed.equals("-")){
            fixed = "-0";
        }

        return fixed;
    }

    public void resetInputProcessor(){
        if(Gdx.input.getInputProcessor() == inputProcessor){
            Gdx.input.setInputProcessor(cachedInputProcessor);
        }
    }

    private int getCaretOffsetForIndex(GlyphLayout layout, int runIndex, int glyphIndex){
        int totalCharsUpTo = 0;
        for (int i = 0; i < runIndex; i++){
            totalCharsUpTo += layout.runs.get(i).glyphs.size;
        }
        totalCharsUpTo += glyphIndex;

        return getTotalGlyphCount(layout) - totalCharsUpTo;
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

    //region Hidden Input

    public boolean isValidMarkup(String markup){
        if(markup.isEmpty()){
            return true;
        }
        else if(markup.startsWith("#")){
            //Check if the rest of the string is a valid hex color code
            String hex = markup.substring(1);
            try{
                Color.valueOf(hex);
                return true;
            }catch (Exception ignored){}
        }
        else if(Colors.getColors().containsKey(markup)){
            return true;
        }

        return false;
    }

    public String removeNullifiedMarkup(String text){
        // Pattern to find [TAG][]
        Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]\\[\\]");
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String tag = matcher.group(1);
            if (isValidMarkup(tag)) {
                matcher.appendReplacement(result, "");
            } else {
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    //endregion

    //region KYB controls

    @Override
    public boolean onLeftInteraction() {
        characterHbManager.clearSelection();

        if(caretOffset == textBox.getText().length()) return true;

        caretOffset++;
        recalculateCaretPosition();
        return true;
    }

    @Override
    public boolean onRightInteraction() {
        characterHbManager.clearSelection();

        if(caretOffset == 0) return true;

        caretOffset--;
        recalculateCaretPosition();
        return true;
    }

    @Override
    public boolean onUpInteraction() {
        characterHbManager.clearSelection();

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
        characterHbManager.clearSelection();

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
