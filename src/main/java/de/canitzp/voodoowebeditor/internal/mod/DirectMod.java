package de.canitzp.voodoowebeditor.internal.mod;

import java.io.FileWriter;

public class DirectMod extends Mod{
    
    @Override
    public String getProvider(){
        return "DIRECT";
    }
    
    @Override
    public String getFileName(){
        return "";
    }
    
    @Override
    public void writeLockJson(FileWriter writer){
    
    }
    
    @Override
    public void writeEntryJson(FileWriter writer){
    
    }
}
