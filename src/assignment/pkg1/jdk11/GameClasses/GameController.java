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
//    
//    public void saveProgress() {
//        DatabaseManager.savePlayer(player);
//    }
//
//    public void saveHighScore() {
//        DatabaseManager.saveHighScore(player.getName(), player.getTotalXP());
//    }
//
//    public void showHighScores() {
//        DatabaseManager.displayHighScores();
//    }
}
