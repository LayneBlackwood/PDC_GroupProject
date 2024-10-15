/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

/**
 *
 * @author jackson and layne
 */

public class FightEnemyAction 
{
    Scanner scanner = new Scanner(System.in);
    Random rand = new Random();

    Player player;
    Enemy enemy;

    public FightEnemyAction(Player player, Enemy enemy) 
    {
        this.player = player; 
        this.enemy = enemy;
    }

    public void fightEnemy(Player player, Enemy enemy) 
    {
        while (player.getHp() > 0 && enemy.getHp() > 0) 
        {
            System.out.println("You attack the " + enemy.getName() + "!");
            System.out.println("How would you like to attack the " + enemy.getName() + "?");
            System.out.println("1. Light Attack.");
            System.out.println("2. Heavy Attack.");
            System.out.println("3. Special Attack.");
            int damage = 0;

            int playerAttackChoice = getPlayerChoice(3);

            switch (playerAttackChoice) 
            {
                case 1:
                    System.out.println("You used a light Attack!");
                    damage = player.getLightAttackDamage();
                    System.out.println("This hit the " + enemy.getName() + " and dealt " + damage);
                    enemy.takeDamage(damage);
                    break;

                case 2:
                    System.out.println("You used a heavy attack!");
                    
                    if (rand.nextInt(3) < 2) 
                    {
                        damage = player.getHeavyAttackDamage();
                        System.out.println("The attack hit the " + enemy.getName() + " and dealt " + damage);
                        enemy.takeDamage(damage);
                    } 
                    else 
                    {
                        System.out.println("The heavy attack missed!");
                    }
                    break;

                case 3:
                    System.out.println("You used a special attack!");
                    if (rand.nextBoolean()) 
                    {
                        System.out.println("The special attack hit the enemy!");
                        damage = player.getEquippedItem() != null ? player.getEquippedItem().getAttackBonus() * 2 : player.getMaxAttack();
                        enemy.takeDamage(damage);
                    } 
                    else 
                    {
                        System.out.println("The special attack missed!");
                    }
                    break;

                default:
                    System.out.println("Invalid input.");
                    break;
            }

            if (enemy.getHp() <= 0) 
            {
                System.out.println("The " + enemy.getName() + " has been defeated");
                player.gainEvilXP(enemy.getXP());
                return;
            }

            enemyAttackSequence();
        }
    }

    private void enemyAttackSequence() 
    {
        String enemyAttack = enemy.generateEnemyAttack();
        System.out.println(enemyAttack);

        System.out.println("How would you like to dodge this attack?");
        System.out.println("1. Jump back");
        System.out.println("2. Side step");
        System.out.println("3. Block attack");

        int dodgeChoice = dodgeChoiceTimer(3);
        int playerDamage = dodgeOutcome(dodgeChoice, enemyAttack);

        if (playerDamage > 0) 
        {
            System.out.println("You take " + playerDamage + " damage!");
            player.loseHP(playerDamage);
        } 
        else if (playerDamage == 0) 
        {
            System.out.println("You dodged the attack!");
        }

        if (!player.isAlive()) 
        {
            player.gameOver();
        }
    }

    private int dodgeOutcome(int dodgeChoice, String enemyAttack) 
    {
        int enemyDamage = enemy.getAttackDamage();
        int damageTaken = 0;

        switch (dodgeChoice) 
        {
            case 1:
                if (enemyAttack.contains("kick")) 
                {
                    System.out.println("You jump back and avoid the kick!");
                } 
                else if (enemyAttack.contains("punch")) 
                {
                    System.out.println("You jump back but the punch still connects! The punch has lost some of its strength.");
                    damageTaken = (enemyDamage + 2) / 3;
                } 
                else if (enemyAttack.contains("weapon")) 
                {
                    System.out.println("You jump back but the weapon still grazes you! You take reduced damage from the weapon attack");
                    damageTaken = (enemyDamage + 1) / 2;
                }
                break;

            case 2:
                if (enemyAttack.contains("kick")) 
                {
                    System.out.println("You side step but the kick still connects! You take reduced damage.");
                    damageTaken = (enemyDamage + 2) / 3;
                } 
                else if (enemyAttack.contains("punch")) 
                {
                    System.out.println("You side step but the punch still hits!");
                    damageTaken = enemyDamage;
                } 
                else if (enemyAttack.contains("weapon")) 
                {
                    System.out.println("You side step the overarm slash and fully dodge the attack!");
                }
                break;

            case 3:
                if (enemyAttack.contains("kick")) 
                {
                    System.out.println("You try and block the kick! The kick still hits but reduced damage is taken.");
                    damageTaken = (enemyDamage + 2) / 3;
                } 
                else if (enemyAttack.contains("punch")) 
                {
                    System.out.println("You block the punch and take no damage!");
                } 
                else if (enemyAttack.contains("weapon")) 
                {
                    System.out.println("You try to block the weapon attack! The weapon still hits and full damage is dealt.");
                    damageTaken = enemyDamage;
                }
                break;

            default:
                System.out.println("Invalid choice.");
                break;
        }

        return damageTaken;
    }

    
    //dodgeChoiceTimer
    
    private int dodgeChoiceTimer(int maxOption) 
    {
        final int[] dodgeChoice = {-1};

        Thread timerThread = new Thread(() -> 
        {
            System.out.println("You have 10 seconds to choose how you want to dodge!");

            for (int i = 10; i >= 0; i--) 
            {
                System.out.println(i);
                try 
                {
                    Thread.sleep(1000);  // Pause for 1 second
                } 
                catch (InterruptedException e) 
                {
                    return;  // Exit the thread if the player makes a choice
                }
            }

            if (dodgeChoice[0] == -1) 
            {
                System.out.println("Time's up! The enemy attack hits!");
                dodgeChoice[0] = maxOption; // Set to maxOption when time is up
                
                System.out.println("Please enter any character to continue: ");
            }
        });

        timerThread.start();

        while (dodgeChoice[0] == -1) 
        {
            try 
            {
                if (scanner.hasNextInt()) 
                {
                    int input = scanner.nextInt();
                    if (input >= 1 && input <= maxOption) 
                    {
                        dodgeChoice[0] = input;
                    } 
                    else 
                    {
                        System.out.println("Invalid choice. You are hit with the attack!");
                        dodgeChoice[0] = maxOption; // Invalid input results in being hit
                    }
                    timerThread.interrupt();  // Stop the timer
                } 
                else 
                {
                    System.out.println("Invalid input. You are hit with the attack!");
                    scanner.next(); // Clear the invalid input
                    dodgeChoice[0] = maxOption; // Invalid input results in being hit
                    timerThread.interrupt();  // Stop the timer
                }
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. You are hit with the attack!");
                scanner.next(); // Clear the invalid input
                dodgeChoice[0] = maxOption; // Invalid input results in being hit
                timerThread.interrupt();  // Stop the timer
            }
        }

        return dodgeChoice[0];
    }



    private int getPlayerChoice(int maxOption) 
    {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) 
        {
            try 
            {
                if (scanner.hasNextInt()) 
                {
                    choice = scanner.nextInt();
                    if (choice < 1 || choice > maxOption) 
                    {
                        System.out.println("Invalid choice. Please select a number between 1 and " + maxOption + ".");
                    } 
                    else 
                    {
                        validInput = true;
                    }
                } 
                else 
                {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return choice;
    }
}
