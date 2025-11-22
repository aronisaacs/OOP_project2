package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

/**
 * ExtraPaddle is a collision strategy decorator that adds the functionality of spawning an extra subPaddle
 * when a brick is hit.
 * @author Ron Stein
 */
public class ExtraPaddle extends CollisionStrategyDecorator {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the ExtraPaddle strategy.
     * @param decorated         The CollisionStrategy to be decorated.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public ExtraPaddle(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
        super(decorated);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision event by spawning a subPaddle at the center of the screen. The GameManager is
     * responsible for the actual spawning of the subPaddle, this class knows when and which brick was hit.
     * @param thisObj  the brick that was collided with
     * @param otherObj the other game object involved in the collision
     */
    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.makePaddle(brickerGameManager.getWindowDimensions().y() / 2f);
    }
}
