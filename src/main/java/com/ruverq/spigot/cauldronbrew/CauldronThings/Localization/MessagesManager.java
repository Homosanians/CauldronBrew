package com.ruverq.spigot.cauldronbrew.CauldronThings.Localization;

import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            copyInputStreamToFile(deffile, loc_en);
        }
        if(!loc_ru.exists()){
            InputStream deffile = getFileFromResource("ru.yml");
            copyInputStreamToFile(deffile, loc_ru);
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

    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            // commons-io
            //IOUtils.copy(inputStream, outputStream);

        }

    }
}
