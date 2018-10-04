package de.canitzp.voodoowebeditor.internal.curse;

import java.net.URL;

public class CurseAuthor{
    
    String name;
    URL url;
    
    @Override
    public String toString() {
        return String.format("name: %s url: %s", name, url);
    }
    
}
