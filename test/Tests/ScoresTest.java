/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tests;

import PDC_Project1.Game;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author alex 20125029
 */
public class ScoresTest 
{
    public Game game;

    @Before
    public void testScore() 
    {
        game = new Game();
        game.displayScores();
    }

    @After
    public void testDB() 
    {
        game.closeDBConn();
    }

    //test scores has been created
    @Test
    public void testSaveScores() 
    {
        Assert.assertNotNull(game.scores);
    }
}
