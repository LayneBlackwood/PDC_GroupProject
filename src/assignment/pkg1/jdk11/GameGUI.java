/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

/**
 *
 * @author jackson and layne
 */

//This class handles the "look" of the game and what the user sees on the GUI
public class GameGUI extends JFrame 
{
    private GameController gameController;
    private JTextField nameTextField;
    private JTextArea scenarioTextArea;
    private JLabel healthLabel, xpLabel;
    private JButton option1Button, option2Button, option3Button, bossButton, confirmButton, quitButton;
    private BufferedReader scenarioReader;

    public GameGUI() 
    {
        // Initialize controller
        gameController = new GameController(this);
        
        // Setup the GUI components
        setupUI();
        
        // Attempt to open the scenario file here or when starting the game
        try 
        {
            scenarioReader = new BufferedReader(new FileReader("scenarios.txt"));
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(this, "Error loading scenarios: " + e.getMessage());
            scenarioReader = null;  // Set to null if loading fails
        }
    }

    private void setupUI() 
    {
        setTitle("RPG Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize Components
        scenarioTextArea = new JTextArea(10, 50);
        scenarioTextArea.setEditable(false);
        scenarioTextArea.setLineWrap(true);
        scenarioTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(scenarioTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        option1Button = new JButton("Option 1");
        option2Button = new JButton("Option 2");
        option3Button = new JButton("Option 3");
        bossButton = new JButton("Fight the Boss");
        bossButton.setVisible(false);

        buttonPanel.add(option1Button);
        buttonPanel.add(option2Button);
        buttonPanel.add(option3Button);
        buttonPanel.add(bossButton);

        add(buttonPanel, BorderLayout.SOUTH);

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel promptLabel = new JLabel("Enter your name: ");
        inputPanel.add(promptLabel);

        nameTextField = new JTextField(15);
        inputPanel.add(nameTextField);

        confirmButton = new JButton("Confirm");
        inputPanel.add(confirmButton);

        add(inputPanel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        healthLabel = new JLabel("Health: ");
        xpLabel = new JLabel("XP: ");
        statsPanel.add(healthLabel);
        statsPanel.add(xpLabel);
        statsPanel.setPreferredSize(new Dimension(200, 0));
        add(statsPanel, BorderLayout.EAST);

        quitButton = new JButton("Quit");
        buttonPanel.add(quitButton);

        // Set up action listeners
        setupListeners();
    }

    private void setupListeners() 
    {
        confirmButton.addActionListener(e -> 
        {
            String playerName = nameTextField.getText().trim();
            if (!playerName.isEmpty()) 
            {
                gameController.startGame(playerName);
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.");
            }
        });

        option1Button.addActionListener(e -> gameController.handleOption1());
        option2Button.addActionListener(e -> gameController.handleOption2());
        option3Button.addActionListener(e -> gameController.handleOption3());

        quitButton.addActionListener(e -> 
        {
            int choice = JOptionPane.showConfirmDialog(
                    this, "Are you sure you want to quit?", "Quit Confirmation", JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) 
            {
                System.exit(0);
            }
        });
    }
    
    // Method to initialize the game for the player
    public void initializeGameForPlayer() 
    {
        // Hide name input components
        nameTextField.setVisible(false);
        confirmButton.setVisible(false);

        // Show game components
        scenarioTextArea.setVisible(true);
        option1Button.setVisible(true);
        option2Button.setVisible(true);
        option3Button.setVisible(true);
        healthLabel.setVisible(true);
        xpLabel.setVisible(true);

        // Display welcome message and load the first scenario
        appendText("Welcome, " + gameController.getPlayerName() + "! The game begins now...");
        updatePlayerStats(100, 0); // Default initial values for health and XP
        loadNextScenario();
    }

    // Method to display the player's score at the end of the game
    public void showScoreboard(String playerName, int xp, int health) 
    {
        StringBuilder scoreboard = new StringBuilder();
        scoreboard.append("Player: ").append(playerName).append("\n");
        scoreboard.append("Total XP: ").append(xp).append("\n");
        scoreboard.append("Final Health: ").append(health).append("\n");

        JOptionPane.showMessageDialog(this, scoreboard.toString(), "Scoreboard", JOptionPane.INFORMATION_MESSAGE);
    }    

    // Load the next scenario from the file
    public void loadNextScenario() 
    {
        if (scenarioReader == null) 
        {
            appendText("Scenarios file not loaded.");
            return;
        }

        try 
        {
            String title = scenarioReader.readLine();
            if (title == null) 
            {
                appendText("\nNo more scenarios available. The game is over.");
                disableGameOptions();
                return;
            }

            appendText("\n" + title + "\n");

            String option1 = scenarioReader.readLine();
            String option2 = scenarioReader.readLine();
            String option3 = scenarioReader.readLine();

            if (option1 == null || option2 == null || option3 == null) 
            {
                appendText("\nScenario options are incomplete.");
                disableGameOptions();
                return;
            }

            option1Button.setText(option1);
            option2Button.setText(option2);
            option3Button.setText(option3);

            bossButton.setVisible(Enemy.isBossUnlocked() && !Enemy.isBossDefeated());
        } 
        catch (IOException e) 
        {
            appendText("\nError reading next scenario: " + e.getMessage());
        }
    }

    public void appendText(String text) 
    {
        scenarioTextArea.append(text);
        scenarioTextArea.setCaretPosition(scenarioTextArea.getDocument().getLength());
    }

    public void updatePlayerStats(int health, int xp) 
    {
        healthLabel.setText("Health: " + health);
        xpLabel.setText("XP: " + xp);
    }

    public void disableGameOptions() 
    {
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
    }

    public JTextArea getScenarioTextArea() 
    {
        return scenarioTextArea;
    }
}



