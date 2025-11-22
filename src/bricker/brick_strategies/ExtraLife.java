package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
/**
 * ExtraLife is a collision strategy decorator that adds the functionality of spawning an extra life (heart)
 * when a brick is hit.
 * @author Ron Stein
 */
public class ExtraLife extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the ExtraLife strategy.
     * @param decorated          The CollisionStrategy to be decorated.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public ExtraLife(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
        super(decorated);
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.spawnHeart(thisObj.getCenter());
    }
}
