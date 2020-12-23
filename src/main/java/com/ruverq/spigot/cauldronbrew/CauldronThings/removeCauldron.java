package com.ruverq.spigot.cauldronbrew.CauldronThings;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class removeCauldron implements Listener {

    @EventHandler
    public void BlockRemove(BlockBreakEvent e){
        if(e.getBlock().getType() != Material.CAULDRON) return;
        Cauldron.removeCauldron(Cauldron.getCauldron(e.getBlock()));
    }
}
