package de.canitzp.voodoowebeditor;

import j2html.TagCreator;
import j2html.tags.DomContent;

import javax.servlet.annotation.WebServlet;

@WebServlet("/test")
public class Test extends ServletEntry implements IWebsite{
    
    @Override
    public void calculate(Site site){
        site.setWebsite(this);
    }
    
    @Override
    public DomContent render(Site site){
        return TagCreator.text("test");
    }
}
