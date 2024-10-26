/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.PlayerClasses;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jackson and layne
 */

public class Inventory 
{
    private Map<String, Integer> items;
    private Map<String, LootItem> lootItems;

    public Inventory() 
    {
        items = new HashMap<>(); // Initializing hashmaps
        lootItems = new HashMap<>();
    }
    
    // Command to add item to inventory
    public void addItem(String itemName, int quantity) 
    { 
        items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
    }
    
    // Command to use the item
    public boolean useItem(String itemName) 
    { 
        if (items.containsKey(itemName) && items.get(itemName) > 0) {
            items.put(itemName, items.get(itemName) - 1);
            return true;
        }
        return false;
    }
    
    // Command to add loot item
    public void addLootItem(LootItem lootItem) 
    { 
        lootItems.put(lootItem.getName(), lootItem);
    }
    
    // Command to return the loot item by name
    public LootItem getLootItem(String itemName) 
    { 
        return lootItems.get(itemName);
    }
    
    // Command to return the quantity of that item
    public int getItemQuantity(String itemName) 
    { 
        return items.getOrDefault(itemName, 0);
    }
    
    // Command to remove item from inventory
    public boolean removeItem(String itemName) 
    { 
        if (items.containsKey(itemName) && items.get(itemName) > 0) 
        {
            items.put(itemName, items.get(itemName) - 1);
            return true;
        }
        return false;
    }
}

