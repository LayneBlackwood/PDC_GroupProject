/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Enemy 
{
    private final String name;
    private int enemyHP;
    private final int maxAttackPower;
    private final int minAttackPower;
    private final int playerXPGain;
    private static int nextEnemyIndex = 0;
    private static boolean bossUnlocked = false;
    private static boolean bossDefeated = false;
    private static final Enemy[] enemiesInOrder = {new Goblin(), new Orc(), new SecurityGuard(), new DarkKnight()};
 

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
        Random randEnemyAttack = new Random();
        return randEnemyAttack.nextInt((maxAttackPower - minAttackPower) + 1) + minAttackPower;
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

    public static Enemy generateEnemy(Player player) 
    {
        if (nextEnemyIndex < enemiesInOrder.length && player.getTotalXP() >= getRequiredXPForEnemy(nextEnemyIndex)) 
        {
            Enemy nextEnemy = enemiesInOrder[nextEnemyIndex];
            nextEnemyIndex++;
            return nextEnemy;
        }
        return Enemy.generateRandomEnemy(player);
    }

    public static Enemy generateRandomEnemy(Player player) 
    {
        int playerTotalXP = player.getTotalXP();
        Random randEnemy = new Random();

        List<Enemy> possibleEnemies = new ArrayList<>();
        possibleEnemies.add(new Goblin());

        if (playerTotalXP >= 15) 
        {
            possibleEnemies.add(new Orc());
        }
        if (playerTotalXP >= 75) 
        {
            possibleEnemies.add(new SecurityGuard());
        }
        if (playerTotalXP >= 100) 
        {
            possibleEnemies.add(new DarkKnight());
        }

        int enemyIndexSize = randEnemy.nextInt(possibleEnemies.size());
        return possibleEnemies.get(enemyIndexSize);
    }

    public static void checkBossUnlock(Player player) 
    {
        if (!bossUnlocked && player.getTotalXP() >= 150) 
        {
            System.out.println("You have unlocked the Boss fight!");
            bossUnlocked = true;
        }
    }

    public static boolean isBossUnlocked() 
    {
        return bossUnlocked;
    }

    public static boolean isBossDefeated() 
    {
        return bossDefeated;
    }

    public static void setBossDefeated(boolean defeated) 
    {
        bossDefeated = defeated;
    }

    public static Enemy getBoss() 
    {
        return new Boss();
    }

    public static int getRequiredXPForEnemy(int enemyIndex) 
    {
        switch (enemyIndex) 
        {
            case 0:
                return 0;  // Goblin is always available
           
            case 1:
                return 15; // Orc is available at 15 XP
            
            case 2:
                return 75; // Security Guard is available at 75 XP
            
            case 3:
                return 100; // Dark Knight is available at 100 XP
            
            default:
                return Integer.MAX_VALUE;
        }
    }

    public String generateEnemyAttack() 
    {
        Random randNum = new Random();
        int randEnemyAttack = randNum.nextInt(3);
        String attackType = null;
        String punch, kick, weapon;
        
        switch(randEnemyAttack)
        {
            case 0:
                attackType = "The " + this.name + " throws a punch at you!";
                break;
                
            case 1:
                attackType = "The " + this.name + " attempts to land a front kick";
                break;
                
            case 2:
                attackType = "The " + this.name + " uses their weapon in an overarm chop!";
                break;
        }
        return attackType;
    }
}

