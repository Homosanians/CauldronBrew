package com.ruverq.spigot.cauldronbrew.Commands;

import com.ruverq.spigot.cauldronbrew.CauldronThings.Cauldron;
import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand{
    @Override
    public void OnCommand(Player player, String[] args) {
        Main.getInstance().reloadConfig();
        Cauldron.reload();
        Main.mm.reload();
        player.sendMessage(Main.mm.getMessage("prefix") + Main.mm.getMessage("command_reload_success"));
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
