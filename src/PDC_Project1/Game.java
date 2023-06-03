/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package PDC_Project1;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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
    FileSaveScores scoreData = new FileSaveScores(); 
    private int questionNum;
    public Question selectedQuestion;
    private GUI window;
    
    //conditions for the game state
    private boolean walkedAway;
    private boolean lost;
    private boolean won;
    Scanner scan;
    
    //constructor
    public Game()
    {
        questions = questionsList();
        
        currentLevel = 0;
        levelProgress = 1;
        random = new Random();
        scan = new Scanner(System.in);
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
 
        //load the scoreboard
        //scoreData.getScores();
        
        window = new GUI();
        window.setVisible(true);
        window.setGame(this);
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
        
    }

    //getters
    public boolean isPlaying() 
    {
        return playing;
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
            window.qButtons.get(i).setText((i+1) + ") " + selectedQuestion.getAnswers());
        }
    }
    
    /*
    check user answer against correct answer
    if user gets it right they progress to the next level
    the question is removed from the list so it can't be asked again
    if wrong, end program
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
                int reply = JOptionPane.showConfirmDialog(window, optionString, "Still want to walk away?", JOptionPane.YES_NO_OPTION);
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
    check user answer against correct answer
    if answered correctly move to the next level and the question is removed from list to avoid asking again
    if player is wrong set lost condition and call end() method
    */
    public void checkAnswer(int playerAnswer)
    {
        if(playerAnswer == selectedQuestion.getCorrectAnswer()) //answer is correct
        {
            safeHaven(); //checkpoint reached
            questions.get(currentLevel).remove(questionNum); //remove used question from list
        }
        else //answer is incorrect
        {
            lost = true;
            end();
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
    calulcates the player's score
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
                congrats = "You walked away with $0. You quit before it even started."; 
            }
        }
        
        if (lost)
        {
            switch (currentLevel) 
            {
                case 0:
                    winningAmount = "$0";
                    congrats = "That is incorrect! You lose, unfortunately you walk away with $0";
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
        
        
        /*
        formula to calculate
        score = prizeAmount * 5 - amount of lifelines used * 3
        e.g. prizeAmount = 32,000 (index 10) - lets say we used 3 lifelines * 3 = 41 
        (assisted via chatGPT)
        */
        int score = (prizeAmount * 5) - (lifelinesUsed * 3);
     
        System.out.println("\nYour score is " + score);
        String saveName;
        System.out.println("Enter your name to save your score, or enter 0 to quit without saving!");
        
        //player enters their name
        saveName = scan.next();
        
        /*
        if user wants to save score
        */
        
        if(saveName.charAt(0) != '0') //saves the score if user inputs 0
        {
            scoreData.finalScore = new Scores(saveName, score);
            scoreData.saveScore();
        }
        playing = false;
    }
    
    /*
    Questions will be randomized in this method
    There are 3 levels that includes 10 questions randomizing each round
    */
    public ArrayList questionsList()
    {
        ArrayList<Question> level0 = new ArrayList();
        ArrayList<Question> level1 = new ArrayList();
        ArrayList<Question> level2 = new ArrayList();
        
        Question q0 = new Question(0, "What sort of animal is Walt Disney's Dumbo?", new String[]{"Deer","Rabbit","Elephant","Donkey"}, 3);
        Question q1 = new Question(0, "A Magnet would most likely attract which of the following?", new String[]{"Metal","Plastic","Wood","The wrong man"},1);
        Question q2 = new Question(0, "Which of these names is NOT in the title of a Shakespeare play?", new String[]{"Hamlet","Romeo","Macbeth","Darren"}, 4);
        Question q3 = new Question(0, "Where did Scotch whisky originate?", new String[]{"Ireland","Wales","The United States","Scotland"}, 4);
        Question q4 = new Question(0, "In fancy hotels, it is traditional for what tantalizing treat to be left on your pillow?", new String[]{"A pretzel","An Apple","A mint","A photo of Wolf Blitzer"},3);
        Question q5 = new Question(0, "Which of these pairs of apps offers roughly the same type of service?", new String[]{"Snapchat and Grubhub","Whatsapp and SHAREit","TikTok and Spotify","Lyft and Uber"}, 4);
        Question q6 = new Question(0, "In which of these films does Whoopi Goldberg dress up as a nun?", new String[]{"Sister Act","Ghost","The Color Purple","How Judas Got His Groove Back"}, 1);
        Question q7 = new Question(0, "A geologist would likely be LEAST helpful for answering questions about which of the following?", new String[]{"Granite Boulders","Precious Stones","Igneous rocks","Fruity Pebbles"}, 4);
        Question q8 = new Question(0, "In history books, leaders named Alexander and Catherine both share what flattering title?", new String[]{"The Ferocious","The Great","The Unruly","The Eco-Conscious"}, 2);
        Question q9 = new Question(0, "What notable part of our nation's topography accounts for roughly 20 percent of the fresh water on Earth?", new String[]{"Death Valley","Grand Canyon","The Great Lakes","Mark Zuckerburg's hot tub"}, 3);
         
        //adding the questions to its assigned level, in this case level 1 (index 0)
        level0.add(q0);
        level0.add(q1);
        level0.add(q2);
        level0.add(q3);
        level0.add(q4);
        level0.add(q5);
        level0.add(q6);
        level0.add(q7);
        level0.add(q8);
        level0.add(q9);
        
        Question q10 = new Question(1, "What word can be put in front of the words 'track', 'way', and 'horse' to make three other words?", new String[]{"Road","Race","Cross","Sound"}, 2);
        Question q11 = new Question(1, "How many X's are there on a regular clock face with Roman numerals?", new String[]{"3","2","4","5"}, 3);
        Question q12 = new Question(1, "What name is given to the revolving belt machinery in an airport that delivers checked luggage from the plane to baggage reclaim?", new String[]{"Hangar","Terminal","Concourse","Carousel"}, 4);
        Question q13 = new Question(1, "Obstetrics is a branch of medicine particulary concerned with what?", new String[]{"Childbirth","Broken bones","Heart Conditions","Old age"}, 1);
        Question q14 = new Question(1, "Which of these religious observances lasts for the shortest period of time during the calendar year?", new String[]{"Ramadan","Diwali","Lent","Hanukkah"}, 2);
        Question q15 = new Question(1, "What does the word loquacious mean?", new String[]{"Angry","Chatty","Beautiful","Shy"}, 2);
        Question q16 = new Question(1, "What was the only painting sold by Vincent van Gogh during his lifetime?", new String[]{"Sunflowers","The Starry Night","The Red Vineyard","The Yellow House"}, 3);
        Question q17 = new Question(1, "What member of the big cat family cannot retract its claws?", new String[]{"Cheetah","Wild Cat","Lion","Tiger"}, 1);
        Question q18 = new Question(1, "Canberra is the capital city of which country?", new String[]{"Australia","Philippines","New Zealand","Vietnam"}, 1);
        Question q19 = new Question(1, "The word \"aristocracy\" literally means power in the hands of whom?", new String[]{"The few","The best","The barons","The rich"}, 2);
        
        //adding the questions to its assigned level, in this case level 2 (index 1)
        level1.add(q10);
        level1.add(q11);
        level1.add(q12);
        level1.add(q13);
        level1.add(q14);
        level1.add(q15);
        level1.add(q16);
        level1.add(q17);
        level1.add(q18);
        level1.add(q19);
        
        Question q20 = new Question(2, "The Earth is approximately how many miles away from the Sun", new String[]{"9.3 million","39 million","93 million","193 million"}, 3);
        Question q21 = new Question(2, "Which insect shorted out an early supercomputer and inspired the term \"computer bug\"?", new String[]{"Moth","Roach","Fly","Japanese Beetle"}, 1);
        Question q22 = new Question(2, "Which of the following men does not have a chemical element named for him?", new String[]{"Albert Einstein","Niels Bohr","Isaac Newton","Enrico Fermi"}, 3);
        Question q23 = new Question(2, "In the children's book series, where is Paddington Bear originally from?", new String[]{"India","Peru","Canada","Iceland"}, 2);
        Question q24 = new Question(2, "Now used to refer to a cat, the word \"tabby\" is derived from the name of a district of what world capital?", new String[]{"Baghdad","New Delhi","Cairo","Moscow"}, 1);
        Question q25 = new Question(2, "Neurologists believe that the brain's medial ventral prefrontal cortex is activated when you do what?", new String[]{"Have a panic attack","Remember a name","Get a joke","Listen to music"}, 3);
        Question q26 = new Question(2, "If you planted the seeds of Quercus robur, what would grow?", new String[]{"Trees","Flowers","Vegetables","Grain"}, 1);
        Question q27 = new Question(2, "Which of the following landlocked countries is entirely contained within another country?", new String[]{"Lesotho","Burkina Faso","Mongolia","Luxembourg"}, 1);
        Question q28 = new Question(2, "During World War II, US soldiers used the first commercial aerosol cans to hold what?", new String[]{"Cleaning Fluid","Antiseptic","Shaving Cream","Insecticide"}, 4);
        Question q29 = new Question(2, "Who did artist Grant Wood use as the model for the farmer in his classic painting \"American Gothic\"?", new String[]{"Travelling Salesman","Local sheriff","His dentist","His butcher"}, 3);
        
        //adding the questions to its assigned level, in this case level 3 (index 2)
        level2.add(q20);
        level2.add(q21);
        level2.add(q22);
        level2.add(q23);
        level2.add(q24);
        level2.add(q25);
        level2.add(q26);
        level2.add(q27);
        level2.add(q28);
        level2.add(q29);
        
        //store 3 levels of questions
        ArrayList lists = new ArrayList<>();
        lists.add(level0);
        lists.add(level1);
        lists.add(level2);
        
        return lists;
    }
}
