package de.canitzp.voodoowebeditor.internal.voodoo;

import de.canitzp.voodoowebeditor.internal.mod.CurseMod;

import java.net.MalformedURLException;

public class ModLockJsonCurse{
    
    private String provider = "CURSE";
    private String id;
    private String name;
    private int projectID;
    private int fileID;
    
    public ModLockJsonCurse(CurseMod mod) throws MalformedURLException{
        this.id = mod.getProject().getSlug();
        this.name = mod.getProject().getName();
        this.projectID = mod.getProject().getId();
        this.fileID = mod.getFile().getId();
    }
}
