/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;
import javax.swing.JTextArea;
import java.util.Random;

/**
 *
 * @author jackson and layne
 */

 //SpecialAttack represents a powerful attack that uses the player's equipped weapon, with a chance to miss.

public class SpecialAttack implements AttackType 
{
    private final Random rand = new Random();

    @Override
    public void execute(Player player, Enemy enemy, JTextArea scenarioTextArea) 
    {
        if (rand.nextBoolean()) // 50% chance to hit
        { 
            int damage = player.getEquippedItem() != null ? player.getEquippedItem().getAttackBonus() * 2 : player.getMaxAttack();
            scenarioTextArea.append("You used a special attack! It dealt " + damage + " damage.\n");
            enemy.takeDamage(damage); // Inflict damage on the enemy
        } 
        else 
        {
            scenarioTextArea.append("The special attack missed!\n");
        }
    }
}

