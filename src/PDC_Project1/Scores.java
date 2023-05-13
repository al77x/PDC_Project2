/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

/**
 *
 * @author alex 20125029
 */
public class Scores implements Comparable //use interface to use the compareTo method
{
    //instance variables
    private String name;
    private int score;
    
    public Scores()
    {
        name = "";
        score = 0;
    }
    
    public Scores(String name, int score)
    {
        this.name = name;
        this.score = score;
    }

    //getters and setters
    public String getName() 
    {
        return name;
    }

    public int getScore() 
    {
        return score;
    }
    
    //this method allows me to sort the scores in order
    @Override
    public int compareTo(Object o)
    {
        //compare the score of the current object with the score of the object being compared
        return score - ((Scores)o).getScore();
    }
}
