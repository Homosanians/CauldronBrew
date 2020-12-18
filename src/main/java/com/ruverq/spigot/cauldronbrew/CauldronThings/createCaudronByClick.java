package com.ruverq.spigot.cauldronbrew.CauldronThings;

import com.ruverq.spigot.cauldronbrew.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class createCaudronByClick implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getClickedBlock().getType() != Material.CAULDRON) return;
        if(Cauldron.getCauldron(e.getClickedBlock()) != null) return;
        if(e.getItem() == null) return;
        String stringmaterialtoupgrage = Main.getInstance().getConfig().getString("Cauldron.blocktoupgrage", "");
        Material material = Material.matchMaterial(stringmaterialtoupgrage);
        if(e.getItem().getType() == material){
            e.setCancelled(true);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            Cauldron cauldron = new Cauldron(e.getClickedBlock());
            Cauldron.saveCauldron(cauldron);

            Block beffect = e.getClickedBlock();

            Location loc = beffect.getLocation();
            loc.add(0.5, 0, 0.5);

            Location s1 = loc.clone().add(0.5, 0, 0.5);
            Location s2 = loc.clone().add(0.5, 0, -0.5);
            Location s3 = loc.clone().add(-0.5, 0, -0.5);
            Location s4 = loc.clone().add(-0.5, 0, 0.5);

            List<Location> points = new ArrayList<>();
            points.add(s1);
            points.add(s2);
            points.add(s3);
            points.add(s4);

            new BukkitRunnable() {

                Location beforepoint = null;
                int quality = 10;
                int iterations = 5;
                int i = 0;

                @Override
                public void run() {
                    if(i >= iterations){
                        cancel();
                        return;
                    }
                    for(Location point : points){
                        point.add(0,1 / (float)iterations, 0);
                        if(beforepoint != null){
                            drawLine(point, beforepoint, 1 / (double)10);
                        }
                        beforepoint = point;
                    }
                    i++;
                }
            }.runTaskTimer(Main.getInstance(), 1, 4);
        }
    }

    public static void drawLine(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.LIME, 1));
            length += space;
        }
    }
}
