/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.bis.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class QuizGame {
    ArrayList palyers = new ArrayList();
    int[] positions = new int[6];
    int[] scores = new int[6];
    boolean[] penaltyBoxStatus = new boolean[6];
    
    LinkedList LiteratureQuestions = new LinkedList();
    LinkedList MusicQuestions = new LinkedList();
    LinkedList HistoryQuestions = new LinkedList();
    LinkedList TechnologyQuestions = new LinkedList();
    
    int activePlayer = 0;
    boolean isExitingPenaltyBox;
    
    public QuizGame(){
        for (int i = 0; i < 50; i++) {
            LiteratureQuestions.addLast("Literature Question " + i);
            MusicQuestions.addLast("Music Question " + i);
            HistoryQuestions.addLast("History Question " + i);
            TechnologyQuestions.addLast(generateTechnologyQuestion(i));
        }
    }
    
    private static boolean gameRunning;
    public static void main(String[] args) {
        QuizGame gameInstance = new QuizGame();
        
        gameInstance.addPlayer("Alex");
        gameInstance.addPlayer("John");
        gameInstance.addPlayer("Mary");
        
        Random randomizer = new Random();
    
        do {
            gameInstance.rollDice(randomizer.nextInt(5) + 1);
            
            if (randomizer.nextInt(8) == 6) {
                gameRunning = gameInstance.answeredIncorrectly();
            } else {
                gameRunning = gameInstance.answeredCorrectly();
            }
            
        } while (gameRunning);
    }

    public String generateTechnologyQuestion(int index){
        return "Technology Question " + index;
    }
    
    public boolean isReadyToStart() {
        return (totalPlayers() >= 2);
    }

    public boolean addPlayer(String player) {
        palyers.add(player);
        positions[totalPlayers()] = 0;
        scores[totalPlayers()] = 0;
        penaltyBoxStatus[totalPlayers()] = false;
        
        System.out.println(player + " has joined the game");
        System.out.println("Participant number " + palyers.size());
        return true;
    }
    
    public int totalPlayers() {
        return palyers.size();
    }

    public void rollDice(int diceRoll) {
        System.out.println(palyers.get(activePlayer) + " is currently playing");
        System.out.println(palyers.get(activePlayer) + " rolled a " + diceRoll);
        
        if (penaltyBoxStatus[activePlayer]) {
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
