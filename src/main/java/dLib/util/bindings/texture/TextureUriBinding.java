package dLib.util.bindings.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.resources.UICommonResources;
import dLib.util.TextureManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CancellationException;
import java.util.function.Consumer;

public class TextureUriBinding extends AbstractTextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    private String textureUri = null;
    private boolean loadImmediately = false;
    private boolean blocking = false;

    private NinePatch texture;

    public TextureUriBinding(String uri){
        this(uri, true, false);
    }
    public TextureUriBinding(String uri, boolean loadImmediately, boolean blocking){
        this.textureUri = uri;
        this.loadImmediately = loadImmediately;
        this.blocking = blocking;

        if(loadImmediately){
            resolve();
        }
    }

    private void resolve(){
        Texture cached = TextureManager.getTextureNoLoad(textureUri);
        if(cached != null){
            this.texture = new NinePatch(cached);
        }
        else{
            fromHttpAsync();
            if(this.texture != null){
                TextureManager.saveTexture(textureUri, this.texture.getTexture());
            }
        }
    }

    @Override
    public NinePatch resolve(Object... params) {
        return texture == null ? UICommonResources.inputfield : texture;
    }

    @Override
    public String toString() {
        return "URI";
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        throw new UnsupportedOperationException("Not implemented - Texture Static Binding should never be edited in-app");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        throw new UnsupportedOperationException("Not implemented - Texture Static Binding should never be edited in-app");
    }

    public void fromHttpAsync() {
        if (blocking) {
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(textureUri).openConnection();
                c.setConnectTimeout(15000);
                c.setReadTimeout(15000);
                c.setInstanceFollowRedirects(true);
                try (InputStream in = c.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream(16 * 1024)) {
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                    byte[] data = out.toByteArray();

                    Pixmap pm = new Pixmap(data, 0, data.length);
                    texture = new NinePatch(new Texture(pm));
                    pm.dispose();
                } finally {
                    c.disconnect();
                }
            } catch (Throwable ignored) {
            }
        }
        else{
            Net.HttpRequest req = new Net.HttpRequest(Net.HttpMethods.GET);
            req.setUrl(textureUri);

            Gdx.net.sendHttpRequest(req, new Net.HttpResponseListener() {
                @Override public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    byte[] data = httpResponse.getResult();
                    // hop to render thread to create GL resources
                    Gdx.app.postRunnable(() -> {
                        try {
                            Pixmap pm = new Pixmap(data, 0, data.length);
                            Texture tex = new Texture(pm);
                            pm.dispose();
                            texture = new NinePatch(tex);
                        } catch (Throwable ignored) {
                        }
                    });
                }
                @Override public void failed(Throwable t) {
                }
                @Override public void cancelled() {
                }
            });
        }
    }
}
