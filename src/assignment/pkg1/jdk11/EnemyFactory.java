/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

/**
 *
 * @author layne
 */
public class EnemyFactory 
{

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
        else 
        {
            return new DarkKnight();
        }
    }
}
