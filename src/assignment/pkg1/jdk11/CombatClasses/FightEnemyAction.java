/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.CombatClasses;


import assignment.pkg1.jdk11.EnemyClasses.Enemy;
import assignment.pkg1.jdk11.GameClasses.GameGUI;
import assignment.pkg1.jdk11.PlayerClasses.Player;
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
    private final GameGUI gameGUI;

    public FightEnemyAction(Player player, Enemy enemy, JTextArea scenarioTextArea, GameGUI gameGUI) 
    {
        this.player = player;
        this.enemy = enemy;
        this.scenarioTextArea = scenarioTextArea;
        this.gameGUI = gameGUI;
    }

    // Starts the fight sequence, alternating between player attacks and enemy responses.
    public void startFight() 
    {
        displayHealthStatus(); // Display current health of both player and enemy
        playerTurn(); // Start with the player's turn
    }

    private void playerTurn() 
    {
        if (player.isAlive() && enemy.getHp() > 0) 
        {
            displayHealthStatus();

            // Get player's attack type and execute it
            AttackType attackType = selectAttackType(getPlayerChoice());
            if (attackType == null) {
                exitFight(); // If player chose to exit the fight
                return;
            }

            attackType.execute(player, enemy, scenarioTextArea);

            // Check if the enemy is defeated
            if (enemy.getHp() <= 0) 
            {
                scenarioTextArea.append("The " + enemy.getName() + " has been defeated!\n");
                player.gainEvilXP(enemy.getXP());
                return; // End the fight after defeating the enemy
            }

            // Proceed to enemy's turn
            enemyTurn();
        }
    }

    private void enemyTurn() 
    {
        if (!player.isAlive()) 
        {
            scenarioTextArea.append("Game Over! You have been defeated.\n");
            return;
        }

        // Enemy's attack
        String enemyAttack = enemy.generateEnemyAttack();
        scenarioTextArea.append(enemyAttack + "\n");

        // Choose dodge type
        DodgeType dodgeType = selectDodgeType(getDodgeChoice());
        int damageTaken = dodgeType.dodge(player, enemy, enemyAttack, scenarioTextArea);

        // Apply damage
        if (damageTaken > 0) 
        {
            scenarioTextArea.append("You take " + damageTaken + " damage!\n");
            player.loseHP(damageTaken);
        } 
        else 
        {
            scenarioTextArea.append("You dodged the attack!\n");
        }

        // Return to player's turn
        playerTurn();
    }

    private int getPlayerChoice() 
    {
        // Define options for the attack dialog
        Object[] options = {"Light Attack", "Heavy Attack", "Special Attack", "Exit Fight"};
        
        // Show the attack options dialog with the additional "Exit Fight" button
        int choice = JOptionPane.showOptionDialog(
            null, 
            "Choose your attack:", 
            "Attack Options", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            options, 
            options[0]
        );

        switch (choice) 
        {
            case 0:
                return 1; // Light Attack
            case 1:
                return 2; // Heavy Attack
            case 2:
                return 3; // Special Attack
            case 3:
                return -1; // Exit Fight
            default:
                scenarioTextArea.append("Invalid choice. Please choose again.\n");
                return getPlayerChoice();
        }
    }

    private int getDodgeChoice() 
    {
        // Define options for the dodge dialog
        Object[] options = {"Jump Back", "Side Step", "Block"};
        
        // Show the dodge options dialog
        int choice = JOptionPane.showOptionDialog(
            null, 
            "Choose your dodge move:", 
            "Dodge Options", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            options, 
            options[0]
        );

        if (choice >= 0 && choice <= 2) 
        {
            return choice + 1; // Valid choices 1, 2, 3 for dodge moves
        } 
        else 
        {
            scenarioTextArea.append("Invalid choice. Please choose again.\n");
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
            case -1:
                return null; // Exit Fight chosen
            default:
                throw new IllegalArgumentException("Invalid attack choice");
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
        scenarioTextArea.append("Player HP: " + player.getHp() + ", Enemy HP: " + enemy.getHp() + "\n");
    }

    // Exit fight method to handle exit logic
    private void exitFight() 
    {
        scenarioTextArea.append("You have exited the fight.\n");
        gameGUI.loadNextScenario(); // Load the next scenario or return to main screen as desired
    }
}