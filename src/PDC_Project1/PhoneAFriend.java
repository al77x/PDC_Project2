/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author alex 20125029
 */
public class PhoneAFriend extends Lifeline
{
    @Override
    public int use(Question question)
    {
        if(super.getUsed()) //check to see if the lifeline has been used, it can only be used once
        {
            System.out.println("Phone a friend has already been used! Select a different lifeline or answer the question.");
            return -1;
        }
        
        //different responses to generate when using this lifeline
        ArrayList<String> responses = new ArrayList();
        responses.add("I'm confident that the answer is ");
        responses.add("I know this! I'm glad you called, it's option ");
        responses.add("I can't really think straight right now, but if you want me to guess I'll just go with number ");
        responses.add("Hmmm... This one sounds familiar. I'll go with number ");
        responses.add("You're in a television show right now and you didn't tell me?! Okay, I'll just go with option ");
        responses.add("Don't get mad at me if I get this wrong, but I think it's ");
        
        Random random = new Random();

        //selects a random response from the list using Random
        String reply = responses.get(random.nextInt(responses.size()));
        
        //answer the friend will give
        int answer = -1;
        
        //70% chance for correct answer
        int correct = random.nextInt(10);
        
        if(correct < 7)
        {
            answer = question.getCorrectAnswer();
        }
        else
        {
            while (answer == -1) //generate random incorrect answer
            {
                answer = random.nextInt(4) + 1; // adjust for index 0-3
                
                if(answer == question.getCorrectAnswer()) //confirm is not correct
                {
                    answer = -1;
                }
            }
        }
        
        reply = reply + answer;
        
        System.out.println("\nYour friend picked up their phone!\n");
        System.out.println("Friend: " + reply);
        
        System.out.println("\nEnter your answer now: ");
        
        super.setUsed(); //call method so that lifeline cannot be activated again
        
        return -1; //rescans for an input after the lifeline is used
    }
}
