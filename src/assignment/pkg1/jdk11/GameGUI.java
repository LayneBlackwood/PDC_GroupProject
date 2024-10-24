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
    private JLabel scenarioLabel;
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
        textArea = new JTextArea(10,50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
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
    }
    
    
}


