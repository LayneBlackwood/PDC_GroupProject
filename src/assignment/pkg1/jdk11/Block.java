/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;
import javax.swing.JTextArea;

/**
 *
 * @author jackson and layne
 */

//Block is a dodge type where the player attempts to block incoming attacks.

public class Block implements DodgeType 
{
    @Override
    public int dodge(Player player, Enemy enemy, String enemyAttack, JTextArea scenarioTextArea) {
        int damage = enemy.getAttackDamage();

        if (enemyAttack.contains("kick")) 
        {
            scenarioTextArea.append("You try to block the kick! The kick still hits but reduced damage is taken.\n");
            return (damage + 2) / 3;
        } 
        else if (enemyAttack.contains("punch")) 
        {
            scenarioTextArea.append("You block the punch and take no damage!\n");
            return 0;
        } 
        else 
        {
            scenarioTextArea.append("You try to block the weapon attack! The weapon still hits and full damage is dealt.\n");
            return damage;
        }
    }
}

