package de.canitzp.voodoowebeditor.internal.curse;

import java.net.URL;
import java.util.List;

public class CurseProject{
    
    private int id;
    private String name;
    private List<CurseAuthor> authors;
    private List<CurseAttachment> attachments;
    private URL webSiteURL;
    private int gameId;
    private String summary;
    private int defaultFileId;
    private int commentCount;
    private float downloadCount;
    private int rating;
    private int installCount;
    private List<CurseFile> latestFiles;
    
    private String slug;
    
    @Override
    public String toString() {
        return String.format("id: %d name: %s authors: %s attachments: %s", id, name, authors, attachments);
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public List<CurseAuthor> getAuthors(){
        return authors;
    }
    
    public List<CurseAttachment> getAttachments(){
        return attachments;
    }
    
    public URL getWebSiteURL(){
        return webSiteURL;
    }
    
    public int getGameId(){
        return gameId;
    }
    
    public String getSummary(){
        return summary;
    }
    
    public int getDefaultFileId(){
        return defaultFileId;
    }
    
    public int getCommentCount(){
        return commentCount;
    }
    
    public float getDownloadCount(){
        return downloadCount;
    }
    
    public int getRating(){
        return rating;
    }
    
    public int getInstallCount(){
        return installCount;
    }
    
    public List<CurseFile> getLatestFiles(){
        return latestFiles;
    }
    
    public String getSlug(){
        return slug;
    }
}
