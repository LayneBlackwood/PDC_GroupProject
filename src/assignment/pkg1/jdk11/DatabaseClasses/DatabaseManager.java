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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager 
{
    private static final String DB_URL = "jdbc:derby:game_database;create=true";
    private Connection connection;

    // Connect to the database
    public void connect() throws SQLException 
    {
        connection = DriverManager.getConnection(DB_URL);
        System.out.println("Connected to the database.");
    }

    // Disconnect from the database
    public void disconnect() throws SQLException 
    {
        if (connection != null) 
        {
            connection.close();
            System.out.println("Disconnected from the database.");
        }
    }

    // Create the Players and HighScores tables if they do not exist
    public void createTables() throws SQLException 
    {
        DatabaseMetaData dbMetaData = connection.getMetaData();

        // Check and create Players table
        ResultSet tables = dbMetaData.getTables(null, null, "PLAYERS", null);
        if (!tables.next()) 
        {
            try (Statement stmt = connection.createStatement()) 
            {
                String createPlayersTable = "CREATE TABLE Players ("
                        + "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "name VARCHAR(100) NOT NULL,"
                        + "hp INTEGER NOT NULL,"
                        + "xp INTEGER NOT NULL)";
                stmt.executeUpdate(createPlayersTable);
                System.out.println("Players table created.");
            }
        } 
        else 
        {
            System.out.println("Players table already exists.");
        }

        // Check and create HighScores table
        tables = dbMetaData.getTables(null, null, "HIGHSCORES", null);
        if (!tables.next()) 
        {
            try (Statement stmt = connection.createStatement()) 
            {
                String createHighScoresTable = "CREATE TABLE HighScores ("
                        + "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                        + "name VARCHAR(100) NOT NULL,"
                        + "score INTEGER NOT NULL,"
                        + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                stmt.executeUpdate(createHighScoresTable);
                System.out.println("HighScores table created.");
            }
        } 
        else 
        {
            System.out.println("HighScores table already exists.");
        }
        tables.close();
    }

    // Insert a new player into the Players table
    public void addPlayer(String name, int hp, int xp) throws SQLException {
        String insertPlayerSQL = "INSERT INTO Players (name, hp, xp) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertPlayerSQL)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, hp);
            pstmt.setInt(3, xp);
            pstmt.executeUpdate();
            System.out.println("Player added: " + name);
        }
    }

    // Retrieve and display all players
    public void displayPlayers() throws SQLException 
    {
        String selectPlayersSQL = "SELECT * FROM Players";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectPlayersSQL)) 
        {

            System.out.println("Players Table:");
            while (rs.next()) 
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int hp = rs.getInt("hp");
                int xp = rs.getInt("xp");
                System.out.println("ID: " + id + ", Name: " + name + ", HP: " + hp + ", XP: " + xp);
            }
        }
    }

    // Insert a new high score into the HighScores table
    public void addHighScore(String name, int score) throws SQLException 
    {
        String insertHighScoreSQL = "INSERT INTO HighScores (name, score) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertHighScoreSQL)) 
        {
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
            System.out.println("High score added for: " + name);
        }
    }

    // Retrieve and display all high scores
    public void displayHighScores() throws SQLException 
    {
        String selectHighScoresSQL = "SELECT * FROM HighScores ORDER BY score DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectHighScoresSQL)) 
        {

            System.out.println("High Scores:");
            while (rs.next()) 
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int score = rs.getInt("score");
                String date = rs.getTimestamp("date").toString();
                System.out.println("ID: " + id + ", Name: " + name + ", Score: " + score + ", Date: " + date);
            }
        }
    }
    
    public List<String> getPlayers() throws SQLException 
    {
        List<String> players = new ArrayList<>();

        String query = "SELECT * FROM Players";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) 
        {
            while (rs.next()) 
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int hp = rs.getInt("hp");
                int xp = rs.getInt("xp");

                players.add("ID: " + id + ", Name: " + name + ", HP: " + hp + ", XP: " + xp);
            }
        }

        return players;
    }

}
