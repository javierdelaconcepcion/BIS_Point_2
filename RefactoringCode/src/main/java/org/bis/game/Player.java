/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bis.game;

/**
 * Date: 23/05/2025 
 * @author javier.delaconcepcion 
 * Company: BIS
 * This class is to model a player
 * 
 */
public class Player {
    
    String Name = "";
    boolean HasPenalty = false;
    int Score = 0;
    int Val = 0;
    int Position = 0;
    
    
    public Player(String _name, boolean _hasPenalty, int _score, int _val, int _position){
        Name = _name;
        HasPenalty = _hasPenalty;
        Score = _score;
        Val = _val;
        Position = _position;
    }
    
}
