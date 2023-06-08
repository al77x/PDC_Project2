/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tests;

import PDC_Project1.Game;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author alex 20125029
 */
public class TestGame 
{
    public Game game;
        
    @Before
    public void testGame()
    {
        game = new Game();
    }
    
    @After
    public void testDB()
    {
        game.closeDBConn();
    }

    //test values for game have been reset after calling resetGame() method (assisted via ChatGPT)
    @Test
    public void testResetGame()
    {
        game.setLevel(2);
        game.askQuestion();
        game.setPlaying(false);
        
        game.resetGame();
        
        boolean playing = game.getPlaying();
        Assert.assertTrue(playing);
        
        int level = game.getLevel();
        int expectedLevel = 0;
        Assert.assertEquals(expectedLevel, level);
    }
}