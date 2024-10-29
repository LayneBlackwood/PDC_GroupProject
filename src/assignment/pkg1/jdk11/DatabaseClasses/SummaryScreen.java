/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.DatabaseClasses;

/**
 *
 * @author jackson and layne
 */
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
        setTitle("Previous Players and High Scores");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTextArea playerTextArea = new JTextArea();
        playerTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(playerTextArea);
        add(scrollPane, BorderLayout.CENTER);

        displayPlayersAndScores(playerTextArea);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
    }

    private void displayPlayersAndScores(JTextArea textArea) 
    {
        try 
        {
            dbManager.connect();
            List<String> players = dbManager.getPlayers();
            List<String> highScores = dbManager.getHighScores();

            textArea.setText("Players and High Scores:\n\n");
            for (int i = 0; i < players.size(); i++) 
            {
                textArea.append(players.get(i) + " - " + (i < highScores.size() ? highScores.get(i) : "No score") + "\n");
            }
            dbManager.disconnect();
        } 
        catch (SQLException e) 
        {
            textArea.setText("Error fetching data.");
            e.printStackTrace();
        }
    }
}


//public class SummaryScreen extends JFrame 
//{
//    private DatabaseManager dbManager;
//
//    public SummaryScreen(DatabaseManager dbManager) 
//    {
//        this.dbManager = dbManager;
//        setTitle("Previous Players and High Scores");
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        
//        JTextArea playerTextArea = new JTextArea();
//        playerTextArea.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(playerTextArea);
//        add(scrollPane, BorderLayout.CENTER);
//
//        // Fetch and display the players and high scores
//        displayPlayersAndScores(playerTextArea);
//
//        // Add a "Close" button at the bottom
//        JButton closeButton = new JButton("Close");
//        closeButton.addActionListener(e -> dispose());
//        add(closeButton, BorderLayout.SOUTH);
//    }
//
//    private void displayPlayersAndScores(JTextArea textArea) 
//    {
//        try 
//        {
//            dbManager.connect();
//            List<String> players = dbManager.getPlayers();  // Assuming you already have this method in DatabaseManager
//            List<String> highScores = dbManager.getHighScores();  // Assuming you have a method like this in DatabaseManager
//
//            textArea.setText("Players and High Scores:\n\n");
//            for (int i = 0; i < players.size(); i++) 
//            {
//                textArea.append(players.get(i) + " - Score: " + highScores.get(i) + "\n");
//            }
//
//            dbManager.disconnect();
//        } 
//        catch (SQLException e) 
//        {
//            textArea.setText("Error fetching data.");
//            e.printStackTrace();
//        }
//    }
//}

