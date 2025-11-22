package bricker.gameobjects;

import bricker.main.BrickerGameManager;

/**
 * SubPaddle is a specialized paddle that notifies the game manager when it is hit.
 * This allows the game manager to track hits on the sub-paddle separately from the main paddle.
 * @author Ron Stein
 */
public class SubPaddle extends Paddle{
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the SubPaddle.
     * @param brickerGameManager composition root for the game manager, this is
     *                           different than the main paddle only the sub paddle needs to notify
     *                           the game manager when hit.
     * @param topLeftCorner The top-left corner of the paddle
     * @param paddleDimensions The dimensions of the paddle.
     * @param windowDimensions The dimensions of the game window.
     * @param renderable The renderable object for the paddle's appearance.
     * @param inputListener The user input listener for handling keyboard input.
     */
    public SubPaddle(danogl.util.Vector2 topLeftCorner, danogl.util.Vector2 paddleDimensions,
                       danogl.util.Vector2 windowDimensions, danogl.gui.rendering.Renderable renderable,
                       danogl.gui.UserInputListener inputListener, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, paddleDimensions, windowDimensions, renderable, inputListener);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles collision events by notifying the game manager when the sub-paddle is hit.
     * This is different behavior than the main paddle, because the game manager needs to track how many
     * times the sub paddle has been hit.
     * @param other the other game object involved in the collision.
     * @param collision the collision details.
     */
    @Override
    public void onCollisionEnter(danogl.GameObject other, danogl.collisions.Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.subPaddleHit(this);
    }
}
