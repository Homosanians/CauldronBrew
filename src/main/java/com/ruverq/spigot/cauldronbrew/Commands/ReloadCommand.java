package com.ruverq.spigot.cauldronbrew.Commands;

import com.ruverq.spigot.cauldronbrew.CauldronThings.Cauldron;
import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand{
    @Override
    public void OnCommand(Player player, String[] args) {
        Main.getInstance().reloadConfig();
        Cauldron.reload();
        player.sendMessage(Main.prefix + "Reloaded!");
    }

    @Override
    public String name() {
        return "reload";
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
