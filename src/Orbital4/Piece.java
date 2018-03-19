package Orbital4;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Piece. Represents a game piece on the board. 
 * 
 * A Piece has an x and y location and is stored in the grid belonging to the
 * board in use. A piece has an owner, which is used to find winning combinations
 * and determine the colour for the piece.
 * 
 * Piece contains a constructor method and a getter for the owner.
 * 
 * @author emulrooney
 */
public class Piece {
    
    /* Horizontal location */
    private final int x;
    /* Vertical location */
    private final int y;
    
    /* Size of piece. */
    private static final int size = Orbital4.getPieceSize();
    
    /* Distance to draw from absolute top-left of window */
    private static int offset = Orbital4.getOffset();
    
    /* Player who placed this piece */
    private int owner;
    
    /**
     * Constructor. Takes a value for the owner and an x y location.
     * @param owner
     * @param x
     * @param y 
     */
    public Piece(int owner, int x, int y) {
        this.owner = owner;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Getter for piece owner. Used in finding winning combinations.
     * @return owner number
     */
    public int getPieceOwner() {
        return owner;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
        
}
