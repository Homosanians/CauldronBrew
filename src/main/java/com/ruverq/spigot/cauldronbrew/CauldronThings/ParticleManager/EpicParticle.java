package com.ruverq.spigot.cauldronbrew.CauldronThings.ParticleManager;

import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class EpicParticle implements CParticle {
    @Override
    public String name() {
        return "Epic";
    }

    @Override
    public int duration() {
        return 20 * 4;
    }

    private final float radius = 0.5f;
    private float angle = 0f;
    private final int speed = 2;
    private final int count = 3;

    @Override
    public void executeParticles(Location location, Color color) {


        new BukkitRunnable() {

            double y = 0;
            int ii = 0;
            @Override
            public void run() {
                if(ii > (1D / speed) * duration()){
                    cancel();
                }
                for(int i = 1; i < count + 1; i++){
                    double newangle = angle + ((120D / count) * i);
                    double x = (radius * Math.sin(newangle));
                    double z = (radius * Math.cos(newangle));

                    location.getWorld().spawnParticle(Particle.REDSTONE, new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ() + z),
                            1, new Particle.DustOptions(color, 1f));

                }

                angle += 0.05;
                y += 0.05;
                ii++;
            }
        }.runTaskTimer(Main.getInstance(), 0, speed);
    }
}
