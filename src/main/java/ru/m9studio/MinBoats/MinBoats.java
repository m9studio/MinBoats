package ru.m9studio.MinBoats;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MinBoats extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginListener listener = new PluginListener();
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
