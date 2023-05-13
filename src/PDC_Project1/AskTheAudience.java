/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

import java.util.Random;

/**
 *
 * @author alex 20125029
 */
public class AskTheAudience extends Lifeline
{
    @Override
    public int use(Question question)
    {
        if(super.getUsed()) //check to see if the lifeline has been used, it can only be used once
        {
            System.out.println("You've already asked the audience, you can't ask them anymore. Select a different Lifeline from the options or answer the question.");
            return -1;
        }
        
        //otherwise if it has not been used, the lifeline is being used in this scenario
        System.out.println("You are getting help from the Audience!");
        System.out.println("The audience are discussing their answers...");
        System.out.println("\nAudience Results:");
        
        int[] probability = new int[4]; //answers 1-4 (index 0-3)
        int correctAnswer = question.getCorrectAnswer() - 1;
        Random random = new Random();
        
        //this much goes towards the correct answer
        probability[correctAnswer] = 40;
        
        //this much goes to all the answers
        int remaining = 60;
        
        //calculating and displaying the audience's vote result for each answer. 
        for(int i = 0; i < 4; i++)
        {
            int generate = 0;
            
            if(remaining > 0)
            {
                generate = random.nextInt(remaining) + 1;
            }
            
            probability[i] += generate;
            remaining -= generate;
            
            System.out.println((i + 1) + ")");
            
            for (int k = 0; k < probability[i]; k++)
            {
                System.out.print("="); //prints the lines based on the probability of the answer
            }
            
            System.out.println("["+ probability[i] + "%" + "]");
        }
        
        System.out.println("\nEnter your answer now: "); //after ask the audience is used
        
        super.setUsed(); //call method so that lifeline cannot be activated again
        
        return -1; //rescans for an input after the lifeline is used (assisted via ChatGPT)
    }
}
