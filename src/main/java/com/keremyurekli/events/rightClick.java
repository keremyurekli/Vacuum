package com.keremyurekli.events;

import com.keremyurekli.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static org.bukkit.Material.DIAMOND_BLOCK;

public class rightClick implements Listener {


    private static ArrayList<Material> blacklist = new ArrayList<Material>(){
        {
            add(Material.AIR);
            add(Material.BEDROCK);
            add(Material.OBSIDIAN);
            add(Material.BEDROCK);
            add(Material.CHEST);
            add(Material.TRAPPED_CHEST);
            add(Material.ENDER_CHEST);
            add(Material.FURNACE);
            add(Material.BLAST_FURNACE);
            add(Material.SMOKER);
            add(Material.HOPPER);
            add(Material.WATER);
            add(Material.LAVA);
        }

    };

    private void getClose(Player p, Entity e1, Entity e2, Material m){

        new BukkitRunnable() {
            @Override
            public void run() {

                for(Entity temp : p.getNearbyEntities(1,1,1)){
                    if(temp.equals(e1)){
                        p.getInventory().addItem(new ItemStack(m, 1));
                        p.playSound(p, Sound.BLOCK_BASALT_STEP, 1, 0);

                        e2.remove();
                        e1.remove();
                    }
                }

            }
        }.runTaskTimer(new Main(), 0, 1);


    }




    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player p = event.getPlayer();
        World w = p.getWorld();


        if(p.getInventory().getItemInMainHand().getType().equals(Material.HOPPER) && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§a§l" + "VACUUM")) {
            p.sendMessage("VACUUM");
            int distance = 30;

            RayTraceResult result = p.rayTraceBlocks(distance, FluidCollisionMode.ALWAYS);
            assert result != null;

            Block b = null;
            if (result.getHitBlock() != null) {
                b = result.getHitBlock();
            }else{
                return;
            }
            Location loc = b.getLocation();
            Material m = loc.getBlock().getType();


            for (Material mat : blacklist) {
                if (m.equals(mat)) {
                    return;
                }
            }


            int x = loc.getBlockX();
            int y = loc.getBlockY() - 1;
            int z = loc.getBlockZ();

            Location temp = new Location(w, x, y, z);

            ArmorStand armorStand = (ArmorStand) w.spawnEntity(temp, EntityType.ARMOR_STAND);
            armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
            armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);

            FallingBlock fallingBlock = w.spawnFallingBlock(loc, b.getBlockData());
            fallingBlock.setGravity(false);
            fallingBlock.setDropItem(false);
            fallingBlock.setInvulnerable(true);

            fallingBlock.setGlowing(true);
            armorStand.addPassenger(fallingBlock);

            Vector unitVector = armorStand.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
            armorStand.setVelocity(unitVector.multiply(-0.1));


            getClose(p,armorStand,fallingBlock,m);


            event.setCancelled(true);
        }
    }

}
