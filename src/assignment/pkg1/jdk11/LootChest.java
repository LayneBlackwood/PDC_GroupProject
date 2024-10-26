/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author jackson and layne
 */

public class LootChest 
{
    private static final Random rand = new Random();
    private static final LootItem[] lootItems = 
    {
        new LootItem("Sword of Might", 10),
        new LootItem("Axe of Fury", 15),
        new LootItem("Bow of Precision", 8),
        new LootItem("Elixir of Life", 0, 50) // Healing item with no attack bonus
    };

    private int xpThreshold = 60;
    private final Inventory inventory;
    private final GameGUI gui;
    private final GameController gameController; // Reference to GameController

    public LootChest(Inventory inventory, GameGUI gui, GameController gameController) 
    {
        this.inventory = inventory;
        this.gui = gui;
        this.gameController = gameController;
    }

    // Checks if the player qualifies for a loot chest based on total XP
    public void checkForLootChest(int totalXP) 
    {
        if (totalXP >= xpThreshold) 
        {
            gui.appendText("Congratulations! You've leveled up and obtained a loot chest!");
            openLootChest();
            xpThreshold += 80; // Increase the threshold for the next loot chest
        }
    }

    // Opens the loot chest and adds a random loot item to the inventory
    private void openLootChest() 
    {
        LootItem selectedLoot = lootItems[rand.nextInt(lootItems.length)];
        inventory.addLootItem(selectedLoot);

        if (selectedLoot.getHealingAmount() > 0) 
        {
            gui.appendText("You've received " + selectedLoot.getName() + " which restores " + selectedLoot.getHealingAmount() + " HP!");
            gameController.getPlayer().heal(selectedLoot.getHealingAmount());
        }
        else 
        {
            gui.appendText("You've received " + selectedLoot.getName() + " with an attack bonus of " + selectedLoot.getAttackBonus() + "!");
            handleLootBox(selectedLoot);
        }
    }

    // Prompt the player to equip the item if it has an attack bonus
    private void handleLootBox(LootItem newItem) 
    {
        gui.appendText("You've found a new loot item: " + newItem.getName() + " with an attack bonus of " + newItem.getAttackBonus());
        int response = JOptionPane.showConfirmDialog(null, "Would you like to equip the " + newItem.getName() + "?", "Equip Loot Item", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) 
        {
            gameController.getPlayer().equipItem(newItem);
            gui.appendText("You have equipped " + newItem.getName());
        } 
        else 
        {
            gui.appendText("You chose not to equip " + newItem.getName());
        }
    }
}