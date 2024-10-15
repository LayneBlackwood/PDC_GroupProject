/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

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
        items = new HashMap<>(); //initializing hashmaps
        lootItems = new HashMap<>();
    }
    public void addItem(String itemName, int quantity) //command to add item to inventory
    {
        items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
    }


    public boolean useItem(String itemName) //command to use the item
    {
        if(items.containsKey(itemName) && items.get(itemName) > 0)
        {
            items.put(itemName, items.get(itemName) - 1);
            return true;
        }
        return false;
    }
    
    public void addLootItem(LootItem lootItem) //command to add loot item
    {
        lootItems.put(lootItem.getName(), lootItem);
    }
    
    public LootItem getLootItem(String itemName) //command to return what the loot items name is
    {
        return lootItems.get(itemName);
    }
    
    public int getItemQuantity(String itemName) //command to return the quantity of that item
    {
        return items.getOrDefault(itemName, 0);
    }
    
    public boolean removeItem(String itemName) //command to remove item from inventory
    {
        if(items.containsKey(itemName) && items.get(itemName) > 0)
        {
            items.put(itemName, items.get(itemName) - 1);
            return true;
        }
        return false;
    }
}
