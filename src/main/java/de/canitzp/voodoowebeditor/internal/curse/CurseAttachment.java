package de.canitzp.voodoowebeditor.internal.curse;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class CurseAttachment{
    
    int id;
    int projectId;
    String description;
    URL thumbnailUrl;
    String title;
    URL url;
    @SerializedName("default")
    boolean isDefault;
    
    @Override
    public String toString(){
        return String.format("CurseAttachment{id '%d' projectId '%d' title '%s' url '%s' default '%b'}", id, projectId, title, url.toString(), isDefault);
    }
}
