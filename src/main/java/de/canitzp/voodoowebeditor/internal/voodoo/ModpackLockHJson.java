package de.canitzp.voodoowebeditor.internal.voodoo;

import de.canitzp.voodoowebeditor.internal.Modpack;
import de.canitzp.voodoowebeditor.internal.mod.Mod;

import java.util.List;

public class ModpackLockHJson{
    
    private String id;
    private String mcVersion;
    private String title;
    private String version;
    private List<String> authors;
    private String forge;
    
    public ModpackLockHJson(Modpack modpack){
        this.id = modpack.getId().toString();
        this.mcVersion = modpack.getMcVersions().isEmpty() ? "" : modpack.getMcVersions().get(0);
        this.title = modpack.getName();
        this.version = modpack.getVersion();
        this.authors = modpack.getAuthors();
        this.forge = modpack.getForgeVersion();
    }
    
}
