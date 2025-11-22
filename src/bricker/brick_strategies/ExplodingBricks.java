package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

public class ExplodingBricks extends CollisionStrategyDecorator {
    private static final String EXPLODED_TAG = "exploded";
    private BrickerGameManager brickerGameManager;

    /**
     * Constructor for the ExplodingBricks strategy.
     *
     * @param decorated          The CollisionStrategy to be decorated.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public ExplodingBricks(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
        super(decorated);
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if(thisObj.getTag().equals(EXPLODED_TAG)) {
            return;
        }
        thisObj.setTag(EXPLODED_TAG);
        brickerGameManager.explodeBricks(thisObj, otherObj);
    }
}
