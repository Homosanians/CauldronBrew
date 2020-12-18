package com.ruverq.spigot.cauldronbrew.CauldronThings;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ResultAfterCraft {

    List<ItemStack> itemsnow;
    Boolean bool;
    int scolko = 1;
    public ResultAfterCraft(Boolean bool, List<ItemStack> itemsnow, int scolko){
        this.bool = bool;
        this.itemsnow = itemsnow;
        this.scolko = scolko;
    }

    public List<ItemStack> getItemsnow() {
        return itemsnow;
    }

    public Boolean getBool() {
        return bool;
    }

    public int getScolko() {
        return scolko;
    }
}
