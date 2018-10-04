package de.canitzp.voodoowebeditor.internal.voodoo;

import de.canitzp.voodoowebeditor.internal.Modpack;

import java.util.List;

public class ModpackLockJson extends ModpackLockHJson{
    
    private List<Object> features;
    
    public ModpackLockJson(Modpack modpack, List<Object> features){
        super(modpack);
        this.features = features;
    }
}
