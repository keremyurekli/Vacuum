package com.keremyurekli;

import com.keremyurekli.commands.giveItem;
import com.keremyurekli.events.rightClick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("item").setExecutor(new giveItem());
        getServer().getPluginManager().registerEvents(new rightClick(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
