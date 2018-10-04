package de.canitzp.voodoowebeditor.internal.mod;

import de.canitzp.voodoowebeditor.FileIO;
import de.canitzp.voodoowebeditor.internal.curse.CurseFile;
import de.canitzp.voodoowebeditor.internal.curse.CurseProject;
import de.canitzp.voodoowebeditor.internal.voodoo.ModEntryJsonCurse;
import de.canitzp.voodoowebeditor.internal.voodoo.ModLockJsonCurse;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurseMod<T extends CurseMod> extends Mod<T>{
    
    private CurseProject project;
    private CurseFile file;
    private List<String> releaseTypes = new ArrayList<>(Arrays.asList("RELEASE", "BETA", "ALPHA"));
    
    public CurseMod(CurseProject project, CurseFile file){
        this.project = project;
        this.file = file;
    }
    
    public T setReleaseTypes(List<String> releaseTypes){
        this.releaseTypes = releaseTypes;
        return this.castThis();
    }
    
    public CurseProject getProject(){
        return project;
    }
    
    public CurseFile getFile(){
        return file;
    }
    
    public List<String> getReleaseTypes(){
        return releaseTypes;
    }
    
    @Override
    public String getProvider(){
        return "CURSE";
    }
    
    @Override
    public String getFileName(){
        return this.project.getSlug();
    }
    
    @Override
    public void writeLockJson(FileWriter writer){
        FileIO.GSON.toJson(new ModLockJsonCurse(this), ModLockJsonCurse.class, writer);
    }
    
    @Override
    public void writeEntryJson(FileWriter writer){
        FileIO.GSON.toJson(new ModEntryJsonCurse(this), ModEntryJsonCurse.class, writer);
    }
}
