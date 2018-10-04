package de.canitzp.voodoowebeditor;

import de.canitzp.voodoowebeditor.internal.user.User;
import j2html.tags.DomContent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static j2html.TagCreator.*;

public class Site{
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    private User user;
    
    private IWebsite website = site -> div();
    
    private Map<String, String> attributes = new HashMap<>();
    
    public Site(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        Object userRaw = request.getSession(true).getAttribute("user");
        if(userRaw instanceof User){
            this.setUser((User) userRaw);
        }
    }
    
    public void setWebsite(IWebsite website){
        this.website = website;
    }
    
    public void setUser(User user){
        this.user = user;
        this.request.getSession(true).setAttribute("user", user);
    }
    
    public User getUser(){
        return user;
    }
    
    public void redirect(String link) throws IOException{
        this.response.sendRedirect(link);
    }
    
    public boolean hasParameter(String parameter){
        return this.hasParameter(parameter, true);
    }
    
    public boolean hasParameter(String parameter, boolean canBeEmpty){
        String s = this.request.getParameter(parameter);
        return s != null && (canBeEmpty || !s.isEmpty());
    }
    
    public String getParameterOrEmpty(String parameter){
        return this.hasParameter(parameter) ? this.request.getParameter(parameter) : "";
    }
    
    public void setAttribute(String key, String value){
        if(key != null){
            this.attributes.put(key, value);
        }
    }
    
    public String getAttribute(String key){
        return this.attributes.get(key);
    }
    
    public String render(){
        return html()
            .withStyle("position: fixed; top: 0; bottom: 0; left: 0; right: 0;")
            .with(
                body()
                    .with(
                        div()
                            .withStyle("position: fixed; width: 15%; left: 0px; top: 0px; bottom: 0px; box-sizing: border-box; background-color: dimgray; border-top-right-radius: 20px; border-bottom-right-radius: 20px; border-color: black; border-style: solid; border-width: 1px")
                            .with(
                                this.renderSidebar()
                            )
                    )
                    .with(
                        div()
                            .withStyle("position: fixed; left: calc(15% + 5px); right: 5px; top: 5px; bottom: 5px; box-sizing: border-box; background-color: whitesmoke;")
                            .with(
                                this.website.render(this)
                            )
                    )
            ).renderFormatted();
    }
    
    private DomContent renderSidebar(){
        AtomicInteger modpackCount = new AtomicInteger();
        return div()
            .with(
                div()
                    .withStyle("text-align: center; margin: 5px 0 5px 0")
                    .with(
                        text("Voodoo Webeditor")
                    )
            )
            .with(
                div()
                    .withStyle("display: flex; margin: 5px; margin-top: 25px; border-radius: 5px; background-color: white")
                    .condWith(this.user != null,
                        div()
                            .withStyle("display: flex")
                            .with(
                                img()
                                    .withStyle("margin: 5px; margin-left: 10px; width: 50px; height: 50px; border-radius: 25px")
                                .withSrc(this.user != null ? this.user.getIcon() : "")
                            )
                            .with(
                                div()
                                    .withStyle("padding-top: 18px")
                                    .with(
                                        text("Hello "),
                                        b(this.user != null ? this.user.getUsername() : "NULL")
                                    )
                            )
                    )
                    .condWith(this.user == null,
                        div()
                            .with(
                                a("Login")
                                    .withHref("login")
                            )
                            .with(
                                br()
                            )
                            .with(
                                a("Register")
                                    .withHref("register")
                            )
                    )
            ).condWith(this.user != null,
                fieldset()
                    .withStyle("text-align: center")
                    .with(
                        legend("Modpacks:")
                            .attr("align", "center")
                    )
                    .with(
                        each(FileIO.getAllModpacks(this.getUser()), modpack -> {
                            modpackCount.getAndIncrement();
                            return button(modpack.getName())
                                .withStyle("margin: 5px auto 5px auto; display: block; width: 80%")
                                .attr("onclick", "window.location='editor?id=" + modpack.getId().toString() + "'");
                        })
                    )
                    .with(
                        button("Add new Modpack")
                            .withStyle("margin: 5px auto 5px auto; display: block; width: 80%")
                            .condAttr(modpackCount.get() >= 10, "disabled", "disabled")
                            .attr("onclick", "window.location='editor?new'")
                    )
            );
    }
    
}