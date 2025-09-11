package main.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {

    private final Sound collisionSound;
    private int collisionCounter = 0;
    public static final float BALL_SIZE = 50f;
    public static final float BALL_SPEED = 100f;
    public static final String BALL_IMAGE_PATH = "assets/ball.png";
    public static final String BALL_SOUND_PATH = "assets/blop.wav";

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    public int getCollisionCounter() {
        return collisionCounter;
    }
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        //uncomment to enable sound!!
        //collisionSound.play();
        collisionCounter++;
    }

    @Override
    public void update(float deltaTime) {
        Vector2 position = getTopLeftCorner();
        Vector2 velocity = getVelocity();
        super.update(deltaTime);
        position = getTopLeftCorner();
        velocity = getVelocity();
    }
}
