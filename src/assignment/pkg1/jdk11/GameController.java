/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Random;
import javax.swing.SwingWorker;

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

    public GameController(GameGUI gameGUI) 
    {
        this.gameGUI = gameGUI;
    }

    public void startGame(String playerName) 
    {
        this.player = new Player(playerName, gameGUI);
        gameGUI.initializeGameForPlayer();
    }
    
    // Method to get the player's name
    public String getPlayerName() 
    {
        return player != null ? player.getName() : "Unknown Player";
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
            FightEnemyAction fight = new FightEnemyAction(player, currentEnemy, gameGUI.getScenarioTextArea());
            fight.fightEnemy(); 
            return null;
        }

        @Override
        protected void done() 
        {
            try 
            {
                // Update the player's stats in the GUI after the fight
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
}
