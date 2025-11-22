package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball in the game that can collide with other game objects.
 * The ball bounces off surfaces and plays a sound upon collision.
 * It also keeps track of the number of collisions it has had.
 * @author Aron Isaacs
 * @see danogl.GameObject
 * @see danogl.collisions.Collision
 */
public class Ball extends GameObject {
    private final Sound collisionSound;

    /**
     * Constructs a Ball object with specified position, size, renderable, and collision sound.
     * @param topLeftCorner the top-left corner position of the ball.
     * @param dimensions the dimensions (width and height) of the ball.
     * @param renderable the visual representation of the ball.
     * @param collisionSound the sound to play upon collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    /**
     * Handles collision events by bouncing the ball off the surface it collides with,
     * playing a collision sound, and incrementing the collision counter.
     * @param other the other game object involved in the collision.
     * @param collision the collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionSound.play();
    }
}
