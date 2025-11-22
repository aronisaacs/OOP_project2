package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

public class ExtraPaddle extends CollisionStrategyDecorator {
    private BrickerGameManager brickerGameManager;
    public ExtraPaddle(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
        super(decorated);
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        brickerGameManager.makePaddle(brickerGameManager.getWindowDimensions().y() / 2f);
    }
}
