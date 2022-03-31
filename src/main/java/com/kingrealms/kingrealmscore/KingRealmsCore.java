package com.kingrealms.kingrealmscore;

import com.kingrealms.kingrealmscore.gui.MainMenu;
import com.starmediadev.plugins.starmenu.manager.MenuManager;
import com.starmediadev.plugins.starquests.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class KingRealmsCore extends JavaPlugin {
    
    private QuestManager questManager;
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("quest")) {
            if (sender instanceof Player player) {
                new MainMenu(this, player);
            }
        }
        
        return true;
    }
    
    @Override
    public void onEnable() {
        RegisteredServiceProvider<QuestManager> questProvider = Bukkit.getServicesManager().getRegistration(QuestManager.class);
        if (questProvider == null) {
            getLogger().severe("Could not load the QuestManager from StarQuests, disabling the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        questManager = questProvider.getProvider();
        
        RegisteredServiceProvider<MenuManager> menuProvider = Bukkit.getServicesManager().getRegistration(MenuManager.class);
        if (menuProvider == null) {
            getLogger().severe("Could not load the MenuManager from StarMenu, disabling the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }
    
    public QuestManager getQuestManager() {
        return this.questManager;
    }
}