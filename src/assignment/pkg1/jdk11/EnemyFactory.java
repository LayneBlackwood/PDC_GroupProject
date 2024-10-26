/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jackson and layne
 */
public class EnemyFactory 
{
    private static boolean bossUnlocked = false;
    private static boolean bossDefeated = false;

    public static Enemy generateEnemy(Player player) 
    {
        int playerXP = player.getTotalXP();

        if (playerXP < 50) 
        {
            return new Goblin();
        } 
        else if (playerXP < 100) 
        {
            return new Orc();
        } 
        else if (playerXP < 150) 
        {
            return new SecurityGuard();
        } 
        else if (playerXP < 200) 
        {
            return new DarkKnight();
        } 
        else 
        {
            return getBoss();
        }
    }

    public static Enemy generateRandomEnemy(Player player) 
    {
        int playerXP = player.getTotalXP();
        List<Enemy> possibleEnemies = new ArrayList<>();
        possibleEnemies.add(new Goblin());

        if (playerXP >= 50) 
        {
            possibleEnemies.add(new Orc());
        }
        if (playerXP >= 100) 
        {
            possibleEnemies.add(new SecurityGuard());
        }
        if (playerXP >= 150) 
        {
            possibleEnemies.add(new DarkKnight());
        }
        if (bossUnlocked) { // Only add boss if it's unlocked
            possibleEnemies.add(new Boss());
        }

        return possibleEnemies.get(new Random().nextInt(possibleEnemies.size()));
    }

    // Boss unlock check
    public static void checkBossUnlock(Player player) 
    {
        if (!bossUnlocked && player.getTotalXP() >= 200) 
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
}


