/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.PlayerClasses;

import assignment.pkg1.jdk11.GameClasses.GameController;
import assignment.pkg1.jdk11.GameClasses.GameGUI;

/**
 *
 * @author jackson and layne
 */

public class Player 
{
    private final String name;
    private int hp;
    private int goodXP;
    private int evilXP;
    private int neutralXP;
    private final Inventory inventory;
    private final LootChest lootChest;
    private GameGUI gui;
    private LootItem equippedItem; // Holds the currently equipped item

    public Player(String name, GameGUI gui, GameController gameController) 
    {
        this.name = name;
        this.hp = 100;
        this.goodXP = 0;
        this.evilXP = 0;
        this.neutralXP = 0;
        this.inventory = new Inventory();
        this.gui = gui;

        // Initialize the LootChest, passing in inventory, gui, and gameController
        this.lootChest = new LootChest(inventory, gui, gameController);

        inventory.addItem("Healing potion", 4); // Initial inventory setup
    }

    // Notify LootChest whenever experience is gained and check for loot
    public void gainGoodXP(int xp) 
    {
        goodXP += xp;
        lootChest.checkForLootChest(getTotalXP());
    }

    public void gainEvilXP(int xp) 
    {
        evilXP += xp;
        lootChest.checkForLootChest(getTotalXP());
    }

    public void gainNeutralXP(int xp) 
    {
        neutralXP += xp;
        lootChest.checkForLootChest(getTotalXP());
    }

    public int getTotalXP() 
    {
        return goodXP + neutralXP + evilXP;
    }

    public void loseHP(int damage) 
    {
        hp -= damage;
        if (hp <= 0) {
            gui.appendText("You have died.");
        } else {
            gui.updatePlayerStats(hp, getTotalXP());
        }
    }

    public void heal(int healing) 
    {
        hp += healing;
        gui.updatePlayerStats(hp, getTotalXP());
    }

    public int getLightAttackDamage() 
    {
        return PlayerAttackFactory.createLightAttack();
    }

    public int getHeavyAttackDamage() 
    {
        return PlayerAttackFactory.createHeavyAttack();
    }

    public int getMaxAttack() 
    {
        return equippedItem != null ? equippedItem.getAttackBonus() : PlayerAttackFactory.createHeavyAttack();
    }

    public LootItem getEquippedItem() 
    {
        return equippedItem;
    }

    public boolean useHealingPotion() 
    {
        if (inventory.useItem("Healing potion")) 
        {
            heal(20);
            gui.appendText("You used a healing potion! Your HP is now: " + getHp());
            return true;
        } 
        else 
        {
            gui.appendText("You have no healing potions left!");
            return false;
        }
    }

    public boolean giveAwayHealingPotion() 
    {
        if (inventory.removeItem("Healing potion")) 
        {
            gui.appendText("You have given away one healing potion.");
            return true;
        } 
        else 
        {
            gui.appendText("You have no healing potions to give away!");
            return false;
        }
    }

    public void equipItem(LootItem newItem) 
    {
        equippedItem = newItem;
        gui.appendText("Equipped " + newItem.getName() + " with attack bonus of " + newItem.getAttackBonus());
    }

    // Check if the player is still alive based on HP
    public boolean isAlive() 
    {
        return hp > 0;
    }

    // Getters for necessary properties
    public int getHp() 
    {
        return hp;
    }

    public int getGoodXP() 
    {
        return goodXP;
    }

    public int getEvilXP() 
    {
        return evilXP;
    }

    public int getNeutralXP() 
    {
        return neutralXP;
    }

    public String getName() 
    {
        return name;
    }
}
