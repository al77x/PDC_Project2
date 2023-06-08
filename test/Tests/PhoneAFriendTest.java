/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tests;

import PDC_Project1.PhoneAFriend;
import PDC_Project1.Question;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author alex 20125029
 */
public class PhoneAFriendTest 
{
    public PhoneAFriend friend;
    public Question testQuestion;
    
    @Before
    public void lifelines()
    {
        friend = new PhoneAFriend();
        testQuestion = new Question(); 
    }

    //test to use unused lifeline
    @Test
    public void testLifelines()
    {
        boolean expectedUsedState = false;
        boolean actualUsedState = friend.getUsed();
        
        Assert.assertEquals(expectedUsedState, actualUsedState);
        
        /*
        return 1 after successfully completing
        return -1 if lifeline had already been used, didn't successfully complete
        (assisted via ChatGPT)
        */
        int expectedReturn = -1;
        int actualReturn = friend.use(testQuestion);
        
        Assert.assertEquals(expectedReturn, actualReturn);
    }
    
    //test to use already used lifeline
    @Test
    public void lifelinesUsed()
    {
        friend.setUsed(); // set to already used
        
        boolean expectedUsedState = true;
        boolean actualUsedState = friend.getUsed();
        
        Assert.assertEquals(expectedUsedState, actualUsedState);
       
        int expectedReturn = -1;
        int actualReturn = friend.use(testQuestion);
        
        Assert.assertEquals(expectedReturn, actualReturn);
    }
}
