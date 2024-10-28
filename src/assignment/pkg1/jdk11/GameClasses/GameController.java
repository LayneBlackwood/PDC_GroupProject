/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.GameClasses;

import assignment.pkg1.jdk11.CombatClasses.FightEnemyAction;
import assignment.pkg1.jdk11.DatabaseClasses.DatabaseManager;
import assignment.pkg1.jdk11.EnemyClasses.Enemy;
import assignment.pkg1.jdk11.EnemyClasses.EnemyFactory;
import assignment.pkg1.jdk11.PlayerClasses.Player;
import java.util.Random;
import javax.swing.SwingWorker;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author jackson and layne
 */

//Game actions are computed in this class

public class GameController 
{
    private final GameGUI gameGUI;
    private Player player;
    private Enemy currentEnemy;
    private DatabaseManager dbManager; 

    public GameController(GameGUI gameGUI) 
    {
        this.gameGUI = gameGUI;
        
        this.dbManager = new DatabaseManager();
        
        try 
        {
            dbManager.connect();
            dbManager.createTables(); // Call createTables on the instance
            dbManager.disconnect();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
    }

    public void startGame(String playerName) 
    {
        this.player = new Player(playerName, gameGUI, this);
        gameGUI.initializeGameForPlayer();
    }

    // Method to get the player's name
    public String getPlayerName() 
    {
        return player != null ? player.getName() : "Unknown Player";
    }

    public Player getPlayer() 
    {
        return player;
    }    
    
    public void handleOption1() 
    {
        if (player.getHp() <= 0) 
        {
            endGame();
        }
        currentEnemy = EnemyFactory.generateEnemy(player);
        gameGUI.appendText("\nYou chose to attack the " + currentEnemy.getName() + "!");
        fightEnemy();
        player.gainEvilXP(10);
        gameGUI.updatePlayerStats(player.getHp(), player.getTotalXP());
    }

    public void handleOption2() 
    {
        if (player.getHp() <= 0) 
        {
            endGame();
        }
        if (attemptEscape()) 
        {
            gameGUI.appendText("\nYou successfully escaped!");
        } 
        else 
        {
            gameGUI.appendText("\nYou failed to escape and must fight!");
            currentEnemy = EnemyFactory.generateEnemy(player);
            fightEnemy();
        }
        player.gainNeutralXP(10);
        gameGUI.updatePlayerStats(player.getHp(), player.getTotalXP());
    }

    public void handleOption3() 
    {
        if (player.getHp() <= 0) 
        {
            endGame();
        }
        if (player.giveAwayHealingPotion()) 
        {
            gameGUI.appendText("\nYou chose to gift generously!");
            player.gainGoodXP(10);
        } 
        else 
        {
            gameGUI.appendText("\nYou have no healing potions to give.");
        }
        gameGUI.updatePlayerStats(player.getHp(), player.getTotalXP());
    }

    private void fightEnemy() 
    {
        SwingWorker<Void, String> worker = new SwingWorker<>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {
                FightEnemyAction fight = new FightEnemyAction(player, currentEnemy, gameGUI.getScenarioTextArea(), gameGUI);
                fight.startFight(); // Start the fight with the updated event-driven approach
                return null;
            }

            @Override
            protected void done() 
            {
                try 
                {
                    gameGUI.updatePlayerStats(player.getHp(), player.getTotalXP());

                    if (player.getHp() <= 0) 
                    {
                        endGame();
                    } 
                    else if (currentEnemy.getHp() <= 0) 
                    {
                        gameGUI.appendText("\nEnemy defeated!");
                        gameGUI.loadNextScenario();
                    } 
                    else 
                    {
                        gameGUI.appendText("\nEnemy HP: " + currentEnemy.getHp());
                    }
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute(); // Start the background task
    }

    private boolean attemptEscape() 
    {
        return new Random().nextInt(100) < 70;
    }

    private void endGame() 
    {
        gameGUI.disableGameOptions();
        gameGUI.showScoreboard(player.getName(), player.getTotalXP(), player.getHp());
    }
    
    //Starts a new game session, adding a new player entry in the database.
    public void startGame(String playerName, int playerHp, int playerXp) 
    {
        try 
        {
            dbManager.connect();
            dbManager.addPlayer(playerName, playerHp, playerXp); // Adds player to the database
            System.out.println("Player added to the database.");
            dbManager.disconnect();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        // Continue with other game start logic as needed
    }
    
    //displays all players and high scores from the database.
    public void displayAllPlayersAndScores() 
    {
        try 
        {
            dbManager.connect();
            System.out.println("Players:");
            dbManager.displayPlayers(); // Displays all players
            System.out.println("\nHigh Scores:");
            dbManager.displayHighScores(); // Displays all high scores
            dbManager.disconnect();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    //Saves the final score of the player to the HighScores table
    public void saveScore(String playerName, int score) 
    {
        try 
        {
            dbManager.connect();
            dbManager.addHighScore(playerName, score); // Adds player's final score to HighScores
            System.out.println("Score saved in HighScores table.");
            dbManager.disconnect();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    //Ends the game, saves the player's final score, and displays all players and high scores.
    public void endGame(int finalScore) 
    {
        if (player != null) 
        {
            saveScore(player.getName(), finalScore); // Save the player’s final score
        }
        displayAllPlayersAndScores(); // Display leaderboard and player history
        // Additional game-ending logic if necessary
    }
    
    public void displayExistingPlayers() 
    {
        try 
        {
            dbManager.connect();
            List<String> players = dbManager.getPlayers();

            System.out.println("Existing Players:");
            for (String player : players) 
            {
                System.out.println(player);
            }

            dbManager.disconnect();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

}
