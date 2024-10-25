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
 * @author jacks
 */
public class GameGUI extends JFrame
{
    private JTextField nameTextField;
    private JLabel promptLabel;
    private JButton confirmButton;
    private JLabel scenarioLabel;
    private JTextArea scenarioTextArea;
    private JButton option1Button;
    private JButton option2Button;
    private JButton option3Button;
    private JButton bossButton;
    private JTextArea textArea;
    private JPanel statsPanel;
    private JLabel healthLabel;
    private JLabel xpLabel;
    private JButton quitButton;
    
    private static final int MAX_LINES = 10;
    
    
    private Player player;
    private Enemy currentEnemy;
    private BufferedReader scenarioReader;
    
    public GameGUI()
    {
        setTitle("RPG Game");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //text area for displaying scenarios
        scenarioTextArea = new JTextArea(10,50);
        scenarioTextArea.setEditable(false);
        scenarioTextArea.setVisible(false);
        scenarioTextArea.setLineWrap(true);
        scenarioTextArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(scenarioTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Show vertical scrollbar as needed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrollbar
        add(scrollPane, BorderLayout.CENTER);
        
        //Panel for the option buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
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
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        promptLabel = new JLabel("Enter your name: ");
        inputPanel.add(promptLabel);

        nameTextField = new JTextField(15);  // Set a reasonable size for the text field
        inputPanel.add(nameTextField);

        confirmButton = new JButton("Confirm");
        inputPanel.add(confirmButton);

        add(inputPanel, BorderLayout.NORTH);  // Add input panel at the top (NORTH)
        
        statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 1));
        healthLabel = new JLabel();  // Initialize healthLabel
        xpLabel = new JLabel();      // Initialize xpLabel
        statsPanel.add(healthLabel); // Add healthLabel to statsPanel
        statsPanel.add(xpLabel);     // Add xpLabel to statsPanel
        statsPanel.setPreferredSize(new Dimension(200, 0)); // Set width to 200 pixels, height to default

        quitButton = new JButton("Quit");
        buttonPanel.add(quitButton);

        add(statsPanel, BorderLayout.EAST); // Add statsPanel to the EAST side of the layout

        quitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int choice = JOptionPane.showConfirmDialog(
                GameGUI.this,
                "Are you sure you want to quit?",
                "Quit Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0); // Exit the application
            }
        }
    });
        
        
        confirmButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            String playerName = nameTextField.getText().trim();
            if(!playerName.isEmpty())
            {
                player = new Player(playerName, GameGUI.this);
                initializeGameForPlayer();
            }else
                {
                    JOptionPane.showMessageDialog(null, "Please enter a valid name.");
                }
            }
        });
        
        option1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleOption1();
            }
        });

        option2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleOption2();
            }
        });

        option3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleOption3();
            }
        });

        bossButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fightBoss();
            }
        });
        
    }
    private void initializeGameForPlayer() {
        // Hide the name input components
        promptLabel.setVisible(false);
        nameTextField.setVisible(false);
        confirmButton.setVisible(false);

        // Show the game components
        scenarioTextArea.setVisible(true);
        option1Button.setVisible(true);
        option2Button.setVisible(true);
        option3Button.setVisible(true);
        statsPanel.setVisible(true);

        // Start the game and display the first scenario
        scenarioTextArea.setText("Welcome, " + player.getName() + "! The game begins now...");
        updatePlayerStats();
        
        try {
            scenarioReader = new BufferedReader(new FileReader("scenarios.txt"));
            loadNextScenario();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading scenarios.");
        }
    }
    
    private void loadNextScenario() {
    try {
        String title = scenarioReader.readLine();

        if (title == null) {
            appendText("\nNo more scenarios available. The game is over.");
            disableGameOptions();
            return;
        }

        // Display the scenario
        appendText("\n" + title + "\n");

        // Read the three options from the file
        String option1 = scenarioReader.readLine();
        String option2 = scenarioReader.readLine();
        String option3 = scenarioReader.readLine();

        if (option1 == null || option2 == null || option3 == null) {
            appendText("\nScenario options are incomplete.");
            disableGameOptions();
            return;
        }

        // Set the buttons with the new options
        option1Button.setText(option1);
        option2Button.setText(option2);
        option3Button.setText(option3);

        // Check if the boss is unlocked and show the boss button if necessary
        if (Enemy.isBossUnlocked() && !Enemy.isBossDefeated()) {
            bossButton.setVisible(true);
        } else {
            bossButton.setVisible(false);
        }
    } catch (IOException e) {
        appendText("\nError reading next scenario: " + e.getMessage());
    }
}

    private void disableGameOptions() 
    {
       option1Button.setEnabled(false);
       option2Button.setEnabled(false);
       option3Button.setEnabled(false);
       bossButton.setEnabled(false);
    }
    
    
    void appendText(String text) {
    // Append text to the JTextArea
    scenarioTextArea.append(text);
    
    // If there's a maximum line limit, handle that here (if needed)
    String[] lines = scenarioTextArea.getText().split("\n");
    if (lines.length > MAX_LINES) {
        StringBuilder newText = new StringBuilder();
        for (int i = lines.length - MAX_LINES; i < lines.length; i++) {
            newText.append(lines[i]).append("\n");
        }
        scenarioTextArea.setText(newText.toString());
    }

    // Auto-scroll to the bottom
    scenarioTextArea.setCaretPosition(scenarioTextArea.getDocument().getLength());
}


   

    
    
    private void handleOption1()
    {
        if (player.getHp() <= 0) 
        {
            endGame();
        };
        
        
        currentEnemy = Enemy.generateEnemy(player);
        appendText("\nYou chose to attack the worker!");
        fightEnemy();
        player.gainEvilXP(10);
        updatePlayerStats();
        
    }
    
    private void handleOption2()
    {
        if (player.getHp() <= 0) 
        {
            endGame();
        }    
        
        if (attemptEscape()) 
        {
            appendText("\nYou successfully escaped!");
        } else 
        {
           appendText("\nYou failed to escape and must fight!");
            currentEnemy = Enemy.generateEnemy(player);
            fightEnemy();
        }
        player.gainNeutralXP(10);
        updatePlayerStats();
        
    }
    
    private void handleOption3()
    {
        if (player.getHp() <= 0)
        {
            endGame();
        }
            
        
        if (player.giveAwayHealingPotion()) 
        {
            appendText("\nYou chose to gift generously!");
            player.gainGoodXP(10);
        } 
        else 
        {
            appendText("\nYou have no healing potions to give.");
        }
        updatePlayerStats();
    }
    
    private void fightBoss() 
    {
        currentEnemy = Enemy.getBoss();
        appendText("\nYou chose to fight the Boss!");
        fightEnemy();
        bossButton.setVisible(false);
        loadNextScenario();
    }

    private void fightEnemy() {
    SwingWorker<Void, String> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
            // Perform the fight logic off the EDT
            FightEnemyAction fight = new FightEnemyAction(player, currentEnemy, scenarioTextArea);
            fight.fightEnemy(); 
            return null;
        }

        @Override
        protected void done() {
            // This runs after doInBackground() completes, on the EDT
            try {
                if(player.getHp() <= 0)
                {
                    endGame();
                }
                else if (currentEnemy.getHp() <= 0) {
                    appendText("\nEnemy defeated!");
                    // After the fight ends, move to the next scenario
                    loadNextScenario();  // Now move to the next scenario
                } else {
                    // If the enemy is not defeated, update the UI with current fight status
                    appendText("\nEnemy HP: " + currentEnemy.getHp());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    
    worker.execute(); // Start the background task
    }

    
    

    
    private boolean attemptEscape() 
    {
        Random rand = new Random();
        int escapeChance = rand.nextInt(100);
        return escapeChance < 70;
    }
     private void updatePlayerStats() 
     {
        healthLabel.setText("Health: " + player.getHp()); // Display health
        // Assuming you have a method in Player to get XP
        xpLabel.setText("XP: " + player.getTotalXP()); // Display XP
    }
     
     private void showScoreboard() 
     {
    StringBuilder scoreboard = new StringBuilder();
    scoreboard.append("Player: ").append(player.getName()).append("\n");
    scoreboard.append("Total XP: ").append(player.getTotalXP()).append("\n");
    scoreboard.append("Final Health: ").append(player.getHp()).append("\n");

    JOptionPane.showMessageDialog(this, scoreboard.toString(), "Scoreboard", JOptionPane.INFORMATION_MESSAGE);
     }
     
     private void endGame()
     {
        disableGameOptions();
        showScoreboard(); // Show the scoreboard at the end of the game
        
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to save your score?", "Save Score", JOptionPane.YES_NO_OPTION);
    
        if (choice == JOptionPane.YES_OPTION) 
        {
            saveScore(player);
        }
        
        System.exit(0);
     }
     
     private void saveScore(Player player) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Scoreboard.txt", true))) 
        {
            writer.write("Player: " + player.getName());
            writer.newLine();
            writer.write("Total XP: " + player.getTotalXP());
            writer.newLine();
            writer.write("Final Health: " + player.getHp());
            writer.newLine();
            writer.write("-----------------------------"); // Separator for clarity
            writer.newLine();
            writer.close();
            
            JOptionPane.showMessageDialog(this, "Score saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (IOException e) 
        {
        JOptionPane.showMessageDialog(this, "Error saving score: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);        
        }
    }

     
     
}



