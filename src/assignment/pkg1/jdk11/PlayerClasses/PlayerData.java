/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.PlayerClasses;

/**
 *
 * @author jackson and layne
 */
public class PlayerData 
{
    private final String name;
    private final int hp;
    private final int goodXP;
    private final int evilXP;
    private final int neutralXP;

    public PlayerData(String name, int hp, int goodXP, int evilXP, int neutralXP) 
    {
        this.name = name;
        this.hp = hp;
        this.goodXP = goodXP;
        this.evilXP = evilXP;
        this.neutralXP = neutralXP;
    }

    public String getName() 
    {
        return name;
    }

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
}


