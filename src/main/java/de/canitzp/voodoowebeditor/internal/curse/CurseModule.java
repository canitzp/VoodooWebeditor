package de.canitzp.voodoowebeditor.internal.curse;

public class CurseModule{
    
    long fingerprint;
    String foldername;
    
    @Override
    public String toString(){
        return String.format("CurseModule{folder '%s' fingerprint '%d'}", foldername, fingerprint);
    }
}
