package com.ruverq.spigot.cauldronbrew.CauldronThings.Localization;

import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MessagesManager {
    Main plugin = Main.getInstance();
    HashMap<String, String> keylist = new HashMap<>();

    public void setUp() throws IOException {
        File locDir = new File(Main.getInstance().getDataFolder() + File.separator + "localization");

        if(!locDir.exists()){
            Path path = locDir.toPath();
            Files.createDirectories(path);
        }

        List<String> locCodes = getLocFiles();

        for (String code : locCodes) {
            File locFile = new File(Main.getInstance().getDataFolder()
                    + File.separator + "localization" + File.separator + code + ".yml");

            if (!locFile.exists()) {
                InputStream deffile = getFileFromResource(code + ".yml");
                copyInputStreamToFile(deffile, locFile);
            }
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

    private List<String> getLocFiles() {
        List<String> filenames = new ArrayList<>();

        try {
            URL resource = plugin.getClass().getResource("/messages");
            File locMessagesDir = Paths.get(resource.toURI()).toFile();

            for (final File f : Objects.requireNonNull(locMessagesDir.listFiles())) {
                if (f.isFile()) {
                    if (f.getName().matches(".*\\.yml")) {
                        filenames.add(f.getAbsolutePath());
                    }
                }

            }
        }
        catch (URISyntaxException e) { }

        return filenames;
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
        }

    }
}
