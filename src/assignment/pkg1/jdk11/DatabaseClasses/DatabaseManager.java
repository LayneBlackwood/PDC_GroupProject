/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.DatabaseClasses;

/**
 *
 * @author jackson and layne
 */

import assignment.pkg1.jdk11.PlayerClasses.PlayerData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager 
{
    private static final String DB_URL = "jdbc:derby:C:/Users/layne/OneDrive - AUT University/Software design and construction/assignment 1 - group project/Assignment 1 - JDK 11/Assignment 1 - JDK11/Game_Database;create=true";
    private Connection connection = null;

    // Constructor to initialize the connection and create tables if necessary
    public DatabaseManager() 
    {
        try 
        {
            connect();
            createTables();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            disconnect();
        }
    }

    // Connect to the database
    public void connect() throws SQLException 
    {
        if (connection == null || connection.isClosed()) 
        {
            connection = DriverManager.getConnection(DB_URL);
        }
    }

    // Disconnect from the database
    public void disconnect() 
    {
        try 
        {
            if (connection != null && !connection.isClosed()) 
            {
                connection.setAutoCommit(true); // Reset auto-commit to true before closing
                connection.close();
            }
        } 
        catch (SQLException e) {}
    }

    // Create tables for Players and HighScores if they don't exist
    public void createTables() 
    {
        try (Statement stmt = connection.createStatement()) 
        {
            String createPlayersTable = "CREATE TABLE Players (" +
                    "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                    "name VARCHAR(100) NOT NULL," +
                    "hp INTEGER NOT NULL," +
                    "goodXP INTEGER NOT NULL," +
                    "evilXP INTEGER NOT NULL," +
                    "neutralXP INTEGER NOT NULL," +
                    "xp INTEGER NOT NULL)";
            stmt.executeUpdate(createPlayersTable);

            String createHighScoresTable = "CREATE TABLE HighScores (" +
                    "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                    "name VARCHAR(100) NOT NULL," +
                    "score INTEGER NOT NULL," +
                    "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(createHighScoresTable);

            System.out.println("Tables created successfully.");
        } 
        catch (SQLException e) 
        {
            if (e.getSQLState().equals("X0Y32")) 
            {
                System.out.println("Tables already exist.");
            } 
        }
    }

    // Insert a player into the Players table
    public void insertPlayer(String name, int hp, int goodXP, int evilXP, int neutralXP, int totalXP) 
    {
        String query = "INSERT INTO Players (name, hp, goodXP, evilXP, neutralXP, xp) VALUES (?, ?, ?, ?, ?, ?)";
        try 
        {
            connect();
            connection.setAutoCommit(false);
            try (PreparedStatement pstmt = connection.prepareStatement(query)) 
            {
                pstmt.setString(1, name);
                pstmt.setInt(2, hp);
                pstmt.setInt(3, goodXP);
                pstmt.setInt(4, evilXP);
                pstmt.setInt(5, neutralXP);
                pstmt.setInt(6, totalXP);
                pstmt.executeUpdate();
                connection.commit();
                System.out.println("Player " + name + " inserted into Players table.");
            }
        } 
        catch (SQLException e) 
        {
            try 
            {
                connection.rollback();
            } 
            catch (SQLException rollbackEx) {}
        } 
        finally 
        {
            disconnect();
        }
    }

    // Insert a high score into the HighScores table
    public void insertHighScore(String name, int score) 
    {
        String query = "INSERT INTO HighScores (name, score) VALUES (?, ?)";
        try 
        {
            connect();
            connection.setAutoCommit(false);
            try (PreparedStatement pstmt = connection.prepareStatement(query)) 
            {
                pstmt.setString(1, name);
                pstmt.setInt(2, score);
                pstmt.executeUpdate();
                connection.commit();
                System.out.println("High score for " + name + " inserted into HighScores table.");
            }
        } 
        catch (SQLException e) 
        {
            try 
            {
                connection.rollback();
            } 
            catch (SQLException rollbackEx) {}
        } 
        finally 
        {
            disconnect();
        }
    }

    // Fetch player data for a specific player from the Players table
    public PlayerData getPlayerData(String playerName) 
    {
        String query = "SELECT * FROM Players WHERE name = ?";
        try 
        {
            connect();
            try (PreparedStatement pstmt = connection.prepareStatement(query)) 
            {
                pstmt.setString(1, playerName);
                try (ResultSet rs = pstmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        int hp = rs.getInt("hp");
                        int goodXP = rs.getInt("goodXP");
                        int evilXP = rs.getInt("evilXP");
                        int neutralXP = rs.getInt("neutralXP");
                        return new PlayerData(playerName, hp, goodXP, evilXP, neutralXP);
                    }
                }
            }
        } 
        catch (SQLException e) {}
        finally 
        {
            disconnect();
        }
        return null; // No player found
    }

    // Update player data in the Players table
    public void updatePlayer(String name, int hp, int goodXP, int evilXP, int neutralXP, int totalXP) 
    {
        String query = "UPDATE Players SET hp = ?, goodXP = ?, evilXP = ?, neutralXP = ?, xp = ? WHERE name = ?";
        try 
        {
            connect();
            try (PreparedStatement pstmt = connection.prepareStatement(query)) 
            {
                pstmt.setInt(1, hp);
                pstmt.setInt(2, goodXP);
                pstmt.setInt(3, evilXP);
                pstmt.setInt(4, neutralXP);
                pstmt.setInt(5, totalXP);
                pstmt.setString(6, name);
                pstmt.executeUpdate();
                System.out.println("Player " + name + "'s data updated in Players table.");
            }
        } 
        catch (SQLException e) {}
        finally 
        {
            disconnect();
        }
    }

    // Delete player data from both Players and HighScores tables
    public void deletePlayerData(String playerName) 
    {
        String deleteFromPlayers = "DELETE FROM Players WHERE name = ?";
        String deleteFromHighScores = "DELETE FROM HighScores WHERE name = ?";
        try 
        {
            connect();
            // Delete from Players table
            try (PreparedStatement pstmt = connection.prepareStatement(deleteFromPlayers)) 
            {
                pstmt.setString(1, playerName);
                pstmt.executeUpdate();
                System.out.println("Player " + playerName + " deleted from Players table.");
            }

            // Delete from HighScores table
            try (PreparedStatement pstmt = connection.prepareStatement(deleteFromHighScores)) 
            {
                pstmt.setString(1, playerName);
                pstmt.executeUpdate();
                System.out.println("Player " + playerName + " deleted from HighScores table.");
            }
        } 
        catch (SQLException e) {}
        finally 
        {
            disconnect();
        }
    }

    // Fetch all high scores from the HighScores table
    public List<String> getHighScores() 
    {
        List<String> highScores = new ArrayList<>();
        String query = "SELECT name, score FROM HighScores ORDER BY score DESC";
        try 
        {
            connect();
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) 
            {
                while (rs.next()) 
                {
                    highScores.add(rs.getString("name") + ": " + rs.getInt("score"));
                }
            }
        } 
        catch (SQLException e) {}
        finally 
        {
            disconnect();
        }
        return highScores;
    }

    public Connection getConnection() 
    {
        return connection;
    }
}