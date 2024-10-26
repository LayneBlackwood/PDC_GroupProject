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






public class FightEnemyAction 
{
    
    private Player player;
    private Enemy enemy;
    private JTextArea scenarioTextArea;

    public FightEnemyAction(Player player, Enemy enemy, JTextArea scenarioTextArea) {
        this.player = player;
        this.enemy = enemy;
        this.scenarioTextArea = scenarioTextArea;
    }

    public void fightEnemy() 
    {
        while (player.getHp() > 0 && enemy.getHp() > 0) 
        {
            scenarioTextArea.append("You attack the " + enemy.getName() + "!\n");
            scenarioTextArea.append("Player HP: " + player.getHp() + ", Enemy HP: " + enemy.getHp() + "\n");

            if (!player.isAlive()) 
            {
                scenarioTextArea.append("Game Over! You have been defeated.\n");
                return; // Exit the fight
            }

            int playerAttackChoice = getPlayerChoice();

            switch (playerAttackChoice) 
            {
                case 1:
                    attackEnemy("light");
                    break;
                case 2:
                    attackEnemy("heavy");
                    break;
                case 3:
                    attackEnemy("special");
                    break;
                default:
                    scenarioTextArea.append("Invalid input.\n");
                    break;
            }

            if (enemy.getHp() <= 0) 
            {
                scenarioTextArea.append("The " + enemy.getName() + " has been defeated!\n");
                player.gainEvilXP(enemy.getXP());
                return;
            }

            enemyAttackSequence();
        }

        // If the player has been defeated, output game over message
        if (!player.isAlive()) 
        {
            scenarioTextArea.append("Game Over! You have been defeated.\n");
        }
    }

    private void attackEnemy(String attackType) 
    {
        Random rand = new Random();
        int damage = 0;
        switch (attackType) 
        {
            case "light":
                damage = player.getLightAttackDamage();
                scenarioTextArea.append("You used a light attack! It dealt " + damage + " damage.\n");
                enemy.takeDamage(damage);
                break;
            case "heavy":
                if (rand.nextInt(3) < 2) 
                {
                    damage = player.getHeavyAttackDamage();
                    scenarioTextArea.append("You used a heavy attack! It dealt " + damage + " damage.\n");
                    enemy.takeDamage(damage);
                } 
                else 
                {
                    scenarioTextArea.append("The heavy attack missed!\n");
                }
                break;
            case "special":
                if (rand.nextBoolean()) 
                {
                    damage = player.getEquippedItem() != null ? player.getEquippedItem().getAttackBonus() * 2 : player.getMaxAttack();
                    scenarioTextArea.append("You used a special attack! It dealt " + damage + " damage.\n");
                    enemy.takeDamage(damage);
                } 
                else 
                {
                    scenarioTextArea.append("The special attack missed!\n");
                }
                break;
        }
    }

    private void enemyAttackSequence() 
    {
        SwingUtilities.invokeLater(() -> 
        { // Ensure UI update happens on the EDT
            String enemyAttack = enemy.generateEnemyAttack();
            scenarioTextArea.append(enemyAttack + "\n");

            int dodgeChoice = getDodgeChoice();
            int playerDamage = dodgeOutcome(dodgeChoice, enemyAttack);

            if (playerDamage > 0) 
            {
                scenarioTextArea.append("You take " + playerDamage + " damage!\n");
                player.loseHP(playerDamage);
            } 
            else 
            {
                scenarioTextArea.append("You dodged the attack!\n");
            }

            if (!player.isAlive()) 
            {
                scenarioTextArea.append("Game Over! You have been defeated.\n");
            }
        });
    }

    private int getPlayerChoice() 
    {
        String options = "1. Light Attack.\n2. Heavy Attack.\n3. Special Attack.";
        String input = JOptionPane.showInputDialog(null, "How would you like to attack the " + enemy.getName() + "?\n" + options);
        return parseChoice(input);
    }

    private int getDodgeChoice() 
    {
        String options = "1. Jump back\n2. Side step\n3. Block attack";
        String input = JOptionPane.showInputDialog(null, "How would you like to dodge this attack?\n" + options);
        return parseChoice(input);
    }

    private int parseChoice(String input) 
    {
        if (input != null) 
        {
            try 
            {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) 
                {
                    return choice;
                }
            } 
            catch (NumberFormatException e) 
            {
                // Handle number format exception if input is not a valid integer
            }
        }
        return -1; // Return an invalid choice
    }

    private int dodgeOutcome(int dodgeChoice, String enemyAttack) {
        int enemyDamage = enemy.getAttackDamage();
        int damageTaken = 0;

        switch (dodgeChoice) {
            case 1: // Jump back
                if (enemyAttack.contains("kick")) {
                    scenarioTextArea.append("You jump back and avoid the kick!\n");
                } else if (enemyAttack.contains("punch")) {
                    scenarioTextArea.append("You jump back but the punch still connects! You take reduced damage.\n");
                    damageTaken = (enemyDamage + 2) / 3;
                } else if (enemyAttack.contains("weapon")) {
                    scenarioTextArea.append("You jump back but the weapon still grazes you! You take reduced damage.\n");
                    damageTaken = (enemyDamage + 1) / 2;
                }
                break;

            case 2: // Side step
                if (enemyAttack.contains("kick")) {
                    scenarioTextArea.append("You side step but the kick still connects! You take reduced damage.\n");
                    damageTaken = (enemyDamage + 2) / 3;
                } else if (enemyAttack.contains("punch")) {
                    scenarioTextArea.append("You side step but the punch still hits! You take full damage.\n");
                    damageTaken = enemyDamage;
                } else if (enemyAttack.contains("weapon")) {
                    scenarioTextArea.append("You side step the overarm slash and fully dodge the attack!\n");
                }
                break;

            case 3: // Block attack
                if (enemyAttack.contains("kick")) {
                    scenarioTextArea.append("You try to block the kick! The kick still hits but reduced damage is taken.\n");
                    damageTaken = (enemyDamage + 2) / 3;
                } else if (enemyAttack.contains("punch")) {
                    scenarioTextArea.append("You block the punch and take no damage!\n");
                } else if (enemyAttack.contains("weapon")) {
                    scenarioTextArea.append("You try to block the weapon attack! The weapon still hits and full damage is dealt.\n");
                    damageTaken = enemyDamage;
                }
                break;

            default:
                scenarioTextArea.append("Invalid choice.\n");
                break;
        }

        return damageTaken;
    }
}
