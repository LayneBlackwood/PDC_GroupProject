/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.GameClasses;

import assignment.pkg1.jdk11.CombatClasses.FightEnemyAction;
import assignment.pkg1.jdk11.DatabaseClasses.DatabaseManager;
import assignment.pkg1.jdk11.DatabaseClasses.SummaryScreen;
import assignment.pkg1.jdk11.EnemyClasses.Enemy;
import assignment.pkg1.jdk11.EnemyClasses.EnemyFactory;
import assignment.pkg1.jdk11.PlayerClasses.Player;
import javax.swing.SwingWorker;

/**
 *
 * @author jackson and layne
 */

//Game actions are computed in this class

import javax.swing.*;
import java.sql.SQLException;
import java.util.Random;

public class GameController 
{
    private final GameGUI gameGUI;
    private Player player;
    private Enemy currentEnemy;
    private final DatabaseManager dbManager;

    public GameController(GameGUI gameGUI) 
    {
        this.gameGUI = gameGUI;
        this.dbManager = new DatabaseManager();
        
        try 
        {
            dbManager.connect();
            dbManager.createTables();
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
        savePlayerToDatabase();
    }
    
    //Method to get the player's name
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
                fight.startFight();
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
                catch (Exception ex) {}
            }
        };
        worker.execute();
    }

    private boolean attemptEscape() 
    {
        return new Random().nextInt(100) < 70;
    }

    private void endGame() 
    {
        gameGUI.disableGameOptions();
        gameGUI.showScoreboard(player.getName(), player.getTotalXP(), player.getHp());
        saveHighScoreToDatabase();
        showSummaryScreen();
    }

    private void savePlayerToDatabase() 
    {
        try 
        {
            dbManager.connect();
            dbManager.insertPlayer(player.getName(), player.getHp(), player.getTotalXP());
            dbManager.disconnect();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private void saveHighScoreToDatabase() 
    {
        try 
        {
            dbManager.connect();
            dbManager.insertHighScore(player.getName(), player.getTotalXP());
            dbManager.disconnect();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    private void showSummaryScreen() 
    {
        SwingUtilities.invokeLater(() -> new SummaryScreen(dbManager));
    }
}