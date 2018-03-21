package Orbital4;

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

    /* Board completed or not */
    private boolean gameWon = false;

    /* Board size */
    private final int WIDTH = 9;
    private final int HEIGHT = 9;

    /* Disallows players placing pieces straight onto edges of the board */
    //Initially meant for debugging/experimenting, makes for a different game-type
    //when disabled. Might add a toggle for this.
    private final boolean NO_SOLID_WALLS = true;

    /* XY grid of board, contains pieces */
    private final Piece[][] grid;

    /* Current player */
    //Initialized at 8 to generate a neutral piece before allowing any other
    //pieces to be dropped
    private int currentPlayer = 8;

    /* Static references to scores */
    private static GameScore scores;

    /**
     * Constructor. Creates a new board with constants WIDTH and HEIGHT, then
     * places a neutral piece in the centre.
     */
    public Board() {
        grid = new Piece[WIDTH][HEIGHT];
        setPiece(WIDTH / 2, HEIGHT / 2);
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
            if (checkStraightLine(true, y) != -1) {
                gameWon();
            };

            //Vertical
            if (checkStraightLine(false, x) != -1) {
                gameWon();
            }

            //Diagonal descending from top left 
            if (checkDiagonalDown(x, y) != -1) {
                gameWon();
            }

            //Diagonal ascending from bottom left
            if (checkDiagonalUp(x, y) != -1) {
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
        scores.pointScored(currentPlayer);
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
     * Returns a string of the current player's number repeated four times. Used
     * for finding 4-in-a-row on the board for a game win.
     *
     * @return player's number 4 times as a string
     */
    private String createPlayerCheck() {
        String playerCheck = "";

        for (int i = 0; i < 4; i++) {
            playerCheck += currentPlayer;
        }

        return playerCheck;
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
        if (getPieceOwner(x, y) == 0) {
            return true;
        }
        return false;
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
    private int checkStraightLine(boolean horizontal, int index) {

        String lineContent = "";

        if (horizontal) {

            //Iterate from left to right
            for (int i = 0; i < WIDTH; i++) {
                if (grid[i][index] != null) {
                    lineContent += grid[i][index].getPieceOwner();
                } else {
                    //Making sure the indexOf can't jump across a gap
                    lineContent += "*";
                }
            }
        } else {

            //Iterate from top to bottom
            for (int i = 0; i < HEIGHT; i++) {
                if (grid[index][i] != null) {
                    lineContent += grid[index][i].getPieceOwner();
                } else {
                    lineContent += "*";
                }
            }
        }

        return lineContent.indexOf(createPlayerCheck());
    }

    /**
     * Checks for four in a row starting as far above and to the left from the
     * given x, y in a straight line and moving diagonally downwards.
     *
     * @param x horizontal position to start
     * @param y vertical position to start
     * @return Start location of winning diagonal found
     */
    private int checkDiagonalDown(int x, int y) {

        String diagonalContent = "";

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
                diagonalContent += grid[x][y].getPieceOwner();
            } else {
                diagonalContent += "*";
            }
            x++;
            y++;
        }

        return diagonalContent.indexOf(createPlayerCheck());
    }

    /**
     * Checks for four in a row starting as far below and to the left from the
     * given x, y in a straight line and moving diagonally upwards.
     *
     * @param x horizontal position to start
     * @param y vertical position to start
     * @return Start location of winning diagonal found
     */
    private int checkDiagonalUp(int x, int y) {
        String diagonalContent = "";

        //Start check as far down-left as possible
        while (x > 0 && y < HEIGHT - 1) {
            x--;
            y++;
        }

        //Loop through every square from bottom left to top right
        while ((x < WIDTH) && (y >= 0)) {
            if (grid[x][y] != null) {
                diagonalContent += grid[x][y].getPieceOwner();
            } else {
                diagonalContent += "*";
            }
            x++;
            y--;
        }

        return diagonalContent.indexOf(createPlayerCheck());
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
     * Returns the number of the player that placed piece at given x,y coordinate.
     * 
     * Also can return zero if no piece at that location.
     *
     * @param x horizontal location
     * @param y vertical location
     * @return int representing owner of piece.
     */
    public int getPieceOwner(int x, int y) {
        //Check piece exists and return
        if (grid[x][y] != null) {
            return grid[x][y].getPieceOwner();
        } else {
            //no piece found
            return 0;
        }
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
     * Getter for game won status.
     *
     * @return Game won or not
     */
    public boolean getGameWon() {
        return gameWon;
    }

    /**
     * Sets the scores reference according to scores parameter
     * passed.
     *
     * @param scores 
     */
    public void setScores(GameScore scores) {
        this.scores = scores;
    }

}
