package Orbital4;

/**
 * Game Score. Holds game score and contains a method to increment a player's
 * score, a method to reset both scores to zero and a toString method.
 * @author Evan Mulrooney 000745477
 */
public class GameScore { 
    
    /** GAME SCORE **/
    private static int player1score = 0;
    private static int player2score = 0;
    
    /**
     * Point Scored. Adds one point to the player given as parameter. Called
     * when a player wins a game. 
     * @param player 
     */
    public static void pointScored(int player) {
        if (player == 1) {
            player1score++;
        } else {
            player2score++;
        }
    }
    
    /**
     * Wipe Scores. Sets player 1 and player 2 scores to zero. Called when a new
     * game is started.
     */
    public static void wipeScores() {
        player1score = 0;
        player2score = 0;
    }
    
    /**
     * To String. Returns the scores in a single string across two lines. Used
     * for testing as well as for the label showing player scores on the main
     * screen.
     * @return String containing each player's score.
     */
    public String toString() {
        return "Player 1: \t" + player1score + "\t\t\t\tPlayer 2: \t" + player2score;
    }
    
    
}
