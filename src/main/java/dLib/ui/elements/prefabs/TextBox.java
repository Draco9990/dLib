package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import dLib.modcompat.ModManager;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.implementations.Renderable;
import dLib.util.FontManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import sayTheSpire.Output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class TextBox extends Renderable {
    //region Variables

    private String text;

    private Color textRenderColor;
    private BitmapFont font;
    private boolean wrap;

    private String onTextChangedLine;

    private float marginPercX = 0.0f;
    private float marginPercY = 0.0f;

    private ArrayList<Consumer<String>> onTextChangedConsumers = new ArrayList<>();

    private Hitbox textRenderHitbox;

    private float minFontScale = 0.0f;
    private float fontScaleOverride = 0.0f;
    private float maxFontScale = 0.0f;

    private boolean obscureText = false;

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
        this(text, xPos, yPos, width, height, 0, 0.33f);
    }
    public TextBox(String text, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height, float xMarginPerc, float yMarginPerc){
        super(null, xPos, yPos, width, height);

        this.marginPercX = xMarginPerc;
        this.marginPercY = yMarginPerc;

        setAlignment(Alignment.HorizontalAlignment.CENTER, Alignment.VerticalAlignment.CENTER);
        wrap = false;

        this.text = text;

        setFont(FontHelper.cardTitleFont);

        textRenderColor = Color.WHITE.cpy();

        setPassthrough(true);
    }

    public TextBox(TextBoxData data){
        super(data);

        this.text = data.text.getValue();

        this.textRenderColor = Color.valueOf(data.textRenderColor.getValue());
        //TODO FONT
        this.wrap = data.wrap.getValue();

        this.marginPercX = data.marginPercX;
        this.marginPercY = data.marginPercY;

        onTextChangedConsumers.add(s -> data.onTextChanged.getValue().executeBinding(getTopParent()));

        setFont(FontManager.genericFont);

        this.obscureText = data.obscureInput;
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void renderSelf(SpriteBatch sb) {
        super.renderSelf(sb);

        if(text == null || text.isEmpty()) return;

        float fontScale = calculateFontScale();

        getFontForRender().getData().setScale(fontScale);

        float xMargin = marginPercX * getWidth();
        float yMargin = marginPercY * getHeight();

        int renderX = getWorldPositionX() + (int) xMargin + (int)(getPaddingLeft() * Settings.xScale);
        int renderY = getWorldPositionY() + (int) yMargin + (int)(getPaddingBottom() * Settings.yScale);
        int renderWidth = getWidth() - (int) xMargin * 2 - getPaddingLeft() - getPaddingRight();
        int renderHeight = getHeight() - (int) yMargin * 2 - getPaddingTop() - getPaddingBottom();

        float halfWidth = (float) renderWidth / 2;
        float halfHeight = (float) renderHeight / 2;

        String textToRender = text;
        if(obscureText){
            textToRender = text.replaceAll(".", "*");
        }

        if(!wrap){
            FontHelper.layout.setText(getFontForRender(), "lL");
            if(getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT){
                if(getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontLeftTopAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            renderX * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontLeft(
                            sb,
                            getFontForRender(),
                            textToRender,
                            renderX * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontLeftDownAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            renderX * Settings.xScale,
                            (renderY) * Settings.yScale,
                            textRenderColor);
                }
            }
            if(getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER){
                if(getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontCenteredTopAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale - FontHelper.layout.height / 2,
                            textRenderColor);
                }
                if(getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontCentered(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
                    FontHelper.renderFontCentered(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + halfWidth) * Settings.xScale,
                            (renderY) * Settings.yScale + FontHelper.layout.height / 2,
                            textRenderColor);
                }
            }
            if(getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT){
                if(getVerticalAlignment() == Alignment.VerticalAlignment.TOP){
                    FontHelper.renderFontRightTopAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + renderHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalAlignment() == Alignment.VerticalAlignment.CENTER){
                    FontHelper.renderFontRightAligned(
                            sb,
                            getFontForRender(),
                            textToRender,
                            (renderX + renderWidth) * Settings.xScale,
                            (renderY + halfHeight) * Settings.yScale,
                            textRenderColor);
                }
                if(getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM){
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
            if(getHorizontalAlignment() == Alignment.HorizontalAlignment.LEFT) align = Align.left;
            else if(getHorizontalAlignment() == Alignment.HorizontalAlignment.CENTER) align = Align.center;
            else if(getHorizontalAlignment() == Alignment.HorizontalAlignment.RIGHT) align = Align.right;

            FontHelper.layout.setText(getFontForRender(), textToRender, Color.WHITE, renderWidth * Settings.xScale, align, true);

            if(getVerticalAlignment() == Alignment.VerticalAlignment.TOP) renderY = renderY + renderHeight;
            else if(getVerticalAlignment() == Alignment.VerticalAlignment.CENTER) renderY = renderY + renderHeight / 2 + (int) (FontHelper.layout.height / 2 / Settings.yScale);
            else if(getVerticalAlignment() == Alignment.VerticalAlignment.BOTTOM) renderY = renderY + (int) (FontHelper.layout.height / Settings.yScale);

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

        for(Consumer<String> consumer : onTextChangedConsumers) consumer.accept(newText);
    }
    public TextBox addOnTextChangedConsumer(Consumer<String> consumer){
        onTextChangedConsumers.add(consumer);
        return this;
    }

    public TextBox setOnTextChangedLine(String newLine) {
        this.onTextChangedLine = newLine;
        return this;
    }
    public String getOnTextChangedLine(String newText){ return this.onTextChangedLine; }

    public BitmapFont getFontForRender(){
        if(containsNonASCIICharacters()){
            return FontManager.nonASCIIFont;
        }
        else{
            return font;
        }
    }

    //endregion

    //region Text Render Color

    public TextBox setTextRenderColor(Color renderColor){
        textRenderColor = renderColor;
        return this;
    }

    public Color getTextRenderColor(){
        return textRenderColor;
    }

    //endregion

    //region Text Margin

    public TextBox setMarginPercX(float value){
        marginPercX = value;
        return this;
    }
    public TextBox setMarginPercY(float value){
        marginPercY = value;
        return this;
    }

    //endregion

    //region Text Wrap

    public TextBox setWrap(boolean wrap){
        this.wrap = wrap;
        return this;
    }

    public boolean getWrap(){
        return wrap;
    }

    //endregion

    //region Text Font

    public TextBox setFont(BitmapFont font){
        this.font = font;
        return this;
    }

    //endregion

    protected float calculateFontScale(){
        if(text == null || text.isEmpty()) return 0.1f;

        float fontScale = 0.1F;

        float xMargin = marginPercX * getWidth();
        float yMargin = marginPercY * getHeight();

        int renderWidth = getWidth() - (int) xMargin * 2;
        int renderHeight = getHeight() - (int) yMargin * 2;

        renderWidth -= getPaddingLeft();
        renderWidth -= getPaddingRight();
        renderHeight -= getPaddingTop();
        renderHeight -= getPaddingBottom();

        if(fontScaleOverride > 0.0f){
            return fontScaleOverride;
        }

        while(true){
            getFontForRender().getData().setScale(fontScale);
            FontHelper.layout.setText(getFontForRender(), text, Color.BLACK, renderWidth * Settings.xScale, 0, wrap);
            if(FontHelper.layout.height > renderHeight * Settings.yScale || (!wrap && FontHelper.layout.width > renderWidth * Settings.xScale)) {
                getFontForRender().getData().setScale(1);
                float calculatedScale = Math.max(fontScale - 0.1F, 0.1f);

                if(minFontScale > 0.0f){
                    calculatedScale = Math.max(calculatedScale, minFontScale);
                }
                if(maxFontScale > 0.0f){
                    calculatedScale = Math.min(calculatedScale, maxFontScale);
                }

                return calculatedScale;
            }
            fontScale+=0.1F;
        }
    }
    public boolean containsNonASCIICharacters(){
        return this.text != null && !this.text.isEmpty() && !this.text.matches("\\A\\p{ASCII}*\\z");
    }

    public TextBox setFontScaleOverride(float fontScaleOverride){
        this.fontScaleOverride = fontScaleOverride;
        return this;
    }

    public TextBox setMinFontScale(float minFontScale){
        this.minFontScale = minFontScale;
        return this;
    }

    public TextBox setMaxFontScale(float maxFontScale){
        this.maxFontScale = maxFontScale;
        return this;
    }

    //region Obscure Text

    public TextBox setObscureText(boolean obscureText){
        this.obscureText = obscureText;
        return this;
    }

    public boolean isObsuringText(){
        return obscureText;
    }

    //endregion

    //endregion

    public static class TextBoxData extends RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public StringProperty text = new StringProperty("TEXT").setName("Text");

        public ColorProperty textRenderColor = new ColorProperty(Color.WHITE).setName("Render Color");
        //TODO FONT
        public BooleanProperty wrap = new BooleanProperty(false).setName("Wrap");

        public float marginPercX = 0.07f; //TODO Propertize
        public float marginPercY = 0.33f;

        public MethodBindingProperty onTextChanged = new MethodBindingProperty().setName("On Text Changed");

        public float fontScaleOverride = 0.0f;

        public boolean obscureInput = false;

        @Override
        public TextBox makeUIElement() {
            return new TextBox(this);
        }
    }
}