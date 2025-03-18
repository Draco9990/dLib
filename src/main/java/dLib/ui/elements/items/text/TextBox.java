package dLib.ui.elements.items.text;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.modcompat.ModManager;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.IntegerVector2;
import dLib.util.bindings.font.AbstractFontBinding;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.font.FontResourceBinding;
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
import sayTheSpire.Output;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class TextBox extends UIElement {
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

    private GlyphLayout layoutCache = null;

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

    public Pair<IntegerVector2, GlyphLayout> prepareForRender(){
        getFontForRender().getData().setScale(getFontSizeForRender());

        BitmapFontCache cache = getFontForRender().getCache();

        int renderX = getWorldPositionX();
        int renderY = getWorldPositionY();
        int renderWidth = getWidth();
        int renderHeight = getHeight();

        float halfWidth = (float) renderWidth / 2;
        float halfHeight = (float) renderHeight / 2;

        BitmapFont font = getFontForRender();

        String msg = text;
        if(obscureText){
            msg = text.replaceAll(".", "*");
        }

        GlyphLayout layout = FontHelper.layout;

        GlyphLayout layoutToReturn = layoutCache;
        int x = 0;
        int y = 0;

        if(!wrap){
            layout.setText(getFontForRender(), msg);
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    x = (int) (renderX * Settings.xScale);
                    y = (int) ((renderY + renderHeight) * Settings.yScale);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER){
                    x = (int) (renderX * Settings.xScale);
                    y = (int) ((renderY + halfHeight) * Settings.yScale + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    x = (int) (renderX * Settings.xScale);
                    y = (int) ((renderY) * Settings.yScale + layout.height);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }
            }
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    layout.setText(font, "lL");
                    x = (int) ((renderX + halfWidth) * Settings.xScale);
                    y = (int) (((renderY + renderHeight) * Settings.yScale - FontHelper.layout.height / 2) + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y, 0.0F, 1, false);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) {
                    x = (int) (((renderX + halfWidth) * Settings.xScale) - layout.width / 2.0F);
                    y = (int) (((renderY + halfHeight) * Settings.yScale) + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    x = (int) (((renderX + halfWidth) * Settings.xScale) - layout.width / 2.0F);
                    y = (int) (((renderY) * Settings.yScale + FontHelper.layout.height / 2) + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

            }
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    x = (int) (((renderX + renderWidth) * Settings.xScale) - layout.width);
                    y = (int) ((renderY + renderHeight) * Settings.yScale);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER){
                    x = (int) (((renderX + renderWidth) * Settings.xScale) - layout.width);
                    y = (int) ((renderY + halfHeight) * Settings.yScale + layout.height / 2.0F);
                    if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y);
                }

                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    x = (int) (((renderX + renderWidth) * Settings.xScale) - layout.width);
                    y = (int) (((renderY) * Settings.yScale + FontHelper.layout.height / 2) + layout.height / 2.0F);
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
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) renderY = renderY + renderHeight / 2 + (int) (FontHelper.layout.height / 2 / Settings.yScale);
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM) renderY = renderY + (int) (FontHelper.layout.height / Settings.yScale);

            x = (int) (renderX * Settings.xScale);
            y = (int) (renderY * Settings.yScale);
            if(layoutToReturn == null) layoutToReturn = cache.addText(msg, x, y, renderWidth * Settings.xScale, align, true);
        }

        getFontForRender().getData().setScale(1.f);

        return new Pair<>(new IntegerVector2(x, y), layoutToReturn);
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
        layoutCache = null;

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
        layoutCache = null;
    }

    //endregion

    //region Text Dimensions

    public int getTextWidth(){
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

        int width = (int) FontHelper.layout.width;

        getFontForRender().getData().setScale(1.f);
        return width;
    }

    public int getTextHeight(){
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

        int height = (int) FontHelper.layout.height;

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

        layoutCache = null;
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

        layoutCache = null;

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