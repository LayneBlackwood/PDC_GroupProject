/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.GameClasses;

/**
 *
 * @author jackson and layne
 */
import assignment.pkg1.jdk11.DatabaseClasses.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SummaryScreen extends JFrame 
{
    private DatabaseManager dbManager;

    public SummaryScreen(DatabaseManager dbManager) 
    {
        this.dbManager = dbManager;
        setTitle("Leaderboard & Summary");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea leaderboardTextArea = new JTextArea();
        leaderboardTextArea.setEditable(false);
        
        // Call fetchLeaderboardData and set text in leaderboardTextArea
        leaderboardTextArea.setText(fetchLeaderboardData());

        JScrollPane scrollPane = new JScrollPane(leaderboardTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> System.exit(0));  // Exit the game when closed
        add(closeButton, BorderLayout.SOUTH);
    }

    private String fetchLeaderboardData() 
    {
        StringBuilder leaderboardData = new StringBuilder("Leaderboard:\n\n");
        boolean wasConnected = true;

        try 
        {
            // Check if already connected, if not, connect to database
            if (dbManager.getConnection() == null || dbManager.getConnection().isClosed()) 
            {
                dbManager.connect();
                wasConnected = false;
            }

            List<String> highScores = dbManager.getHighScores();
            for (String score : highScores) 
            {
                leaderboardData.append(score).append("\n");
            }
        } 
        catch (SQLException e) 
        {
            leaderboardData.append("Error fetching data.");
            e.printStackTrace();
        } 
        finally 
        {
            // Disconnect only if we connected within this method
            if (!wasConnected) 
            {
                dbManager.disconnect();
            }
        }
        return leaderboardData.toString();
    }
}




