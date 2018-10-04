package de.canitzp.voodoowebeditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import de.canitzp.voodoowebeditor.internal.Modpack;
import de.canitzp.voodoowebeditor.internal.curse.LoadCurse;
import de.canitzp.voodoowebeditor.internal.mod.Mod;
import de.canitzp.voodoowebeditor.internal.user.User;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

import javax.servlet.ServletException;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class FileIO{
    
    public static final File ROOT_DIR = new File("voodoo_webeditor");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static boolean hasInitialized = false;
    
    private static final Map<UUID, Modpack> LOADED_MODPACKS = new HashMap<>();
    public static final Map<String, User> LOADED_USER = new HashMap<>();
    
    public static void javaStart() throws ServletException, IOException{
        if(!hasInitialized){
            System.out.println("Initializing started");
            hasInitialized = true;
            File usersDir = new File(ROOT_DIR, "users");
            usersDir.mkdirs();
            System.out.println("ROOT DIR: " + ROOT_DIR.getAbsolutePath());
            for(File userdata : FileUtils.listFiles(usersDir, new String[]{"json"}, true)){
                if("userdata.json".equals(userdata.getName())){
                    try(FileReader reader = new FileReader(userdata)){
                        User user = GSON.fromJson(reader, User.class);
                        if(user != null){
                            LOADED_USER.put(user.getUsername(), user);
                        }
                    }catch(IOException | JsonParseException e){
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("User files analysed. " + LOADED_USER.size() + " users found.");
            for(User user : LOADED_USER.values()){
                File modpackDir = new File(getUserDir(user), "modpacks");
                File[] directories = modpackDir.listFiles();
                if(directories != null){
                    for(File dir : directories){
                        try{
                            Modpack modpack = loadModpack(dir);
                            if(modpack != null){
                                LOADED_MODPACKS.put(modpack.getId(), modpack);
                            }
                        } catch(IllegalArgumentException ignored){}
                    }
                }
            }
            System.out.println("Modpacks analysed. " + LOADED_MODPACKS.size() + " modpacks found.");
    
            LoadCurse.updateSlugIdMap();
    
            System.out.println("Initializing finished");
        }
    }
    
    public static void saveUser(User user){
        File userData = new File(getUserDir(user), "userdata.json");
        if(userData.exists()){
            userData.delete();
        }
        userData.getParentFile().mkdirs();
        try(FileWriter writer = new FileWriter(userData)){
            GSON.toJson(user, User.class, writer);
        }catch(IOException e){
            e.printStackTrace();
        }
        FileIO.LOADED_USER.put(user.getUsername(), user);
    }
    
    public static void createNewModpack(User owner, String name){
        Modpack modpack = Modpack.createNewModpack(owner, name);
        
        File modpackDir = getModpackDir(modpack);
        modpackDir.mkdirs();
        
        File modpackLockHJson = new File(modpackDir, modpack.getId().toString() + ".lock.hjson");
        File modpackLockJson = new File(modpackDir, modpack.getId().toString() + ".lock.json");
        if(modpackLockHJson.exists()){
            modpackLockHJson.delete();
        }
        if(modpackLockJson.exists()){
            modpackLockJson.delete();
        }
        
        try(FileWriter writer = new FileWriter(modpackLockHJson)){
            modpack.writeLockHJson(writer);
        } catch(IOException e){
            e.printStackTrace();
        }
    
        try(FileWriter writer = new FileWriter(modpackLockJson)){
            modpack.writeLockJson(writer);
        } catch(IOException e){
            e.printStackTrace();
        }
        LOADED_MODPACKS.put(modpack.getId(), modpack);
        saveModpack(modpack, modpackDir);
    }
    
    public static void addMod(Modpack modpack, Mod mod) throws MalformedURLException{
        File modsDir = new File(getModpackSourceDir(modpack), "mods" + (mod.isClientOnly() ? "/_CLIENT" : (mod.isServerOnly() ? "/_SERVER" : "")));
        modsDir.mkdirs();
        File modLock = new File(modsDir, mod.getFileName() + ".lock.hjson");
        if(modLock.exists()){
            modLock.delete();
        }
        try(FileWriter writer = new FileWriter(modLock)){
            mod.writeLockJson(writer);
        } catch(IOException e){
            e.printStackTrace();
        }
        File modEntry = new File(modsDir, mod.getFileName() + ".entry.hjson");
        if(modEntry.exists()){
            modEntry.delete();
        }
        try(FileWriter writer = new FileWriter(modEntry)){
            mod.writeEntryJson(writer);
        } catch(IOException e){
            e.printStackTrace();
        }
        modpack.getAllMods().add(mod);
        mod.setMcVersions(modpack.getMcVersions());
        saveModpack(modpack, getModpackDir(modpack));
    }
    
    public static void saveModpack(Modpack modpack, File modpackDir){
        File modpackJson = new File(modpackDir, "modpack.json");
        if(modpackJson.exists()){
            modpackJson.delete();
        }
        modpackJson.getParentFile().mkdirs();
        try(FileWriter writer = new FileWriter(modpackJson)){
            GSON.toJson(modpack, Modpack.class, writer);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static Modpack loadModpack(File modpackDir){
        File modpackJson = new File(modpackDir, "modpack.json");
        if(modpackJson.exists()){
            try(FileReader reader = new FileReader(modpackJson)){
                return GSON.fromJson(reader, Modpack.class);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static File getUserDir(User user){
        return new File(ROOT_DIR, "users/" + user.getFileNameSaveUsername());
    }
    
    public static File getModpackDir(Modpack modpack){
        User user = getUser(modpack.getAuthors().get(0));
        return new File(getUserDir(user), "modpacks/" + modpack.getId().toString());
    }
    
    public static File getModpackSourceDir(Modpack modpack){
        return new File(getModpackDir(modpack), "src");
    }
    
    public static List<Modpack> getAllModpacks(User user){
        List<Modpack> modpacks = new ArrayList<>();
        if(user != null){
            for(Modpack modpack : LOADED_MODPACKS.values()){
                if(modpack.canUserUse(user)){
                    modpacks.add(modpack);
                }
            }
        }
        return Collections.unmodifiableList(modpacks);
    }
    
    public static Modpack getModpack(String id, User user){
        try{
            UUID uuid = UUID.fromString(id);
            return getModpack(uuid, user);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static Modpack getModpack(UUID id, User user){
        if(id != null && user != null){
            Modpack modpack = LOADED_MODPACKS.get(id);
            if(modpack != null && modpack.canUserUse(user)){
                return modpack;
            }
        }
        return null;
    }
    
    public static boolean doesUsernameExist(String username){
        return LOADED_USER.containsKey(username);
    }
    
    public static User getUser(String username){
        return LOADED_USER.get(username);
    }
}
