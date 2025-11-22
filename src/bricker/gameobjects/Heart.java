package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A Heart object in the Bricker game. When it collides with the main paddle,
 * it executes a specified collision strategy.
 * @see danogl.GameObject
 * @see bricker.brick_strategies.CollisionStrategy
 * @author Ron Stein
 */
public class Heart extends GameObject {
    private final String mainPaddleTag;
    private final CollisionStrategy collisionStrategy;
    public final static String HEART_TAG = "heart";

    /**
     * Constructs a Heart object with specified position, size, renderable,
     * main paddle tag, and collision strategy.
     * @param topLeftCorner the top-left corner position of the heart.
     * @param dimensions the dimensions (width and height) of the heart.
     * @param renderable the visual representation of the heart.
     * @param mainPaddleTag the tag of the main paddle to detect collisions with.
     * @param collisionStrategy the strategy to execute upon collision.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, String mainPaddleTag,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.mainPaddleTag = mainPaddleTag;
        this.collisionStrategy = collisionStrategy;
        this.setTag(HEART_TAG);
    }

    /**
     * Determines if this Heart should collide with another GameObject. This is important to ensure that
     * the Heart only interacts with the main paddle.
     * @param other The other GameObject.
     * @return true if the other GameObject is the main paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other){
        return other.getTag().equals(mainPaddleTag);
    }

    /**
     * Handles collision events by executing the heart's collision strategy
     * when it collides with the main paddle.
     * @param other the other game object involved in the collision.
     * @param collision the collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }
}

