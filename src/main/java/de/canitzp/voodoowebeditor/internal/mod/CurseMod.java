package de.canitzp.voodoowebeditor.internal.mod;

import com.google.gson.annotations.Expose;
import de.canitzp.voodoowebeditor.FileIO;
import de.canitzp.voodoowebeditor.internal.curse.CurseFile;
import de.canitzp.voodoowebeditor.internal.curse.CurseProject;
import de.canitzp.voodoowebeditor.internal.curse.LoadCurse;
import de.canitzp.voodoowebeditor.internal.voodoo.ModEntryJsonCurse;
import de.canitzp.voodoowebeditor.internal.voodoo.ModLockJsonCurse;

import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurseMod<T extends CurseMod> extends Mod<T>{
    
    private int projectId;
    private int fileId;
    private List<String> releaseTypes = new ArrayList<>(Arrays.asList("RELEASE", "BETA", "ALPHA"));
    
    public CurseMod(CurseProject project, CurseFile file){
        this.projectId = project.getId();
        this.fileId = file.getId();
    }
    
    public T setReleaseTypes(List<String> releaseTypes){
        this.releaseTypes = releaseTypes;
        return this.castThis();
    }
    
    public CurseProject getProject() throws MalformedURLException{
        return LoadCurse.getProjectById(this.projectId);
    }
    
    public CurseFile getFile() throws MalformedURLException{
        return LoadCurse.getFileById(this.getProject(), this.fileId);
    }
    
    public List<String> getReleaseTypes(){
        return releaseTypes;
    }
    
    @Override
    public String getProvider(){
        return "CURSE";
    }
    
    @Override
    public String getFileName() throws MalformedURLException{
        return this.getProject().getSlug();
    }
    
    @Override
    public void writeLockJson(FileWriter writer) throws MalformedURLException{
        FileIO.GSON.toJson(new ModLockJsonCurse(this), ModLockJsonCurse.class, writer);
    }
    
    @Override
    public void writeEntryJson(FileWriter writer) throws MalformedURLException{
        FileIO.GSON.toJson(new ModEntryJsonCurse(this), ModEntryJsonCurse.class, writer);
    }
}
