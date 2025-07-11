package dLib.util;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProjectInfoFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("generatedResourcesPath")
    public String generatedResourcesPath;
}
