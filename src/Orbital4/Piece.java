package Orbital4;

/**
 * Piece. Represents a game piece on the board. 
 * 
 * A Piece has an x and y location and is stored in the grid belonging to the
 * board in use. A piece has an owner, which is used to find winning combinations
 * and determine the colour for the piece.
 * 
 * Piece contains a constructor method and getters for the owner, x and y.
 * 
 * @author Evan Mulrooney 000745477
 */
public class Piece {
    
    /* Horizontal location */
    private final int x;
    /* Vertical location */
    private final int y;
    /* Player who placed this piece */
    private int owner;
    
    /**
     * Constructor. Takes a value for the owner and an x y location.
     * @param owner Player who placed this piece
     * @param x horizontal placement
     * @param y vertical placement
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
    
    /**
     * Getter for horizontal position.
     * @return horizontal spacing
     */
    public int getX() {
        return x;
    }
    
    /**
     * Getter for vertical position.
     * @return vertical spacing
     */
    public int getY() {
        return y;
    }
        
}
