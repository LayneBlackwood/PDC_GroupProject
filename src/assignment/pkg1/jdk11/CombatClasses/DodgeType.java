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

//DodgeType is an interface representing a specific type of dodge the player can use.
public interface DodgeType 
{
    int dodge(Player player, Enemy enemy, String enemyAttack, JTextArea scenarioTextArea);
}

