package de.canitzp.voodoowebeditor.internal;

import de.canitzp.voodoowebeditor.FileIO;
import de.canitzp.voodoowebeditor.internal.mod.Mod;
import de.canitzp.voodoowebeditor.internal.user.User;
import de.canitzp.voodoowebeditor.internal.voodoo.ModpackLockHJson;
import de.canitzp.voodoowebeditor.internal.voodoo.ModpackLockJson;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Modpack{
    
    private UUID id;
    private String name;
    private String version;
    private String forgeVersion;
    private List<String> authors = new ArrayList<>();
    
    private List<Mod> allMods = new ArrayList<>();
    private List<String> mcVersions = new ArrayList<>();
    
    private Modpack(User owner, UUID id){
        this.authors.add(owner.getUsername());
        this.id = id;
    }
    
    public static Modpack createNewModpack(User owner, String name){
        return new Modpack(owner, UUID.randomUUID()).setName(name);
    }
    
    public static Modpack loadEmptyModpack(User owner, UUID id){
        return new Modpack(owner, id);
    }
    
    public Modpack setName(String name){
        this.name = name;
        return this;
    }
    
    public void setVersion(String version){
        this.version = version;
    }
    
    public void setMcVersions(List<String> mcVersions){
        this.mcVersions = mcVersions;
    }
    
    public void setAuthors(List<String> authors){
        this.authors = authors;
    }
    
    public void setForgeVersion(String forgeVersion){
        this.forgeVersion = forgeVersion;
    }
    
    public UUID getId(){
        return id;
    }
    
    public String getName(){
        return name != null ? name : getId().toString();
    }
    
    public String getVersion(){
        return version;
    }
    
    public String getForgeVersion(){
        return forgeVersion;
    }
    
    public List<String> getAuthors(){
        return authors;
    }
    
    public List<Mod> getAllMods(){
        return allMods;
    }
    
    public List<String> getMcVersions(){
        return mcVersions;
    }
    
    public boolean canUserUse(User user){
        return this.authors.contains(user.getUsername());
    }
    
    public void writeLockJson(FileWriter writer){
        FileIO.GSON.toJson(new ModpackLockJson(this, Collections.emptyList()), ModpackLockJson.class, writer);
    }
    
    public void writeLockHJson(FileWriter writer){
        FileIO.GSON.toJson(new ModpackLockHJson(this), ModpackLockHJson.class, writer);
    }
    
}
