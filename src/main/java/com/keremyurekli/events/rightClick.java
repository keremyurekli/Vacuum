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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.AIR;
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

    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if(!location.getWorld().getBlockAt(x, y, z).getType().equals(AIR)){
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }


    private void getClose(Player p, Entity e1, Material m){
        int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getProvidingPlugin(rightClick.class), new Runnable() {
            @Override
            public void run() {
                double x = p.getLocation().getX();
                double y = p.getLocation().getY()+0.3;
                double z = p.getLocation().getZ();

                Location temploc = new Location(p.getWorld(),x,y,z);

                Vector unitVector = e1.getLocation().toVector().subtract(temploc.toVector()).normalize();
                e1.setVelocity(unitVector.multiply(-2));


                for(Entity temp : p.getNearbyEntities(1,1,1)){
                    if(temp.equals(e1)){
                        Item i = p.getWorld().dropItem(p.getLocation(), new ItemStack(m, 1));
                        i.setPickupDelay(0);

                        e1.remove();
                    }
                }

            }
        }, 0, 1);



    }




    @EventHandler(priority= EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player p = event.getPlayer();
        World w = p.getWorld();

        if(p.getInventory().getItemInMainHand().getType().equals(Material.HOPPER) && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§a§l" + "VACUUM")) {
            int distance = 40;


            RayTraceResult result = p.rayTraceBlocks(distance, FluidCollisionMode.ALWAYS);
            if(result == null){
                throw null;
            }



            Block b = null;
            if (result.getHitBlock() != null) {
                b = result.getHitBlock();
            }else{
                return;
            }
            Location loc = b.getLocation();


            List<Block> end = getNearbyBlocks(loc,1);
            end.add(b);

            for(Block blocks : end){
                Material m = blocks.getType();
                for (Material mat : blacklist) {
                    if (m.equals(mat)) {
                        return;
                    }
                }

                FallingBlock fallingBlock = w.spawnFallingBlock(blocks.getLocation(), blocks.getBlockData());

                fallingBlock.setDropItem(false);
                fallingBlock.setInvulnerable(true);


                fallingBlock.setGlowing(true);
                getClose(p,fallingBlock,m);
                blocks.setType(AIR);
                p.playSound(p, Sound.BLOCK_BASALT_STEP, 1, 0);
            }


            event.setCancelled(true);
        }
    }

}
