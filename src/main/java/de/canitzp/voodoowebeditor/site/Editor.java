package de.canitzp.voodoowebeditor.site;

import de.canitzp.voodoowebeditor.FileIO;
import de.canitzp.voodoowebeditor.IWebsite;
import de.canitzp.voodoowebeditor.ServletEntry;
import de.canitzp.voodoowebeditor.Site;
import de.canitzp.voodoowebeditor.internal.Modpack;
import de.canitzp.voodoowebeditor.internal.curse.CurseFile;
import de.canitzp.voodoowebeditor.internal.curse.CurseProject;
import de.canitzp.voodoowebeditor.internal.curse.LoadCurse;
import de.canitzp.voodoowebeditor.internal.mod.CurseMod;
import j2html.TagCreator;
import j2html.tags.DomContent;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import static j2html.TagCreator.*;

@WebServlet("/editor")
public class Editor extends ServletEntry implements IWebsite{
    
    @Override
    public void calculate(Site site) throws ServletException, IOException{
        if(site.hasParameter("id")){
            site.setAttribute("ModpackID", site.getParameterOrEmpty("id"));
            Modpack modpack = FileIO.getModpack(site.getParameterOrEmpty("id"), site.getUser());
            if(modpack != null){
                boolean changed = false;
                if(site.hasParameter("modpack_name", false)){
                    modpack.setName(site.getParameterOrEmpty("modpack_name"));
                    changed = true;
                }
                if(site.hasParameter("modpack_version", false)){
                    modpack.setVersion(site.getParameterOrEmpty("modpack_version"));
                    changed = true;
                }
                if(site.hasParameter("modpack_forge", false)){
                    modpack.setForgeVersion(site.getParameterOrEmpty("modpack_forge"));
                    changed = true;
                }
                if(site.hasParameter("modpack_mc", false)){
                    modpack.setMcVersions(Collections.singletonList(site.getParameterOrEmpty("modpack_mc")));
                    changed = true;
                }
                if(site.hasParameter("modpack_authors", false)){
                    List<String> authors = Arrays.asList(site.getParameterOrEmpty("modpack_authors").split("\r\n"));
                    if(authors.contains(site.getUser().getUsername())){
                        modpack.setAuthors(authors);
                        changed = true;
                    }else{
                        // TODO Error cannot remove yourself from modpack
                    }
                }
                if(changed){
                    FileIO.saveModpack(modpack, FileIO.getModpackDir(modpack));
                }
                if(site.hasParameter("cf", false)){
                    CurseProject project = LoadCurse.getProjectByName(site.getParameterOrEmpty("cf"));
                    String filter = site.getParameterOrEmpty("cffilter");
                    if(project != null){
                        CurseFile file = LoadCurse.getFileFor(project.getId(), modpack.getMcVersions(), filter);
                        if(file != null){
                            FileIO.addMod(modpack, new CurseMod(project, file));
                        } else {
                            System.out.println("No file could be found for '" + project.getName() + "'!");
                        }
                    } else {
                        System.out.println("Project '" + site.getParameterOrEmpty("cf") + "' couldn't be found!");
                    }
                } else if(site.hasParameter("dl")){
                    // add direct link mod
                }
            }
            site.setWebsite(this);
        }else if(site.hasParameter("new")){
            FileIO.createNewModpack(site.getUser(), String.valueOf(new Random().nextInt()));
            site.redirect("home");
        }
    }
    
    @Override
    public DomContent render(Site site){
        Modpack modpack = FileIO.getModpack(site.getAttribute("ModpackID"), site.getUser());
        if(modpack == null || !modpack.canUserUse(site.getUser())){
            return div();
        }
        Map<String, String> map = LoadCurse.getAllModsNamesAndSlug();
        List<String> name = new ArrayList<>(map.keySet());
        List<String> slugs = new ArrayList<>(map.values());
        return div()
            .withStyle("width: 50%; margin: 0 auto")
            .with(
                fieldset()
                    .with(
                        legend(modpack.getName())
                    )
                    .with(
                        form()
                            .withMethod("post")
                            .with(
                                label("Name: "),
                                input()
                                    .withValue(modpack.getName())
                                    .withType("text")
                                    .withName("modpack_name")
                                    .attr("maxlength", 20),
                                br()
                            )
                            .with(
                                label("Version: "),
                                input()
                                    .withValue(modpack.getVersion())
                                    .withType("text")
                                    .withName("modpack_version")
                                    .attr("maxlength", 20),
                                br()
                            )
                            .with(
                                label("Forge-Version: "),
                                input()
                                    .withValue(modpack.getForgeVersion())
                                    .withType("text")
                                    .withName("modpack_forge")
                                    .attr("maxlength", 4),
                                br()
                            )
                            .with(
                                label("Minecraft-Version: "),
                                select()
                                    .withName("modpack_mc")
                                    .with(
                                        optgroup()
                                            .attr("label", "1.12")
                                            .with(
                                                createOption(modpack, "1.12.2"),
                                                createOption(modpack, "1.12.1"),
                                                createOption(modpack, "1.12")
                                            ),
                                        optgroup()
                                            .attr("label", "1.11")
                                            .with(
                                                createOption(modpack, "1.11.2"),
                                                createOption(modpack, "1.11.1"),
                                                createOption(modpack, "1.11")
                                            ),
                                        optgroup()
                                            .attr("label", "1.10")
                                            .with(
                                                createOption(modpack, "1.10.2"),
                                                createOption(modpack, "1.10.1"),
                                                createOption(modpack, "1.10")
                                            )
                                    ),
                                br()
                            )
                            .with(
                                label("Authors: "),
                                br(),
                                textarea(String.join("\r\n", modpack.getAuthors()))
                                    .withName("modpack_authors")
                                    .attr("rows", 4),
                                br()
                            )
                            .with(
                                input()
                                    .withType("submit")
                                    .withValue("Update"),
                                br()
                            )
                    )
                    .with(
                        table()
                            .withStyle("border: 1px solid black")
                            .with(
                                th("Name"),
                                th("Provider")
                            )
                            .with(
                                each(modpack.getAllMods(), mod -> {
                                    try{
                                        return tr(td(mod.getFileName()), td(mod.getProvider())).withStyle("border: 1px solid black");
                                    }catch(MalformedURLException e){
                                        e.printStackTrace();
                                    }
                                    return null;
                                })
                            )
                    )
                    .with(
                        button("Add Curseforge mod")
                            .attr("onclick", "window.location='editor?id=" + modpack.getId().toString() + "&cf'")
                    )
                    .with(
                        button("Add direct URL mod")
                            .attr("onclick", "window.location='editor?id=" + modpack.getId().toString() + "&dl'")
                    )
                    .condWith(site.hasParameter("cf"),
                        div()
                            .with(
                                form()
                                    .with(
                                        label("Mod name:"),
                                        input()
                                            .withType("text")
                                            .withName("cf")
                                            .attr("list", "cursemods"),
                                        datalist()
                                            .withId("cursemods")
                                            .with(
                                                each(name, TagCreator::option)
                                            ),
                                        br()
                                    )
                                    .with(
                                        label("Name filter:"),
                                        input()
                                            .withType("text")
                                        .withName("cffilter")
                                    )
                                    .with(
                                        input()
                                            .withType("hidden")
                                            .withName("id")
                                            .withValue(modpack.getId().toString())
                                    )
                                    .with(
                                        input()
                                            .withType("submit")
                                    )
                            )
                    )
            );
    }
    
    private DomContent createOption(Modpack modpack, String mcVersion){
        return option(mcVersion).withValue(mcVersion).condAttr(modpack.getMcVersions().contains(mcVersion), "selected", null);
    }
    
}