/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private Enemy enemy;
    
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
        // Load the first scenario, or proceed with the game logic
    }
    
    
}


