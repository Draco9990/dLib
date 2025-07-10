package dLib.ui.elements.items.text;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.modcompat.ModManager;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.bindings.font.AbstractFontBinding;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.font.FontResourceBinding;
import dLib.util.bindings.string.interfaces.ITextProvider;
import dLib.util.events.Event;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;
import dLib.util.helpers.FontHelpers;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.AutoDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.dimensions.MirrorDimension;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import dLib.util.ui.text.TextMetadata;
import sayTheSpire.Output;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class TextBox extends UIElement implements ITextProvider {
    //region Variables

    private String text;

    private float fontSize = 16f;
    private boolean trueSize = false;

    public BiConsumerEvent<Float, Float> onFontSizeChangedEvent = new BiConsumerEvent<>();                              public TriConsumerEvent<UIElement, Float, Float> onFontSizeChangedGlobalEvent = new TriConsumerEvent<>();

    private Color textRenderColor;
    private AbstractFontBinding font;
    private boolean wrap;

    private boolean useSelfAsMask = true;

    private boolean obscureText = false;

    private Alignment contentAlignment = new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);

    private String onTextChangedLine; //TODO expose

    public Event<Consumer<String>> onTextChangedEvent = new Event<>();

    private TextMetadata metadata = new TextMetadata();

    //endregion

    //region Constructors

    public TextBox(String text){
        this(text, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }
    public TextBox(String text, AbstractPosition xPos, AbstractPosition yPos){
        this(text, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public TextBox(String text, AbstractDimension width, AbstractDimension height){
        this(text, Pos.px(0), Pos.px(0), width, height);
    }
    public TextBox(String text, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        wrap = false;

        this.text = text;

        this.font = Font.stat(FontHelper.cardTitleFont);

        textRenderColor = Color.WHITE.cpy();

        setPassthrough(true);
    }

    public TextBox(TextBoxData data){
        super(data);

        this.text = data.text.getValue();

        this.fontSize = data.fontSize.getValue();
        this.trueSize = data.trueSize.getValue();

        this.font = data.font.getValue();

        this.textRenderColor = Color.valueOf(data.textRenderColor.getValue());
        this.contentAlignment = data.contentAlignment.getValue();

        this.obscureText = data.obscureText.getValue();

        this.wrap = data.wrap.getValue();

        onTextChangedEvent.subscribeManaged(s -> data.onTextChanged.getValue().executeBinding(this));
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        if(text == null || text.isEmpty()) return;

        getFontForRender().setColor(textRenderColor);

        prepareForRender();
        getFontForRender().getCache().draw(sb);

        sb.flush(); //* We have to flush after drawing because ScissorStack only applies to the last drawn elements for some reason

        getFontForRender().setColor(Color.WHITE);

        if(hb != null){
            hb.render(sb);
        }
    }

    public static void renderFont(SpriteBatch sb, BitmapFont font, String msg, float x, float y, Color c) {
        font.draw(sb, msg, x, y);
    }

    public Pair<Vector2, GlyphLayout> prepareForRender(){
        getFontForRender().getData().setScale(getFontSizeForRender());

        BitmapFontCache cache = getFontForRender().getCache();
        cache.clear();

        float renderX = getWorldPositionX();
        float renderY = getWorldPositionY();
        float renderWidth = getWidth();
        float renderHeight = getHeight();

        float halfWidth = renderWidth / 2;
        float halfHeight = renderHeight / 2;

        BitmapFont font = getFontForRender();

        String msg = text;
        if(obscureText){
            msg = text.replaceAll(".", "*");
        }

        GlyphLayout layout = FontHelper.layout;

        GlyphLayout layoutToReturn = null;
        float x = 0;
        float y = 0;

        if(!wrap){
            layout.setText(getFontForRender(), msg);
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    x = (renderX * Settings.xScale);
                    y = ((renderY + renderHeight) * Settings.yScale);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER){
                    x = (renderX * Settings.xScale);
                    y = ((renderY + halfHeight) * Settings.yScale + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    x = (renderX * Settings.xScale);
                    y = ((renderY) * Settings.yScale + layout.height);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }
            }
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    layout.setText(font, "lL");
                    x = ((renderX + halfWidth) * Settings.xScale);
                    y = (((renderY + renderHeight) * Settings.yScale - FontHelper.layout.height / 2) + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y, 0.0F, 1, false);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) {
                    x = (((renderX + halfWidth) * Settings.xScale) - layout.width / 2.0F);
                    y = (((renderY + halfHeight) * Settings.yScale) + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    x = (((renderX + halfWidth) * Settings.xScale) - layout.width / 2.0F);
                    y = (((renderY) * Settings.yScale + FontHelper.layout.height / 2) + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

            }
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    x = (((renderX + renderWidth) * Settings.xScale) - layout.width);
                    y = ((renderY + renderHeight) * Settings.yScale);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER){
                    x = (((renderX + renderWidth) * Settings.xScale) - layout.width);
                    y = ((renderY + halfHeight) * Settings.yScale + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    x = (((renderX + renderWidth) * Settings.xScale) - layout.width);
                    y = (((renderY) * Settings.yScale + FontHelper.layout.height / 2) + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }
            }
        }
        else{
            int align = 0;
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT) align = Align.left;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER) align = Align.center;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT) align = Align.right;

            FontHelper.layout.setText(getFontForRender(), msg, Color.WHITE, renderWidth * Settings.xScale, align, true);

            if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP) renderY = renderY + renderHeight;
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) renderY = renderY + renderHeight / 2 + (FontHelper.layout.height / 2 / Settings.yScale);
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM) renderY = renderY + (FontHelper.layout.height / Settings.yScale);

            x = (renderX * Settings.xScale);
            y = (renderY * Settings.yScale);
            if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y, renderWidth * Settings.xScale, align, true);
        }

        getFontForRender().getData().setScale(1.f);

        return new Pair<>(new Vector2(x, y), layoutToReturn);
    }

    //endregion

    //region Text

    public void setText(String text){
        if(!Objects.equals(this.text, text)){
            this.text = text;

            onTextChanged(text);
        }
    }
    public String getText(){
        return text;
    }

    public void onTextChanged(String newText){
        metadata = null;

        if(ModManager.SayTheSpire.isActive()){
            if(getOnTextChangedLine(text) != null){
                Output.text(getOnTextChangedLine(text), true);
            }
        }

        if(getWidthRaw() instanceof AutoDimension || getWidthRaw() instanceof MirrorDimension){
            requestWidthRecalculation();
            onDimensionsChanged();
        }

        if(getHeightRaw() instanceof AutoDimension || getHeightRaw() instanceof MirrorDimension){
            requestHeightRecalculation();
            onDimensionsChanged();
        }

        onTextChangedEvent.invoke(stringConsumer -> stringConsumer.accept(newText));
    }

    public void setOnTextChangedLine(String newLine) {
        this.onTextChangedLine = newLine;
    }
    public String getOnTextChangedLine(String newText){ return this.onTextChangedLine; }

    public BitmapFont getFontForRender(){
        if(containsNonASCIICharacters()){
            return FontHelpers.nonASCIIFont;
        }
        else{
            return font.getBoundObject();
        }
    }

    //endregion

    //region Text Render Color

    public void setTextRenderColor(Color renderColor){
        textRenderColor = renderColor;
    }

    public Color getTextRenderColor(){
        return textRenderColor;
    }

    //endregion

    //region Text Wrap

    public void setWrap(boolean wrap){
        this.wrap = wrap;
    }

    public boolean canWrap(){
        return wrap;
    }

    //endregion

    //region Text Font

    public void setFont(BitmapFont font){
        setFont(Font.stat(font));
    }
    public void setFont(Class<?> resourceClass, String fieldName){
        setFont(Font.resource(resourceClass, fieldName));
    }

    public void setFont(AbstractFontBinding font){
        this.font = font;
    }

    //endregion

    //region Text Dimensions

    public float getTextWidth(){
        if(text == null || text.isEmpty()) return 0;

        getFontForRender().getData().setScale(getFontSizeForRender());

        if(getHeightRaw() instanceof AutoDimension && canWrap() && !(getWidthRaw() instanceof AutoDimension)){
            int align = 0;
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT) align = Align.left;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER) align = Align.center;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT) align = Align.right;

            FontHelper.layout.setText(getFontForRender(), text, Color.WHITE, getWidth() * Settings.xScale, align, true);
        }
        else{
            FontHelper.layout.setText(getFontForRender(), text);
        }

        float width = FontHelper.layout.width;

        getFontForRender().getData().setScale(1.f);
        return width;
    }

    public float getTextHeight(){
        if(text == null || text.isEmpty()) return 0;

        getFontForRender().getData().setScale(getFontSizeForRender());

        if(getHeightRaw() instanceof AutoDimension && canWrap()){
            int align = 0;
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT) align = Align.left;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER) align = Align.center;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT) align = Align.right;

            FontHelper.layout.setText(getFontForRender(), text, Color.WHITE, getWidth() * Settings.xScale, align, true);
        }
        else{
            FontHelper.layout.setText(getFontForRender(), text);
        }

        float height = FontHelper.layout.height;

        getFontForRender().getData().setScale(1.f);
        return height;
    }

    //endregion

    //region Mask

    @Override
    public PositionBounds getMaskWorldBounds() {
        PositionBounds superBounds = super.getMaskWorldBounds();

        if(usesSelfAsMask()){
            PositionBounds myBounds = getWorldBounds();
            myBounds.left -= getPaddingLeft();
            myBounds.right += getPaddingRight();
            myBounds.top += getPaddingTop();
            myBounds.bottom -= getPaddingBottom();

            if(superBounds != null){
                superBounds.clip(myBounds);
                return superBounds;
            }
            else{
                return myBounds;
            }
        }
        else{
            return superBounds;
        }
    }

    public boolean usesSelfAsMask(){
        return useSelfAsMask;
    }

    public void setUseSelfAsMask(boolean useSelfAsMask){
        this.useSelfAsMask = useSelfAsMask;
    }


    //endregion

    //region Content Alignment

    public void setHorizontalContentAlignment(Alignment.HorizontalAlignment horizontalAlignment){
        setContentAlignment(horizontalAlignment, contentAlignment.verticalAlignment);
    }
    public void setVerticalContentAlignment(Alignment.VerticalAlignment verticalAlignment){
        setContentAlignment(contentAlignment.horizontalAlignment, verticalAlignment);
    }
    public void setContentAlignment(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment){
        contentAlignment.horizontalAlignment = horizontalAlignment;
        contentAlignment.verticalAlignment = verticalAlignment;
    }

    public Alignment.HorizontalAlignment getHorizontalContentAlignment(){
        return contentAlignment.horizontalAlignment;
    }
    public Alignment.VerticalAlignment getVerticalContentAlignment(){
        return contentAlignment.verticalAlignment;
    }
    public Alignment getContentAlignment(){
        return contentAlignment;
    }

    //endregion Alignment

    public boolean containsNonASCIICharacters(){
        return this.text != null && !this.text.isEmpty() && !this.text.matches("\\A\\p{ASCII}*\\z");
    }

    public void setFontSize(float fontSize){
        this.fontSize = fontSize;

        onFontSizeChangedEvent.invoke(fontSize, getFontSizeForRender());
        onFontSizeChangedGlobalEvent.invoke(this, fontSize, getFontSizeForRender());
    }

    public float getFontSizeForRender(){
        if(trueSize){
            return (fontSize / 14f) * getScaleY() * Settings.xScale;
        }
        else{
            float trueSize = FontHelpers.getFontTrueScale(getFontForRender());
            return (trueSize * fontSize) * getScaleY() * Settings.xScale;
        }
    }

    public float getFontSizeRaw(){
        return fontSize;
    }

    //region Metadata

    public TextMetadata getTextMetadata(){
        if(metadata == null){
            Pair<Vector2, GlyphLayout> pair = prepareForRender();
            metadata = TextMetadata.generateFor(text, pair.getValue());
        }
        return metadata;
    }

    //endregion

    //region Obscure Text

    public void setObscureText(boolean obscureText){
        this.obscureText = obscureText;
    }

    public boolean isObscuringText(){
        return obscureText;
    }

    //endregion

    //endregion

    public static class TextBoxData extends UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public StringProperty text = new StringProperty("TEXT")
                .setName("Text")
                .setDescription("The text to display in the text box.")
                .setCategory("Text");

        public FloatProperty fontSize = new FloatProperty(16f)
                .setName("Font Size")
                .setDescription("Size of the font.")
                .setCategory("Text")
                .setDecrementAmount(1f).setIncrementAmount(1f)
                .setMinimumValue(0.01f);
        public BooleanProperty trueSize = new BooleanProperty(false)
                .setName("True Size")
                .setDescription("Whether or not the font should use it's true size - useful for discerning between different font qualities at different resolutions.")
                .setCategory("Text");

        public FontBindingProperty font = new FontBindingProperty(new FontResourceBinding(FontHelper.class, "cardTitleFont"))
                .setName("Font")
                .setDescription("The font to use for the text.")
                .setCategory("Text");

        public ColorProperty textRenderColor = new ColorProperty(Color.WHITE)
                .setName("Text Color")
                .setDescription("Color of the text.")
                .setCategory("Text");

        public AlignmentProperty contentAlignment = new AlignmentProperty(new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER))
                .setName("Content Alignment")
                .setDescription("Alignment of the text inside of the text box.")
                .setCategory("Text");

        public BooleanProperty obscureText = new BooleanProperty(false)
                .setName("Obscure Text")
                .setDescription("Whether or not the text should be obscured (replaced with *).")
                .setCategory("Text");

        public BooleanProperty wrap = new BooleanProperty(false)
                .setName("Wrap")
                .setDescription("Whether or not the text should wrap to the next line if it is too long.")
                .setCategory("Text");

        public MethodBindingProperty onTextChanged = new MethodBindingProperty()
                .setName("On Text Changed")
                .setDescription("Method to call when the text in the text box changes.")
                .setCategory("Text");

        public TextBoxData(){
            super();
            width.setValue(Dim.px(300));
            height.setValue(Dim.px(150));

            isPassthrough.setValue(true);
        }

        @Override
        public TextBox makeUIElement_internal() {
            return new TextBox(this);
        }
    }
}