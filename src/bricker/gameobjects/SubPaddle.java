package bricker.gameobjects;

import bricker.main.BrickerGameManager;

public class SubPaddle extends Paddle{
    private final BrickerGameManager brickerGameManager;
    public SubPaddle(danogl.util.Vector2 topLeftCorner, danogl.util.Vector2 paddleDimensions,
                       danogl.util.Vector2 windowDimensions, danogl.gui.rendering.Renderable renderable,
                       danogl.gui.UserInputListener inputListener, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, paddleDimensions, windowDimensions, renderable, inputListener);
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollisionEnter(danogl.GameObject other, danogl.collisions.Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.subPaddleHit(this);
    }
}
