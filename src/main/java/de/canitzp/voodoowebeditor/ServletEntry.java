package de.canitzp.voodoowebeditor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ServletEntry extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        this.call(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        this.call(req, resp);
    }
    
    private void call(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        FileIO.javaStart(); // This is initialized once the server has started, never twice!
        Site site = new Site(request, response);
        this.calculate(site);
        response.getWriter().append(site.render());
    }
    
    public abstract void calculate(Site site) throws ServletException, IOException;
    
}
