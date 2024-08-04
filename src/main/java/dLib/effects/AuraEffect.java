package dLib.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AuraEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private TextureAtlas.AtlasRegion img;
    public boolean switcher = true;

    public AuraEffect(Color auraColor, AbstractCreature target) {
        this(auraColor,
                target.hb.cX + MathUtils.random(-target.hb.width / 16.0F, target.hb.width / 16.0F),
                target.hb.cY + MathUtils.random(-target.hb.height / 16.0F, target.hb.height / 12.0F));
    }

    public AuraEffect(Color auraColor, float cX, float cY){
        this.img = ImageMaster.EXHAUST_L;
        this.duration = 2.0F;
        this.scale = MathUtils.random(2.7F, 2.5F) * Settings.scale;
        this.color = auraColor.cpy();
        this.color.a = 0f;
        this.x = cX;
        this.y = cY;
        this.x -= (float)this.img.packedWidth / 2.0F;
        this.y -= (float)this.img.packedHeight / 2.0F;
        switcher = !switcher;
        this.renderBehind = true;
        this.rotation = MathUtils.random(360.0F);
        if (switcher) {
            this.renderBehind = true;
            this.vY = MathUtils.random(0.0F, 40.0F);
        } else {
            this.renderBehind = false;
            this.vY = MathUtils.random(0.0F, -40.0F);
        }
    }

    public void update() {
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.fade.apply(0.3F, 0.0F, this.duration - 1.0F);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.3F, this.duration);
        }

        this.rotation += Gdx.graphics.getDeltaTime() * this.vY;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
