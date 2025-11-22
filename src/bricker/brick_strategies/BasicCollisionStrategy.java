package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;

/**
 * A basic collision strategy that removes the brick from the game upon collision.
 * When a collision is detected, the brick is removed from the game.
 * This strategy can be extended to include additional behaviors, such as playing a sound
 * or updating the score.
 * @see CollisionStrategy
 * @author Aron Isaacs
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the BasicCollisionStrategy.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision event by removing the brick from the game.
     * @param thisObj the brick that was collided with
     * @param otherObj the other game object involved in the collision
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
    }
}
