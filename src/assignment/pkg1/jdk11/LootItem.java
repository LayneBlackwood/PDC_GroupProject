/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

/**
 *
 * @author jackson and layne
 */
public class LootItem 
{
    String name;
    private int attackBonus;
    private int healingAmount;
    
    //for attacking items
    public LootItem(String name, int attackBonus) //setting name and attack values of loot items 
    {
        this.name = name;
        this.attackBonus = attackBonus;
        this.healingAmount = 0;
    }
    
    //for healing items
    public LootItem(String name, int attackBonus, int healingAmount) //setting loot item values specifically for healing items
    {
        this.name = name;
        this.attackBonus = attackBonus;
        this.healingAmount = healingAmount;
    }
    
    public String getName()//return name of loot item
    {
        return name;
    }
    
    public int getAttackBonus() //return attack bonus of loot item
    {
        return attackBonus;
    }
    
    public int getHealingAmount() //return healing amount of loot item
    {
        return healingAmount;
    }
}
