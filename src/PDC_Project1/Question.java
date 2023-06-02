/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

/**
 *
 * @author alex 20125029
 */
public class Question 
{
    //instance variables
    private String questions;
    private String[] answers;
    private int correctAnswer;
    private int level;

    //constructor
    public Question() 
    {
        this.questions = "";
        this.answers = new String[4];
        this.correctAnswer = 0;
        this.level = 0;
    }

    //second constructor 
    public Question(int level, String questions, String[] answers, int correctAnswer) 
    {
        this.questions = questions;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.level = level;
    }

    //getters and setters
    public void setAnswers(int index, String newAnswer) 
    {
        answers[index] = newAnswer;
    }

    public int getCorrectAnswer() 
    {
        return correctAnswer;
    }

    public String getQuestions() 
    {
        return questions;
    }

    public String[] getAnswers() 
    {
        return answers;
    }
    
    //displays a question and its answer choices 
    public void printQuestion()
    {
        System.out.println(questions);
        
        for (int count = 0; count < 4; count++)
        {
            try
            {
                Thread.sleep(1000); //1 second delay to print the answers
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            System.out.println((count + 1) + ") " + answers[count]);
        }
    }
    
}
