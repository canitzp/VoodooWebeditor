package de.canitzp.voodoowebeditor.site;

import de.canitzp.voodoowebeditor.FileIO;
import de.canitzp.voodoowebeditor.IWebsite;
import de.canitzp.voodoowebeditor.ServletEntry;
import de.canitzp.voodoowebeditor.Site;
import de.canitzp.voodoowebeditor.internal.user.User;
import j2html.tags.DomContent;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;

import static j2html.TagCreator.*;

@WebServlet("/register")
public class Register extends ServletEntry implements IWebsite{
    
    @Override
    public void calculate(Site site) throws ServletException, IOException{
        if(site.hasParameter("username", false) && site.hasParameter("password", false) && site.hasParameter("mail", false)){
            String username = site.getParameterOrEmpty("username");
            String pwdHash = DigestUtils.sha256Hex(site.getParameterOrEmpty("password"));
            String email = site.getParameterOrEmpty("mail");
            if(!FileIO.doesUsernameExist(username)){
                User user = new User();
                user.setUsername(username);
                user.setEmail(email);
                user.setPwdHash(pwdHash);
                site.setUser(user);
                FileIO.saveUser(user);
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
                    .with(label("Password: (Choose what you want, no necessary pattern)"))
                    .with(br())
                    .with(
                        input()
                        .withType("password")
                        .withName("password")
                    )
                    .with(br())
                    .with(label("E-Mail:"))
                    .with(br())
                    .with(
                        input()
                        .withType("email")
                        .withName("mail")
                    )
                    .with(br())
                    .with(
                        input()
                        .withType("submit")
                    )
                )
            );
    }
}
