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
        JScrollPane scrollPane = new JScrollPane(scenarioTextArea);
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
        
        confirmButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            String playerName = nameTextField.getText().trim();
            if(!playerName.isEmpty())
            {
                player = new Player(playerName);
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

        // Start the game and display the first scenario
        scenarioTextArea.setText("Welcome, " + player.getName() + "! The game begins now...");
        
        
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
            scenarioTextArea.append("\nNo more scenarios available. The game is over.");
            disableGameOptions();
            return;
        }

        // Display the scenario
        scenarioTextArea.append("\n" + title + "\n");

        // Read the three options from the file
        String option1 = scenarioReader.readLine();
        String option2 = scenarioReader.readLine();
        String option3 = scenarioReader.readLine();

        if (option1 == null || option2 == null || option3 == null) {
            scenarioTextArea.append("\nScenario options are incomplete.");
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
        scenarioTextArea.append("\nError reading next scenario: " + e.getMessage());
    }
}

    private void disableGameOptions() 
    {
       option1Button.setEnabled(false);
       option2Button.setEnabled(false);
       option3Button.setEnabled(false);
       bossButton.setEnabled(false);
    }

    
    
    private void handleOption1()
    {
        currentEnemy = Enemy.generateEnemy(player);
        scenarioTextArea.append("\nYou chose to attack the worker!");
        fightEnemy();
        player.gainEvilXP(10);
        loadNextScenario();
    }
    
    private void handleOption2()
    {
        if (attemptEscape()) 
        {
            scenarioTextArea.append("\nYou successfully escaped!");
        } else 
        {
            scenarioTextArea.append("\nYou failed to escape and must fight!");
            currentEnemy = Enemy.generateEnemy(player);
            fightEnemy();
        }
        player.gainNeutralXP(10);
        loadNextScenario();
    }
    
    private void handleOption3()
    {
        if (player.giveAwayHealingPotion()) 
        {
            scenarioTextArea.append("\nYou chose to gift generously!");
            player.gainGoodXP(10);
        } 
        else 
        {
            scenarioTextArea.append("\nYou have no healing potions to give.");
        }
        loadNextScenario();
    }
    
    private void fightBoss() 
    {
        currentEnemy = Enemy.getBoss();
        scenarioTextArea.append("\nYou chose to fight the Boss!");
        fightEnemy();
        bossButton.setVisible(false);
        loadNextScenario();
    }

    private void fightEnemy() {
    SwingWorker<Void, String> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
            // Perform the fight logic off the EDT
            FightEnemyAction fight = new FightEnemyAction(player, currentEnemy);
            fight.fightEnemy(player, currentEnemy); 
            return null;
        }

        @Override
        protected void done() {
            // This runs after doInBackground() completes, on the EDT
            try {
                if (currentEnemy.getHp() <= 0) {
                    scenarioTextArea.append("\nEnemy defeated!");
                    // After the fight ends, move to the next scenario
                    loadNextScenario();  // Now move to the next scenario
                } else {
                    // If the enemy is not defeated, update the UI with current fight status
                    scenarioTextArea.append("\nEnemy HP: " + currentEnemy.getHp());
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
    
}


