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
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager 
{
    private static final String DB_URL = "jdbc:derby:game_database;create=true";
    private Connection connection;

    public void connect() throws SQLException 
    {
        connection = DriverManager.getConnection(DB_URL);
        connection.setAutoCommit(false); // Disable auto-commit for better transaction management
    }

    public void disconnect() throws SQLException 
    {
        if (connection != null && !connection.isClosed()) 
        {
            connection.close();
        }
    }

    public void createTables() throws SQLException 
    {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet tables;

        // Create Players table if it doesn't exist
        tables = dbMetaData.getTables(null, null, "PLAYERS", null);
        if (!tables.next()) 
        {
            try (Statement stmt = connection.createStatement()) 
            {
                String createPlayersTable = "CREATE TABLE Players (" +
                        "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "name VARCHAR(100) NOT NULL," +
                        "hp INTEGER NOT NULL," +
                        "xp INTEGER NOT NULL)";
                stmt.executeUpdate(createPlayersTable);
                connection.commit(); // Commit after table creation
                System.out.println("Players table created.");
            }
        }
        tables.close();

        // Create HighScores table if it doesn't exist
        tables = dbMetaData.getTables(null, null, "HIGHSCORES", null);
        if (!tables.next()) 
        {
            try (Statement stmt = connection.createStatement()) 
            {
                String createHighScoresTable = "CREATE TABLE HighScores (" +
                        "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "name VARCHAR(100) NOT NULL," +
                        "score INTEGER NOT NULL," +
                        "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                stmt.executeUpdate(createHighScoresTable);
                connection.commit();
                System.out.println("HighScores table created.");
            }
        }
        tables.close();
    }

    public void insertPlayer(String name, int hp, int xp) throws SQLException 
    {
        String query = "INSERT INTO Players (name, hp, xp) VALUES (?, ?, ?)";
        if (connection == null || connection.isClosed()) 
        {
            throw new SQLException("Database connection is not established.");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) 
        {
            pstmt.setString(1, name);
            pstmt.setInt(2, hp);
            pstmt.setInt(3, xp);
            pstmt.executeUpdate();
            connection.commit();  // Commit the transaction
            System.out.println("Player " + name + " inserted into Players table.");
        } 
        catch (SQLException e) 
        {
            connection.rollback(); // Rollback if there's an error
            e.printStackTrace();
        }
    }

    public void insertHighScore(String name, int score) throws SQLException {
        String query = "INSERT INTO HighScores (name, score) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            connection.commit(); // Commit after insertion
            System.out.println("High score for " + name + " inserted into HighScores table.");
        }
        catch (SQLException e) 
        {
            connection.rollback(); // Rollback if there's an error
            e.printStackTrace();
        }
    }
    
    public List<String> getPlayers() throws SQLException 
    {
        List<String> players = new ArrayList<>();
        String query = "SELECT name FROM Players";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) 
        {
            while (rs.next()) 
            {
                players.add(rs.getString("name"));
            }
        }
        return players;
    }

    public List<String> getHighScores() throws SQLException 
    {
        List<String> highScores = new ArrayList<>();
        String query = "SELECT name, score FROM HighScores ORDER BY score DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) 
        {
            while (rs.next()) 
            {
                highScores.add(rs.getString("name") + ": " + rs.getInt("score"));
            }
        }
        return highScores;
    }
}



//public class DatabaseManager 
//{
//    private static final String DB_URL = "jdbc:derby:game_database;create=true";
//    private Connection connection;
//    
//    public static void main(String[] args) {
//    DatabaseManager dbManager = new DatabaseManager();
//    try {
//        dbManager.connect();
//        dbManager.insertPlayer("TestPlayer", 100, 0);
//        dbManager.disconnect();
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//}
//
//
//    public void connect() throws SQLException 
//    {
//        connection = DriverManager.getConnection(DB_URL);
//        if (connection == null || connection.isClosed()) 
//        {
//            throw new SQLException("Database connection is closed.");
//        }
//
//    }
//
//    public void disconnect() throws SQLException 
//    {
//        if (connection != null && !connection.isClosed()) 
//        {
//            connection.close();
//        }
//    }
//
//    public void createTables() throws SQLException 
//    {
//        DatabaseMetaData dbMetaData = connection.getMetaData();
//        ResultSet tables;
//
//        // Check and create Players table
//        tables = dbMetaData.getTables(null, null, "PLAYERS", null);
//        if (!tables.next()) 
//        {
//            try (Statement stmt = connection.createStatement()) 
//            {
//                String createPlayersTable = "CREATE TABLE Players (" +
//                        "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
//                        "name VARCHAR(100) NOT NULL," +
//                        "hp INTEGER NOT NULL," +
//                        "xp INTEGER NOT NULL)";
//                stmt.executeUpdate(createPlayersTable);
//                System.out.println("Players table created.");
//            }
//        }
//        tables.close();
//
//        // Check and create HighScores table
//        tables = dbMetaData.getTables(null, null, "HIGHSCORES", null);
//        if (!tables.next()) 
//        {
//            try (Statement stmt = connection.createStatement()) 
//            {
//                String createHighScoresTable = "CREATE TABLE HighScores (" +
//                        "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
//                        "name VARCHAR(100) NOT NULL," +
//                        "score INTEGER NOT NULL," +
//                        "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
//                stmt.executeUpdate(createHighScoresTable);
//                System.out.println("HighScores table created.");
//            }
//        }
//        tables.close();
//    }
//
//    public void insertPlayer(String name, int hp, int xp) throws SQLException 
//    {
//        String query = "INSERT INTO Players (name, hp, xp) VALUES (?, ?, ?)";
//        if (connection == null || connection.isClosed()) 
//        {
//            throw new SQLException("Database connection is not established.");
//        }
//
//        try (PreparedStatement pstmt = connection.prepareStatement(query)) 
//        {
//            pstmt.setString(1, name);
//            pstmt.setInt(2, hp);
//            pstmt.setInt(3, xp);
//
//            int rowsInserted = pstmt.executeUpdate();
//            connection.commit();  // Ensure changes are saved
//            System.out.println("Player " + name + " inserted into Players table. Rows affected: " + rowsInserted);
//        } catch (SQLException e) 
//        {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public void insertHighScore(String name, int score) throws SQLException 
//    {
//        String query = "INSERT INTO HighScores (name, score) VALUES (?, ?)";
//        try (PreparedStatement pstmt = connection.prepareStatement(query)) 
//        {
//            pstmt.setString(1, name);
//            pstmt.setInt(2, score);
//            pstmt.executeUpdate();
//            System.out.println("High score for " + name + " inserted into HighScores table.");
//        }
//    }
//
//    public List<String> getPlayers() throws SQLException 
//    {
//        List<String> players = new ArrayList<>();
//        String query = "SELECT name FROM Players";
//        try (Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) 
//        {
//            while (rs.next()) 
//            {
//                players.add(rs.getString("name"));
//            }
//        }
//        return players;
//    }
//
//    public List<String> getHighScores() throws SQLException 
//    {
//        List<String> highScores = new ArrayList<>();
//        String query = "SELECT name, score FROM HighScores ORDER BY score DESC";
//        try (Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) 
//        {
//            while (rs.next()) 
//            {
//                highScores.add(rs.getString("name") + ": " + rs.getInt("score"));
//            }
//        }
//        return highScores;
//    }
//}

