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
 * Piece contains a constructor method and getters for the owner, x and y. Also
 * has setters for static variables boardOffset and pieceSize.
 * 
 * @author Evan Mulrooney 000745477
 */
public class Piece {
    
    /* Colors for players */
    private final static Color P1_COLOR = Color.DARKRED;
    private final static Color P2_COLOR = Color.NAVY;

    /* Piece outlines */
    private final static Color BORDER_SHADING = new Color(0, 0, 0, 0.25);
    
    /* Offset from top-left of window */
    private static int boardOffset;
    /* Size for pieces */
    private static int pieceSize;
    
    /* Horizontal location */
    private final int x;
    /* Vertical location */
    private final int y;
    
    /* Player who placed this piece */
    private final int owner;
    
    
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
     * Draws the piece.
     * Sets the fill colour based on the piece found and then draws the piece.
     * 
     * Public to allow for GameButton to draw pieces.
     *
     * @param gc
     */
    public void draw(GraphicsContext gc) {

            switch (owner) {
                case 1:
                    //1st player
                    gc.setFill(P1_COLOR);
                    break;
                case 2:
                    //2nd player
                    gc.setFill(P2_COLOR);
                    break;
                case 3:
                    //Neutral piece
                    gc.setFill(Color.WHITE);
                    break;
                default:
                    //This should never come up, but if there's a piece with a bad owner,
                    //it should be visible.
                    System.err.println("Piece with bad owner drawn at " + x + ", " + y );
                    gc.setFill(Color.LIMEGREEN);
                    break;
            }
            
            //Actually draw the oval
            gc.fillOval(boardOffset + x * pieceSize, boardOffset + y * pieceSize, pieceSize, pieceSize);

            //Decorative outline
            gc.setStroke(BORDER_SHADING);
            gc.setLineWidth(2);
            gc.strokeOval(boardOffset + x * pieceSize, boardOffset + y * pieceSize,
                    pieceSize, pieceSize);
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
    
    /**
     * Getter for piece size.
     * @return piece size
     */
    public static int getPieceSize() {
        return pieceSize;
    }
    
    /**
     * Sets static value of piece size
     * @param size Pixel size for pieces
     */
    public static void setPieceSize(int size) {
        pieceSize = size;
    }   
    
    /**
     * Sets static value of board's offset from origin
     * @param offset Pixel offset from top left of window
     */
    public static void setBoardOffset(int offset) {
        boardOffset = offset;
    }
    
}
