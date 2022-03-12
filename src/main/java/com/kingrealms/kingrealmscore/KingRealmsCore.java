package com.kingrealms.kingrealmscore;

import com.starmediadev.plugins.starquests.QuestManager;
import com.starmediadev.plugins.starquests.objects.Quest;
import com.starmediadev.plugins.starquests.objects.QuestLine;
import com.starmediadev.plugins.starquests.objects.QuestObjective;
import com.starmediadev.plugins.starquests.objects.actions.BlockBreakAction;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

import static org.bukkit.Material.*;

public class KingRealmsCore extends JavaPlugin {
    
    @Override
    public void onEnable() {
        RegisteredServiceProvider<QuestManager> questProvider = Bukkit.getServicesManager().getRegistration(QuestManager.class);
        if (questProvider == null) {
            getLogger().severe("Could not load the QuestManager from StarQuests, disabling the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        QuestManager questManager = questProvider.getProvider();
    
        QuestLine questLine = new QuestLine("Test Quest Line");
        questLine.setDescription("The testing questline to test the quest plugin.");
        questManager.register(questLine);
    
        Quest gatherWood = new Quest("Gather Wood");
        gatherWood.setDescription("Punch some trees to get wood logs");
        questManager.register(gatherWood);
        questLine.addQuest(gatherWood);
        
        QuestObjective getLogs = new QuestObjective("Chop 5 Wood Logs", gatherWood, new BlockBreakAction(Arrays.asList(ACACIA_LOG, BIRCH_LOG, OAK_LOG, JUNGLE_LOG, SPRUCE_LOG, DARK_OAK_LOG), 5));
        questManager.register(getLogs);
        
        Quest gatherStone = new Quest("Gather Stone");
        gatherStone.setDescription("Craft a pickaxe and mine some stone.");
        questManager.register(gatherStone);
        questLine.addQuest(gatherStone);
        
        QuestObjective getStone = new QuestObjective("Mine 5 stone", gatherStone, new BlockBreakAction(Arrays.asList(STONE, COBBLESTONE), 10));
        questManager.register(getStone);
    }
}