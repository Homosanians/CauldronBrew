package com.ruverq.spigot.cauldronbrew.Commands;

import com.ruverq.spigot.cauldronbrew.CauldronThings.Localization.MessagesManager;
import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandManager implements CommandExecutor {

    // Thanks for tutorial, {TheSourceCode}!

    private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();

    private final Main plugin = Main.getInstance();

    //main command
    public String main = "cb";
    //Sub Commands
    public List<String> subcomm = new ArrayList<>();

    public List<String> helpinfo = new ArrayList<>();
    private String prefix = Main.prefix;
    private final MessagesManager mm = Main.mm;

    //Help info

    public void setup(){
        prefix = mm.getMessage("prefix");
        plugin.getCommand(main).setExecutor(this);

        this.commands.add(new AddItem());
        this.commands.add(new ReloadCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(prefix + mm.getMessage("commands_error_notplayer"));
            return true;
        }
        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase(main)){
            if(args.length == 0){
                return true;
            }

            SubCommand target = this.get(args[0]);

            if(target == null){
                return true;
            }
            if(!player.hasPermission("CauldronBrew.admin")){
                return true;
            }

            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.addAll(Arrays.asList(args));
            arrayList.remove(0);

            try{
                target.OnCommand(player, args);
            }catch (Exception eee){
                player.sendMessage(prefix + mm.getMessage("commands_error"));
                eee.printStackTrace();
            }

        }
        return true;
    }

    private SubCommand get(String name){
        Iterator<SubCommand> SubCommands = this.commands.iterator();

        while(SubCommands.hasNext()){
            SubCommand sc = SubCommands.next();
            if(sc.name().equalsIgnoreCase(name)){
                return sc;
            }
            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for(int var5 = 0; var5 < length; ++var5){
                String alias = aliases[var5];
                if(name.equalsIgnoreCase(alias)){
                    return sc;
                }
            }
        }
        return null;
    }
}
