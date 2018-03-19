package Orbital4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import static javafx.application.Application.launch;
import javafx.scene.paint.Color;

/**
 * Main class for application. 
 * 
 * Orbital4 is a variation on connect4 wherein players may drop pieces
 * in from any direction but pieces must connect to a neutral piece at the very
 * centre of the board.
 * 
 * It's similar to the board game Gomoku, but pieces travel in a straight line
 * from the edge of the board and must connect to the piece in the centre of the
 * board.
 * 
 * This main class handles the user's view as well as the generation of the model
 * and controller elements - but once they're done, the only work this class does
 * is calling a new game when appropriate and holding a few constants 
 * 
 * 
 * @author Evan Mulrooney 000745477
 */
public class Orbital4 extends Application {
    
    /* Static values */
    
    /* Offset from top-left of window */
    private final static int BOARD_OFFSET = 60;
    /* Size for pieces */
    private final static int PIECE_SIZE = 48;
    /* Size of buttons */
    private final static int BUTTON_SIZE = PIECE_SIZE / 2;
    
    /* Player 1 color */
    private static final Color P1_COLOR = Color.RED;
    /* Player 2 color */
    private static final Color P2_COLOR = Color.NAVY;
    /* Outlines */
    private static final Color SHADING = new Color(0, 0, 0, 0.5);
    
    private final Color BOARD_COLOR_1 = Color.LIGHTGRAY;
    private final Color BOARD_COLOR_2 = Color.DARKGRAY;
    
    private GraphicsContext gc;
    private Board board;
    
    
     /**
     * Start method. Generates everything the player needs at the start of the
     * game.
     *
     * @param stage The main stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600); // set the size here
        stage.setTitle("Orbital4"); // set the window title here
        stage.setScene(scene);

        Canvas canvas = new Canvas(552, 600); // canvas size here
        root.getChildren().add(canvas);

        newGame();

        Label gameTitle = new Label("ORBITAL4");
        gameTitle.setLayoutX(555);
        root.getChildren().add(gameTitle);

        GameButton[][] buttons = generateButtons();
        buttons[0][0].setReferences(this, board);

        //Unfortunately, addAll won't allow for adding 2D arrays. Split into 4 lines.
        root.getChildren().addAll(buttons[0]);
        root.getChildren().addAll(buttons[1]);
        root.getChildren().addAll(buttons[2]);
        root.getChildren().addAll(buttons[3]);

        // 1. Create the model
        // 2. Create the GUI components
        // 3. Add components to the root
        // 4. Configure the components (colors, fonts, size, location)
        // 5. Add Event Handlers and do final setup
        // 6. Show the stage
        stage.show();

        gc = canvas.getGraphicsContext2D();

        //Initializing game
        drawBoard(gc);

        // This code starts a "thread" which will run your animation
        //Thread t = new Thread(() -> animate(gc));
        //t.start();

    }

    /**
     * Make no changes here.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Animation thread. This is where you put your animation code.
     *
     * @param gc The drawing surface
     */
    public void redrawAll() {
        Piece[][] grid = board.getGrid();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {

                if (grid[x][y] != null) {
                    drawPiece(grid[x][y]);
                }

            }
        }
    }

    /**
     * Use this method instead of Thread.sleep(). It handles the possible
     * exception by catching it, because re-throwing it is not an option in this
     * case.
     *
     * @param duration Pause time in milliseconds.
     */
    public static void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Exits the app completely when the window is closed. This is necessary to
     * kill the animation thread.
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    private void newGame() {

        board = new Board();

    }
    
    public void drawBoard(GraphicsContext gc) {
        
        Piece[][] grid = board.getGrid();
        
        int w = board.getWidth();
        int h = board.getHeight();
        
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
        
        
    }
    
    public void drawPiece(Piece piece) {
        
        if (piece.getPieceOwner() == -1) {
          gc.setFill(Color.WHITE); //Neutral piece for centre;
        } else if (piece.getPieceOwner() == 1) {
            gc.setFill(P1_COLOR);
        } else if (piece.getPieceOwner() == 2) {
            gc.setFill(P2_COLOR);
        } else {
            //This should never come up, but if there's a piece with a bad owner,
            //it should be visible.
            System.err.println("Piece with bad owner drawn at " + piece.getX() + ", " + piece.getY());
            gc.setFill(Color.GREEN);
        }
        gc.fillOval(BOARD_OFFSET + piece.getX() * PIECE_SIZE, BOARD_OFFSET + piece.getY() * PIECE_SIZE, PIECE_SIZE, PIECE_SIZE);
        
        //Decorative
        gc.setStroke(SHADING);
        gc.strokeOval(BOARD_OFFSET + piece.getX() * PIECE_SIZE, BOARD_OFFSET + piece.getY() * PIECE_SIZE, PIECE_SIZE, PIECE_SIZE);
    }
    
    public void drawPiece(int x, int y) {
        System.out.println("got to drawpiece(xy)");
        if (board.getGrid()[x][y] != null) {
            System.out.println("got the non-null space on grid");
            drawPiece(board.getGrid()[x][y]);
        } else {
            System.err.println("Tried to draw piece at " + x + "," +  y + " but there's nothing there.");
        }
        System.out.println("done drawpiece");
    }
    

    private GameButton[][] generateButtons() {
        GameButton[][] buttons = new GameButton[4][];

        buttons[0] = generateHorizontalButtons(BOARD_OFFSET, BOARD_OFFSET - BUTTON_SIZE, false);
        buttons[1] = generateHorizontalButtons(BOARD_OFFSET, BOARD_OFFSET + (PIECE_SIZE * board.getHeight()), true);
        buttons[2] = generateVerticalButtons(BOARD_OFFSET - BUTTON_SIZE, BOARD_OFFSET, false);
        buttons[3] = generateVerticalButtons(BOARD_OFFSET + (PIECE_SIZE * board.getWidth()), BOARD_OFFSET, true);

        return buttons;
    }

    private GameButton[] generateHorizontalButtons(int xStart, int yStart, boolean negative) {
        int w = board.getWidth();

        GameButton[] buttons = new GameButton[w];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new GameButton(true, i);
            buttons[i].setLayoutY(xStart + (PIECE_SIZE * i));
            buttons[i].setLayoutX(yStart);

            if (negative) {
                buttons[i].setNegative(true);
            }
        }

        return buttons;
    }

    private GameButton[] generateVerticalButtons(int xStart, int yStart, boolean negative) {
        int h = board.getHeight();

        GameButton[] buttons = new GameButton[h];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new GameButton(false, i);
            buttons[i].setLayoutY(xStart);
            buttons[i].setLayoutX(yStart + (PIECE_SIZE * i));

            if (negative) {
                buttons[i].setNegative(true);
            }
        }

        return buttons;
    }

    public static int getOffset() {
        return BOARD_OFFSET;
    }

    public static int getButtonSize() {
        return BUTTON_SIZE;
    }
    
    public void updateView(int x, int y) {
        if (gc!= null) {
            drawPiece(board.getGrid()[x][y]);
        }
    }
    
    public static int getPieceSize() {
        return PIECE_SIZE;
    }
}
