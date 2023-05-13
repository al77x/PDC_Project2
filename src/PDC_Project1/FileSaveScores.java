/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author alex 20125029
 */

public class FileSaveScores 
{
    public ArrayList<Scores> scores = new ArrayList<>();
    public Scores finalScore; //final score of the game

    /*
    Read scores from file "scores.txt" and populates the scores ArrayList.
    */
    public void getScores() 
    {
        //create a new File object that represents the "scores.txt" file
        File scoreFile = new File("scores.txt");
        
        //check if the file exists
        boolean file = scoreFile.exists();

        //if file exists, read the scores
        if (file) 
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(scoreFile))) 
            {
                String line;
                
                //read each line from the file
                while ((line = reader.readLine()) != null) 
                {
                    //store both name and score (assisted via chatGPT)
                    String[] parts = line.split(" ");
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    
                    //create new scores object with name and score and add it to scores
                    scores.add(new Scores(name, score));
                }
            } catch (IOException e) 
            {
                System.err.println(e); //if an exception occurs
            }
        }
    }

    /*
    Saves the final score to the file and updates the high score list.
    */
    public void saveScore() 
    {
        //Create a new File object that represents the "scores.txt" file
        File scoreFile = new File("scores.txt");

        scores.add(finalScore); //add the final score to scores
        Collections.sort(scores); //sort the list in ascending order
        Collections.reverse(scores); //reverse the order so that highest scores are first

        if (scores.size() > 15) 
        {
            scores.remove(15); //deletes the score at index 15 to keep the list size limited to 15
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreFile))) 
        {
            //iterate over each Scores object in the scores list
            for (Scores score : scores) 
            {
                //write the name and score of each Scores object to the file
                writer.write(score.getName() + " " + score.getScore());
                
                writer.newLine(); //move to the next line in the file
            }
        } catch (IOException e) 
        {
            //if an exception occurs during the file writing process, print an error message along with the exception
            System.out.println("Error saving to high score file!\n" + e);
        }

        //call method to display the updated high scores list
        printScoreBoard();
    }
    
    /*
    prints the scoreboard and formats it to align the text
    iterate over each element in the scores list
    */
    public void printScoreBoard() 
    {
        System.out.println("+-------------------+");
        System.out.println("|         HIGH SCORES       |");
        System.out.println("+-------------------+");
        System.out.println("\nRecent Players:");
        
        for (int i = 0; i < scores.size(); i++) 
        {
            System.out.printf("\n%2d) %-20s %d", i + 1, scores.get(i).getName(), scores.get(i).getScore());
        }
    }
}
