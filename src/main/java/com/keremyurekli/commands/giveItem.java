package com.keremyurekli.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class giveItem implements CommandExecutor {

    public static ItemStack itemStack(){
        ItemStack item = new ItemStack(Material.HOPPER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a§l" + "VACUUM");
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            ItemStack item = new ItemStack(Material.HOPPER, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a§l" + "VACUUM");
            item.setItemMeta(meta);
            p.getInventory().addItem(item);
            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 2);

        }else{

        }
        return true;
    }
}