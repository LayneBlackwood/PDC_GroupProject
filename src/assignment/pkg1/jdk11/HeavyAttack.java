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

//HeavyAttack represents a strong, high-damage attack with a chance to miss.
public class HeavyAttack implements AttackType 
{
    private final Random rand = new Random();

    @Override
    public void execute(Player player, Enemy enemy, JTextArea scenarioTextArea) 
    {
        if (rand.nextInt(3) < 2) // 2/3 chance to hit
        { 
            int damage = player.getHeavyAttackDamage(); // Calculate damage for heavy attack
            scenarioTextArea.append("You used a heavy attack! It dealt " + damage + " damage.\n");
            enemy.takeDamage(damage); // Inflict damage on the enemy
        } 
        else 
        {
            scenarioTextArea.append("The heavy attack missed!\n");
        }
    }
}

