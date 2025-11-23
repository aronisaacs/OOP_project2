package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;

/**
 * HeartCollisionStrategy is a collision strategy that handles the event when a heart object collides
 * with the main paddle. Upon collision, the heart is removed from the game and the player's
 * lives are increased.
 *
 * @author Ron Stein
 */
public class HeartCollisionStrategy implements CollisionStrategy {
	private final BrickerGameManager brickerGameManager;

	/**
	 * Constructor for the HeartCollisionStrategy.
	 *
	 * @param brickerGameManager The game manager to handle brick removal.
	 */
	public HeartCollisionStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager = brickerGameManager;
	}

	/**
	 * Handles the collision event by removing the heart from the game
	 *
	 * @param thisObj  the brick that was collided with
	 * @param otherObj the other game object involved in the collision
	 */
	@Override
	public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
		brickerGameManager.removeGameObject(thisObj, Layer.DEFAULT);
		brickerGameManager.increaseLives();
	}
}
