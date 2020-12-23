package com.ruverq.spigot.cauldronbrew.Commands;

import com.ruverq.spigot.cauldronbrew.CauldronThings.Cauldron;
import com.ruverq.spigot.cauldronbrew.CauldronThings.Localization.MessagesManager;
import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class AddItem extends SubCommand {

    private final String prefix = mm.getMessage("prefix");
    private static final MessagesManager mm = Main.mm;
    
    @Override
    public void OnCommand(Player player, String[] args){


        if(args.length < 2){
            player.sendMessage(prefix + mm.getMessage("command_additem_error_arguments"));
            player.sendMessage(prefix + mm.getMessage("command_additem_usage"));
            return;
        }

        String name = args[1];
        ItemStack item = null;
        if(player.getInventory().getItemInMainHand().getType() == Material.AIR){
            if(player.getInventory().getItemInOffHand().getType() == Material.AIR){
                player.sendMessage(prefix + mm.getMessage("command_additem_error_notholdingitem"));
                return;
            }else{
                item = player.getInventory().getItemInOffHand();
            }
        }else{
            item = player.getInventory().getItemInMainHand();
        }

        if(Cauldron.rpitems.get(name) != null){
            player.sendMessage(prefix + mm.getMessage("command_additem_error_name"));
            return;
        }
        String a = addItem(name, item);
        if(!a.isEmpty()){
            player.sendMessage(ChatColor.RED + prefix + a);
            return;
        }

        String messagesucc = prefix + mm.getMessage("command_additem_success").replace("%itemname%", name);
        player.sendMessage(messagesucc);

    }

    public static String addItem(String name, ItemStack item){
        File items = new File(Main.getInstance().getDataFolder() + File.separator + "items" + File.separator + name + ".yml");
        if(items.exists()){
            return "Error. This file already somehow exists";
        }
        try {
            items.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error. File is not created. Check the console";
        }

        FileConfiguration yml_item = YamlConfiguration.loadConfiguration(items);
        yml_item.set(name + ".itemstack", item);

        try {
            yml_item.save(items);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error. File is not saved. Check the console";
        }
        Cauldron.rpitems.put(name, item);
        return "";
    }

    @Override
    public String name() {
        return "additem";
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
