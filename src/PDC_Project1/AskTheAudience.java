/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
            JOptionPane.showMessageDialog(null, "You've already asked the audience, you can't ask them anymore. Select a different Lifeline from the options or answer the question."); //shows a dialog box with the panel
            return -1; 
        }
        
        //otherwise if it has not been used, the lifeline is being used in this scenario

        //display to the user in the audience interaction dialog box
        String text1 = "You are getting help from the Audience!";
        String text2 = "The audience are discussing their answers..."; 
        
        int[] probability = new int[4]; //answers 1-4 (index 0-3)
        int correctAnswer = question.getCorrectAnswer() - 1;
        Random random = new Random();
        
        ArrayList<String> output = new ArrayList();
        
        //this much goes towards the correct answer
        probability[correctAnswer] = 40;
        
        //this much goes to all the answers
        int remaining = 60;
        
        //calculating and displaying the audience's vote result for each answer. 
        for(int i = 0; i < 4; i++)
        {
            String line = "";
            int generate = 0;
            
            if(remaining > 0)
            {
                generate = random.nextInt(remaining) + 1;
            }
            
            probability[i] += generate;
            remaining -= generate;
            
            line = line + (i+1) + ") ";
            
            for (int k = 0; k < probability[i]; k++)
            {
                line = line + "="; //prints the lines based on the probability of the answer
            }
            
            line = line + probability[i] + "%";
            
            output.add(line);
        }
        
        JPanel display = new JPanel();
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
        
        display.add(new JLabel(text1));
        display.add(new JLabel(text2));
        
        for (int i = 0; i < output.size(); i++)
        {
            display.add(new JLabel(output.get(i)));
        }
        
        JOptionPane.showMessageDialog(null, display);
        
        super.setUsed(); //call method so that lifeline cannot be activated again
        
        return -1; //rescans for an input after the lifeline is used (assisted via ChatGPT)
    }
}
