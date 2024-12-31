package dLib.ui.elements.items.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.modcompat.ModManager;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.util.FontManager;
import dLib.util.bindings.font.AbstractFontBinding;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.font.FontResourceBinding;
import dLib.util.events.Event;
import dLib.util.ui.bounds.PositionBounds;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import sayTheSpire.Output;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

public class TextBox extends UIElement {
    //region Variables

    private String text;

    private float fontScale = 0.8f;

    private Color textRenderColor;
    private AbstractFontBinding font;
    private boolean wrap;

    private boolean obscureText = false;

    private Alignment contentAlignment = new Alignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);

    private String onTextChangedLine; //TODO expose

    public Event<Consumer<String>> onTextChangedEvent = new Event<>();

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

        this.fontScale = data.fontScale.getValue();
        this.font = data.font.getValue();

        this.textRenderColor = Color.valueOf(data.textRenderColor.getValue());
        this.contentAlignment = data.contentAlignment.getValue();

        this.obscureText = data.obscureInput.getValue();

        this.wrap = data.wrap.getValue();

        onTextChangedEvent.subscribeManaged(s -> data.onTextChanged.getValue().executeBinding(getTopParent()));
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        if(text == null || text.isEmpty()) return;

        getFontForRender().getData().setScale(fontScale);

        int renderX = getWorldPositionX() + (int)(getPaddingLeft() * Settings.xScale);
        int renderY = getWorldPositionY() + (int)(getPaddingBottom() * Settings.yScale);
        int renderWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int renderHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        float halfWidth = (float) renderWidth / 2;
        float halfHeight = (float) renderHeight / 2;

        String textToRender = text;
        if(obscureText){
            textToRender = text.replaceAll(".", "*");
        }

        if(!wrap){
            FontHelper.layout.setText(getFontForRender(), "lL");
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontLeftTopAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            renderX * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontLeft(
                            sb,
                            getFontForRender(),
                            textToRender,
                            renderX * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontLeftDownAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            renderX * Settings.xScale,
                            (renderY) * Settings.yScale,
                            textRenderColor);
                }
            }
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontCenteredTopAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale - FontHelper.layout.height / 2,
                            textRenderColor);
                }
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontCentered(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontCentered(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY) * Settings.yScale + FontHelper.layout.height / 2,
                            textRenderColor);
                }
            }
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT){
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontRightTopAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontRightAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontRightAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY) * Settings.yScale + FontHelper.layout.height / 2,
                            textRenderColor);
                }
            }
        }
        else{
            getFontForRender().getData().setScale(fontScale);
            getFontForRender().setColor(textRenderColor);

            int align = 0;
            if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT) align = Align.left;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER) align = Align.center;
            else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT) align = Align.right;

            FontHelper.layout.setText(getFontForRender(), textToRender, Color.WHITE, renderWidth * Settings.xScale, align, true);

            if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP) renderY = renderY + renderHeight;
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) renderY = renderY + renderHeight / 2 + (int) (FontHelper.layout.height / 2 / Settings.yScale);
            else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM) renderY = renderY + (int) (FontHelper.layout.height / Settings.yScale);

            getFontForRender().draw(sb, textToRender, renderX * Settings.xScale, renderY * Settings.yScale, renderWidth * Settings.xScale, align, true);
            getFontForRender().getData().setScale(1.0F);
        }

        sb.flush(); //* We have to flush after drawing because ScissorStack only applies to the last drawn elements for some reason

        getFontForRender().getData().setScale(1.f);

        if(hb != null){
            hb.render(sb);
        }
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
        if(ModManager.SayTheSpire.isActive()){
            if(getOnTextChangedLine(text) != null){
                Output.text(getOnTextChangedLine(text), true);
            }
        }

        onTextChangedEvent.invoke(stringConsumer -> stringConsumer.accept(newText));
    }

    public void setOnTextChangedLine(String newLine) {
        this.onTextChangedLine = newLine;
    }
    public String getOnTextChangedLine(String newText){ return this.onTextChangedLine; }

    public BitmapFont getFontForRender(){
        if(containsNonASCIICharacters()){
            return FontManager.nonASCIIFont;
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

    public boolean getWrap(){
        return wrap;
    }

    //endregion

    //region Text Font

    public void setFont(AbstractFontBinding font){
        this.font = font;
    }

    //endregion

    //region Mask

    @Override
    public PositionBounds getMaskWorldBounds() {
        PositionBounds superBounds = super.getMaskWorldBounds();
        PositionBounds myBounds = getWorldBounds();
        myBounds.left -= getPaddingLeft();
        myBounds.right += getPaddingRight();
        myBounds.top += getPaddingTop();
        myBounds.bottom -= getPaddingBottom();

        if(superBounds == null){
            return myBounds;
        }
        else{
            superBounds.clip(myBounds);
            return superBounds;
        }
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

    public void setFontScale(float fontScale){
        this.fontScale = fontScale;
    }

    //region Obscure Text

    public void setObscureText(boolean obscureText){
        this.obscureText = obscureText;
    }

    public boolean isObsuringText(){
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

        private FloatProperty fontScale = new FloatProperty(0.8f)
                .setName("Font Scale")
                .setDescription("The scale of the font.")
                .setCategory("Text")
                .setDecrementAmount(0.1f).setIncrementAmount(0.1f)
                .setMinimumValue(0.01f);
        private FontBindingProperty font = new FontBindingProperty(new FontResourceBinding(FontHelper.class, "cardTitleFont"))
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

        public BooleanProperty obscureInput = new BooleanProperty(false)
                .setName("Obscure Input")
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
        public TextBox makeUIElement() {
            return new TextBox(this);
        }
    }
}