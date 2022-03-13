package com.kingrealms.kingrealmscore.gui;

import com.starmediadev.plugins.starmcutils.builder.ItemBuilder;
import com.starmediadev.plugins.starmenu.element.Element;
import com.starmediadev.plugins.starmenu.gui.Menu;
import com.starmediadev.plugins.starquests.objects.Quest;
import com.starmediadev.plugins.starquests.objects.QuestObjective;
import com.starmediadev.plugins.starquests.objects.rewards.QuestReward;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public class QuestInfoMenu extends Menu {
    public QuestInfoMenu(JavaPlugin plugin, Player player, Quest quest) {
        super(plugin, "questinfo", "Info for " + quest.getTitle(), 3);
    
        setElement(0, 4, new Element(ItemBuilder.start(Material.CAMPFIRE).setDisplayName("&f").build()));
        setElement(1, 3, new Element(ItemBuilder.start(Material.RAW_GOLD).setDisplayName("&fQuest Name").withLore("&f" + quest.getTitle()).build()));
    
        String objectivesName = "&f" + ((quest.getObjectives().size() == 1) ? "Objective" : "Objectives");
        
        List<String> objectivesLore = new LinkedList<>();
        if (!quest.getObjectives().isEmpty()) {
            for (QuestObjective objective : quest.getObjectives()) {
                objectivesLore.add("&f - " + objective.getTitle());
            }
        } else {
            objectivesLore.add("&4No objectives, this is a bug, please report.");
        }
        
        setElement(1, 4, new Element(ItemBuilder.start(Material.COAL).setDisplayName("&f" + objectivesName).setLore(objectivesLore).build()));
        
        String rewardsName = "&f" + ((quest.getRewards().size() == 1) ? "Reward" : "Rewards");
    
        List<String> rewardsLore = new LinkedList<>();
        if (!quest.getRewards().isEmpty()) {
            for (QuestReward reward : quest.getRewards()) {
                rewardsLore.add("&f - " + reward.getTitle());
            }
        } else {
            rewardsLore.add("&cNo rewards");
        }    
        
        setElement(1, 5, new Element(ItemBuilder.start(Material.GOLD_INGOT).setDisplayName("&f" + rewardsName).setLore(rewardsLore).build()));
        setElement(2, 4, new Element(ItemBuilder.start(Material.SPRUCE_SIGN).setDisplayName("&fQuest Description").withLore("&f" + quest.getDescription()).build()));
        player.openInventory(getInventory());
    }
}
