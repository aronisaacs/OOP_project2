package bricker.main;

import danogl.util.Counter;

/**
 * A class to manage the game state, including lives and bricks counters.
 * It provides methods to increment and decrement these counters,
 * as well as to check for victory or game over conditions.
 *
 * @author Aron Isaacs
 * @see danogl.GameManager
 */
public class GameState {
	private final Counter lives;
	private final Counter bricks;
	private final Counter paddles;
	private final Counter subPaddleHits;
	private static int MAX_LIVES;

	/**
	 * Constructs a GameState with specified initial lives and total bricks.
	 *
	 * @param initialLives the initial number of lives.
	 * @param totalBricks  the total number of bricks.
	 * @param totalPaddles the total number of paddles.
	 * @param maxLives     the maximum number of lives allowed.
	 */
	public GameState(int initialLives, int totalBricks, int totalPaddles, int maxLives) {
		lives = new Counter(initialLives);
		bricks = new Counter(totalBricks);
		paddles = new Counter(totalPaddles);
		subPaddleHits = new Counter(0);
		MAX_LIVES = maxLives;
	}

	/**
	 * Decrements the lives counter by one.
	 */
	public void decrementLivesCounter() {
		lives.decrement();
	}

	/**
	 * Increments the lives counter by one.
	 */
	public void incrementLivesCounter() {
		if (lives.value() < MAX_LIVES) {
			lives.increment();
		}
	}

	/**
	 * Decrements the paddles counter by one.
	 */
	public void decrementPaddlesCounter() {
		paddles.decrement();
	}

	/**
	 * Increments the paddles counter by one.
	 */
	public void incrementPaddlesCounter() {
		paddles.increment();
	}

	/**
	 * Decrements the bricks counter by one.
	 */
	public void decrementBricksCounter() {
		bricks.decrement();
	}

	/**
	 * Increments the sub-paddle hits counter by one.
	 */
	public void incrementSubPaddleHitsCounter() {
		subPaddleHits.increment();
	}

	/**
	 * Resets the sub-paddle hits counter to zero.
	 */
	public void resetSubPaddleHitsCounter() {
		subPaddleHits.reset();
	}

	/**
	 * gets the current number of lives.
	 *
	 * @return the current number of lives.
	 */
	public int getLivesCounter() {
		return lives.value();
	}

	/**
	 * gets the current number of Paddles.
	 *
	 * @return the current number of paddles.
	 */
	public int getPaddlesCounter() {
		return paddles.value();
	}

	/**
	 * gets the current number of hits on the sub-paddle.
	 *
	 * @return the current number of hits on the sub-paddle.
	 */
	public int getSubPaddleHitsCounter() {
		return subPaddleHits.value();
	}

	/**
	 * decides if the game is won.
	 *
	 * @return true if all bricks are destroyed, false otherwise.
	 */
	public boolean isVictory() {
		return bricks.value() == 0;
	}

	/**
	 * decides if the game is over.
	 *
	 * @return true if no lives remain, false otherwise.
	 */
	public boolean isGameOver() {
		return lives.value() <= 0;
	}
}


