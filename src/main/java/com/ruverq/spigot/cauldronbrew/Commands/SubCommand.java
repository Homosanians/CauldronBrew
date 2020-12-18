package com.ruverq.spigot.cauldronbrew.Commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public SubCommand(){

    }

    public abstract void OnCommand(Player player, String[] args);

    public abstract String name();
    public abstract String info();
    public abstract String[] aliases();

}
