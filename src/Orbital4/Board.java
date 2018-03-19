package Orbital4;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author emulrooney
 */
public class Board {
   
    private final int WIDTH = 9;
    private final int HEIGHT = 9;
    
    private boolean NO_SOLID_WALLS = true;
    
    private Piece[][] grid;
    
    private int currentPlayer = -1;
    
    
    public Board() {
        
        grid = new Piece[WIDTH][HEIGHT];
        
        setPiece(WIDTH / 2, HEIGHT / 2);
    }
    
   
    public Piece[][] getGrid() {
        return grid;
    }
    
    public void setPiece(int x, int y) {
        grid[x][y] = new Piece(currentPlayer, x, y);
                
        if (checkVertical(x) != -1) {
            System.out.println("Vertical victory");
        };
        
        if (checkHorizontal(y) != -1) {
            System.out.println("Horizontal Victory");
        }
        
        if (checkDiagonalDown(x, y) != -1) {
            System.out.println("Diagonal Down victory");
        }
        
        if (checkDiagonalUp(x, y) != -1) {
            System.out.println("Diagonal Up victory");
        }
        
        flipCurrentPlayer();
    }
    
    public int getPieceOwner(int x, int y) {
        if (grid[x][y] != null) {
            return grid[x][y].getPieceOwner();        
        } else {
            return 0;
        }
    }
    
    public int checkForValidMove(boolean horizontal, boolean negative, int index) {
        
        int validMoveLocation = -1;
        
        //Check horizontal
        
        //Check diago
        if (horizontal && !negative) {
            validMoveLocation = checkFromLeft(index);
        } else if (horizontal && negative) {
            validMoveLocation = checkFromRight(index);
        } else if (!horizontal && !negative) {
            validMoveLocation = checkFromTop(index);
        } else if (!horizontal && negative) {
            validMoveLocation = checkFromBottom(index);
        }
        
        if (NO_SOLID_WALLS) {
            if ((validMoveLocation == 0 && negative) || (validMoveLocation == 8 && !negative)) {
                return -1;
            }
        }
        
        return validMoveLocation;
    }
    
    private boolean checkSpaceFree(int x, int y) {
        if (getPieceOwner(x, y) == 0) {
            return true;
        }
        return false;
    }
    
    public int checkFromLeft(int index) {
        int validMoveLocation = -1;
        boolean stillChecking = true;
        
        for (int i = 0; i < WIDTH; i++) {
            if (checkSpaceFree(i, index) && stillChecking) {
                validMoveLocation = i;
            } else {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }
    
    public int checkFromRight(int index) {
        int validMoveLocation = -1;
        boolean stillChecking = true;
        
        for (int i = (WIDTH - 1); i >= 0; i--) {
            if (checkSpaceFree(i, index) && stillChecking) {
                validMoveLocation = i;
            } else {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }
    
        public int checkFromTop(int index) {
        int validMoveLocation = -1;
        boolean stillChecking = true;
        
        for (int i = 0; i < HEIGHT; i++) {
            if (checkSpaceFree(index, i) && stillChecking) {
                validMoveLocation = i;
            } else {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }
        
        public int checkFromBottom(int index) {
        int validMoveLocation = -1;
        boolean stillChecking = true;
        
        for (int i = HEIGHT - 1; i >= 0; i--) {
            if (checkSpaceFree(index, i) && stillChecking) {
                validMoveLocation = i;
            } else {
                stillChecking = false;
            }
        }
        return validMoveLocation;
    }
    
    
    public int getWidth() {
        return WIDTH;
    }
    
    public int getHeight() {
        return HEIGHT;
    }

    /*public String boardToText() { 
        
        String boardAsText = "";
        
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (grid[x][y] != null) {
                    if (grid[x][y].getPieceOwner() == -1) {
                        boardAsText += "*";
                    } else {
                        boardAsText += grid[x][y].getPieceOwner();
                    }
                } else {
                    boardAsText += "'";
                }
            }
           
        }
        
        return boardAsText;
    }*/
    
    private void flipCurrentPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }
    
    public int[] getCenterLocation() {
        return new int[] {WIDTH / 2, HEIGHT / 2};
    }

    public int checkVertical(int x) {
        
        String columnContent = "";
        
        for (int y = 0; y < HEIGHT; y++) {
            if (grid[x][y] != null) {
                columnContent += grid[x][y].getPieceOwner();
            }
        }
        
        return columnContent.indexOf(getPlayerCheck());
    }

    
    public int checkHorizontal(int y) {
        
        String columnContent = "";
        
        for (int x = 0; x < WIDTH; x++) {
            if (grid[x][y] != null) {
                columnContent += grid[x][y].getPieceOwner();
            }
        }
        
        return columnContent.indexOf(getPlayerCheck());
    }
    
    public int checkDiagonalDown(int x, int y) {
        String diagonalContent = "";
        
        if (x > y) {
            x -= y;
            y = 0;
        } else {
            y -= x;
            x = 0;
        }
        
        while ((x < WIDTH) && (y < HEIGHT)) {
            if (grid[x][y] != null) {
                diagonalContent += grid[x][y].getPieceOwner();
            }
            x++;
            y++;
        }
        
        return diagonalContent.indexOf(getPlayerCheck());
    }
    
    
    public int checkDiagonalUp(int x, int y) {
        String diagonalContent = "";
        
        //Start check as far down-left as possible
        while (x > 0 && y < HEIGHT - 1) {
            x--;
            y++;
        }
        
        //need a check in case we can't go all the way to x = 0;
        
        while ((x < WIDTH - 1) && (y >= 0)) {
            if (grid[x][y] != null) {
                diagonalContent += grid[x][y].getPieceOwner();
            }
            x++;
            y--;
        }
        
        return diagonalContent.indexOf(getPlayerCheck());
    }
    
    private String getPlayerCheck() {
        String playerCheck = "";
        
        for (int i = 0; i < 4; i++) {
            playerCheck += currentPlayer;
        }
        
        return playerCheck;
    }

    
}
