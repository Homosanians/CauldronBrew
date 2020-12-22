package com.ruverq.spigot.cauldronbrew;

import com.ruverq.spigot.cauldronbrew.CauldronThings.*;
import com.ruverq.spigot.cauldronbrew.CauldronThings.Hologramchiki.Hologram;
import com.ruverq.spigot.cauldronbrew.CauldronThings.Localization.MessagesManager;
import com.ruverq.spigot.cauldronbrew.Commands.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public final class Main extends JavaPlugin {

    public static Main main;
    public static String prefix = ChatColor.YELLOW + "[CB] " + ChatColor.GRAY;
    public static MessagesManager mm;

    File config;
    @Override
    public void onEnable() {
        setInstance(this);
        config = createConfig();

        File cauldrons = new File(Main.getInstance().getDataFolder() + File.separator + "cauldrons.yml");

        Bukkit.getPluginManager().registerEvents(new EventDropInCauldron(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new createCauldron(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new createCaudronByClick(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new removeCauldron(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new cleanCauldron(), Main.getInstance());

        try {
            cauldrons.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mm = new MessagesManager();
        try {
            mm.setUp();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cauldron.setup();
        new CommandManager().setup();

    }

    @Override
    public void onDisable() {
        Hologram.cleanAll();
    }

    private File createConfig(){

        File config = new File(getDataFolder() + File.separator + "config.yml");
        if(!config.exists()){
            getLogger().info("Creating a config file...");
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
