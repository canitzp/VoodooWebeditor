package de.canitzp.voodoowebeditor.site;

import de.canitzp.voodoowebeditor.IWebsite;
import de.canitzp.voodoowebeditor.ServletEntry;
import de.canitzp.voodoowebeditor.Site;
import j2html.tags.DomContent;

import javax.servlet.annotation.WebServlet;

import static j2html.TagCreator.*;

@WebServlet("/home")
public class Home extends ServletEntry implements IWebsite{
    
    @Override
    public void calculate(Site site){
        site.setWebsite(this);
    }
    
    @Override
    public DomContent render(Site site){
        return div();
    }
    
}
