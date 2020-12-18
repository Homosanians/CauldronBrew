package com.ruverq.spigot.cauldronbrew.CauldronThings;

import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EventDropInCauldron implements Listener {

    @EventHandler
    public void onDropInCauldron(PlayerDropItemEvent e){
        Item itementity = e.getItemDrop();

        float radius = (float) Main.getInstance().getConfig().getDouble("dropitem.checknearbycauldron.radius", 0.1);
        int delay = Main.getInstance().getConfig().getInt("dropitem.checknearbycauldron.delay", 5);
        int period = Main.getInstance().getConfig().getInt("dropitem.checknearbycauldron.period", 5);
        int secondstocheck = Main.getInstance().getConfig().getInt("dropitem.checknearbycauldron.secondstocheck", 20);
        int maxitemsincauldron = Main.getInstance().getConfig().getInt("Cauldron.maxitemsincauldron", 32);

        new BukkitRunnable() {

            Cauldron cauldron;
            int i = 0;
            @Override
            public void run() {
                Location locrightnow = itementity.getLocation();

                List<Block> blocksnearby = getNearbyBlocks(locrightnow, radius);
                for(Block block : blocksnearby){
                    if(block.getType() == Material.CAULDRON){
                        Cauldron cauldron = Cauldron.getCauldron(block);
                        if(cauldron == null) continue;
                        if(cauldron.isBoing){
                            long amountofitemsincauldron = 0;
                            for (ItemStack itemStack : cauldron.getItemsincauldron()) {
                                amountofitemsincauldron += itemStack.getAmount();
                            }

                            if(amountofitemsincauldron + itementity.getItemStack().getAmount() > maxitemsincauldron){
                                if(maxitemsincauldron - amountofitemsincauldron > 0){
                                    ItemStack result = itementity.getItemStack().clone();
                                    int scolkoamount = (int) (maxitemsincauldron - amountofitemsincauldron);
                                    int maxtoadd = result.getAmount() - scolkoamount;

                                    int scolkoostalos = result.getAmount() - maxtoadd;

                                    itementity.getItemStack().setAmount(maxtoadd);
                                    result.setAmount(scolkoostalos);

                                    cauldron.addItem(result);
                                    cauldron.addCheck();
                                    locrightnow.getWorld().spawnParticle(Particle.WATER_SPLASH, locrightnow, 10);
                                    block.getLocation().getWorld().playSound(block.getLocation(), Sound.ENTITY_DROWNED_HURT_WATER, 1f, 0f);
                                    itementity.setVelocity(Vector.getRandom().normalize().multiply(1f));
                                    cancel();
                                    break;
                                }else{
                                    itementity.setVelocity(Vector.getRandom().normalize().multiply(0.5f));
                                    continue;
                                }
                            }

                            if(itementity.getItemStack().getType() == Material.DIAMOND && itementity.getItemStack().getAmount() == 32){
                                try{
                                    e.getPlayer().getLocation().getWorld().strikeLightning(e.getPlayer().getLocation());
                                }catch (Exception ignored){

                                }
                            }

                            cauldron.addItem(itementity.getItemStack());
                            cauldron.addCheck();
                            locrightnow.getWorld().spawnParticle(Particle.WATER_SPLASH, locrightnow, 10);
                            itementity.remove();
                            block.getLocation().getWorld().playSound(block.getLocation(), Sound.ENTITY_DROWNED_HURT_WATER, 1f, 0f);
                            cancel();
                            break;
                        }
                    }
                }


                if(i > (20 / period) * secondstocheck){
                    this.cancel();
                }
                i++;
            }
        }.runTaskTimer(Main.getInstance(), delay, period);
    }

    public List<Block> getNearbyBlocks(Location center, float raduis){
        List<Block> blocks = new ArrayList<>();
        World world = center.getWorld();
        if(world == null){
            System.out.println("World is null wtf");
            return null;
        }

        for (float x = raduis; x >= -raduis; x-= raduis) {
            for (float y = raduis; y >= -raduis; y-= raduis) {
                for (float z = raduis; z >= -raduis; z-= raduis) {
                    Block b = world.getBlockAt(new Location(world, x + center.getX(), y + center.getY(), z + center.getZ()));
                    if(!blocks.contains(b) && (!b.getType().equals(Material.VOID_AIR) && !b.getType().equals(Material.CAVE_AIR)  && !b.getType().equals(Material.AIR))){
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }
}
