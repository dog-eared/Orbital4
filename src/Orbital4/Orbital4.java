package Orbital4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

/**
 * Main class for application.
 *
 * Orbital4 is a variation on connect4 wherein two players may drop pieces in
 * from any direction, but pieces must connect to a neutral piece at the very
 * centre of the board or to another piece in at least one of four directions.
 *
 * Rules available in game. 
 *
 * This main class handles the user's view as well as the generation of the
 * board model and controller elements - but once they're built, all changes to
 * the model come from input from GameButtons or regular GUI buttons. If a new 
 * model is needed (ie. the user starts a new game), this class calls the model's
 * constructor but all logic is handled in the board model class. 
 *
 * @author Evan Mulrooney 000745477
 */
public class Orbital4 extends Application {

    /* VIEW RELATED CONSTANTS */
 /* Offset from top-left of window */
    private final static int BOARD_OFFSET = 60;
    /* Size for pieces */
    private final static int PIECE_SIZE = 48;
    /* Size of buttons */
    private final static int BUTTON_SIZE = PIECE_SIZE / 2;

    /* Colors for board tiles */
    private final static Color BOARD_COLOR_1 = Color.LIGHTGRAY;
    private final static Color BOARD_COLOR_2 = Color.DARKGRAY;

    /* Colors for players */
    private final static Color P1_COLOR = Color.DARKRED;
    private final static Color P2_COLOR = Color.NAVY;

    /* Piece outlines */
    private final static Color BORDER_SHADING = new Color(0, 0, 0, 0.25);

    /* ASSOCIATONS */
    /* Buttons */
    private GameButton[][] gameButtons;
    /* Score Display */
    private Label scoresDisplay;
    private Label gameWonDisplay;

    /* Graphics Context, generated within start method */
    private static GraphicsContext gc;
    /* Currently displayed board */
    private static Board board;
    /* Score Data */
    private static GameScore scores;
    

    /**
     * Start method. Generates everything the player needs at the start of the
     * game. Builds the GUI to view model and buttons that control the model.
     *
     * @param stage The main stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        //Regular init from template
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600); // set the size here
        stage.setTitle("Orbital4"); // set the window title here
        stage.setScene(scene);
        Canvas canvas = new Canvas(552, 600); // canvas size here
        root.getChildren().add(canvas);

        //Build the main components
        board = new Board();
        scores = new GameScore();
        board.setScores(scores);

        /* GUI - TEXT */
        //Set the title
        Label gameTitle = new Label("O R B I T A L 4\nby Evan Mulrooney");
        gameTitle.setLayoutX(555);

        //Display rules 
        Label gameRulesTitle = new Label("Rules:");
        gameRulesTitle.setLayoutX(635);
        gameRulesTitle.setLayoutY(110);

        //multiple lines for readability
        Label gameRules = new Label("1.) Players take turns dropping\npieces from any direction.\n\n"
                + "2.) Pieces travel in a straight line.\n\n"
                + "3.) Players want to get 4 pieces in\na row in any direction.\n\n"
                + "4.) Dropped pieces must connect\nto another piece above, below,\n"
                + "to the left or to the right.");
        gameRules.setLayoutX(555);
        gameRules.setLayoutY(140);
        gameRules.setId("rules");

        //Display scores
        scoresDisplay = new Label(scores.toString());
        scoresDisplay.setLayoutX(10);
        scoresDisplay.setLayoutY(550);
        
        //Game Won display -- init empty, set with text on game win
        gameWonDisplay = new Label("");
        gameWonDisplay.setLayoutX(200);
        gameWonDisplay.setLayoutY(550);

        /* GUI - BUTTONS */
        Group uiButtons = new Group();

        //New Game
        Button newGameButton = new Button("New Game");
        newGameButton.setMinWidth(240);
        newGameButton.setMinHeight(50);
        newGameButton.setLayoutY(390);
        newGameButton.setOnAction(this::guiButtonClick);

        //Wipe Scores
        Button wipeScoresButton = new Button("Wipe Scores");
        wipeScoresButton.setMinWidth(240);
        wipeScoresButton.setMinHeight(50);
        wipeScoresButton.setLayoutY(465);
        wipeScoresButton.setOnAction(this::guiButtonClick);

        //Quit
        Button quitGameButton = new Button("Quit Game");
        quitGameButton.setMinWidth(240);
        quitGameButton.setMinHeight(50);
        quitGameButton.setLayoutY(540);
        quitGameButton.setOnAction(this::guiButtonClick);

        //Generate all game buttons and make sure they know where to add pieces
        gameButtons = generateButtons();
        //Setting static fields, we have to have at least one button
        gameButtons[0][0].setGameReference(this);
        gameButtons[0][0].setBoard(board);

        //Add all the buttons together.
        uiButtons.getChildren().addAll(newGameButton, wipeScoresButton, quitGameButton);
        uiButtons.setLayoutX(555);

        root.getChildren().addAll(uiButtons, gameTitle, gameRulesTitle, gameRules, scoresDisplay, gameWonDisplay);

        //Unfortunately, addAll won't allow for adding 2D arrays. Split into 4 lines.
        root.getChildren().addAll(gameButtons[0]);
        root.getChildren().addAll(gameButtons[1]);
        root.getChildren().addAll(gameButtons[2]);
        root.getChildren().addAll(gameButtons[3]);

        //Building canvas and adding style elements
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, 552, 800);
        scene.getStylesheets().add("/style/orbital.css");

        //Draw the game board
        drawBoard(gc);

        //Showing the stage
        stage.show();
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
     * Exits the app completely when the window is closed. This is necessary to
     * kill the animation thread.
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * Draws a new board and neutral centre piece.
     * Loops through every space on grid and creates a grid of alternating
     * colours. Draws the neutral piece found at the centre of the board.
     * @param gc Graphics Context to draw on
     */
    private void drawBoard(GraphicsContext gc) {

        //Simplifying for readability
        int w = board.getWidth();
        int h = board.getHeight();
        
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
        drawPiece(w / 2, h / 2);

    }

    /**
     * Draws a piece at given x y.
     * Sets the fill colour based on the piece found and then draws the piece.
     * 
     * Public to allow for GameButton to draw pieces.
     *
     * @param x Horizontal position
     * @param y Vertical position
     */
    public void drawPiece(int x, int y) {

        Piece piece = board.getPieceAt(x, y);

        if (piece != null) {
            switch (piece.getPieceOwner()) {
                case 1:
                    //1st player
                    gc.setFill(P1_COLOR);
                    break;
                case 2:
                    //2nd player
                    gc.setFill(P2_COLOR);
                    break;
                case 8:
                    //Neutral piece
                    gc.setFill(Color.WHITE);
                    break;
                default:
                    //This should never come up, but if there's a piece with a bad owner,
                    //it should be visible.
                    System.err.println("Piece with bad owner drawn at " + piece.getX() + ", " + piece.getY());
                    gc.setFill(Color.LIMEGREEN);
                    break;
            }
            
            //Actually draw the oval
            gc.fillOval(BOARD_OFFSET + piece.getX() * PIECE_SIZE, BOARD_OFFSET + piece.getY() * PIECE_SIZE, PIECE_SIZE, PIECE_SIZE);

            //Decorative outline
            gc.setStroke(BORDER_SHADING);
            gc.setLineWidth(2);
            gc.strokeOval(BOARD_OFFSET + piece.getX() * PIECE_SIZE, BOARD_OFFSET + piece.getY() * PIECE_SIZE,
                    PIECE_SIZE, PIECE_SIZE);
            
        } else if (board.getGameWon()) {
            //Prompt incase a player misses the text saying Game Over
            System.out.println("Game won. Click new game to play again.");
        }
    }

    /**
     * General method called when one of the main GUI buttons is pressed.
     * Checks the name of the button found in the ActionEvent's toString method 
     * to determine what should be done.
     * 
     * Contains instructions to start a new game, wipe the scores or end the
     * program run.
     * @param e ActionEvent from button
     */
    private void guiButtonClick(ActionEvent e) {
        String action = e.toString();

        if (action.contains("New")) {
            //Make a new board and draw it
            board = new Board();
            drawBoard(gc);
            //Make sure buttons add pieces to the newly made board
            gameButtons[0][0].setBoard(board);
            gameWonDisplay.setText("");
        } else if (action.contains("Wipe")) {
            scores.wipeScores();
            scoresDisplay.setText(scores.toString());
        } else if (action.contains("Quit")) {
            stop();
        }

    }

    /**
     * Creates the array of game buttons surrounding the game board. Calls
     * generateHorizontalButtons and generateVerticalButtons and returns a 2D
     * array of appropriate buttons.
     * @return 2D array of GameButtons
     */
    private GameButton[][] generateButtons() {
        //init array
        GameButton[][] buttons = new GameButton[4][];

        //Generate each line of buttons
        buttons[0] = generateHorizontalButtons(BOARD_OFFSET, BOARD_OFFSET - BUTTON_SIZE, false);
        buttons[1] = generateHorizontalButtons(BOARD_OFFSET, BOARD_OFFSET + (PIECE_SIZE * board.getHeight()), true);
        buttons[2] = generateVerticalButtons(BOARD_OFFSET - BUTTON_SIZE, BOARD_OFFSET, false);
        buttons[3] = generateVerticalButtons(BOARD_OFFSET + (PIECE_SIZE * board.getWidth()), BOARD_OFFSET, true);

        return buttons;
    }

    /**
     * Creates a row of buttons and returns as an array.
     * Takes the initial location as parameters and a boolean to check if buttons
     * should send pieces up or down.
     * @param xStart Horizontal pixel location to start array of buttons.
     * @param yStart Vertical pixel location to start array of buttons.
     * @param negative If true, button is sending pieces to the left of screen.
     * @return Row of buttons on top or bottom of board as an array.
     */
    private GameButton[] generateHorizontalButtons(int xStart, int yStart, boolean negative) {
        
        //for readability
        int w = board.getWidth();

        //Generate each button horizontally
        GameButton[] buttons = new GameButton[w];
        for (int i = 0; i < buttons.length; i++) {
            //Pass horizontal, up/down direction, current index
            buttons[i] = new GameButton(true, negative, i);
            //Place button.
            //Button sizing handled in button constructor
            buttons[i].setLayoutY(xStart + (PIECE_SIZE * i));
            buttons[i].setLayoutX(yStart);
        }

        return buttons;
    }

    /**
     * Creates a column of buttons and returns as an array.
     * Takes the initial location as parameters and a boolean to check if buttons
     * should send pieces right or left.
     * @param xStart Horizontal pixel location to start array of buttons.
     * @param yStart Vertical pixel location to start array of buttons.
     * @param negative If true, button is sending pieces upwards.
     * @return Column of buttons on left or right of board as an array.
     */
    private GameButton[] generateVerticalButtons(int xStart, int yStart, boolean negative) {
        
        //readability
        int h = board.getHeight();

        //Initialize all buttons
        GameButton[] buttons = new GameButton[h];
        for (int i = 0; i < buttons.length; i++) {
            //Pass not-horizontal, right/left direction, current index
            buttons[i] = new GameButton(false, negative, i);
            
            //Place button.
            //Button sizing handled in button constructor
            buttons[i].setLayoutY(xStart);
            buttons[i].setLayoutX(yStart + (PIECE_SIZE * i));
        }

        return buttons;
    }

    /**
     * Displays score increase and game over text. 
     * Called when a player wins the game.
     * Only ever called by GameButton.
     */
    public void showGameWon() {
        scoresDisplay.setText(scores.toString());
        gameWonDisplay.setText("Game Over");
    }

}
