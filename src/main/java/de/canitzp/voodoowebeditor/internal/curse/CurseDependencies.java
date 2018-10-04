package de.canitzp.voodoowebeditor.internal.curse;

public class CurseDependencies{
    
    int addOnId;
    String type;
    
    @Override
    public String toString(){
        return String.format("CurseDependency{type '%s' addOnId '%d'}", type, addOnId);
    }
}
