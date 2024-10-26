/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg1.jdk11;

import java.util.Random;
/**
 *
 * @author jackson and layne
 */

public class PlayerAttackFactory 
{
    private static final Random rand = new Random();

    public static int createLightAttack() 
    {
        int minLightAttackDamage = 5;
        int maxLightAttackDamage = 15;
        return rand.nextInt((maxLightAttackDamage - minLightAttackDamage) + 1) + minLightAttackDamage;
    }

    public static int createHeavyAttack() 
    {
        int minHeavyAttackDamage = 10;
        int maxHeavyAttackDamage = 25;
        return rand.nextInt((maxHeavyAttackDamage - minHeavyAttackDamage) + 1) + minHeavyAttackDamage;
    }
}
