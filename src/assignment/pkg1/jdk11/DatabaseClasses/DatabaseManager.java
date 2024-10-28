/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.DatabaseClasses;

/**
 *
 * @author jackson and layne
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager 
{

    private Connection connection;
    private final String URL = "jdbc:derby:game_database;create=true";

    // Connect to the database
    public void connect() throws SQLException 
    {
        connection = DriverManager.getConnection(URL);
        System.out.println("Connected successfully!");
    }

    // Disconnect from the database
    public void disconnect() throws SQLException 
    {
        if (connection != null && !connection.isClosed()) 
        {
            connection.close();
            System.out.println("Disconnected successfully!");
        } 
        else 
        {
            System.out.println("Connection is already closed or was never opened.");
        }
    }

    // Create tables
    public void createTables() throws SQLException 
    {
        try (Statement stmt = connection.createStatement()) 
        {
            String createPlayersTable = "CREATE TABLE Players ("
                    + "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "name VARCHAR(100),"
                    + "hp INTEGER,"
                    + "xp INTEGER"
                    + ")";
            stmt.execute(createPlayersTable);

            String createHighScoresTable = "CREATE TABLE HighScores ("
                    + "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "name VARCHAR(100),"
                    + "score INTEGER,"
                    + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            stmt.execute(createHighScoresTable);

            System.out.println("Tables checked/created successfully.");
        }
    }
}

