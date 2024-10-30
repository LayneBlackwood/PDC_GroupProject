/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11.GameClasses;

/**
 *
 * @author jackson and layne
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNameEntry extends JDialog 
{
    private JTextField nameField;
    private JButton confirmButton;
    private GameController gameController;

    public PlayerNameEntry(JFrame parent, GameController gameController) 
    {
        super(parent, "Enter Player Name", true);
        this.gameController = gameController;

        setLayout(new BorderLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);

        // Input field for player name
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Player Name:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);
        add(inputPanel, BorderLayout.CENTER);

        // Confirm button
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) 
                {
                    gameController.startGame(playerName);
                    dispose(); // Close the dialog
                } 
                else 
                {
                    JOptionPane.showMessageDialog(PlayerNameEntry.this, "Please enter a valid name.");
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);
    }
}

