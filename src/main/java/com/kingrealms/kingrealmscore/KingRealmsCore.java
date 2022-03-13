package com.kingrealms.kingrealmscore;

import com.kingrealms.kingrealmscore.gui.MainMenu;
import com.starmediadev.plugins.starmcutils.util.Config;
import com.starmediadev.plugins.starmenu.manager.MenuManager;
import com.starmediadev.plugins.starquests.QuestManager;
import com.starmediadev.plugins.starquests.objects.Quest;
import com.starmediadev.plugins.starquests.objects.QuestLine;
import com.starmediadev.plugins.starquests.objects.QuestObject;
import com.starmediadev.plugins.starquests.objects.QuestObjective;
import com.starmediadev.plugins.starquests.objects.actions.BlockBreakAction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Material.*;

public class KingRealmsCore extends JavaPlugin {
    
    private Map<String, String> existingIds = new HashMap<>();
    
    private Config config;
    private QuestManager questManager;
    
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
        
        config = new Config(this, "quests.yml");
        config.setup();
        
        if (config.contains("quests")) {
            ConfigurationSection questsSection = config.getConfigurationSection("quests");
            for (String key : questsSection.getKeys(false)) {
                this.existingIds.put(key, questsSection.getString(key));
            }
        }
        
        QuestManager questManager = questProvider.getProvider();
    
        QuestLine questLine = new QuestLine("Test Quest Line");
        questLine.setDescription("The testing questline to test the quest plugin.");
        setExistingId(questLine);
        questManager.register(questLine);
    
        Quest gatherWood = new Quest("Gather Wood");
        gatherWood.setDescription("Punch some trees to get wood logs");
        setExistingId(gatherWood);
        questManager.register(gatherWood);
        questLine.addQuest(gatherWood);
        
        QuestObjective getLogs = new QuestObjective("Chop 5 Wood Logs", gatherWood, new BlockBreakAction(Arrays.asList(ACACIA_LOG, BIRCH_LOG, OAK_LOG, JUNGLE_LOG, SPRUCE_LOG, DARK_OAK_LOG), 5));
        setExistingId(getLogs);
        questManager.register(getLogs);
        
        Quest gatherStone = new Quest("Gather Stone");
        gatherStone.setDescription("Craft a pickaxe and mine some stone.");
        setExistingId(gatherStone);
        questManager.register(gatherStone);
        gatherStone.addPrerequisite(gatherWood);
        questLine.addQuest(gatherStone);
        
        QuestObjective getStone = new QuestObjective("Mine 5 stone", gatherStone, new BlockBreakAction(Arrays.asList(STONE, COBBLESTONE), 5));
        setExistingId(getStone);
        questManager.register(getStone);
        
        Quest gatherCoal = new Quest("Gather Coal");
        gatherCoal.setDescription("Get some coal for cooking and smelting.");
        setExistingId(gatherCoal);
        questManager.register(gatherCoal);
        gatherCoal.addPrerequisite(gatherStone);
        questLine.addQuest(gatherCoal);
        
        QuestObjective getCoal = new QuestObjective("Mine 5 coal ore", gatherCoal, new BlockBreakAction(COAL, 5));
        setExistingId(getCoal);
        questManager.register(getCoal);
        
        Quest gatherIron = new Quest("Gather Iron");
        gatherIron.setDescription("Get some iron ore.");
        setExistingId(gatherIron);
        questManager.register(gatherIron);
        gatherIron.addPrerequisite(gatherCoal);
        questLine.addQuest(gatherIron);
        
        QuestObjective getIron = new QuestObjective("Mine 5 iron ore", gatherIron, new BlockBreakAction(IRON_ORE, 5));
        setExistingId(getIron);
        questManager.register(getIron);
        
        this.existingIds.put(questLine.getName(), questLine.getId());
        this.existingIds.put(gatherWood.getName(), gatherWood.getId());
        this.existingIds.put(getLogs.getName(), getLogs.getId());
        this.existingIds.put(gatherStone.getName(), gatherStone.getId());
        this.existingIds.put(getStone.getName(), getStone.getId());
        this.existingIds.put(gatherCoal.getName(), gatherCoal.getId());
        this.existingIds.put(getCoal.getName(), getCoal.getId());
        this.existingIds.put(gatherIron.getName(), gatherIron.getId());
        this.existingIds.put(getIron.getName(), getIron.getId());
    }
    
    private void setExistingId(QuestObject questObject) {
        if (existingIds.containsKey(questObject.getName())) {
            questObject.setId(existingIds.get(questObject.getName()));
        }
    }
    
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
    public void onDisable() {
        this.existingIds.forEach((name, id) -> config.set("quests." + name, id));
        config.save();
    }
    
    public QuestManager getQuestManager() {
        return this.questManager;
    }
}