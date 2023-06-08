/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author alex 20125029
 */
public class FiftyFifty extends Lifeline
{
    @Override
    public int use(Question question)
    {
        if(super.getUsed()) //check to see if the lifeline has been used, it can only be used once
        {
            JOptionPane.showMessageDialog(null, "You've already used your 50:50. Select a different Lifeline from the options or answer the question.");
            return -1;
        }
        
        //otherwise if it has not been used, the lifeline is being used in this scenario
        String text1 = "You have used the 50:50 lifeline!";
        String text2 = "Two answers have been removed from the options.";
        
        Random random = new Random();
        
        int remove1 = -1;
        int remove2 = -1;
        
        boolean option1 = false;
        boolean option2 = false;
        
        while (option1 == false)
        {
            remove1 = random.nextInt(4);
            if((remove1 + 1) != question.getCorrectAnswer())
            {
                option1 = true;
            }
        }
        
        while(option2 == false)
        {
            remove2 = random.nextInt(4);
            if((remove2 + 1) != question.getCorrectAnswer() && remove2 != remove1)
            {
                option2 = true;
            }
        }
        
        //removes 2 answers, output will be blank
        question.setAnswers(remove1, "");
        question.setAnswers(remove2, "");
        
        JPanel display = new JPanel();
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
        
        display.add(new JLabel(text1));
        display.add(new JLabel(text2));
        
        JOptionPane.showMessageDialog(null, display);
        
        super.setUsed(); //call method so that lifeline cannot be activated again
        
        return -1; //rescans for an input after the lifeline is used
    }
}
