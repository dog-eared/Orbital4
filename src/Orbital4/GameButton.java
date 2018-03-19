/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Orbital4;


import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 *
 * @author emulrooney
 */
public class GameButton extends Button {

    private final int PIECE_SIZE = Orbital4.getPieceSize();
    private final int BUTTON_SIZE = Orbital4.getButtonSize();
    
    private static Board board;
    
    private boolean horizontal = false;
    private boolean negative = false;
    private int index; 
    
    private static Orbital4 o4;
    
    public GameButton(boolean horizontal, int index) {
        
        this.horizontal = horizontal;
        this.index = index;
        
        this.setOnAction(this::placePiece);
        
        if (horizontal) {
            this.setMinWidth(BUTTON_SIZE);
            this.setMaxWidth(BUTTON_SIZE);
            this.setMinHeight(PIECE_SIZE);
            this.setMaxHeight(PIECE_SIZE);
            
            
        } else {
            this.setMinWidth(PIECE_SIZE);
            this.setMaxWidth(PIECE_SIZE);
            this.setMinHeight(BUTTON_SIZE);
            this.setMaxHeight(BUTTON_SIZE);
        }
     
    }
    
    public void setNegative(boolean negative) {
        this.negative = negative;
    }
    
    private void placePiece(ActionEvent e) {
        
        int location = board.checkForValidMove(horizontal, negative, index);
        
        if (location != -1) {
            
            if (horizontal) {
                board.setPiece(location, index); //change 1 for owner when we have that figured out
                System.out.println("Location:" + location + " and index:" + index);
                System.out.println(o4);
                o4.drawPiece(location, index);
            } else {
                board.setPiece(index, location);
                o4.drawPiece(index, location);
            }
            
        } else {
            System.out.println("Can't place a piece.");
        }
        
        
    }

    @Override
    public String toString() {
        return "GameButton{" + "horizontal=" + horizontal + ", negative=" + negative + ", index=" + index + '}';
    }
    
    public void setReferences(Orbital4 o4, Board board) {
        this.board = board;
        this.o4 = o4;
        
    }
    
    
}
