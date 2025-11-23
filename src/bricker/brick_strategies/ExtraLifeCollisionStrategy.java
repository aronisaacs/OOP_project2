package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

/**
 * ExtraLife is a collision strategy decorator that adds the functionality of spawning an extra life (heart)
 * when a brick is hit.
 * @author Ron Stein
 */
public class ExtraLifeCollisionStrategy extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the ExtraLife strategy.
     * @param decorated          The CollisionStrategy to be decorated.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public ExtraLifeCollisionStrategy(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
        super(decorated);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision event by spawning a heart at the brick's location. The GameManager is
     * responsible for the actual spawning of the heart, this class knows when and which brick was hit.
     * @param thisObj  the brick that was collided with
     * @param otherObj the other game object involved in the collision
     */
    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.spawnHeart(thisObj.getCenter());
    }
}
