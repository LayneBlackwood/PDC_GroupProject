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

    // Constructor to initialize the connection
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
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
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
            else 
            {
                e.printStackTrace();
            }
        }
    }

    // Insert a player into the Players table
    public void insertPlayer(String name, int hp, int xp) 
    {
        String query = "INSERT INTO Players (name, hp, xp) VALUES (?, ?, ?)";
        try 
        {
            connect();
            connection.setAutoCommit(false);
            try (PreparedStatement pstmt = connection.prepareStatement(query)) 
            {
                pstmt.setString(1, name);
                pstmt.setInt(2, hp);
                pstmt.setInt(3, xp);
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
            catch (SQLException rollbackEx) 
            {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
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
            catch (SQLException rollbackEx) 
            {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } 
        finally 
        {
            disconnect();
        }
    }

    // Fetch all players from the Players table
    public List<String> getPlayers() 
    {
        List<String> players = new ArrayList<>();
        String query = "SELECT name, hp, xp FROM Players";
        try 
        {
            connect();
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) 
            {
                while (rs.next()) 
                {
                    String player = "Name: " + rs.getString("name") + ", HP: " + rs.getInt("hp") + ", XP: " + rs.getInt("xp");
                    players.add(player);
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            disconnect();
        }
        return players;
    }
    
    public List<String> getHighScores() 
    {
        List<String> highScores = new ArrayList<>();
        String query = "SELECT name, score FROM HighScores ORDER BY score DESC";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) 
        {
            connect();
            while (rs.next()) 
            {
                highScores.add(rs.getString("name") + ": " + rs.getInt("score"));
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
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

    public void updatePlayer(String name, int finalHp, int finalXp) 
    {
        String query = "UPDATE Players SET hp = ?, xp = ? WHERE name = ?";
        try 
        {
            connect();
            try (PreparedStatement pstmt = connection.prepareStatement(query)) 
            {
                pstmt.setInt(1, finalHp);
                pstmt.setInt(2, finalXp);
                pstmt.setString(3, name);
                pstmt.executeUpdate();
                System.out.println("Player " + name + "'s final data updated in Players table.");
            }
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
}