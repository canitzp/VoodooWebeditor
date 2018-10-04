package de.canitzp.voodoowebeditor.internal.curse;


import com.google.gson.reflect.TypeToken;
import de.canitzp.voodoowebeditor.FileIO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author canitzp
 */
public class LoadCurse{
    
    public static Map<String, Pair<Integer, String>> SLUG_CACHE = new HashMap<>();
    private static long LAST_SLUG_UPDATE = System.currentTimeMillis();
    
    public static void updateSlugIdMap() throws IOException{
        if(SLUG_CACHE.isEmpty() || LAST_SLUG_UPDATE < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5)){
            LAST_SLUG_UPDATE = System.currentTimeMillis();
            System.out.println("Start to update slug-id cache! " + new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy").format(new Date()));
            long time = System.currentTimeMillis();
            URL url = new URL("https://curse.nikky.moe/graphql");
            HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            byte[] outputInBytes = "{\"query\": \"{addons(gameID: 432) {id slug name}}\"}".getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(outputInBytes);
            os.close();
            String s = IOUtils.toString(connection.getInputStream(), "UTF-8");
            System.out.println("Download of the slug-id map finished! Needed " + (System.currentTimeMillis() - time) + " milliseconds! " + new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy").format(new Date()));
            time = System.currentTimeMillis();
            int id = -1;
            String slug = null;
            for(String line : s.split("\n")){
                if(!line.contains("{") && !line.contains("}")){
                    if(line.contains("\"id\"")){
                        id = Integer.parseInt(line.substring(line.indexOf(":") + 2, line.indexOf(",")));
                    }else if(id != -1 && line.contains("\"slug\"")){
                        slug = line.substring(line.indexOf(":") + 3, line.lastIndexOf("\""));
                    }else if(id != -1 && slug != null && line.contains("\"name\"")){
                        SLUG_CACHE.put(slug, Pair.of(id, line.substring(line.indexOf(":") + 3, line.lastIndexOf("\""))));
                        id = -1;
                        slug = null;
                    }
                }
            }
            System.out.println("Finished to update slug-id cache! Needed " + (System.currentTimeMillis() - time) + " milliseconds! " + new SimpleDateFormat("HH:mm:ss.SSS dd.MM.yyyy").format(new Date()));
        }
    }
    
    public static int getIdFromSlug(String slug) throws IOException{
        updateSlugIdMap();
        return SLUG_CACHE.getOrDefault(slug, Pair.of(-1, null)).getKey();
    }
    
    public static List<CurseFile> getFilesJsonFor(int id) throws MalformedURLException{
        return readOnlineJson(new URL("https://curse.nikky.moe/api/addon/" + id + "/files"), new TypeToken<ArrayList<CurseFile>>(){}.getType());
    }
    
    static <T> T readOnlineJson(URL url, Type type){
        try {
            String text = IOUtils.toString(url.openConnection().getInputStream(), "UTF-8");
            if(text != null){
                return FileIO.GSON.fromJson(text, type);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static Map<String, String> getAllModsNamesAndSlug(){
        return SLUG_CACHE.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue().getValue(), Map.Entry::getKey, (a, b) -> b));
    }
    
    public static CurseProject getProjectById(int id) throws MalformedURLException{
        return readOnlineJson(new URL("https://curse.nikky.moe/api/addon/" + id), CurseProject.class);
    }
    
    public static CurseProject getProjectBySlug(String slug) throws IOException{
        int id = getIdFromSlug(slug);
        if(id != -1){
            return readOnlineJson(new URL("https://curse.nikky.moe/api/addon/" + id), CurseProject.class);
        }
        return null;
    }
    
    public static CurseProject getProjectByName(String name) throws MalformedURLException{
        for(Map.Entry<String, Pair<Integer, String>> entry : SLUG_CACHE.entrySet()){
            if(entry.getValue().getValue().equals(name)){
                return getProjectById(entry.getValue().getKey());
            }
        }
        return null;
    }
    
    public static CurseFile getFileFor(int projectId, List<String> gameVersions, String fileNameFilter) throws MalformedURLException{
        for(CurseFile file : getFilesJsonFor(projectId)){
            if(!Collections.disjoint(gameVersions, file.getGameVersion())){
                if(fileNameFilter.isEmpty() || file.getFileName().contains(fileNameFilter)){
                    return file;
                }
            }
        }
        return null;
    }
    
    public static CurseFile getFileById(CurseProject project, int fileId) throws MalformedURLException{
        for(CurseFile file : getFilesJsonFor(project.getId())){
            if(file.getId() == fileId){
                return file;
            }
        }
        return null;
    }
    
}
