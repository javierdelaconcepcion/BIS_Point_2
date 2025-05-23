/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.bis.game;

//region IMPORTS
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
//endregion



public class QuizGame {
    
    //region VARIABLES
    
    int[] positions = new int[6]; //Determine the category of the question based on the value
    int[] scores = new int[6];    //Save the score for each player
    boolean[] penaltyBoxStatus = new boolean[6]; // Save if a player has a penalty
    
    final int questions = 50;  // Const for define the number of questions
    
    // List of categories for the game
    static LinkedList LiteratureQuestions = new LinkedList();
    static LinkedList MusicQuestions = new LinkedList();
    static LinkedList HistoryQuestions = new LinkedList();
    static LinkedList TechnologyQuestions = new LinkedList();
    
    
    // Control the active player. Setting to 0 by default
    int activePlayer = 0;
    boolean isExitingPenaltyBox;   
    
    // Define the list of players
    static String[] listOfPlayers = {"Alex","John","Mary"};
    // Save the players
    static ArrayList<Player> palyers = new ArrayList<Player>();
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
        
        // Creating the QuizGame object with the list of players
        QuizGame gameInstance = new QuizGame(listOfPlayers);
        
        // Log to see the number of partipants
        
        for (Player _player : palyers) {
            System.out.println("Participant: "+ _player.Name + " has joined the game at the position: " + _player.Position);
        }
        
        System.out.println("Total players: "+ palyers.size());
        
        // Checking if requisites are fulfilled before start the game
        if(checkRequisites()){
            
            do {
                // Simulate a rollDice
                // Setting nextInt(5) + 1 to avoid 0.
                gameInstance.rollDice(randomizer.nextInt(5) + 1); 

                if (randomizer.nextInt(8) == 6) {
                    gameRunning = gameInstance.answeredIncorrectly();
                } else {
                    gameRunning = gameInstance.answeredCorrectly();
                }

            } while (gameRunning);

        }
        
        
    }

    // endregion
    
    
    // region Methods
    
    /*
      Simulate a DiceRoll
      Receive the value as a parameter
    */
    private void rollDice(int diceRoll) {
        
        System.out.println(palyers.get(activePlayer).Name + " is currently playing");
        System.out.println(palyers.get(activePlayer).Name + " rolled a " + diceRoll);
        
        if (palyers.get(activePlayer).HasPenalty) {
            if (diceRoll % 2 != 0) {
                isExitingPenaltyBox = true;
                
                System.out.println(palyers.get(activePlayer) + " is leaving the penalty box");
                positions[activePlayer] = positions[activePlayer] + diceRoll;
                if (positions[activePlayer] > 11) positions[activePlayer] = positions[activePlayer] - 12;
                
                System.out.println(palyers.get(activePlayer) 
                        + "'s new position is " 
                        + positions[activePlayer]);
                System.out.println("The current category is " + determineCategory());
                askQuestion();
            } else {
                System.out.println(palyers.get(activePlayer) + " is staying in the penalty box");
                isExitingPenaltyBox = false;
            }
        } else {
            positions[activePlayer] = positions[activePlayer] + diceRoll;
            if (positions[activePlayer] > 11) positions[activePlayer] = positions[activePlayer] - 12;
            
            System.out.println(palyers.get(activePlayer) + "'s new position is " 
                    + positions[activePlayer]);
            System.out.println("The current category is " + determineCategory());
            askQuestion();
        }
    }
    
    
    
    
    
    
    
    /*
        Populate the list of questions
    */
    private void creatingQuestions() {
        
        // Adding questions on the ArrayLists
        for (int i = 0; i < questions; i++) {
            LiteratureQuestions.addLast("Literature Question " + i);
            MusicQuestions.addLast("Music Question " + i);
            HistoryQuestions.addLast("History Question " + i);
            TechnologyQuestions.addLast("Technology Question " + i);
        }
    }
    
    /*
      List of required checks to keep running the game.
    */
    private static boolean checkRequisites(){
        
        // Checking number of players
        if(palyers.size() < 2){
            System.out.println("It is required more than 1 player to play. Nº of players: " + palyers.size());
            return false;
        }

        //Checking is there are questions available on the list
        if( LiteratureQuestions.isEmpty() || MusicQuestions.isEmpty() || HistoryQuestions.isEmpty() || TechnologyQuestions.isEmpty()  ){
            System.out.println("There are no questions available to keep playing the game");
            return false;
        }
        
        return true;
    }
    
    
    
    
    
    
    //endregion
    
    
    
  

    

    private void askQuestion() {
        if (determineCategory() == "Literature")
            System.out.println(LiteratureQuestions.removeFirst());
        if (determineCategory() == "Music")
            System.out.println(MusicQuestions.removeFirst());
        if (determineCategory() == "History")
            System.out.println(HistoryQuestions.removeFirst());
        if (determineCategory() == "Technology")
            System.out.println(TechnologyQuestions.removeFirst());
    }
    
    
    private String determineCategory() {
        if (positions[activePlayer] == 0) return "Literature";
        if (positions[activePlayer] == 4) return "Literature";
        if (positions[activePlayer] == 8) return "Literature";
        if (positions[activePlayer] == 1) return "Music";
        if (positions[activePlayer] == 5) return "Music";
        if (positions[activePlayer] == 9) return "Music";
        if (positions[activePlayer] == 2) return "History";
        if (positions[activePlayer] == 6) return "History";
        if (positions[activePlayer] == 10) return "History";
        return "Technology";
    }

    public boolean answeredCorrectly() {
        if (penaltyBoxStatus[activePlayer]){
            if (isExitingPenaltyBox) {
                System.out.println("Correct answer!");
                scores[activePlayer]++;
                System.out.println(palyers.get(activePlayer) 
                        + " now has "
                        + scores[activePlayer]
                        + " points.");
                
                boolean gameWon = checkGameStatus();
                activePlayer++;
                if (activePlayer == palyers.size()) activePlayer = 0;
                
                return gameWon;
            } else {
                activePlayer++;
                if (activePlayer == palyers.size()) activePlayer = 0;
                return true;
            }
        } else {
            System.out.println("Correct answer!");
            scores[activePlayer]++;
            System.out.println(palyers.get(activePlayer) 
                    + " now has "
                    + scores[activePlayer]
                    + " points.");
            
            boolean gameWon = checkGameStatus();
            activePlayer++;
            if (activePlayer == palyers.size()) activePlayer = 0;
            
            return gameWon;
        }
    }
    
    public boolean answeredIncorrectly(){
        System.out.println("Incorrect answer");
        System.out.println(palyers.get(activePlayer) + " has been sent to the penalty box");
        penaltyBoxStatus[activePlayer] = true;
        
        activePlayer++;
        if (activePlayer == palyers.size()) activePlayer = 0;
        return true;
    }

    private boolean checkGameStatus() {
        return !(scores[activePlayer] == 6);
    }
}
