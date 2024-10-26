/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public abstract class Enemy 
{
    private final String name;
    protected int enemyHP;
    protected final int maxAttackPower;
    protected final int minAttackPower;
    protected final int playerXPGain;

    public Enemy(String name, int enemyHP, int playerXPGain, int maxAttackPower, int minAttackPower) 
    {
        this.name = name;
        this.enemyHP = enemyHP;
        this.playerXPGain = playerXPGain;
        this.maxAttackPower = maxAttackPower;
        this.minAttackPower = minAttackPower;
    }

    public void takeDamage(int damage) 
    {
        enemyHP -= damage;
    }

    public int getAttackDamage() 
    {
        Random rand = new Random();
        return rand.nextInt((maxAttackPower - minAttackPower) + 1) + minAttackPower;
    }

    public int getHp() 
    {
        return enemyHP;
    }

    public int getXP() 
    {
        return playerXPGain;
    }

    public String getName() 
    {
        return name;
    }

    public String generateEnemyAttack() 
    {
        String[] attacks = 
        {
            "The " + name + " throws a punch at you!",
            "The " + name + " attempts to land a front kick!",
            "The " + name + " uses their weapon in an overarm chop!"
        };
        return attacks[new Random().nextInt(attacks.length)];
    }
}

