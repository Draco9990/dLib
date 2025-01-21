package dLib.util;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectInfoFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("generatedResourcesPath")
    public String generatedResourcesPath;
}
