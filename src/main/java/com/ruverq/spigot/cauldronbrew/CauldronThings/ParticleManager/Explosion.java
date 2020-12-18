package com.ruverq.spigot.cauldronbrew.CauldronThings.ParticleManager;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.Random;

public class Explosion implements CParticle{
    @Override
    public String name() {
        return "explosion";
    }

    @Override
    public int duration() {
        return 0;
    }

    @Override
    public void executeParticles(Location location, Color color) {
        location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 0);
        location.getWorld().spawnParticle(Particle.WATER_SPLASH, location, 50, 0.1,0.1,0.1);
        location.getWorld().spawnParticle(Particle.WATER_SPLASH, location, 20, 1,1,1);
        location.getWorld().spawnParticle(Particle.FALLING_WATER, location, 20, 1,1,1);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, new Random().nextFloat());
    }
}
