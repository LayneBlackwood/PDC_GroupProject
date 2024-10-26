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

//LightAttack represents a quick, low-damage attack that is guaranteed to hit.
public class LightAttack implements AttackType 
{
    @Override
    public void execute(Player player, Enemy enemy, JTextArea scenarioTextArea) 
    {
        int damage = player.getLightAttackDamage(); // Calculate damage for light attack
        scenarioTextArea.append("You used a light attack! It dealt " + damage + " damage.\n");
        enemy.takeDamage(damage); // Inflict damage on the enemy
    }
}

