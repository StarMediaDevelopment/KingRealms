package com.kingrealms.kingrealmscore;

import com.kingrealms.kingrealmscore.gui.MainMenu;
import com.starmediadev.plugins.starmcutils.region.Cuboid;
import com.starmediadev.plugins.starmenu.manager.MenuManager;
import com.starmediadev.plugins.starquests.QuestManager;
import com.starmediadev.plugins.starquests.objects.*;
import com.starmediadev.plugins.starquests.objects.actions.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

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
        
        Quest bedEnter = new Quest("Enter Bed", questLine);
        questManager.register(bedEnter);
        questManager.register(new QuestObjective("Enter a Bed", bedEnter, new BedEnterAction()));
        
        Quest bedLeave = new Quest("Leave Bed", questLine, bedEnter);
        questManager.register(bedLeave);
        questManager.register(new QuestObjective("Leave a bed", bedLeave, new BedLeaveAction()));
        
        Quest blockBreak = new Quest("Block Break", questLine, bedLeave);
        questManager.register(blockBreak);
        questManager.register(new QuestObjective("Break dirt", blockBreak, new BlockBreakAction(Material.DIRT, 1)));
        
        Quest blockPlace = new Quest("Block Place", questLine, blockBreak);
        questManager.register(blockPlace);
        questManager.register(new QuestObjective("Place Dirt", blockPlace, new BlockPlaceAction(Material.DIRT, 1)));
        
        Quest bucketFill = new Quest("Bucket Fill", questLine, blockPlace);
        questManager.register(bucketFill);
        questManager.register(new QuestObjective("Fill a bucket with water", bucketFill, new BucketFillAction(Material.WATER, 1)));
        
        Quest bucketEmpty = new Quest("Bucket Empty", questLine, bucketFill);
        questManager.register(bucketEmpty);
        questManager.register(new QuestObjective("Empty the bucket", bucketEmpty, new BucketEmptyAction(Material.WATER_BUCKET, 1)));
        
        Quest entityBucketCapture = new Quest("Entity Bucket Capture", questLine, bucketEmpty);
        questManager.register(entityBucketCapture);
        questManager.register(new QuestObjective("Capture an Axolotl", entityBucketCapture, new BucketEntityCaptureAction(EntityType.AXOLOTL, 1)));
        
        Quest cuboidEnter = new Quest("Cuboid Enter", questLine, entityBucketCapture);
        questManager.register(cuboidEnter);
        World world = Bukkit.getWorlds().get(0);
        questManager.register(new QuestObjective("Enter a cuboid", cuboidEnter, new CuboidEnterAction(new Cuboid(new Location(world, 5, 105, 5), new Location(world, -5, 95, 5)))));
        
        Quest enchantUnbreaking = new Quest("Enchant Unbreaking", questLine, cuboidEnter);
        questManager.register(enchantUnbreaking);
        questManager.register(new QuestObjective("Enchant Unbreaking", enchantUnbreaking, new EnchantItemAction(Enchantment.DURABILITY)));
        
        Quest enchantDiamondChestplate = new Quest("Enchant Diamond Chestplate", questLine, enchantUnbreaking);
        questManager.register(enchantDiamondChestplate);
        questManager.register(new QuestObjective("Enchant Diamond Chestplate", enchantDiamondChestplate, new EnchantItemAction(Material.DIAMOND_CHESTPLATE)));
        
        Quest enchantDiamondChestPlateAndUnbreaking = new Quest("Enchant Diamond Chestplate and Unbreaking", questLine, enchantDiamondChestplate);
        questManager.register(enchantDiamondChestPlateAndUnbreaking);
        questManager.register(new QuestObjective("Enchant diamond chesplate and unbreaking", enchantDiamondChestPlateAndUnbreaking, new EnchantItemAction(Enchantment.DURABILITY, Material.DIAMOND_CHESTPLATE)));
        
        Quest breedCows = new Quest("Breed Cows", questLine, enchantDiamondChestPlateAndUnbreaking);
        questManager.register(breedCows);
        questManager.register(new QuestObjective("Breed Cows", breedCows, new EntityBreedAction(EntityType.COW, 1)));
         
        Quest entityInteract = new Quest("Entity interact", questLine, breedCows);
        questManager.register(entityInteract);
        questManager.register(new QuestObjective("Interact Item Frame", entityInteract, new EntityInteractAction(EntityType.ITEM_FRAME)));
        
        Quest entityKill = new Quest("Entity Kill", questLine, entityInteract);
        questManager.register(entityKill);
        questManager.register(new QuestObjective("Kill a cow", entityKill, new EntityKillAction(EntityType.COW, 1)));
        
        Quest shearSheep = new Quest("Shear Sheep", questLine, entityKill);
        questManager.register(shearSheep);
        questManager.register(new QuestObjective("Shear a Sheep", shearSheep, new EntityShearAction(EntityType.SHEEP, 1)));
        
        Quest entityTame = new Quest("Entity tame", questLine, shearSheep);
        questManager.register(entityTame);
        questManager.register(new QuestObjective("Tame a cat", entityTame, new EntityTameAction(EntityType.CAT, 1)));
        
        Quest eatFood = new Quest("Eat Food", questLine, entityTame);
        questManager.register(eatFood);
        questManager.register(new QuestObjective("Eat a steak", eatFood, new ItemConsumeAction(Material.COOKED_BEEF, 1)));
        
        Quest craftStick = new Quest("Craft stick", questLine, eatFood);
        questManager.register(craftStick);
        questManager.register(new QuestObjective("Craft a stick", craftStick, new ItemCraftAction(Material.STICK, 1)));
        
        Quest dropStick = new Quest("Drop stick", questLine, craftStick);
        questManager.register(dropStick);
        questManager.register(new QuestObjective("Drop a stick", dropStick, new ItemDropAction(Material.STICK, 1)));
        
        Quest pickupStick = new Quest("Pickup stick", questLine, dropStick);
        questManager.register(pickupStick);
        questManager.register(new QuestObjective("Pickup a stick", pickupStick, new ItemPickupAction(Material.STICK, 1)));
        
        Quest reachLocation = new Quest("Reach location", questLine, pickupStick);
        questManager.register(reachLocation);
        questManager.register(new QuestObjective("Reach a location", reachLocation, new LocationReachAction(new Location(world, 0, 80, 0))));
        
        Quest performCommand = new Quest("Perform Command", questLine, reachLocation);
        questManager.register(performCommand);
        questManager.register(new QuestObjective("Run the command /quest", performCommand, new PlayerCommandAction("/quest")));
    
        Quest potionBrew = new Quest("Brew a potion", questLine, performCommand);
        questManager.register(potionBrew);
        questManager.register(new QuestObjective("Brew an Awkward Potion and take it out", potionBrew, new PotionBrewAction(PotionType.AWKWARD, 1)));
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