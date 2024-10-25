/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Random;
import javax.swing.*;
/**
 *
 * @author jackson and layne
 */


public class Player {
    private final String name;
    private int hp;
    private int goodXP;
    private int evilXP;
    private int neutralXP;
    private final Inventory inventory;

    private int minAttack = 10;
    private int maxAttack = 20;
    private final int minLightAttackDamage = 5;
    private final int maxLightAttackDamage = 15;
    private final int minHeavyAttackDamage = 10;
    private final int maxHeavyAttackDamage = 25;

    private LootItem equippedItem;
    private int xpThreshold = 60;
    private final Random rand = new Random();
    private GameGUI gui;

    public Player(String name, GameGUI gui) {
        this.name = name;
        this.hp = 100;
        this.goodXP = 0;
        this.evilXP = 0;
        this.neutralXP = 0;
        this.inventory = new Inventory();
        this.equippedItem = null;
        this.gui = gui;

        inventory.addItem("Healing potion", 4);
    }

    public void checkLootChest() {
        int totalXP = getTotalXP();
        if (totalXP >= xpThreshold) {
            gui.appendText("Congratulations! You've leveled up and obtained a loot chest!");
            openLootChest();
            resetXP();
        }
    }

    public void resetXP() {
        xpThreshold += 80;
    }

    private void openLootChest() {
        LootItem[] lootItems = {
            new LootItem("Sword of Might", 10),
            new LootItem("Axe of Fury", 15),
            new LootItem("Bow of Precision", 8),
            new LootItem("Elixir of Life", 0, 50)
        };

        LootItem selectedLoot = lootItems[rand.nextInt(lootItems.length)];

        if (selectedLoot.getHealingAmount() > 0) 
        {
            gui.appendText("You've received a " + selectedLoot.getName() + " which restores " + selectedLoot.getHealingAmount() + " HP!");
            inventory.addLootItem(selectedLoot);
            heal(selectedLoot.getHealingAmount());
            gui.appendText("Your HP is now: " + getHp());
        } 
        else 
        {
            gui.appendText("You've received a " + selectedLoot.getName() + " with an attack bonus of " + selectedLoot.getAttackBonus() + "!");
            inventory.addLootItem(selectedLoot);
            handleLootBox(selectedLoot); // Call the method to display the loot item
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void gainGoodXP(int xp) {
        goodXP += xp;
        checkLootChest();
    }

    public void gainEvilXP(int xp) {
        evilXP += xp;
        checkLootChest();
    }

    public void gainNeutralXP(int xp) {
        neutralXP += xp;
        checkLootChest();
    }

    public int getTotalXP() {
        return goodXP + neutralXP + evilXP;
    }

    public boolean useLootItem(String itemName) {
        LootItem lootItem = inventory.getLootItem(itemName);
        if (lootItem != null) {
            minAttack += lootItem.getAttackBonus();
            return true;
        }
        return false;
    }

    public void loseHP(int damage) {
        hp -= damage;
        if (hp <= 0) {
            gui.appendText("You have died.");
            //gui.showGameOverScreen(); // Ensure showGameOverScreen() is implemented in GameGUI
        }
    }

    public void heal(int healing) {
        hp += healing;
    }

    public int getLightAttackDamage() {
        return rand.nextInt((maxLightAttackDamage - minLightAttackDamage) + 1) + minLightAttackDamage;
    }

    public int getHeavyAttackDamage() {
        return rand.nextInt((maxHeavyAttackDamage - minHeavyAttackDamage) + 1) + minHeavyAttackDamage;
    }

    public void attackIncrease(int increase) {
        maxAttack += increase;
        minAttack += increase;
    }

    public void addItemToInventory(String itemName, int quantity) {
        inventory.addItem(itemName, quantity);
    }

    public boolean useHealingPotion() {
        if (inventory.useItem("Healing potion")) {
            heal(20);
            gui.appendText("You used a healing potion! Your HP is now: " + getHp());
            return true;
        } else {
            gui.appendText("You have no healing potions left!");
            return false;
        }
    }

    public boolean giveAwayHealingPotion() {
        if (inventory.getItemQuantity("Healing potion") > 0) {
            inventory.removeItem("Healing potion");
            gui.appendText("You have given away one healing potion.");
            return true;
        } else {
            gui.appendText("You have no healing potions to give away!");
            return false;
        }
    }

    public LootItem getEquippedItem() {
        return equippedItem;
    }

    public void equipItem(LootItem newItem) {
        if (equippedItem != null) {
            gui.appendText("Replacing " + equippedItem.getName() + " with " + newItem.getName());
        } else {
            gui.appendText("Equipping " + newItem.getName());
        }
        equippedItem = newItem;
    }

    public void handleLootBox(LootItem newItem) {
    // Display the loot item in the text area
    gui.appendText("You've found a new loot item: " + newItem.getName() + " with an attack bonus of " + newItem.getAttackBonus());
    
    // Optional: Implement a dialog to ask if the player wants to equip the item
    int response = JOptionPane.showConfirmDialog(null,
            "Would you like to equip the " + newItem.getName() + "?",
            "Equip Loot Item",
            JOptionPane.YES_NO_OPTION);
    
    if (response == JOptionPane.YES_OPTION) {
        equipItem(newItem); // Ensure this method is implemented in Player
        gui.appendText("You have equipped " + newItem.getName());
    } else {
        gui.appendText("You chose not to equip " + newItem.getName());
    }
}

    // Getters
    public int getHp() {
        return hp;
    }

    public int getGoodXP() {
        return goodXP;
    }

    public int getEvilXP() {
        return evilXP;
    }

    public int getNeutralXP() {
        return neutralXP;
    }

    public String getName() {
        return name;
    }

    public int getMaxAttack() {
        return maxAttack;
    }

    public int getMinAttack() {
        return minAttack;
    }
}
