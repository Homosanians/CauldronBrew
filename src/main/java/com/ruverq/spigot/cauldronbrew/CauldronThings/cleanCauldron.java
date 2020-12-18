package com.ruverq.spigot.cauldronbrew.CauldronThings;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class cleanCauldron implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getItem() == null) return;
        if(e.getClickedBlock().getType() != Material.CAULDRON) return;
        if(!Objects.equals(e.getHand(), EquipmentSlot.HAND)) return;
        List<Material> allowedshovels = new ArrayList<>();
        allowedshovels.add(Material.DIAMOND_SHOVEL);
        allowedshovels.add(Material.NETHERITE_SHOVEL);
        allowedshovels.add(Material.GOLDEN_SHOVEL);
        allowedshovels.add(Material.IRON_SHOVEL);
        allowedshovels.add(Material.WOODEN_SHOVEL);
        allowedshovels.add(Material.STONE_SHOVEL);

        if(!allowedshovels.contains(e.getItem().getType())) return;
        Cauldron cauldron = Cauldron.getCauldron(e.getClickedBlock());
        if(cauldron == null) return;
        if(cauldron.getItemsincauldron().isEmpty()) return;
        cauldron.clear();
        Location loc = e.getClickedBlock().getLocation();
        loc.getWorld().playSound(loc, Sound.BLOCK_SAND_BREAK, 1,  new Random().nextFloat());
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0.5, 0.5, 0.5);

        ItemMeta itemMeta = e.getItem().getItemMeta();
        Damageable damageable = (Damageable) itemMeta;
        damageable.setDamage(damageable.getDamage() + 1);

        e.getItem().setItemMeta((ItemMeta) damageable);
        cauldron.clear();
    }
}
