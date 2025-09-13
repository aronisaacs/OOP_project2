package main.bricker.strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import main.bricker.BrickerGameManager;

/**
 * A basic collision strategy that removes the brick from the game upon collision.
 * When a collision is detected, the brick is removed from the game.
 * This strategy can be extended to include additional behaviors, such as playing a sound
 * or updating the score.
 * @see CollisionStrategy
 * @author Aron Isaacs
 */
public class BasicCollisionStrategy implements  CollisionStrategy {

    private final BrickerGameManager brickerGameManager;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision event by removing the brick from the game.
     * @param brick the brick that was collided with
     * @param other the other game object involved in the collision
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        System.out.println("collision with brick detected");
        brickerGameManager.removeBrick(brick);
    }
}
