package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

/**
 * A collision strategy that creates two extra pucks when a brick is hit.
 * Extends the CollisionStrategyDecorator to add this behavior on top of an existing strategy.
 * @author Ron Stein
 */
public class ExtraPuckCollisionStrategy extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the ExtraPuck strategy.
     *
     * @param decorated          The CollisionStrategy to be decorated.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public ExtraPuckCollisionStrategy(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
        super(decorated);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision event by creating two new pucks at the location of the hit brick.
     *
     * @param thisObj  the brick that was collided with
     * @param otherObj the other game object involved in the collision
     */
    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.makePucks(thisObj);
    }
}
