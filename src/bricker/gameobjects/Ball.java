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
    private int collisionCounter = 0;
    public static final float BALL_SIZE = 50f;
    public static final float BALL_SPEED = 100f;
    public static final String BALL_IMAGE_PATH = "assets/ball.png";
    public static final String BALL_SOUND_PATH = "assets/blop.wav";

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
     * Returns the number of collisions the ball has had.
     * @return the collision count.
     */
    public int getCollisionCounter() {
        return collisionCounter;
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
        //uncomment to enable sound!!
        //collisionSound.play();
        collisionCounter++;
    }
}
