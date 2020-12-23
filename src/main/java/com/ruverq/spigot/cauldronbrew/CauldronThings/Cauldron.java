package com.ruverq.spigot.cauldronbrew.CauldronThings;

import com.ruverq.spigot.cauldronbrew.CauldronThings.ParticleManager.ParticleManager;
import com.ruverq.spigot.cauldronbrew.CauldronThings.Hologramchiki.Hologram;
import com.ruverq.spigot.cauldronbrew.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Cauldron {

    List<ItemStack> itemsincauldron;
    Block cauldronblock;

    boolean isBoing = false;

    public static Collection<Cauldron> cauldrons = new ArrayList<>();
    public static HashMap<Block, Cauldron> blockCauldronHashMap = new HashMap<>();

    public static HashMap<String,ItemStack> rpitems = new HashMap<>();
    public static HashMap<ItemStack, String> itemsrp = new HashMap<>();
    public static HashMap<String, CraftCauldron> crafts = new HashMap<>();

    Hologram hologram;
    boolean hologramisEnabled = false;
    Cauldron cauldron;
    public Cauldron(Block block){
        cauldron = this;
        cauldronblock = block;
        itemsincauldron = new ArrayList<>();
        cauldrons.add(this);

        checksLava();
        blockCauldronHashMap.put(block, this);

        // Hologram func
        hologramisEnabled = Main.getInstance().getConfig().getBoolean("Hologram.enabled");
        if(hologramisEnabled){
            boolean boolvisibleradius = Main.getInstance().getConfig().getBoolean("Hologram.visiblinradius.enabled", true);
            boolean boolfloating = Main.getInstance().getConfig().getBoolean("Hologram.floating.enabled", true);
            int visibleradius = Main.getInstance().getConfig().getInt("Hologram.visiblinradius.radius", 10);
            double distance = Main.getInstance().getConfig().getDouble("Hologram.floating.distance", 0.1);

            hologram = new Hologram("Empty", block.getLocation().add(0.5,1.5,0.5));
            if(boolfloating){
                hologram.enableFloating((float) distance);
            }
            if(boolvisibleradius){
                hologram.enableVisibleInRadius(visibleradius);
            }
        }
        //
    }

    public static void reload(){
        cauldrons.forEach(cauldron1 -> {
            if(cauldron1.hologramisEnabled){
                cauldron1.hologram.remove();
            }
        });
        cauldrons.clear();
        rpitems.clear();
        crafts.clear();
        itemsrp.clear();
        blockCauldronHashMap.clear();

        setup();
    }

    public void stop(){
        cauldrons.remove(this);
    }

    public void addItem(ItemStack itemStack){
        boolean exsits = false;
        for(ItemStack itemincaud : itemsincauldron){
            if(isSimilarWithoutAmount(itemStack,itemincaud)){
                itemsincauldron.remove(itemincaud);
                ItemStack nowitem = new ItemStack(itemincaud);
                        nowitem.setAmount(itemincaud.getAmount() + itemStack.getAmount());
                itemsincauldron.add(nowitem);
                exsits = true;
                break;
            }
        }
        if(!exsits){
            itemsincauldron.add(itemStack);
        }
    }

    public static boolean isSimilarWithoutAmount(ItemStack i, ItemStack ii){
        ItemStack i2 = i.clone();
        ItemStack ii2 = ii.clone();
        i2.setAmount(1);
        ii2.setAmount(1);
        return i2.equals(ii2);
    }

    public void removeItem(ItemStack itemStack){
        itemsincauldron.remove(itemStack);
    }

    public List<ItemStack> getItemsincauldron() {
        return itemsincauldron;
    }

    public boolean isBoing(){
        return isBoing;
    }

    public static void setup(){
        cauldrons = new ArrayList<>();
        blockCauldronHashMap = new HashMap<>();

        try {
            Cauldron.CreateFoldersAndFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File cauldrons = new File(Main.getInstance().getInstance().getDataFolder() + File.separator + "cauldrons.yml");
        FileConfiguration yml_cauldrons = YamlConfiguration.loadConfiguration(cauldrons);

        List<String> strings = yml_cauldrons.getStringList("Cauldrons");
        for(String s : strings){
            s.split("\n");

            String w = s.split("\n")[0].split("w:")[1];
            double x = Double.parseDouble(s.split("\n")[1].split("x:")[1]);
            double y = Double.parseDouble(s.split("\n")[2].split("y:")[1]);
            double z = Double.parseDouble(s.split("\n")[3].split("z:")[1]);

            World world = Bukkit.getWorld(w);
            Location location = new Location(world,x,y,z);

            Block block = location.getBlock();
            if(block.getType() != Material.CAULDRON){
                removeCauldron(new Cauldron(location.getBlock()));
            }else{
                new Cauldron(location.getBlock());
            }
        }

        File items = new File(Main.getInstance().getDataFolder() + File.separator + "items");
        File crafts = new File(Main.getInstance().getDataFolder() + File.separator + "crafts");

        for(final File item : items.listFiles()){
            if(item.isDirectory()) continue;

            FileConfiguration yml_item = YamlConfiguration.loadConfiguration(item);
            for(String string : yml_item.getConfigurationSection("").getKeys(false)){
                if(rpitems.containsKey(string)) continue;

                ItemStack actucalitem = yml_item.getItemStack(string + ".itemstack");
                if(actucalitem != null){
                    rpitems.put(string, actucalitem);
                    itemsrp.put(actucalitem, string);
                }
            }
        }

        for(final File craft : crafts.listFiles()){
            if(craft.isDirectory()) continue;

            FileConfiguration yml_craft = YamlConfiguration.loadConfiguration(craft);

            for(String string : yml_craft.getConfigurationSection("").getKeys(false)){
                if(!yml_craft.getBoolean(string + ".enabled")) continue;
                if(Cauldron.crafts.containsKey(string)) continue;
                String craftstring = yml_craft.getString(string + ".ingredients");
                String color = yml_craft.getString(string + ".effect.color", "white");
                String effect = yml_craft.getString(string + ".effect.name", "null");

                String[] ingr = craftstring.split(";");
                List<ItemStack> ingredientsitems = new ArrayList<>();
                for(int i = 0; i < ingr.length; i++){
                    String ingrr = ingr[i];
                    String[] bruh = ingrr.split("\\(");
                    String name = bruh[0];
                    String amount = "1";
                    if(bruh.length > 1){
                        amount = bruh[1].replace(")", "");
                    }

                    ItemStack itemingr = null;
                    if(!rpitems.containsKey(name)){
                        Material material = Material.matchMaterial(name);
                        if(material == null) continue;
                        itemingr = new ItemStack(material);
                    }else{
                        itemingr = rpitems.get(name);
                    }
                    itemingr.setAmount(Integer.parseInt(amount));
                    ingredientsitems.add(itemingr);
                }

                String sresult = yml_craft.getString(string + ".result");
                if(sresult == null) continue;
                ItemStack result = null;
                if(!rpitems.containsKey(sresult)){
                    Material material = Material.matchMaterial(sresult);
                    if(material == null) continue;
                    result = new ItemStack(material);
                }else{
                    result = rpitems.get(sresult);
                }

                Cauldron.crafts.put(string, new CraftCauldron(result, ingredientsitems, effect, color));
            }
        }
    }

    private static void CreateFoldersAndFiles() throws IOException {
        File itemsfolder = new File(Main.getInstance().getDataFolder() + File.separator + "items");
        if(!itemsfolder.isDirectory()){
            Path path = itemsfolder.toPath();
            Files.createDirectories(path);
        }

        File items = new File(Main.getInstance().getDataFolder() + File.separator + "items" + File.separator + "defaultitem.yml");
        if(!items.exists()){
            items.createNewFile();
            FileConfiguration yml_items = YamlConfiguration.loadConfiguration(items);

            yml_items.set("defaultitem.itemstack", new ItemStack(Material.BEDROCK));

            yml_items.save(items);
        }

        File craftsfolder = new File(Main.getInstance().getDataFolder() + File.separator + "crafts");
        if(!craftsfolder.isDirectory()){
            Path path = craftsfolder.toPath();
            Files.createDirectories(path);
        }

        File crafts = new File(Main.getInstance().getDataFolder() + File.separator + "crafts" + File.separator + "defaultcraft.yml");
        if(!crafts.exists()){
            crafts.createNewFile();
            FileConfiguration yml_crafts = YamlConfiguration.loadConfiguration(crafts);

            yml_crafts.set("defaultcraft.enabled", true);

            yml_crafts.set("defaultcraft.result", "defaultitem");

            yml_crafts.set("defaultcraft.effect.name", "null");
            yml_crafts.set("defaultcraft.effect.color", "null");

            yml_crafts.set("defaultcraft.ingredients", "string(2);deafultitem(1)");

            yml_crafts.save(crafts);
        }
    }

    public static void saveCauldron(Cauldron cauldron){

        File cauldrons = new File(Main.getInstance().getInstance().getDataFolder() + File.separator + "cauldrons.yml");
        FileConfiguration yml_cauldrons = YamlConfiguration.loadConfiguration(cauldrons);

        List<String> strings = yml_cauldrons.getStringList("Cauldrons");

        Location loc = cauldron.getCauldronblock().getLocation();
        strings.add("w:" + loc.getWorld().getName() + "\n" + "x:" + loc.getX() + "\n" + "y:" + loc.getY() + "\n" + "z:" + loc.getZ());

        yml_cauldrons.set("Cauldrons", strings);

        try {
            yml_cauldrons.save(cauldrons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeCauldron(Cauldron cauldron){
        if(cauldron == null) return;
        File cauldrons = new File(Main.getInstance().getInstance().getDataFolder() + File.separator + "cauldrons.yml");
        FileConfiguration yml_cauldrons = YamlConfiguration.loadConfiguration(cauldrons);

        List<String> strings = yml_cauldrons.getStringList("Cauldrons");

        Location loc = cauldron.getCauldronblock().getLocation();
        strings.remove("w:" + loc.getWorld().getName() + "\n" + "x:" + loc.getX() + "\n" + "y:" + loc.getY() + "\n" + "z:" + loc.getZ());

        yml_cauldrons.set("Cauldrons", strings);

        try {
            yml_cauldrons.save(cauldrons);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        blockCauldronHashMap.remove(cauldron.getCauldronblock());
        Cauldron.cauldrons.remove(cauldron);
    }

    private void checksLava(){

        int loc = ThreadLocalRandom.current().nextInt(10,70);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!cauldrons.contains(cauldron)){
                    cancel();
                    return;
                }

                if(checkAuthenticity()){
                    StartBoil();
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), loc, 20 * 10);
    }

    public Block getCauldronblock() {
        return cauldronblock;
    }

    private boolean checkAuthenticity(){
        if(cauldronblock.getData() < 3) return false;

        Block blockunder = cauldronblock.getWorld().getBlockAt(cauldronblock.getX(), cauldronblock.getY() - 1, cauldronblock.getZ());
        Material mat = blockunder.getType();

        return mat == Material.LAVA || mat == Material.FIRE || mat == Material.CAMPFIRE;
    }

    public void clear(){
        if(hologramisEnabled){
            hologram.setDisplayName("Пусто.");
        }
        itemsincauldron = new ArrayList<>();
    }

    private void StartBoil(){
        int secondstoboil = Main.getInstance().getInstance().getConfig().getInt("Cauldron.secondstoboil", 1);

        if(isBoing) return;
        int period = 20;
        new BukkitRunnable() {
            @Override
            public void run() {

                if(!cauldrons.contains(cauldron)){
                    cancel();
                    return;
                }

                particleswithticks(period, cauldronblock.getLocation().clone().add(0.5,1,0.5));
                isBoing = true;


                if(checks > 0){

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            checkcrafts();
                        }
                    }.runTaskLater(Main.getInstance(), 1);

                    removeCheck();

                    if(hologramisEnabled){
                        StringBuilder sb = new StringBuilder();
                        for(ItemStack items : itemsincauldron){
                            if(items.getAmount() <= 1){
                                sb.append(getNormalName(items)).append(" ");
                            }else{
                                sb.append(getNormalName(items)).append("x").append(items.getAmount()).append(" ");
                            }
                        }
                        hologram.setDisplayName(removeLastCharacter(sb.toString()));
                    }

                }

                if(!checkAuthenticity()){
                    clear();
                    checksLava();
                    isBoing = false;
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), secondstoboil * 20L, period);
    }

    public static String getNormalName(ItemStack item){
        if(item == null){
            return "";
        }
        if(item.getItemMeta() == null || item.getItemMeta().getDisplayName().isEmpty()){
            return item.getType().getKey().toString().replace("minecraft:", "");
        }else{
            return item.getItemMeta().getDisplayName() + ChatColor.RESET;
        }
    }

    public int checks;

    public void addCheck(){
        if(checks < 10){
            checks++;
        }
    }

    public void removeCheck(){
        checks--;
    }

    public static String removeLastCharacter(String str) {
        String result = Optional.ofNullable(str)
                .filter(sStr -> sStr.length() != 0)
                .map(sStr -> sStr.substring(0, sStr.length() - 1))
                .orElse(str);
        return result;
    }

    private void checkcrafts(){

        crafts.forEach((s,c) ->{
            ResultAfterCraft resultAfterCraft = c.isCraftableWith(itemsincauldron);
            if(resultAfterCraft.getBool()){
                ItemStack result = c.getResult().clone();
                result.setAmount(result.getAmount() * resultAfterCraft.getScolko());
                Craft(result, c.getParticleeffect(), c.getColor());
                itemsincauldron = resultAfterCraft.getItemsnow();

                itemsincauldron.removeIf(itemincoud -> itemincoud.getType() == Material.AIR);
            }
        });
    }

    private void Craft(ItemStack result, String effect, Color color){

        Location dropitems = cauldronblock.getLocation().add(0.5,0.5,0.5);
        Item item = dropitems.getWorld().dropItem(dropitems, result);
        item.setPickupDelay(60);
        item.setGravity(false);
        item.setVelocity(new Vector(0,0.02,0));
        int period = 5;
        int seconds = 10;
        ParticleManager pm = new ParticleManager();
        pm.executeParticles(cauldronblock.getLocation().add(0.5,0.8,0.5), effect, color);

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(item.isDead() || i > (20 / period) * seconds) {
                    cancel();
                    return;
                }
                item.getLocation().getWorld().spawnParticle(Particle.DRIP_WATER, item.getLocation(), 1,0.1, 0.1, 0.1);
                i++;
            }
        }.runTaskTimer(Main.getInstance(), 1, period);
    }

    private void particleswithticks(int period, Location location){
        new BukkitRunnable() {

            int timess = 0;

            @Override
            public void run() {
                location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.15, 0, 0.15, 1, new Particle.DustOptions(Color.AQUA, 0.3f));

                if(timess >= 10){
                    cancel();
                }
                timess++;
            }
        }.runTaskTimer(Main.getInstance().getInstance(), 0, period / 10);
    }

    public static Cauldron getCauldron(Block block){
        return blockCauldronHashMap.get(block);
    }
}
