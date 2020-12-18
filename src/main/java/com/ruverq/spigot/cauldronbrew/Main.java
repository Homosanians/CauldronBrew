package com.ruverq.spigot.cauldronbrew;

import com.ruverq.spigot.cauldronbrew.CauldronThings.*;
import com.ruverq.spigot.cauldronbrew.CauldronThings.hologramchiki.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    public static Main main;

    File config;
    @Override
    public void onEnable() {
        setInstance(this);
        config = createConfig();

        Bukkit.getPluginManager().registerEvents(new EventDropInCauldron(), this);
        Bukkit.getPluginManager().registerEvents(new createCauldron(), this);
        Bukkit.getPluginManager().registerEvents(new createCaudronByClick(), this);
        Bukkit.getPluginManager().registerEvents(new removeCauldron(), this);
        Bukkit.getPluginManager().registerEvents(new cleanCauldron(), this);

        File cauldrons = new File(Main.getInstance().getDataFolder() + File.separator + "cauldrons.yml");

        try {
            cauldrons.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cauldron.setup();
    }

    @Override
    public void onDisable() {
        Hologram.cleanAll();
    }

    private File createConfig(){

        File config = new File(getDataFolder() + File.separator + "config.yml");
        if(!config.exists()){
            getLogger().info("Создаю новый config файл...");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        return config;
    }

    public static Main getInstance() {
        return main;
    }

    public void setInstance(Main main) {
        this.main = main;
    }
}
