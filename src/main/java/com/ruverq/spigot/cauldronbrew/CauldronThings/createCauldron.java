package com.ruverq.spigot.cauldronbrew.CauldronThings;

import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class createCauldron implements Listener {

    @EventHandler
    public void BlockAdd(BlockPlaceEvent e){
        if(e.getBlock().getType() != Material.CAULDRON) return;
        Player pl = e.getPlayer();
        String stringmaterialtoupgrage = Main.getInstance().getConfig().getString("Cauldron.blocktoupgrage", "");
        if(stringmaterialtoupgrage.isEmpty()){
            Cauldron cauldron = new Cauldron(e.getBlock());
            Cauldron.saveCauldron(cauldron);
        }else{

        }

    }
}
