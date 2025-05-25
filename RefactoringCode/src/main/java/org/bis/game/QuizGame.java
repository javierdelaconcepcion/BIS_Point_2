/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.bis.game;

//region IMPORTS
import java.util.ArrayList;
import java.util.Random;
//endregion



public class QuizGame {
    
    //region VARIABLES
        
    final int questions = 50;  // Const for define the number of questions
    
    // If you want to add a new category, you need to add the string here.
    String[] categoryList = {"Literature","Music","History","Technology"};
    
    // Control the active player. Setting to 0 by default
    static int activePlayer = 0;
    boolean isExitingPenaltyBox;   
    
    // Define the list of players
    // If a new playes wants to be added just add it at the end of the string.
    static String[] listOfPlayers = {"Alex","John","Mary"};
    // Save the players
    static ArrayList<Player> palyers = new ArrayList<Player>();
    static ArrayList<Question> questionsList = new ArrayList<Question>();
    //endregion
    
    
    // region Constructor
    public QuizGame( String[] _listOfPlayers){
        
        // Creating the list of questions
        creatingQuestions();
        
        // Creating the list of players based on listOfPlayers list
        // This is done because positions[] and scores[] just have 6 slots available so if players are more than 6 this needs to be updated.
        // In this way Players, scores & positions are flexible based on the String[] listOfPlayers that we have defined.
        // This can be also pased to a parameter to the main method.
        for (String _player : _listOfPlayers) {
            palyers.add(new Player(_player,false,0,0, palyers.size() + 1));
            
          }
    }
    
    // endregion
    
    
    // region Main 
    
    public static void main(String[] args) {
        
        boolean gameRunning; // Using this to control the iterator
        Random randomizer = new Random();
        
        try
        {
            // Creating the QuizGame object with the list of players
            QuizGame gameInstance = new QuizGame(listOfPlayers);

            // Log to see the number of partipants

            for (Player _player : palyers) {
                LogMessage("Participant: "+ _player.Name + " has joined the game at the position: " + _player.Position);
            }

            LogMessage("Total players: "+ palyers.size());

            do {

                // Checking if requisites before any interaction are fulfilled before start the game
                if(checkRequisites()){
                    // Simulate a rollDice
                    // Setting nextInt(5) + 1 to avoid 0.
                    gameInstance.rollDice(randomizer.nextInt(5) + 1); 

                    if (randomizer.nextInt(8) == 6) {
                        gameRunning = gameInstance.answeredIncorrectly();
                    } else {
                        gameRunning = gameInstance.answeredCorrectly();
                    }

                    // Increasing the active player on each iteration
                    activePlayer++;
                }
                else{
                    gameRunning = false;
                }

            } while (gameRunning);
        }
        catch(Exception e) {
            LogMessage("Error during the game: "+ e.getMessage());
        }
        
        
    }

    // endregion
    
    
    // region Methods
    
    /*
      Simulate a DiceRoll
      Receive the value as a parameter
    */
    private void rollDice(int diceRoll) {
        
        Player tmpPlayer = palyers.get(activePlayer);
        
        LogMessage(tmpPlayer.Name + " is currently playing");
        LogMessage(tmpPlayer.Name + " rolled a " + diceRoll);
        
        if (palyers.get(activePlayer).HasPenalty) {
            if (diceRoll % 2 != 0) {
                
                palyers.get(activePlayer).HasPenalty = false;
                isExitingPenaltyBox = true;
                
                LogMessage(tmpPlayer.Name + " is leaving the penalty box");
               
                // Increment the value for this player              
                addingDiceValue(activePlayer,diceRoll);
            } else {
                LogMessage(tmpPlayer.Name + " is staying in the penalty box");
                isExitingPenaltyBox = false;
            }
        } else {
            // Increment the value for this player              
            addingDiceValue(activePlayer,diceRoll);
        }
    }
    
    
    
    /*
      Method to add the dice Roll value into the active player
    */
    private void addingDiceValue(int _activePlayer, int _diceRoll) {
        String _category = "";
        int _val = 0;
        
        try {
            
            palyers.get(activePlayer).Val = ( (palyers.get(activePlayer).Val + _diceRoll) > 11 ) ? ( palyers.get(activePlayer).Val + _diceRoll - 12 ) : palyers.get(activePlayer).Val + _diceRoll;
            LogMessage(palyers.get(activePlayer).Name + "'s new position is " + palyers.get(activePlayer).Val);

            _val = palyers.get(activePlayer).Val;
            
            // Adding "categoryList.length" to calculate the category based on the number of categories.
            _category = categoryList[(_val % categoryList.length)];

            LogMessage("The current category is " + _category);       
            askQuestion(_category);
            
          }
          catch(Exception e) {
            throw e;
          }
    }
    

    /*
      Populate the list of questions 
    */
    private void creatingQuestions() {
        
        // Adding questions on the ArrayLists
        for (int i = 0; i < questions; i++) {
            for (String _category : categoryList) {
                questionsList.addLast(new Question(_category + " Question " + i,_category));
            }
        }
    }
    
    /*
      Retrieve true if there are questions on the list for the input category.    
    */
    private static boolean existsQuestions( String _category){
        return (questionsList.stream().filter(question -> _category.equals(question.Category)).count() > 0) ? true: false;
    }
    

    /*
      List of required checks to keep running the game.
      Return true if requisites to keep running are match, false in case execution needs to be finished
    */
    private static boolean checkRequisites(){
        
        // Checking number of players
        if(palyers.size() < 2){
            LogMessage("It is required more than 1 player to play. Nº of players: " + palyers.size());
            return false;
        }

        //Checking is there are questions available on the list
        if( !(existsQuestions("Literature") && existsQuestions("Music") && existsQuestions("History") && existsQuestions("Technology"))  ){
            LogMessage("There are no questions available to keep playing the game");
            return false;
        }
        
        // Set the active player to 0 because the round has finished
        if (activePlayer == palyers.size()){
            activePlayer = 0;
        }
        
        return true;
    }
    
    /*
      Log the input string on the Console
    */
    private static void LogMessage(String _meessage){
        System.out.println(_meessage);
    }
    
   
    /*
    Show on the Console the categoryName and remove one question from the list
    
    */
    private void askQuestion(String _category) {

      try {
        Question tmpQuestion = questionsList.stream().filter(question -> _category.equals(question.Category)).findFirst().get();
        LogMessage(tmpQuestion.Text);     
        questionsList.remove(tmpQuestion);
      }
      catch(Exception e) {
        throw e;
      }

      
    }
    
    /*  
        Player answered incorrectly
        Set a penalty to the player and move to the next one.
    */
    private boolean answeredIncorrectly(){
        
        try {
            
            LogMessage("Incorrect answer");
            LogMessage(palyers.get(activePlayer).Name + " has been sent to the penalty box");
            palyers.get(activePlayer).HasPenalty = true;
        
          }
          catch(Exception e) {
            throw e;
          }
        
        return true;
    }
    
    
    /*  
        Player answered correctly
        Increasing the score for this player and moving to the next one
        Return true if game needs to continue, false when score is 6 and game it is finished.
    */
    private boolean answeredCorrectly() {       
        if (palyers.get(activePlayer).HasPenalty && !isExitingPenaltyBox ){
            return true;
        } else {          
            return manageAnswer(activePlayer);          
        }
    }
    
    /*
        Increasing the score for the active player and log into the Console the outputmessages
        Return true if the player score is 6 and the game can be finished
    */
    private boolean manageAnswer(int _activePlayer)
    {      
        try {
            
            LogMessage("Correct answer!");
            palyers.get(_activePlayer).Score++;
            LogMessage(palyers.get(activePlayer).Name + " now has " + palyers.get(activePlayer).Score + " points.");
            
          }
          catch(Exception e) {
            throw e;
          }

        return !(palyers.get(activePlayer).Score == 6);
    }
    
    //endregion
    
   
}
