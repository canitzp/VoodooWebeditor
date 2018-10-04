package de.canitzp.voodoowebeditor.internal.voodoo;

import de.canitzp.voodoowebeditor.internal.mod.CurseMod;
import de.canitzp.voodoowebeditor.internal.mod.Mod;

import java.net.MalformedURLException;
import java.util.List;

public class ModEntryJsonCurse{
    
    private String provider = "CURSE";
    private String id;
    private List<String> validMcVersions;
    private List<String> curseReleaseTypes;
    
    public ModEntryJsonCurse(CurseMod mod) throws MalformedURLException{
        this.id = mod.getProject().getSlug();
        this.validMcVersions = mod.getValidMcVersions();
        this.curseReleaseTypes = mod.getReleaseTypes();
    }
}
