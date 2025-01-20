package dLib.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ShaderManager {
    private static HashMap<String, ShaderProgram> registeredShaders = new HashMap<>();

    private static ArrayList<ShaderProgram> activeShaders = new ArrayList<>();

    public static void init(){
        activeShaders.add(SpriteBatch.createDefaultShader());

        registerShader("hueShift", "dLibResources/shaders/hueshift");
    }

    public static void registerShader(String shaderId, String resourceDirectory){
        registerShader(shaderId, resourceDirectory + "/vertex.glsl", resourceDirectory + "/fragment.glsl");
    }
    public static void registerShader(String shaderId, String vertexFilePath, String fragmentFilePath){
        String vertexContent = Gdx.files.internal(vertexFilePath).readString();
        String fragmentContent = Gdx.files.internal(fragmentFilePath).readString();
        ShaderProgram shader = new ShaderProgram(vertexContent, fragmentContent);
        if (!shader.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        } else {
            registeredShaders.put(shaderId, shader);
        }
    }

    public static void pushShader(SpriteBatch sb, String shaderId){
        ShaderProgram shader = registeredShaders.get(shaderId);
        if(shader == null){
            throw new IllegalArgumentException("Shader not found: " + shaderId);
        }
        activeShaders.add(shader);
        sb.setShader(shader);
    }
    public static void popShader(SpriteBatch sb){
        if(activeShaders.size() <= 1){
            throw new IllegalArgumentException("Cannot pop default shader");
        }

        activeShaders.remove(activeShaders.size() - 1);
        sb.setShader(activeShaders.get(activeShaders.size() - 1));
    }
}
