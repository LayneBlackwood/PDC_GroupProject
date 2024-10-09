/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

/**
 *
 * @author jackson and layne
 */
public class ScoreEntry 
{
    private String name;
    private int xp;
    
    public ScoreEntry(String name, int xp)
    {
        this.name = name;
        this.xp = xp;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getXp()
    {
        return xp;
    }
}
