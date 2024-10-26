/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author jackson and layne
 */

//FightEnemyAction coordinates the fight sequence between the player and the enemy, handling the flow of player attacks and enemy responses.
public class FightEnemyAction 
{
    private final Player player;
    private final Enemy enemy;
    private final JTextArea scenarioTextArea;

    public FightEnemyAction(Player player, Enemy enemy, JTextArea scenarioTextArea) 
    {
        this.player = player;
        this.enemy = enemy;
        this.scenarioTextArea = scenarioTextArea;
    }

   //Starts the fight sequence, alternating between player attacks and enemy responses.
    public void fightEnemy() 
    {
        while (player.isAlive() && enemy.getHp() > 0) 
        {
            displayHealthStatus(); // Display current health of both player and enemy

            // Get the player's chosen attack type and execute the attack
            AttackType attackType = selectAttackType(getPlayerChoice());
            attackType.execute(player, enemy, scenarioTextArea);

            // Check if the enemy has been defeated
            if (enemy.getHp() <= 0) 
            {
                scenarioTextArea.append("The " + enemy.getName() + " has been defeated!\n");
                player.gainEvilXP(enemy.getXP());
                return; // Exit the fight after defeating the enemy
            }

            // Enemy's turn to attack and player's turn to dodge
            enemyAttackSequence();
        }

        // Check if the player has been defeated
        if (!player.isAlive()) 
        {
            scenarioTextArea.append("Game Over! You have been defeated.\n");
        }
    }
    
    private int getPlayerChoice() 
    {
        // Display options for the player
        String options = "1. Light Attack\n2. Heavy Attack\n3. Special Attack";
        String input = JOptionPane.showInputDialog(null, "Choose your attack:\n" + options);

        // Parse and return the choice as an integer
        try 
        {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= 3) 
            {
                return choice; // Valid choice
            } 
            else 
            {
                scenarioTextArea.append("Invalid choice. Please choose again.\n");
                return getPlayerChoice(); // Recursively ask again
            }
        } 
        catch (NumberFormatException e) 
        {
            scenarioTextArea.append("Invalid input. Please enter a number.\n");
            return getPlayerChoice(); // Recursively ask again
        }
    }
    
    private int getDodgeChoice() 
    {
        // Display dodge options for the player
        String options = "1. Jump Back\n2. Side Step\n3. Block";
        String input = JOptionPane.showInputDialog(null, "Choose your dodge move:\n" + options);

        // Parse and return the choice as an integer
        try 
        {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= 3) {
                return choice; // Valid choice
            } 
            else 
            {
                scenarioTextArea.append("Invalid choice. Please choose again.\n");
                return getDodgeChoice(); // Recursively ask again
            }
        } 
        catch (NumberFormatException e) {
            scenarioTextArea.append("Invalid input. Please enter a number.\n");
            return getDodgeChoice(); // Recursively ask again
        }
    }

    private AttackType selectAttackType(int choice) 
    {
        switch (choice) 
        {
            case 1:
                return new LightAttack();
            case 2:
                return new HeavyAttack();
            case 3:
                return new SpecialAttack();
            default:
                throw new IllegalArgumentException("Invalid attack choice");
        }
    }
    
    //Executes the enemy's attack sequence, allowing the player to choose a dodge type.
    private void enemyAttackSequence() 
    {
        String enemyAttack = enemy.generateEnemyAttack();
        scenarioTextArea.append(enemyAttack + "\n"); // Display enemy's attack

        // Select the player's dodge type based on choice
        DodgeType dodgeType = selectDodgeType(getDodgeChoice());
        int damageTaken = dodgeType.dodge(player, enemy, enemyAttack, scenarioTextArea);

        // Apply damage to the player if the dodge was unsuccessful
        if (damageTaken > 0) 
        {
            scenarioTextArea.append("You take " + damageTaken + " damage!\n");
            player.loseHP(damageTaken);
        } 
        else 
        {
            scenarioTextArea.append("You dodged the attack!\n");
        }
    }

   private DodgeType selectDodgeType(int choice) 
   {
        switch (choice) 
        {
            case 1:
                return new JumpBack();
            case 2:
                return new SideStep();
            case 3:
                return new Block();
            default:
                throw new IllegalArgumentException("Invalid dodge choice");
        }
    }

    private void displayHealthStatus() 
    {
        scenarioTextArea.append("You attack the " + enemy.getName() + "!\n");
        scenarioTextArea.append("Player HP: " + player.getHp() + ", Enemy HP: " + enemy.getHp() + "\n");
    }
}