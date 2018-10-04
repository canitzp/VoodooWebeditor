package de.canitzp.voodoowebeditor.site;

import de.canitzp.voodoowebeditor.FileIO;
import de.canitzp.voodoowebeditor.IWebsite;
import de.canitzp.voodoowebeditor.ServletEntry;
import de.canitzp.voodoowebeditor.Site;
import de.canitzp.voodoowebeditor.internal.user.User;
import j2html.tags.DomContent;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.annotation.WebServlet;

import java.io.IOException;

import static j2html.TagCreator.*;
import static j2html.TagCreator.br;
import static j2html.TagCreator.input;

@WebServlet("/login")
public class Login extends ServletEntry implements IWebsite{
    
    @Override
    public void calculate(Site site) throws IOException{
        if(site.hasParameter("username", false) && site.hasParameter("password", false)){
            String username = site.getParameterOrEmpty("username");
            String pwdHash = DigestUtils.sha256Hex(site.getParameterOrEmpty("password"));
            User user = FileIO.LOADED_USER.get(username);
            if(user != null && user.arePasswordsTheSame(pwdHash)){
                site.setUser(user);
                site.redirect("home");
            }
        }
        site.setWebsite(this);
    }
    
    @Override
    public DomContent render(Site site){
        return div()
            .with(
                fieldset()
                    .with(
                        form()
                            .withMethod("post")
                            .with(label("Username:"))
                            .with(br())
                            .with(
                                input()
                                    .withType("text")
                                    .withName("username")
                            )
                            .with(br())
                            .with(label("Password:"))
                            .with(br())
                            .with(
                                input()
                                    .withType("password")
                                    .withName("password")
                            )
                            .with(br())
                            .with(
                                input()
                                    .withType("submit")
                                    .withValue("Login")
                            )
                    )
            );
    }
}
