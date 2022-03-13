package com.kingrealms.kingrealmscore;

import com.kingrealms.kingrealmscore.gui.MainMenu;
import com.starmediadev.plugins.starmenu.manager.MenuManager;
import com.starmediadev.plugins.starquests.QuestManager;
import com.starmediadev.plugins.starquests.objects.Quest;
import com.starmediadev.plugins.starquests.objects.QuestLine;
import com.starmediadev.plugins.starquests.objects.QuestObjective;
import com.starmediadev.plugins.starquests.objects.actions.BlockBreakAction;
import com.starmediadev.plugins.starquests.objects.rewards.ItemReward;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

import static org.bukkit.Material.*;

public class KingRealmsCore extends JavaPlugin {
    
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
        
        QuestManager questManager = questProvider.getProvider();
    
        QuestLine questLine = new QuestLine("Test Quest Line");
        questLine.setDescription("The testing questline to test the quest plugin.");
        questManager.register(questLine);
    
        Quest gatherWood = new Quest("Gather Wood", questLine);
        gatherWood.setDescription("Punch some trees to get wood logs");
        questManager.register(gatherWood);
        
        questManager.register(new QuestObjective("Chop 5 Wood Logs", gatherWood, new BlockBreakAction(Arrays.asList(ACACIA_LOG, BIRCH_LOG, OAK_LOG, JUNGLE_LOG, SPRUCE_LOG, DARK_OAK_LOG), 5)));
        questManager.register(new ItemReward("1 x Crafting Table", gatherWood, new ItemStack(CRAFTING_TABLE)));
        
        Quest gatherStone = new Quest("Gather Stone", questLine, gatherWood);
        gatherStone.setDescription("Craft a pickaxe and mine some stone.");
        questManager.register(gatherStone);
        
        questManager.register(new QuestObjective("Mine 5 stone", gatherStone, new BlockBreakAction(Arrays.asList(STONE, COBBLESTONE), 5)));
        questManager.register(new ItemReward("1 x Furnace", gatherStone, new ItemStack(FURNACE)));
        
        Quest gatherCoal = new Quest("Gather Coal", questLine, gatherStone);
        gatherCoal.setDescription("Get some coal for cooking and smelting.");
        questManager.register(gatherCoal);
        
        questManager.register(new QuestObjective("Mine 5 coal ore", gatherCoal, new BlockBreakAction(COAL_ORE, 5)));
        questManager.register(new ItemReward("32 x Torches", gatherCoal, new ItemStack(TORCH, 32)));
        Quest gatherIron = new Quest("Gather Iron", questLine, gatherCoal);
        gatherIron.setDescription("Get some iron ore.");
        questManager.register(gatherIron);
        
        questManager.register(new QuestObjective("Mine 5 iron ore", gatherIron, new BlockBreakAction(IRON_ORE, 5)));
        questManager.register(new ItemReward("1 x Iron Sword", gatherIron, new ItemStack(IRON_SWORD)));
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
    
    public QuestManager getQuestManager() {
        return this.questManager;
    }
}