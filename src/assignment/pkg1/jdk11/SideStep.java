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

//SideStep is a dodge type where the player sidesteps to avoid attacks.
public class SideStep implements DodgeType 
{
    @Override
    public int dodge(Player player, Enemy enemy, String enemyAttack, JTextArea scenarioTextArea) 
    {
        int damage = enemy.getAttackDamage();

        if (enemyAttack.contains("kick")) 
        {
            scenarioTextArea.append("You side step but the kick still connects! You take reduced damage.\n");
            return (damage + 2) / 3;
        } 
        else if (enemyAttack.contains("punch")) 
        {
            scenarioTextArea.append("You side step but the punch still hits! You take full damage.\n");
            return damage;
        } 
        else 
        {
            scenarioTextArea.append("You side step the overarm slash and fully dodge the attack!\n");
            return 0;
        }
    }
}

