/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignment.pkg1.jdk11;

//hi
//hello
//trying something else uno momento please
//???

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Random;


public class Game 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        int enemyBattleCount = 0;

        System.out.println("Welcome to the game!");
        System.out.print("Please enter your player's name: ");
        String playerName = scanner.nextLine();
        Player player = new Player(playerName);
        
        if(playerName.equalsIgnoreCase("Crawly"))
        {
            player = new Player(playerName);
            player.gainGoodXP(50);
            player.gainEvilXP(50);
            player.gainNeutralXP(50);
            System.out.println("Demo mode has been activated the boss has been unlocked.");
        }
        else
        {
            player = new Player(playerName);
        }

        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader("scenarios.txt"));
            String line;

            while (player.getHp() > 0) 
            {
                Enemy.checkBossUnlock(player); // Check if the boss fight should be unlocked

                String title = reader.readLine();
                
                if (title == null) 
                {
                    break;
                }

                System.out.println(formatScenario(title, 80));

                String option1 = reader.readLine();
                String option2 = reader.readLine();
                String option3 = reader.readLine();

                if (option1 == null || option2 == null || option3 == null) 
                {
                    System.out.println("Scenario options are incomplete");
                    break;
                }

                boolean inFight = true;

                while (inFight) 
                {
                    System.out.println("1. " + option1);
                    System.out.println("2. " + option2);
                    System.out.println("3. " + option3);
                    
                    if (Enemy.isBossUnlocked() && !Enemy.isBossDefeated()) 
                    {
                        System.out.println("4. Fight the Boss");
                    }

                    int maxOption = Enemy.isBossUnlocked() && !Enemy.isBossDefeated() ? 4 : 3;
                    int choice = getPlayerChoice(scanner, maxOption);
                    Enemy enemy = null;

                    switch (choice) 
                    {
                        case 1:
                            System.out.println("You chose to attack the worker!");

                            enemy = Enemy.generateEnemy(player);
                            fightEnemy(player, enemy);
                            player.gainEvilXP(10);
                            break;

                        case 2:
                            if (attemptEscape()) 
                            {
                                System.out.println("You successfully escaped!");
                                inFight = false;
                            } 
                            else 
                            {
                                System.out.println("You failed to escape and must fight!");
                                enemy = Enemy.generateEnemy(player);
                                fightEnemy(player, enemy);
                            }
                            player.gainNeutralXP(10);
                            break;

                        case 3:
                            if (player.giveAwayHealingPotion()) 
                            {
                                System.out.println("You chose to gift generously!");
                                player.gainGoodXP(10);
                                inFight = false;
                            }
                            break;

                        case 4:
                            if (Enemy.isBossUnlocked() && !Enemy.isBossDefeated()) 
                            {
                                System.out.println("You chose to fight the Boss!");
                                Enemy boss = Enemy.getBoss();
                                fightEnemy(player, boss);
                                Enemy.setBossDefeated(true);
                            }
                            break;

                        default:
                            System.out.println("Invalid choice");
                            break;
                    }

                    if (enemy != null && enemy.getHp() <= 0) 
                    {
                        inFight = false;
                        enemyBattleCount++;
                    }

                    if (player.getHp() < 0) 
                    {
                        player.gameOver();
                        break;
                    }
                }
            }

            if (enemyBattleCount >= 4 && Enemy.isBossUnlocked() && !Enemy.isBossDefeated()) 
            {
                System.out.println("Boss battle begins!");
                Enemy boss = Enemy.getBoss();
                fightEnemy(player, boss);
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error reading scenarios: " + e.getMessage());
        }

        if (player.getHp() > 0) 
        {
            Scanner scoreScan = new Scanner(System.in);
            System.out.println("Congratulations! You've finished the game!");
            System.out.println("Would you like to save your score? (Y/N)");

            String choiceIn = scoreScan.nextLine();

            if (choiceIn.equalsIgnoreCase("Y")) 
            {
                saveScore(player);
                System.out.println("Your score has been saved!");
            } 
            else 
            {
                System.out.println("Your score has not been saved");
            }

            System.out.println("You are a " + player.determineKingType());

            System.out.println("Would you like to see the scoreboard? (Y/N)");
            String choice = scoreScan.nextLine();
           
            if (choice.equalsIgnoreCase("Y")) 
            {
                displaySortedScoreboard();
            }
        }
    }

    private static boolean attemptEscape() 
    {
        Random rand = new Random();
        int escapeChance = rand.nextInt(100);
        return escapeChance < 70;
    }

    public static void fightEnemy(Player player, Enemy enemy) 
    {
        FightEnemyAction fight = new FightEnemyAction(player, enemy);
        fight.fightEnemy(player, enemy);
        afterFight(player);
    }

    private static void saveScore(Player player) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Scoreboard.txt", true))) 
        {
            String kingType = player.determineKingType();
            writer.write(player.getName() + ": " + player.getTotalXP() + " , King type: " + kingType + "\n");
            System.out.println("Score saved successfully!");
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred while trying to save: " + e.getMessage());
        }
    }

    public static void afterFight(Player player) 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to use a healing potion? (Y/N)");
        String choice = scanner.nextLine();

        while (true) 
        {
            if (choice.equalsIgnoreCase("Y")) 
            {
                player.useHealingPotion();
                break;
            } 
            else if (choice.equalsIgnoreCase("N")) 
            {
                System.out.println("You chose not to use a healing potion.");
                break;
            } 
            else 
            {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
                choice = scanner.nextLine().trim();
            }
        }
    }

    public static void displaySortedScoreboard() 
    {
        List<ScoreEntry> scoreboard = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("Scoreboard.txt"))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(":");

                if (parts.length >= 2) 
                {
                    String name = parts[0].trim();
                    String xpPart = parts[1].split(",")[0].trim();

                    int xp = 0;
                    try 
                    {
                        xp = Integer.parseInt(xpPart.split(" ")[0].trim());
                    } 
                    catch (NumberFormatException e) 
                    {
                        System.out.println("Error parsing XP: " + e.getMessage());
                        continue;
                    }

                    scoreboard.add(new ScoreEntry(name, xp));
                }
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error reading scoreboard: " + e.getMessage());
            return;
        }

        scoreboard.sort(Comparator.comparingInt(ScoreEntry::getXp).reversed());

        System.out.println("===== Scoreboard =====");
        for (ScoreEntry entry : scoreboard) 
        {
            System.out.println(entry.getName() + " - XP: " + entry.getXp());
        }
        System.out.println("====================");
    }

    public static String formatScenario(String scenario, int lineLength) 
    {
        StringBuilder formattedScenario = new StringBuilder();
        int currentLineLength = 0;

        for (String word : scenario.split(" ")) 
        {
            
            if (currentLineLength + word.length() + 1 > lineLength) 
            {
                formattedScenario.append("\n");
                currentLineLength = 0;
            }

            formattedScenario.append(word).append(" ");
            currentLineLength += word.length() + 1;
        }

        return formattedScenario.toString();
    }

    private static int getPlayerChoice(Scanner scanner, int maxOption) 
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


