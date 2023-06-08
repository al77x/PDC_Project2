/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package PDC_Project1;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.logging.*;
import java.sql.DriverManager;

/**
 *
 * @author alex 20125029
 */
public class Game //Who Wants to Be a Millionaire game functionality
{    
    //instance variables
    public ArrayList<ArrayList> questions;
    private Lifeline AskTheAudience; //ask the audience lifeline
    private Lifeline FiftyFifty; //50-50 lifeline
    private Lifeline PhoneAFriend; //Phone a friend lifeline
    private int levelProgress; //the progress of the player
    private int currentLevel; //for the question
    private Random random; //use Random function
    private boolean playing; //if the game is active
    private String[] prize; //match with prizeAmount
    private int prizeAmount; //current prize player is on
    private int lifelinesUsed; // check lifelines used
    private int questionCounter = 0; //displays how many questions so far
    public ArrayList<Scores> scores;
    private Scores finalScore;
    private int questionNum;
    public Question selectedQuestion;
    private GUI window;
    private Connection conn; //database
    private Statement state;
    
    //conditions for the game state
    private boolean walkedAway;
    private boolean lost;
    private boolean won;
    
    public static String url = "jdbc:derby:ScoresDB; create=true";
    public static String username = "pdc";
    public static String password = "pdc"; 
    
    //constructor
    public Game()
    {
        conn = null;
        state = null;
        
        connToDB();
        questions = questionsList();
        
        currentLevel = 0;
        levelProgress = 1;
        random = new Random();
        playing = true;
        
        prizeAmount = 0; //prize amount goes higher each time the player gets a question right
        prize = new String[]{"$100","$200","$300","$500","$1,000","$2,000", "$4,000", "$8,000","16,000","32,000","64,000","125,000","250,000","$500,000","$1,000,000"};
        
        //lifelines
        AskTheAudience = new AskTheAudience();
        PhoneAFriend = new PhoneAFriend();
        FiftyFifty = new FiftyFifty();
        
        //set to false at start
        walkedAway = false;
        won = false;
        lost = false;
        
        getScores();
        
        window = new GUI();
        window.setVisible(true);
        window.setGame(this);
        
        displayScores();
    }
    
    public void resetGame()
    {
        currentLevel = 0; //restart level and progress
        questionNum = 0;
        levelProgress = 1;
        playing = true;
        prizeAmount = 0;
        
        //lifelines
        AskTheAudience = new AskTheAudience();
        PhoneAFriend = new PhoneAFriend();
        FiftyFifty = new FiftyFifty();
        
        //set to false at start
        walkedAway = false;
        won = false;
        lost = false;
        
        DefaultTableModel dtm = (DefaultTableModel) window.jTable1.getModel();
        dtm.setRowCount(0);
        dtm.setColumnCount(0);
        
        for(JButton btnLifeline : window.btnLifelines)
        {
            btnLifeline.setEnabled(true);
        }
        
        scores = new ArrayList<Scores>();
        connToDB();
        getScores();
        
        questions = new ArrayList<ArrayList>();
        questions = questionsList();
        
        displayScores();
        
        play();
    }

    //getters and setters
    public int getLevel()
    {
        return currentLevel;
    }
    
    public void setLevel(int level)
    {
        currentLevel = level;
    }
    
    public boolean getPlaying()
    {
        return playing;
    }
    
    public void play()
    {
        askQuestion();
    }
    
    public void setPlaying(boolean play)
    {
        playing = play;
    }
    
    /*
    questions selected from current level
    displays the question and gets player answer
    method is repeated until end condition reached
    */
    public void askQuestion()
    {
        //select random question from current level
        if(questions.get(currentLevel).size() > 1)
        {
            questionNum = random.nextInt(questions.get(currentLevel).size());
        }
        else
        {
            questionNum = 0;
            safeHaven();
        }
        
        selectedQuestion = (Question) questions.get(currentLevel).get(questionNum);
        
        window.setQuestionText(selectedQuestion.getQuestions());
        
        for(int i = 0; i < window.qButtons.size(); i++)
        {
            window.qButtons.get(i).setText((i+1) + ") " + selectedQuestion.getAnswers()[i]);
        }
        
        window.setPrize("For " + prize[prizeAmount]);
    }
    
    /*
    asks a specific question to the player
    */
    public void askSpecificQuestion(int specificQNum)
    {
        selectedQuestion = (Question) questions.get(currentLevel).get(specificQNum);
        
        window.setQuestionText(selectedQuestion.getQuestions());
        
        for (int i = 0; i < window.qButtons.size(); i++)
        {
            window.qButtons.get(i).setText((i+1) + ") " + selectedQuestion.getAnswers()[i]);
        }
    }
    
    /*
    check user answer against correct answer
    if answered correctly move to the next level and the question is removed from list to avoid asking again
    if player is wrong set lost condition and call end() method
    */
    public void checkAnswer(int userAnswer)
    {
        if (userAnswer == selectedQuestion.getCorrectAnswer())
        {
            safeHaven();
            questions.get(currentLevel).remove(questionNum);
            
            askQuestion();
        }
        else
        {
            lost = true;
            end();
        }
    }

    //handling the usage of different lifelines during the game
    public void useLifeLine(char lifeline)
    {
        switch(lifeline)
        {
            case 'A':
                AskTheAudience.use(selectedQuestion);
                break;
            case 'F':
                FiftyFifty.use(selectedQuestion);
                askSpecificQuestion(questionNum);
                break;
            case 'P':
                PhoneAFriend.use(selectedQuestion);
                break;
            case 'W':
                String optionString = "Are you sure you want to walk away?";
                int reply = JOptionPane.showConfirmDialog(window, optionString, "Confirm walk away?", JOptionPane.YES_NO_OPTION);
                if(reply == JOptionPane.YES_OPTION)
                {
                    walkedAway = true;
                    end();
                }
                break;
            default:
                break;
        }
    }
    
    /*
    There are 2 checkpoints in the game
    If players get question 5 wrong, they leave with nothing. If they get it right, players are guaranteed $1,000 even
    if they answer incorrectly before reaching the next safe haven at Question 10, which is $32,000.
    */
    private void safeHaven() //reach checkpoint or level up
    {
        if(levelProgress < 5) //user cannot progress to next level
        {
            levelProgress++;
            prizeAmount++; //increment prize amount player is currently on
        }
        else if(levelProgress == 5 && prizeAmount < 14)
        {
            System.out.println("CHECKPOINT REACHED!\n");
            currentLevel++; //move up a level
            levelProgress = 1; // reset progression of current level
            prizeAmount++; // increment prize num player is currently on
        }
        else if(prizeAmount == 14)
        {
            won = true;
            end();
        }
    }
   
    /*
    terminate the program
    calculate the player's score
    */
    private void end()
    {
        window.changeCard("card5");
        
        String winningAmount = "";
        String congrats = "";
        
        if(walkedAway)
        {
            if(prizeAmount != 0)
            {
                congrats = "Congratulations! You walked away with ";
                winningAmount = prize[prizeAmount-1];
            }
            else
            {
                winningAmount = "$0";
                congrats = "You quit before it even started. You walked away with "; 
            }
        }
        
        if (lost)
        {
            switch (currentLevel) 
            {
                case 0:
                    winningAmount = "$0";
                    congrats = "That is incorrect! You lose, unfortunately you walk away with ";
                    break;
                case 1:
                    winningAmount = prize[5];
                    congrats = "That is incorrect! You lose, but you still get to walk away with ";
                    break;
                case 2:
                    winningAmount = prize[10];
                    congrats = "That is incorrect! You lose, but you still get to walk away with ";
                    break;
                default:
                    break;
            }
        }
        
        if(won)
        {
            congrats = "CONGRATULATIONS! YOU ARE A MILLIONAIRE!!!";
        }
        
        congrats += winningAmount;
        window.EndMessage.setText(congrats);
        
        playing = false;
    }
    
    /*
    JDBC for Scores and Questions
    */
    
    
    private void getScores()
    {
        //initialize scores list
        scores = new ArrayList<Scores>();
        ResultSet rs = null;
        
        try
        {
            //execute SELECT query to fetch scores from the database
            rs = state.executeQuery("SELECT * FROM SCORES");
            
            //iterate through the result set and create Scores objects
            while(rs.next())
            {
                Scores s = new Scores(rs.getString("scorename"), rs.getInt("score"));
                scores.add(s);
            }
        }catch (SQLException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //sort scores in descending order
        Collections.sort(scores); //sort scores low to high
        Collections.reverse(scores); //reverse to display high to low
    }
    
    public void saveScore()
    {
          /*
        formula to calculate
        score = prizeAmount * 5 - amount of lifelines used * 3
        e.g. prizeAmount = 32,000 (index 10) - lets say we used 3 lifelines * 3 = 41 
        (assisted via chatGPT)
        */
        int score = (prizeAmount * 5) - (lifelinesUsed * 3);
        
        //prompt the user to enter their name for saving the score
        String saveName = JOptionPane.showInputDialog(window, "Enter your name to save your score:");
        
        if(saveName != null)
        {
            saveName = saveName.substring(0, Math.min(saveName.length(), 50));
            finalScore = new Scores(saveName, score);
            scores.add(finalScore);
            
            //sort scores in descending order
            Collections.sort(scores);
            Collections.reverse(scores);
            
            try
            {
                Scores s = (Scores) finalScore;
                ResultSet row = state.executeQuery("SELECT MAX(SCOREID) FROM PDC.SCORES");
                int scoreID = 1;
                
                if(row.next())
                {
                    scoreID = row.getInt(1) + 1;
                }
                
                state.executeUpdate("INSERT INTO PDC.SCORES (SCOREID, SCORENAME, SCORE) VALUES ("+ scoreID +",'" + s.getName() + "'," + s.getScore() + ")");
            } catch (SQLException ex)
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //display the scores in a table after player enters their name
            displayScores();
        }
    }
    
    public void displayScores()
    {
        //create arrays to store the data for the table columns
        Integer[] nums = new Integer[scores.size()];
        String[] names = new String[scores.size()];
        Integer[] scoresArr = new Integer[scores.size()];
        
        //get the table model and reset its column and row counts
        DefaultTableModel dtm = (DefaultTableModel) window.jTable1.getModel();
        dtm.setColumnCount(0);
        dtm.setRowCount(0);
        
        //populate the arrays with data from the scores list
        for(int i = 0; i < scores.size(); i++)
        {
            nums[i] = i+1;
            names[i] = scores.get(i).getName();
            scoresArr[i] = scores.get(i).getScore();
        }
        
        //add the arrays as columns to the table model
        dtm.addColumn("Place", nums);
        dtm.addColumn("Name", names);
        dtm.addColumn("Score", scoresArr);
    }
    
    //connect to the database
    private void connToDB()
    {
        //embedded driver
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        
        //assisted via ChatGPT
        try {
            Class<?> driverClass = Class.forName(driver);
            Object driverInstance = driverClass.getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(url, username, password);
            state = conn.createStatement();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | SQLException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //close the database connection
    public void closeDBConn()
    {
        try
        {
            //assisted via ChatGPT
            state.close();
            //DriverManager.getConnection("jdbc:derby:;shutdown=true");
            conn.close();
        } catch (SQLException ex)
        {
            
        }
    }
    
    /*
    Questions will be randomized in this method
    There are 3 levels that includes 10 questions randomizing each round
    */
    public ArrayList questionsList()
    {
        //create ArrayLists to store questions for each level
        ArrayList<Question> level0 = new ArrayList();
        ArrayList<Question> level1 = new ArrayList();
        ArrayList<Question> level2 = new ArrayList();
        
        ResultSet rs = null;
        
        //check if the connection and statement objects are not null
        if(conn != null && state != null)
        {
            try
            {
                //execute SELECT query to fetch questions from the database
                rs = state.executeQuery("SELECT * FROM QUESTIONS");
                
                while(rs.next())
                {
                    String[] answers = {rs.getString("ANS1"),rs.getString("ANS2"),rs.getString("ANS3"),rs.getString("ANS4")};
                    Question q = new Question(rs.getInt("QLEVEL"), rs.getString("QUESTION"), answers, rs.getInt("CORRANS"));
                    
                    switch (q.getLevel())
                    {
                        case 0:
                            level0.add(q);
                            break;
                        case 1:
                            level1.add(q);
                            break;
                        case 2:
                            level2.add(q);
                            break;
                        default:
                            break;
                    }
                }
            } catch (SQLException ex)
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                connToDB();
            }
        }
        else
        {
            //if the connection or statement is null, establish a new connection
            connToDB();
            
            if(conn != null && state != null)
            {
                questionsList();
            }
        }
        
        ArrayList lists = new ArrayList<>();
        lists.add(level0);
        lists.add(level1);
        lists.add(level2);
        
        return lists;
    }
}
    