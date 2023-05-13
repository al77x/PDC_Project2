/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

/**
 *
 * @author alex 20125029
 */
public abstract class Lifeline 
{
    //lifelines can only be used once, set to true if used.
    //instance variables
    private boolean used = false;

    //getters and setters
    public boolean getUsed() 
    {
        return used;
    }

    public void setUsed() 
    {
        this.used = true;
    }
    
    //this method is used on all lifelines and is called when the player activates one
    public abstract int use(Question question);
}
