package de.canitzp.voodoowebeditor.internal.curse;

import java.util.List;

public class CurseFile{
    
    private int id;
    private String fileName;
    private String fileNameOnDisk;
    private long fileDate;
    private String releaseType;
    private String fileStatus;
    private String downloadURL;
    private int alternateFileId;
    private List<CurseDependencies> dependencies;
    private List<CurseModule> modules;
    private long packageFingerprint;
    private List<String> gameVersion;
    private boolean alternate;
    private boolean available;
    
    @Override
    public String toString() {
        return String.format("CurseFile{id %d name (%s/%s) releaseType %s url %s gameversions %s}", id, fileName, fileNameOnDisk, releaseType, downloadURL, gameVersion);
    }
    
    public int getId(){
        return id;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public String getFileNameOnDisk(){
        return fileNameOnDisk;
    }
    
    public long getFileDate(){
        return fileDate;
    }
    
    public String getReleaseType(){
        return releaseType;
    }
    
    public String getFileStatus(){
        return fileStatus;
    }
    
    public String getDownloadURL(){
        return downloadURL;
    }
    
    public int getAlternateFileId(){
        return alternateFileId;
    }
    
    public List<CurseDependencies> getDependencies(){
        return dependencies;
    }
    
    public List<CurseModule> getModules(){
        return modules;
    }
    
    public long getPackageFingerprint(){
        return packageFingerprint;
    }
    
    public List<String> getGameVersion(){
        return gameVersion;
    }
    
    public boolean isAlternate(){
        return alternate;
    }
    
    public boolean isAvailable(){
        return available;
    }
}
