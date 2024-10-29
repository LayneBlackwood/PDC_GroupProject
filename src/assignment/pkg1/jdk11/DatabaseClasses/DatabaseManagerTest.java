/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.DatabaseClasses;

import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author jackson and layne
 */

public class DatabaseManagerTest 
{

    public static void main(String[] args) 
    {
        DatabaseManager dbManager = new DatabaseManager();

        try 
        {
            // Connect to the database
            dbManager.connect();
            System.out.println("Connected to database successfully!");

            // Create tables if they don't exist
            dbManager.createTables();

            // Insert a sample player
            System.out.println("Inserting sample player...");
            dbManager.insertPlayer("TestPlayer", 100, 0);

            // Insert a sample high score
            System.out.println("Inserting sample high score...");
            dbManager.insertHighScore("TestPlayer", 500);

            // Fetch and display all players (keep the connection open for retrieval)
            System.out.println("Retrieving players:");
            List<String> players = dbManager.getPlayers();
            for (String player : players) 
            {
                System.out.println("Player: " + player);
            }

            // Fetch and display all high scores
            System.out.println("Retrieving high scores:");
            List<String> highScores = dbManager.getHighScores();
            for (String highScore : highScores) 
            {
                System.out.println("High Score: " + highScore);
            }

        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            // Disconnect from the database after all operations are complete
            dbManager.disconnect();
            System.out.println("Disconnected from database successfully!");
        }
    }
}



