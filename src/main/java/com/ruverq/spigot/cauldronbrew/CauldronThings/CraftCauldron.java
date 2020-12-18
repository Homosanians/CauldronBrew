package com.ruverq.spigot.cauldronbrew.CauldronThings;

import com.ruverq.spigot.cauldronbrew.CauldronThings.ParticleManager.CParticle;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CraftCauldron {


    ItemStack result;
    String particleeffect;
    Color color;
    List<ItemStack> ingredients;
    public CraftCauldron(ItemStack result, List<ItemStack> ingredients, String particleeffect, String color){
        this.result = result;
        this.ingredients = ingredients;
        this.color = getColorFromString(color);
        this.particleeffect = particleeffect;
    }

    ItemStack before = new ItemStack(Material.AIR, 0);
    ItemStack min = new ItemStack(Material.AIR, 0);

    public ResultAfterCraft isCraftableWith(List<ItemStack> itemStacks){
        HashMap<ItemStack,ItemStack> hashMap = new HashMap<>();
        if(itemStacks == null || itemStacks.isEmpty()){
            before = new ItemStack(Material.AIR, 0);
            min = new ItemStack(Material.AIR, 0);
            return new ResultAfterCraft(false, itemStacks, 1);
        }
        List<ItemStack> newitemStacks = new ArrayList<>(itemStacks);
        for(ItemStack itemincauldron : itemStacks){

            for(ItemStack ingr : ingredients){
                if(Cauldron.isSimilarWithoutAmount(ingr, itemincauldron)) {
                    if (itemincauldron.getAmount() >= ingr.getAmount()) {
                        hashMap.put(itemincauldron, ingr);
                    }
                }
            }
        }

        int rerere = 1;

        if(hashMap.size() == ingredients.size()){

            boolean bool = true;
            while(bool){
                for(ItemStack itemincoud : hashMap.keySet()){
                    ItemStack itemincoudclone = itemincoud.clone();
                    if(itemincoudclone.getAmount() - (hashMap.get(itemincoud).getAmount() * rerere) < 0){
                        bool = false;
                        rerere--;
                        break;
                    }
                }
                if(!bool){
                    break;
                }
                rerere++;
            }

            int finalRerere = rerere;
            hashMap.forEach((itemincoud, ingr)->{
                if(itemincoud.getAmount() == (ingr.getAmount() * finalRerere)){
                    newitemStacks.remove(itemincoud);
                }else{
                    newitemStacks.remove(itemincoud);
                    ItemStack thing = itemincoud.clone();
                    thing.setAmount(itemincoud.getAmount() - (ingr.getAmount() * finalRerere));
                    newitemStacks.add(thing.clone());
                }
            });
            return new ResultAfterCraft(true, newitemStacks, rerere);
        }else{
            return new ResultAfterCraft(false, newitemStacks, rerere);
        }
    }

    public static Color getColorFromString(String color){
        HashMap<String, Color> colorHashMap = new HashMap<>();
        colorHashMap.put("red", Color.RED);
        colorHashMap.put("blue", Color.BLUE);
        colorHashMap.put("green", Color.GREEN);
        colorHashMap.put("purple", Color.PURPLE);
        colorHashMap.put("aqua", Color.AQUA);
        colorHashMap.put("fuchsia", Color.FUCHSIA);
        colorHashMap.put("black", Color.BLACK);
        colorHashMap.put("white", Color.WHITE);
        colorHashMap.put("gray", Color.GRAY);
        colorHashMap.put("lime", Color.LIME);
        colorHashMap.put("maroon", Color.MAROON);
        colorHashMap.put("navy", Color.NAVY);
        colorHashMap.put("olive", Color.OLIVE);
        colorHashMap.put("yellow", Color.YELLOW);
        colorHashMap.put("orange", Color.ORANGE);
        colorHashMap.put("silver", Color.SILVER);
        colorHashMap.put("teal", Color.TEAL);

        if(colorHashMap.containsKey(color.toLowerCase())){
            return colorHashMap.get(color.toLowerCase());
        }else if(color.startsWith("#")){
            return hex2Rgb(color);
        }else{
            return Color.WHITE;
        }
    }

    public static Color hex2Rgb(String colorStr) {
        return Color.fromRGB(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    public String getParticleeffect() {
        return particleeffect;
    }

    public Color getColor() {
        return color;
    }
}
