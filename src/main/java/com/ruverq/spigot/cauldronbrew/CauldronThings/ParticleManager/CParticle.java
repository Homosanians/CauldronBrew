package com.ruverq.spigot.cauldronbrew.CauldronThings.ParticleManager;

import org.bukkit.Color;
import org.bukkit.Location;

public interface CParticle {

    String name();
    int duration();

    void executeParticles(Location location, Color color);
}
