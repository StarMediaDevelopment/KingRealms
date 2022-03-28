package com.kingrealms.kingrealmscore.gui;

import com.kingrealms.kingrealmscore.KingRealmsCore;
import com.starmediadev.plugins.starmcutils.builder.ItemBuilder;
import com.starmediadev.plugins.starmenu.element.button.Button;
import com.starmediadev.plugins.starmenu.gui.Menu;
import com.starmediadev.plugins.starquests.QuestManager;
import com.starmediadev.plugins.starquests.objects.Quest;
import com.starmediadev.plugins.starquests.objects.QuestLine;
import com.starmediadev.plugins.starquests.objects.QuestObject;
import com.starmediadev.plugins.starquests.objects.QuestObjective;
import com.starmediadev.plugins.starquests.objects.rewards.QuestReward;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class MainMenu extends Menu {
    
    public MainMenu(KingRealmsCore plugin, Player player) {
        super(plugin, "main_menu", "Main Menu", 1);
    
        QuestManager questManager = plugin.getQuestManager();
    
        QuestLine questLine = null;
        for (QuestLine line : questManager.getQuestLineRegistry().getAllRegistered()) {
            if (line.isAvailable(player.getUniqueId())) {
                questLine = line;
                break;
            }
        }
        
        Quest quest = null;
        if (questLine != null) {
            for (Quest q : questLine.getQuests()) {
                if (q.isAvailable(player.getUniqueId())) {
                    quest = q;
                    break;
                }
            }
        }
        
        String currentQuestLineTitle = (questLine != null) ? questLine.getTitle() : "None";
        String currentQuestTitle = (quest != null) ? quest.getTitle() : "None";
        List<String> currentQuestLore = new LinkedList<>();
        if (quest != null) {
            currentQuestLore.add("&fDescription: " + quest.getDescription());
            
            if (!quest.getObjectives().isEmpty()) {
                if (quest.getObjectives().size() == 1) {
                    currentQuestLore.add("&fObjective");
                } else {
                    currentQuestLore.add("&fObjectives");
                }
    
                for (QuestObjective objective : quest.getObjectives()) {
                    currentQuestLore.add("&f - " + objective.getTitle());
                }
            } else {
                currentQuestLore.add("&4No objectives, this is a bug, please report.");
            }
            
            if (!quest.getRewards().isEmpty()) {
                if (quest.getRewards().size() == 1) {
                    currentQuestLore.add("&fReward");
                } else {
                    currentQuestLore.add("&fRewards");
                }
                for (QuestReward reward : quest.getRewards()) {
                    currentQuestLore.add("&f - " + reward.getTitle());
                }
            } else {
                currentQuestLore.add("&cNo rewards");
            }
        } else {
            currentQuestLore.add("&cNo current quest.");
        }
        
        List<String> currentQuestLineLore = new LinkedList<>();
        if (questLine != null) {
            currentQuestLineLore.add("&7&o" + questLine.getDescription());
            int totalRequired = 0, totalSide = 0, totalRequiredCompleted = 0, totalSideCompleted = 0; 
            for (Quest qlq : questLine.getQuests()) {
                totalRequired++;
                if (qlq.isComplete(player.getUniqueId())) {
                    totalRequiredCompleted++;
                }
            }
    
            for (QuestObject sideQuestObject : questLine.getSideQuestObjects()) {
                if (sideQuestObject instanceof Quest sideQuest) {
                    totalSide++;
                    if (sideQuest.isComplete(player.getUniqueId())) {
                        totalSideCompleted++;
                    }
                }
            }
            
            int totalQuests = totalRequired + totalSide;
            int totalCompleted = totalRequiredCompleted + totalSideCompleted;
    
            double totalCompletePercent = 0;
            if (totalQuests > 0) {
                totalCompletePercent = (totalCompleted / (totalQuests * 1.0));
            }
            double totalPrimaryCompletePercent = 0;
            if (totalRequired > 0) {
                totalPrimaryCompletePercent = (totalRequiredCompleted / (totalRequired * 1.0));
            }
            double totalSideCompletePercent = 0;
            if (totalSide > 0) {
                totalSideCompletePercent = (totalSideCompleted / (totalSide * 1.0));
            }
    
            currentQuestLineLore.add("&fTotal Completed: " + totalCompleted + "/" + totalQuests + " (" + DecimalFormat.getPercentInstance().format(totalCompletePercent) + ")");
            currentQuestLineLore.add("&fTotal Primary Completed: " + totalRequiredCompleted + "/" + totalRequired + " (" + DecimalFormat.getPercentInstance().format(totalPrimaryCompletePercent) + ")");
            currentQuestLineLore.add("&fTotal Side Completed: " + totalSideCompleted + "/" + totalSide + " (" + DecimalFormat.getPercentInstance().format(totalSideCompletePercent) + ")");
        } else {
            currentQuestLineLore.add("&cNo current questline.");
        }
        
        Sound click = Sound.UI_BUTTON_CLICK;
        Button currentLineQuests = new Button(ItemBuilder.start(Material.WRITABLE_BOOK).displayName("&fCurrent Questline Quests").build(), click);
        QuestLine finalQuestLine = questLine;
        currentLineQuests.setLeftClickAction((p, menu, type) -> {
            if (finalQuestLine != null) {
                new QuestsMenu(plugin, finalQuestLine, player);
            }
        });
        Button currentQuest = new Button(ItemBuilder.start(Material.CAMPFIRE).displayName("&fCurrent Quest: " + currentQuestTitle).lore(currentQuestLore).build(), click);
        Button currentQuestLine = new Button(ItemBuilder.start(Material.SOUL_CAMPFIRE).displayName("&fCurrent Questline: " + currentQuestLineTitle).lore(currentQuestLineLore).build(), click);
        Button allQuestLines = new Button(ItemBuilder.start(Material.CARTOGRAPHY_TABLE).displayName("&fAll Questlines").build(), click);
        allQuestLines.setLeftClickAction((p, menu, type) -> new QuestLinesMenu(plugin, player));
        Button playerInfo = new Button(ItemBuilder.start(Material.PLAYER_HEAD).displayName("&fPlayer Info").lore("&cNot implemented").skullOwner(player.getUniqueId()).build(), click);
        
        setElement(0, 0, currentLineQuests);
        setElement(0, 3, currentQuest);
        setElement(0, 4, playerInfo);
        setElement(0, 5, currentQuestLine);
        setElement(0, 8, allQuestLines);
        player.openInventory(getInventory());
    }
}
