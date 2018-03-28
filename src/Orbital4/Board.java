package Orbital4;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Board class. Acts as model for view contained in Orbital4, and is manipulated
 * by input from GUI buttons and GameButtons in Orbital4.
 *
 * Contains a grid of pieces with methods allowing the view to get relevant data
 * and for GameButtons to affect the state of the board. Also affects GameScores
 * object held by Orbital 4. Contains private methods to check for win
 * conditions, find valid moves and increment GameScore.
 *
 * @author Evan Mulrooney 000745477
 */
public class Board {
    
    /* Offset from top-left of window */
    private final static int BOARD_OFFSET = 60;
    /* Size for pieces */
    private final static int PIECE_SIZE = 48;

    /* Colors for board tiles */
    private final static Color BOARD_COLOR_1 = Color.LIGHTGRAY;
    private final static Color BOARD_COLOR_2 = Color.DARKGRAY;

    /* Board completed or not */
    private boolean gameWon = false;

    /* Board size */
    private final int WIDTH = 9;
    private final int HEIGHT = 9;
    
    /* Player checks for 4-in-a-row */
    // Blank for simple index, 4 in a row for players, X for neutral block
    private final String[] PLAYER_CHECKS = new String[] {" ", "1111", "2222", "X"}; 

    /* Disallows players placing pieces straight onto edges of the board */
    //Initially meant for debugging/experimenting, makes for a different game-type
    //when disabled. Might add a toggle for this.
    private final boolean NO_SOLID_WALLS = true;

    /* XY grid of board, contains pieces */
    private final Piece[][] grid;

    /* Current player */
    //Initialized at 8 to generate a neutral piece before allowing any other
    //pieces to be dropped
    private int currentPlayer = 3;

    /**
     * Constructor. Creates a new board with constants WIDTH and HEIGHT, then
     * places a neutral piece in the centre.
     */
    public Board() {
        grid = new Piece[WIDTH][HEIGHT];
        setPiece(WIDTH / 2, HEIGHT / 2);
    }
    
    /**
     * Draws board and neutral centre piece.
     * Loops through every space on grid and creates a grid of alternating
     * colours. Draws the neutral piece found at the centre of the board.
     * @param gc Graphics Context to draw on
     */
    public void draw(GraphicsContext gc) {

        //Shortened for readability
        int w = WIDTH;
        int h = HEIGHT;
        
        //Loop through and draw each space
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {

                if ((x + y) % 2 == 0) {
                    gc.setFill(BOARD_COLOR_1);
                } else {
                    gc.setFill(BOARD_COLOR_2);
                }

                gc.fillRect(BOARD_OFFSET + (x * PIECE_SIZE), BOARD_OFFSET + (y * PIECE_SIZE), PIECE_SIZE, PIECE_SIZE);
            }
        }

        //Make sure centre piece is drawn, otherwise players don't know where
        //to drop pieces.
        grid[w / 2][h / 2].draw(gc);

    }

    /**
     * Places a new piece belonging to currentPlayer at given x,y coordinates.
     * Then, checks for a win condition in all directions and awards a point if 
     * a winning line is found. Finally, flips the current player.
     *
     * Public so that GameButton can call this method.
     *
     * @param x horizontal placement
     * @param y vertical placement
     */
    public void setPiece(int x, int y) {

        //Only let players set pieces if no one has won yet.
        if (!gameWon) {
            grid[x][y] = new Piece(currentPlayer, x, y);

            //All four checks done separately to allow for possibility of a 
            //player scoring with two lines. Unlikely, but fun to allow one 
            //player to totally stomp on the other.
            //Horizontal
            if (checkStraightLine(true, y)) {
                gameWon();
            };

            //Vertical
            if (checkStraightLine(false, x)) {
                gameWon();
            }

            //Diagonal descending from top left 
            if (checkDiagonalDown(x, y)) {
                gameWon();
            }

            //Diagonal ascending from bottom left
            if (checkDiagonalUp(x, y)) {
                gameWon();
            }

            //Flip players
            flipCurrentPlayer();
        }
    }

    /**
     * Called when a win condition found on board. Sets gameWon to true and 
     * awards a point to the current player.
     */
    private void gameWon() {
        gameWon = true;
        GameScore.pointScored(currentPlayer);
    }

    /**
     * Toggles between player 1 and 2, or if placing a neutral piece, straight
     * to player 1.
     */
    private void flipCurrentPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    /**
     * * CHECKS FOR VALID MOVES/WIN CONDITIONS**
     */
    
    /**
     * Checks if a valid move exists in a straight line starting at the location
     * nearest the origin inferred from horizontal, negative and index.
     *
     * If horizontal, check from right/left, otherwise check up and down. If
     * negative, check up/left, otherwise check right/down. Index represents
     * which row/column to check.
     *
     * Public so that GameButton can call this method.
     *
     * @param horizontal - checking left/right or not
     * @param negative - checking up/left or not
     * @param index - row/column to check
     * @return
     */
    public int checkForValidMove(boolean horizontal, boolean negative, int index) {

        //Initialize as invalid 
        int validMoveLocation = -1;

        //Series of checks of both bools for four directions
        if (horizontal && !negative) {
            validMoveLocation = checkFromLeft(index);
        } else if (horizontal && negative) {
            validMoveLocation = checkFromRight(index);
        } else if (!horizontal && !negative) {
            validMoveLocation = checkFromTop(index);
        } else if (!horizontal && negative) {
            validMoveLocation = checkFromBottom(index);
        }

        //Check if player is trying to place something on the farthest square
        //from the button they clicked. If so, deny that option.
        if (NO_SOLID_WALLS) {
            if ((validMoveLocation == 0 && negative) || (validMoveLocation == 8 && !negative)) {
                return -1;
            }
        }

        return validMoveLocation;
    }

    /**
     * Checks if a location is empty and returns true or false.
     *
     * @param x horizontal position
     * @param y vertical position
     * @return space free or not
     */
    private boolean checkSpaceFree(int x, int y) {
        return (grid[x][y] == null);
    }

    /**
     * Checks straight line from far left of board to far right at vertical index 
     * given as parameter. If a free space is found, returns
     * that space.
     *
     * @param index
     * @return location of valid space from left
     */
    private int checkFromLeft(int index) {

        //initialize as no valid move
        int validMoveLocation = -1;
        //flag to stop checking if we've found the right place to stop
        boolean stillChecking = true;

        //Loop through every space horizontally from left to right
        for (int i = 0; i < WIDTH; i++) {
            if (checkSpaceFree(i, index) && stillChecking) {
                validMoveLocation = i;
            } else if (stillChecking) {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }

    /**
     * Checks straight line from far right of board to far left at vertical index
     * given as parameter. If a free space is found, returns that space.
     *
     * @param index
     * @return location of valid space from right
     */
    private int checkFromRight(int index) {

        //initialize as no valid move
        int validMoveLocation = -1;
        //flag to stop checking if we've found the right place to stop
        boolean stillChecking = true;

        //Loop through every space horizontally from right to left
        for (int i = (WIDTH - 1); i >= 0; i--) {
            if (checkSpaceFree(i, index) && stillChecking) {
                validMoveLocation = i;
            } else if (stillChecking) {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }

    /**
     * Checks straight line from top of board to bottom at the horizontal index
     * given as parameter. If a free space is found, returns that space.
     *
     * @param index horizontal index to check
     * @return location of valid space from right
     */
    private int checkFromTop(int index) {
        //initialize as no valid move
        int validMoveLocation = -1;
        //flag to stop checking if we've found the right place to stop
        boolean stillChecking = true;

        //Loop through every space from top to bottom
        for (int i = 0; i < HEIGHT; i++) {
            if (checkSpaceFree(index, i) && stillChecking) {
                validMoveLocation = i;
            } else if (stillChecking) {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }

    /**
     * Checks straight line from top of board to bottom at the horizontal index
     * given as parameter. If a free space is found, returns that space.
     *
     * @param index horizontal index to check
     * @return location of valid space from right
     */
    private int checkFromBottom(int index) {
        //initialize as no valid move
        int validMoveLocation = -1;
        //flag to stop checking if we've found the right place to stop
        boolean stillChecking = true;

        //loop through every space from bottom to top
        for (int i = HEIGHT - 1; i >= 0; i--) {
            if (checkSpaceFree(index, i) && stillChecking) {
                validMoveLocation = i;
            } else if (stillChecking) {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }

    /**
     * Checks for four in a row and returns the index of the beginning of the
     * line.
     *
     * @param horizontal If true, checks row. Otherwise, checks the column.
     * @param index Row/column to check
     * @return Start location of winning column/row found
     */
    private boolean checkStraightLine(boolean horizontal, int index) {

        String content = "";

        if (horizontal) {

            //Iterate from left to right
            for (int i = 0; i < WIDTH; i++) {
                if (grid[i][index] != null) {
                    content += grid[i][index].getPieceOwner();
                } else {
                    //Making sure the indexOf can't jump across a gap
                    content += "*";
                }
            }
        } else {

            //Iterate from top to bottom
            for (int i = 0; i < HEIGHT; i++) {
                if (grid[index][i] != null) {
                    content += grid[index][i].getPieceOwner();
                } else {
                    content += "*";
                }
            }
        }

        return content.contains(PLAYER_CHECKS[currentPlayer]);
    }

    /**
     * Checks for four in a row starting as far above and to the left from the
     * given x, y in a straight line and moving diagonally downwards.
     *
     * @param x horizontal position to start
     * @param y vertical position to start
     * @return Start location of winning diagonal found
     */
    private boolean checkDiagonalDown(int x, int y) {

        String content = "";

        //Move to far top left
        if (x > y) {
            x -= y;
            y = 0;
        } else {
            y -= x;
            x = 0;
        }

        //Iterate through diagonally downwards
        while ((x < WIDTH) && (y < HEIGHT)) {
            if (grid[x][y] != null) {
                content += grid[x][y].getPieceOwner();
            } else {
                content += "*";
            }
            x++;
            y++;
        }

        return content.contains(PLAYER_CHECKS[currentPlayer]);
    }

    /**
     * Checks for four in a row starting as far below and to the left from the
     * given x, y in a straight line and moving diagonally upwards.
     *
     * @param x horizontal position to start
     * @param y vertical position to start
     * @return Start location of winning diagonal found
     */
    private boolean checkDiagonalUp(int x, int y) {
        String content = "";

        //Start check as far down-left as possible
        while (x > 0 && y < HEIGHT - 1) {
            x--;
            y++;
        }

        //Loop through every square from bottom left to top right
        while ((x < WIDTH) && (y >= 0)) {
            if (grid[x][y] != null) {
                content += grid[x][y].getPieceOwner();
            } else {
                content += "*";
            }
            x++;
            y--;
        }

        return content.contains(PLAYER_CHECKS[currentPlayer]);
    }

    /**
     * * GETTERS/SETTERS **
     */
    
    /**
     * Getter for board width. Used mostly to keep code readable.
     *
     * @return width of board as int
     */
    public int getWidth() {
        return WIDTH;
    }

    /**
     * Getter for board height. Used mostly to keep code readable.
     *
     * @return height of board as int
     */
    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Gets the piece located at x y on the board.
     *
     * @param x horizontal position
     * @param y vertical position
     * @return piece at given location
     */
    public Piece getPieceAt(int x, int y) {
        return grid[x][y];
    }

    /**
     * Getter for next player to place a piece.
     *
     * @return Number of current player
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Getter for board offset.
     * @return board offset in pixels
     */
    public static int getBoardOffset() {
        return BOARD_OFFSET;
    }

    /**
     * Getter for game won status.
     *
     * @return Game won or not
     */
    public boolean getGameWon() {
        return gameWon;
    }

}
