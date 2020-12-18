package com.ruverq.spigot.cauldronbrew.CauldronThings.ParticleManager;

import org.bukkit.Color;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    List<CParticle> particles = new ArrayList<>();
    public ParticleManager(){
        particles.add(new EpicParticle());
        particles.add(new Explosion());
    }

    public void executeParticles(Location location, String name, Color color){
        particles.forEach(particles ->{
            if(particles.name().equalsIgnoreCase(name)){
                particles.executeParticles(location, color);
            }
        });
    }
}
