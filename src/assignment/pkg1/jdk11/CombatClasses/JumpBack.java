/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.CombatClasses;

import assignment.pkg1.jdk11.EnemyClasses.Enemy;
import assignment.pkg1.jdk11.PlayerClasses.Player;
import javax.swing.JTextArea;

/**
 *
 * @author jackson and layne
 */

//JumpBack is a dodge type where the player jumps back, effectively dodging some attacks.
public class JumpBack implements DodgeType 
{
    @Override
    public int dodge(Player player, Enemy enemy, String enemyAttack, JTextArea scenarioTextArea) 
    {
        int damage = enemy.getAttackDamage();

        if (enemyAttack.contains("kick")) 
        {
            scenarioTextArea.append("You jump back and avoid the kick!\n");
            return 0;
        } 
        else if (enemyAttack.contains("punch")) 
        {
            scenarioTextArea.append("You jump back but the punch still connects! You take reduced damage.\n");
            return (damage + 2) / 3;
        } 
        else 
        {
            scenarioTextArea.append("You jump back but the weapon still grazes you! You take reduced damage.\n");
            return (damage + 1) / 2;
        }
    }
}

