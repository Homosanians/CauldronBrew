package com.ruverq.spigot.cauldronbrew.CauldronThings.Localization;

import com.ruverq.spigot.cauldronbrew.Main;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;

public class MessagesManager {
    Main plugin = Main.getInstance();
    HashMap<String, String> keylist = new HashMap<>();

    public void setUp() throws IOException {
        File loc = new File(Main.getInstance().getDataFolder() + File.separator + "localization");

        File loc_en = new File(Main.getInstance().getDataFolder() + File.separator + "localization" + File.separator + "en" + ".yml");
        File loc_ru = new File(Main.getInstance().getDataFolder() + File.separator + "localization" + File.separator + "ru" + ".yml");

        if(!loc.exists()){
            Path path = loc.toPath();
            Files.createDirectories(path);
        }
        if(!loc_en.exists()){
            InputStream deffile = getFileFromResource("en.yml");
            FileUtils.copyInputStreamToFile(deffile, loc_en);
        }
        if(!loc_ru.exists()){
            InputStream deffile = getFileFromResource("ru.yml");
            FileUtils.copyInputStreamToFile(deffile, loc_ru);
        }

        loadAllLocalizations();
        String code = plugin.getConfig().getString("Language", "en");
        loadLocalization(code);
    }

    public HashMap<String, File> locfiles = new HashMap<>();
    public void loadAllLocalizations(){
        locfiles.clear();
        File localization = new File(Main.getInstance().getDataFolder() + File.separator + "localization");

        for (File file : localization.listFiles()){
            if(!file.exists()) break;
            try{
                FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
                String code = fc.getString("code");
                locfiles.put(code, file);
            }catch (Exception ignored){

            }
        }
    }

    public void loadLocalization(String loc){

        String country = loc.toLowerCase();

        File locfilee = locfiles.get(country);
        FileConfiguration locfile = YamlConfiguration.loadConfiguration(locfilee);

        keylist.clear();

        for(String string : locfile.getConfigurationSection("").getKeys(true)){
            String s = locfile.getString(string).replace("&", "\u00a7");
            keylist.put(string, s);
        }

    }

    public void reload(){
        loadAllLocalizations();
        String code = plugin.getConfig().getString("localization", "en");
        loadLocalization(code);
    }

    private InputStream getFileFromResource(String fileName){
        return Main.getInstance().getResource(fileName);
    }

    public String getMessage(String code){
        code = code.toLowerCase(Locale.ROOT);
        return keylist.get(code);
    }
}
