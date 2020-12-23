package com.ruverq.spigot.cauldronbrew.CauldronThings.Hologramchiki;

import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    ArmorStand hologram;
    Location originallocation;
    boolean isFloating;
    boolean isVisibleInRadius;
    public static List<Hologram> hologramList = new ArrayList<>();
    public Hologram(String name, Location location){
        originallocation = location;
        hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setCollidable(false);
        hologram.setInvulnerable(false);
        hologram.setCustomName(name);
        hologramList.add(this);
        hologram.setCustomNameVisible(true);
        hologram.setMarker(true);
    }

    public void setDisplayName(String newname){
        hologram.setCustomName(newname);
    }

    public void enableVisibleInRadius(int radius){
        if(isVisibleInRadius) return;
        isVisibleInRadius = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(hologram == null){
                    cancel();
                    return;
                }

                boolean isPlayerNearby = false;
                for(Entity entity : originallocation.getWorld().getNearbyEntities(originallocation, 1, radius ,radius)){
                    if(entity instanceof Player){
                        isPlayerNearby = true;
                        break;
                    }
                }
                setVisible(isPlayerNearby);
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    public void setVisible(boolean visibility){
        hologram.setCustomNameVisible(visibility);
    }

    public void remove(){
        hologram.remove();
        hologramList.remove(this);
        hologram = null;
    }

    public void setLocation(Location location){
        originallocation = location;
        hologram.teleport(location);
    }

    public static void cleanAll(){
        for(Hologram holo : hologramList){
            holo.getArmorStand().remove();
        }
        hologramList.clear();
    }

    public ArmorStand getArmorStand() {
        return hologram;
    }

    public void enableFloating(float distance){
        if(isFloating) return;
        isFloating = true;
        new BukkitRunnable() {
            float angle = 0f;
            @Override
            public void run() {
                if(getArmorStand() == null) {
                    cancel();
                    return;
                }
                if(!isFloating){
                    hologram.teleport(originallocation);
                    cancel();
                    return;
                }
                if(angle >= 360f){
                    angle = 0f;
                }
                double y = distance * Math.sin(angle);
                hologram.teleport(new Location(originallocation.getWorld(), originallocation.getX(), originallocation.getY() + y, originallocation.getZ()));
                angle += 0.1;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }

    public void disableFloating(int distance){
        isFloating = false;
    }

    public boolean isFloating() {
        return isFloating;
    }
}
