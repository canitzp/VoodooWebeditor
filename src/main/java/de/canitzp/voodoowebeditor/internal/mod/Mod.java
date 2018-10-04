package de.canitzp.voodoowebeditor.internal.mod;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class Mod<T extends Mod>{
    
    private boolean optional, selected, client = true, server = true;
    private List<String> validMcVersions = new ArrayList<>();
    
    protected T castThis(){
        return (T) this;
    }
    
    public abstract String getProvider();
    
    public abstract String getFileName();
    
    public T setMcVersions(List<String> validMcVersions){
        this.validMcVersions = validMcVersions;
        return this.castThis();
    }
    
    public T setClientOnly(){
        this.client = true;
        this.server = false;
        return castThis();
    }
    
    public T setServerOnly(){
        this.server = true;
        this.client = false;
        return castThis();
    }
    
    public T setBothSides(){
        this.server = this.client = true;
        return castThis();
    }
    
    public T setOptional(boolean defaultSelected){
        this.optional = true;
        this.selected = defaultSelected;
        return castThis();
    }
    
    public List<String> getValidMcVersions(){
        return validMcVersions;
    }
    
    public boolean isClientOnly(){
        return client;
    }
    
    public boolean isServerOnly(){
        return server;
    }
    
    public boolean isOptional(){
        return optional;
    }
    
    public boolean isSelected(){
        return selected;
    }
    
    public abstract void writeLockJson(FileWriter writer);
    
    public abstract void writeEntryJson(FileWriter writer);
}
