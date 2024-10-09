/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Random;
import java.util.Scanner;

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
    
    private int minAttack = 10;
    private int maxAttack = 20;
    //light attack damage
    private final int minLightAttackDamage = 5;
    private final int maxLightAttackDamage = 15;
    //heavy attack damage
    private final int minHeavyAttackDamage = 10;
    private final int maxHeavyAttackDamage = 25;
   
    private LootItem equippedItem;
    
    private int xpThreshold = 60;
    private final Random rand = new Random();
    private final Scanner scanner = new Scanner(System.in);
    
    
    
    

    public Player(String name) //setting initial values for players
    {
        this.name = name;
        this.hp = 100;
        this.goodXP = 0;
        this.evilXP = 0;
        this.neutralXP = 0;
        this.inventory = new Inventory();
        this.equippedItem = null;
        
        inventory.addItem("Healing potion" , 4);
    }
    
    public void checkLootChest() //function to run through the loot chest process
    {
        int totalXP = getTotalXP();
        if(totalXP >= xpThreshold)
        {
            System.out.println("Congratulations! You've leveled up and obtained a loot chest!");
            openLootChest();
            resetXP();
        }
    }
    
    public void resetXP() //function to up the loot box xp threshold for once a player has been awarded a loot box
    {
        xpThreshold += 80;
    }
    
    private void openLootChest() //function for opening loot chests
    {
        LootItem[] lootItems = 
        {
            //loot chest items and their attack values
            new LootItem("Sword of Might", 10),
            new LootItem("Axe of Fury", 15),
            new LootItem("Bow of Precision", 8),
            new LootItem("Elixir of Life", 0, 50)
        };
        
        LootItem selectedLoot = lootItems[rand.nextInt(lootItems.length)];
        
        if(selectedLoot.getHealingAmount() > 0)
        {
            //output if the item is a healing item
            System.out.println("You've received a " + selectedLoot.getName() + " which restores " + selectedLoot.getHealingAmount() + " HP!");
            inventory.addLootItem(selectedLoot);
            heal(selectedLoot.getHealingAmount());
            System.out.println("Your HP is now: " + getHp());
        }
        else
        {
            //output if the item is an attack based item
            System.out.println("You've received a " + selectedLoot.getName() + " with an attack bonus of " + selectedLoot.getAttackBonus() + "!");
            inventory.addLootItem(selectedLoot);
            handleLootBox(selectedLoot);
        }
    }
    
    public boolean isAlive() //function to check if player is alive
    {
        return hp > 0;
    }
    
    public void gainGoodXP(int xp)
    {
        goodXP += xp;
        checkLootChest();
    }
    
    public void gainEvilXP(int xp)
    {
        evilXP += xp;
        checkLootChest();
    }
    
    public void gainNeutralXP(int xp)
    {
        neutralXP += xp;
        checkLootChest();
    }
    
    public int getTotalXP()
    {
        int playerTotalXP = goodXP +  neutralXP + evilXP;
        return playerTotalXP;
    }
    
    public boolean useLootItem(String itemName)
    {
        LootItem lootItem = inventory.getLootItem(itemName);
        if(lootItem != null)
        {
            minAttack += lootItem.getAttackBonus();
            maxAttack += lootItem.getAttackBonus();
            System.out.println("You used " + lootItem.getName() + " which increased your attack!");
            return true;
        }
        else
        {
            System.out.println("You do not have a " + itemName + " in your inventory.");
            return false;
        }
    }
    
    public void loseHP(int damage)
    {
        hp -= damage;
        if(hp <= 0)
        {
            System.out.println("You have died");
            gameOver();
        }
    }
    
    public void heal(int healing)
    {
        hp += healing;
    }
    
    public int getLightAttackDamage()
    {
        Random randEnemyAttack = new Random();
        return randEnemyAttack.nextInt((maxLightAttackDamage - minLightAttackDamage) + 1) + minLightAttackDamage;
    }
    
    public int getHeavyAttackDamage()
    {
        Random randEnemyAttack = new Random();
        return randEnemyAttack.nextInt((maxHeavyAttackDamage - minHeavyAttackDamage) + 1) + minHeavyAttackDamage;       
    }
    
    public void attackIncrease(int increase)
    {
        maxAttack += increase;
        minAttack += increase;
    }
    
    public void gameOver()
    {
        System.out.println("Game over!");
        System.out.println("You have died!");
        
        
        String choice = scanner.nextLine();
        
        if(choice.equalsIgnoreCase("y"))
        {
            restartGame();
        }
        else
        {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
    }
    
    public void restartGame()
    {
        Game.main(null);
    }
    
    
    public void addItemToInventory(String itemName, int quantity)
    {
        inventory.addItem(itemName, quantity);
    }
    
    public boolean useHealingPotion()
    {
        if(inventory.useItem("Healing potion"))
        {
            heal(20);
            System.out.println("You used a healing potion! your HP is now: " + getHp());
            return true;
        }
        else
        {
            System.out.println("You have no healing potions left!");
            return false;
        }
    }
    
    public boolean giveAwayHealingPotion()
    {
        if(inventory.getItemQuantity("Healing potion") > 0)
        {
            inventory.removeItem("Healing potion");
            System.out.println("You have given away one healing potion.");
            return true;
        }
        else
        {
            System.out.println("You have no healing potions to give away!");
            return false;
        }
    }

    public LootItem getEquippedItem()
    {
        return equippedItem;
    }
    
    public void equipItem(LootItem newItem)
    {
        if(equippedItem != null)
        {
            System.out.println("Replacing " + equippedItem.getName() + " with " + newItem.getName());
        }
        else
        {
            System.out.println("Equipping " + newItem.getName());
        }
        equippedItem = newItem;
    }
    
    public void handleLootBox(LootItem newItem)
    {
        System.out.println("You've found a new loot item: " + newItem.getName() + " with attack bonus of " + newItem.getAttackBonus());
        
        
        System.out.println("Do you want to equip this item? (Y/N)");
        String choice = scanner.nextLine();
        
        if(choice.equalsIgnoreCase("Y"))
        {
            equipItem(newItem);
            int currentMaxAttack = getMaxAttack();
            System.out.println("You have equipped the " + newItem.getName() + ". Your current max attack damage is now " + currentMaxAttack);
        }
        else
        {
            System.out.println("You chose not to equip the " + newItem.getName());
        }
    }
    
    //getters and setters
    public int getHp() {return hp;}
    public int getGoodXP() { return goodXP;}
    public int getEvilXP() { return evilXP;}
    public int getNeutralXP() { return neutralXP;}
    public String getName() { return name;}
    public int getMaxAttack() {return maxAttack;}
    public int getMinAttack() {return minAttack;}
    
    public void printStatus()
    {
        System.out.println("Name: " + name);
        System.out.println("HP: " + hp);
        System.out.println("Good XP: " + goodXP);
        System.out.println("Evil XP: " + evilXP);
        System.out.println("Neutral XP: " + neutralXP);
    }
    
    public String determineKingType()
    {
        if(this.getGoodXP() > this.getEvilXP())
        {
            return "Good king!";
        }
        else if(this.getEvilXP() > this.getGoodXP())
        {
            return "Evil king!";
        }
        else
        {
            return "Neither";
        }
    }
}