/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.CombatClasses.DatabaseClasses;

/**
 *
 * @author layne
 */
import assignment.pkg1.jdk11.PlayerClasses.Player;
import java.sql.*;

import java.sql.*;

public class DatabaseManager 
{
    private static final String URL = "jdbc:derby:game_database;create=true"; // Embedded mode URL for Derby
    private static final String USER = "pdc";
    private static final String PASSWORD = "pdc";
    
//    static 
//    {
//        try 
//        {
//        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
//            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//        } 
//        catch (ClassNotFoundException | SQLException e) 
//        {
//            e.printStackTrace();
//        }
//    }


    // Connect to the Derby database
    public static Connection connect() throws SQLException 
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Create necessary tables if they don't already exist
    public static void createTables() 
    {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // Create Players table
            stmt.execute("CREATE TABLE Players (id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, name VARCHAR(50), hp INTEGER, xp INTEGER)");

            // Create HighScores table
            stmt.execute("CREATE TABLE HighScores (id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, name VARCHAR(50), score INTEGER, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {
            // Check for SQLState "X0Y32" to avoid errors if tables already exist
            if (!e.getSQLState().equals("X0Y32")) {
                e.printStackTrace();
            }
        }
    }

    // Save player progress to the Players table
    public static void savePlayer(Player player) {
        String sql = "INSERT INTO Players (name, hp, xp) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getName()); // Get name using getName()
            pstmt.setInt(2, player.getHp());
            pstmt.setInt(3, player.getTotalXP());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load player progress by player name from the Players table
    public static Player loadPlayer(String name) {
        String sql = "SELECT * FROM Players WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name); // Directly pass the name variable
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int hp = rs.getInt("hp");
                int xp = rs.getInt("xp");
                return new Player(name, hp, xp); // Adjusted constructor to use name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Save a high score to the HighScores table
    public static void saveHighScore(String name, int score) {
        String sql = "INSERT INTO HighScores (name, score) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name); // Directly pass the name variable
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display top 10 high scores from the HighScores table
    public static void displayHighScores() {
        String sql = "SELECT name, score, date FROM HighScores ORDER BY score DESC FETCH FIRST 10 ROWS ONLY";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Top 10 High Scores:");
            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                String date = rs.getString("date");
                System.out.println(name + " - " + score + " (" + date + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

