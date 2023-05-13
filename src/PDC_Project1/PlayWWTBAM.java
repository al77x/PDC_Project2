/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PDC_Project1;

import java.util.Scanner;

/**
 *
 * @author alex 20125029
 */
public class PlayWWTBAM 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("o-----------------------------------------------------------------------------------------o");
        System.out.println(" Who Wants to Be a Millionaire?\n");
        
        System.out.println(" The Rules:\n");
        System.out.println(" You are given a question and you have 4 options to choose from. If you need help you can have 3 \"lifelines\" to choose from.");
        System.out.println(" Each question gets harder and the more you get right, the more amount of prize you take home for yourself.");
        System.out.println(" There are 2 checkpoints in the game. The first one is at Question 5, if you get this right you will take home ");
        System.out.println(" $1000 and if you get it wrong, you go home with nothing. The second checkpoint is at Question 10 for $32,000.\n");
        
        System.out.println(" How to Play:\n");
        System.out.println(" Choose from options 1-4 to answer questions and 6-8 to activate Lifelines!");
        System.out.println(" (6) 50:50 = Activate this lifeline to remove 2 options from the answers.");
        System.out.println(" (7) Phone A Friend = Activate this lifeline to call a friend for help!");
        System.out.println(" (8) Ask the Audience = Activate this lifeline to get the audience to help.");
        System.out.println(" (5) To walk away with your current winnings at any time during the game.\n");
        System.out.println(" So you want to be a Millionaire?\n");
        System.out.println(" Are you ready to begin? Enter 'Y' to start a new game!");
        System.out.println(" S = View High Scores");
        System.out.println(" Q = Quit");
        System.out.println("o-----------------------------------------------------------------------------------------o");
        
        char scanned = ' ';
        char playing = ' ';
        
        while (playing != 'N') //(assisted via chatGPT)
        {
            while (scanned != 'Y' && scanned != 'S' && scanned != 'Q' && scanned != 'N') 
            {
                if (scanned != ' ') 
                {
                    System.out.println("Invalid input! Please enter 'Y' to start a new game, 'S' to view the high scores board, or 'Q' to quit."); 
                }
                
                scanned = scan.next().toUpperCase().charAt(0);
            }
            
            switch (scanned) 
            {
                case 'Y'://start game
                    Game game = new Game();
                    while (game.isPlaying()) 
                    {
                        game.askQuestion();
                    }
                    System.out.println("\nPlay again? (Y/N)");
                    scanned = ' ';
                    scanned = ' '; //reset scanned value for the next iteration
                    break;
                case 'S':// view scores
                    FileSaveScores saves = new FileSaveScores();
                    saves.getScores();
                    saves.printScoreBoard();
                    System.out.println("\nPlay Who Wants to Be a Millionaire? (Y/N)");
                    scanned = ' '; //reset scanned value for the next iteration
                    break;
                case 'Q':// quit game
                    playing = 'N';
                    break;
                case 'N':// quit game
                    playing = 'N';
                    scanned = ' '; //reset scanned value for the next iteration
                    break;
                default:
                    break;
            }
        }
    }
}

