package bricker.gameobjects;

import danogl.util.Counter;

/**
 * A class to manage the game state, including lives and bricks counters.
 * It provides methods to increment and decrement these counters,
 * as well as to check for victory or game over conditions.
 * @see danogl.GameManager
 * @author Aron Isaacs
 */
public class GameState {
    private final Counter lives;
    private final Counter bricks;
    public static final int INITIAL_LIVES = 3;
    public static final int MAX_LIVES = 4;
    /**
     * Constructs a GameState with specified initial lives and total bricks.
     * @param initialLives the initial number of lives.
     * @param totalBricks the total number of bricks.
     */
    public GameState(int initialLives, int totalBricks) {
        lives = new Counter(initialLives);
        bricks = new Counter(totalBricks);
    }
    /** Decrements the lives counter by one.
    */
    public void decrementLivesCounter(){
        lives.decrement();
    }

    /** Increments the lives counter by one. */
    public void incrementLivesCounter(){
        lives.increment();
    }


    /** Decrements the bricks counter by one. */
    public void decrementBricksCounter(){
        bricks.decrement();
    }
    /** gets the current number of lives.
     * @return the current number of lives.
     */
    public int getLivesCounter() { return lives.value(); }
    /** decides if the game is won.
     * @return true if all bricks are destroyed, false otherwise.
     */
    public boolean isVictory() { return bricks.value() == 0;}
    /** decides if the game is over.
     * @return true if no lives remain, false otherwise.
     */
    public boolean isGameOver() { return lives.value() <= 0; }
}


