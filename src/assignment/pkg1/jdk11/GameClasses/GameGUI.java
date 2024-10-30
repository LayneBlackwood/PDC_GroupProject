/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.GameClasses;

import assignment.pkg1.jdk11.EnemyClasses.EnemyFactory;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        gameController = new GameController(this);
        setupUI();
        
        try 
        {
            scenarioReader = new BufferedReader(new FileReader("scenarios.txt"));
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(this, "Error loading scenarios: " + e.getMessage());
            scenarioReader = null;
        }
    }

    private void setupUI() 
    {
        setTitle("RPG Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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
        
        quitButton.addActionListener(e -> showCustomQuitDialog());
    }
    
    private void showCustomQuitDialog() 
    {
        Object[] options = {"Quit and Save Player", "Quit and Don't Save Player", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Are you sure you want to quit?",
                "Quit Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) 
        {
            case JOptionPane.YES_OPTION: // "Quit and Save Player"
                gameController.saveFinalPlayerData(); // Ensures final player data is saved before quitting
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION: // "Quit and Don't Save Player"
                System.exit(0);
                break;
            case JOptionPane.CANCEL_OPTION: // "Cancel"
                // Do nothing, returning the player to the game
                break;
            default:
                break;
        }
    }

    
    public void initializeGameForPlayer() 
    {
        nameTextField.setVisible(false);
        confirmButton.setVisible(false);

        scenarioTextArea.setVisible(true);
        option1Button.setVisible(true);
        option2Button.setVisible(true);
        option3Button.setVisible(true);
        healthLabel.setVisible(true);
        xpLabel.setVisible(true);

        appendText("Welcome, " + gameController.getPlayerName() + "! The game begins now...");
        updatePlayerStats(100, 0); 
        loadNextScenario();
    }

    public void showScoreboard(String playerName, int xp, int health) 
    {
        StringBuilder scoreboard = new StringBuilder();
        scoreboard.append("Player: ").append(playerName).append("\n");
        scoreboard.append("Total XP: ").append(xp).append("\n");
        scoreboard.append("Final Health: ").append(health).append("\n");

        JOptionPane.showMessageDialog(this, scoreboard.toString(), "Scoreboard", JOptionPane.INFORMATION_MESSAGE);
    }    

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

            bossButton.setVisible(EnemyFactory.isBossUnlocked() && !EnemyFactory.isBossDefeated());
        } 
        catch (IOException e) 
        {
            appendText("\nError reading next scenario: " + e.getMessage());
        }
    }

    // Method to append text to the scenario text area
    public void appendText(String text) 
    {
        scenarioTextArea.append(text);
        scenarioTextArea.setCaretPosition(scenarioTextArea.getDocument().getLength());
    }

    // Method to update the player's health and XP display
    public void updatePlayerStats(int health, int xp) 
    {
        healthLabel.setText("Health: " + health);
        xpLabel.setText("XP: " + xp);
    }

    // Method to disable game option buttons
    public void disableGameOptions() 
    {
        option1Button.setEnabled(false);
        option2Button.setEnabled(false);
        option3Button.setEnabled(false);
        bossButton.setEnabled(false);
    }

    // Getter for scenario text area for use by other classes
    public JTextArea getScenarioTextArea() 
    {
        return scenarioTextArea;
    }
}