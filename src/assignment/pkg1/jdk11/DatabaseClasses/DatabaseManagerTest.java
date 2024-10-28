/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.DatabaseClasses;

import java.sql.SQLException;


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
            dbManager.connect();
            dbManager.createTables();

            // Add test data
            dbManager.addPlayer("Alice", 100, 500);
            dbManager.addPlayer("Bob", 120, 300);
            dbManager.addHighScore("Alice", 1500);
            dbManager.addHighScore("Bob", 1200);

            // Display data
            dbManager.displayPlayers();
            dbManager.displayHighScores();

            dbManager.disconnect();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}

