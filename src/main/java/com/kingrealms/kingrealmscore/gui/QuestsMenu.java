package com.kingrealms.kingrealmscore.gui;

import com.starmediadev.plugins.starmcutils.builder.ItemBuilder;
import com.starmediadev.plugins.starmenu.element.Element;
import com.starmediadev.plugins.starmenu.element.button.Button;
import com.starmediadev.plugins.starmenu.element.button.NextPageButton;
import com.starmediadev.plugins.starmenu.element.button.PreviousPageButton;
import com.starmediadev.plugins.starmenu.gui.Menu;
import com.starmediadev.plugins.starquests.objects.Quest;
import com.starmediadev.plugins.starquests.objects.QuestLine;
import com.starmediadev.plugins.starquests.objects.QuestObject;
import com.starmediadev.plugins.starquests.objects.QuestRequirement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public class QuestsMenu extends Menu {
    public QuestsMenu(JavaPlugin plugin, QuestLine questLine, Player player) {
        super(plugin, "questsmenu", "Quests for " + questLine.getTitle(), 3);
        setFillerSlots(Material.WHITE_STAINED_GLASS_PANE, 0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 19, 20, 21, 22, 23, 24, 25);
        
        setElement(0, 4, new Element(ItemBuilder.start(Material.DARK_OAK_SIGN).setDisplayName("&f").build()));
        setElement(2, 0, new PreviousPageButton(Material.TRIPWIRE_HOOK, "&c"));
        setElement(2, 8, new NextPageButton(Material.TRIPWIRE_HOOK, "&a"));
        
        LinkedList<Quest> questOrder = new LinkedList<>();
        for (Quest quest : questLine.getQuests()) {
            if (questOrder.size() == 0) {
                questOrder.add(quest);
            } else {
                boolean isPrerequisite = false;
                questOrderLoop:
                for (int i = 0; i < questOrder.size(); i++) {
                    Quest indexQuest = questOrder.get(i);
                    for (QuestObject prerequisiteObject : indexQuest.getPrerequisiteObjects()) {
                        if (prerequisiteObject instanceof Quest prequest) {
                            if (prequest.getId().equals(quest.getId())) {
                                questOrder.add(i, quest);
                                isPrerequisite = true;
                                break questOrderLoop;   
                            }
                        }
                    }
                }
                
                if (!isPrerequisite) {
                    questOrderLoop:
                    for (int i = 0; i < questOrder.size(); i++) {
                        Quest indexQuest = questOrder.get(i);
                        for (QuestObject prerequisiteObject : quest.getPrerequisiteObjects()) {
                            if (prerequisiteObject instanceof Quest prequest) {
                                if (indexQuest.getId().equals(prequest.getId())) {
                                    questOrder.add(i + 1, quest);
                                    break questOrderLoop;
                                }
                            }
                        }
                    }
                }
            }
        }
    
        for (Quest quest : questOrder) {
            ItemBuilder itemBuilder = new ItemBuilder();
            itemBuilder.setDisplayName("&fQuest Name: " + quest.getTitle());
            List<String> lore = new LinkedList<>();
            
            if (quest.isComplete(player.getUniqueId())) {
                itemBuilder.setMaterial(Material.OXIDIZED_COPPER);
                lore.add("&7&o" + quest.getDescription());
                lore.add("&2&lCOMPLETE");
            } else if (!quest.isComplete(player.getUniqueId()) && !quest.isAvailable(player.getUniqueId())) {
                itemBuilder.setMaterial(Material.COPPER_BLOCK);
                lore.add("&4&lLOCKED");
                lore.add("&fRequirements to Unlock");
                String baseComplete = "&fYou must complete the ";
                for (QuestObject prerequisiteObject : quest.getPrerequisiteObjects()) {
                    String line = baseComplete;
                    if (prerequisiteObject instanceof QuestLine) {
                        line += "quest line ";
                    } else {
                        line += "quest ";
                    }
        
                    line += prerequisiteObject.getTitle();
                    lore.add(line);
                }
    
                for (QuestRequirement requirement : quest.getRequirements()) {
                    lore.add("&fYou must " + requirement.getTitle());
                }
            } else if (quest.isAvailable(player.getUniqueId())) {
                itemBuilder.setMaterial(Material.TARGET);
                lore.add("&7&o" + quest.getDescription());
                lore.add("&6&lLeft Click &ffor more info!");
            }
            
            itemBuilder.setLore(lore);
            Button questButton = new Button(itemBuilder.build());
            questButton.setLeftClickAction((p, menu, type) -> new QuestInfoMenu(plugin, player, quest));
            addElement(questButton);
        }
        
        player.openInventory(getInventory());
    }
}
