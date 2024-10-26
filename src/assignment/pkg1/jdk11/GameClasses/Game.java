/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignment.pkg1.jdk11.GameClasses;

/**
 *
 * @author jackson and layne
 */

import javax.swing.*;

public class Game 
{
    public static void main(String[] args) 
    {
        // Create an instance of GameGUI
        GameGUI game = new GameGUI();
        
        // Make the GUI visible
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                game.setVisible(true);
            }
        });
    }
}