package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that causes this brick and surrounding bricks to explode.
 * When a collision is detected, the brick and its neighboring bricks are removed from the game.
 * This strategy extends the decorated strategy's behavior.
 * @see CollisionStrategyDecorator
 * @author Ron Stein
 */
public class ExplodingBricksCollisionStrategy extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the ExplodingBricks strategy.
     * @param decorated          The CollisionStrategy to be decorated.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public ExplodingBricksCollisionStrategy(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
        super(decorated);
        this.brickerGameManager = brickerGameManager;

    }

    /**
     * Handles the collision event by exploding the brick and its neighbors. The BrickerGameManager
     * can find the neighboring bricks and remove them from the game. This class cannot.
     * @param thisObj  the brick that was collided with
     * @param otherObj the other game object involved in the collision
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.explodeBricks(thisObj, otherObj);
    }
}
