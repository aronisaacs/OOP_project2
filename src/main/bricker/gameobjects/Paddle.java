package main.bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private static final Float MOVEMENT_SPEED = 400.0f;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    public static final float PADDLE_WIDTH = 100f;
    public static final float PADDLE_HEIGHT = 15f;
    public static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    public Paddle(Vector2 topLeftCorner, Vector2 paddleDimensions,
                  Vector2 windowDimensions, Renderable renderable,
                  UserInputListener inputListener) {
        super(topLeftCorner, paddleDimensions, renderable);
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 position = getTopLeftCorner();

        // Check if the paddle is off-screen
        // Updated the off-screen check in the Paddle class to account for the borders
        if (position.x() < 0) {
            setTopLeftCorner(new Vector2(0, getTopLeftCorner().y()));
            setVelocity(Vector2.ZERO);
        } else if (position.x() + getDimensions().x() > windowDimensions.x()) {
            setTopLeftCorner(new Vector2(windowDimensions.x() - getDimensions().x(),
                    getTopLeftCorner().y()));
            setVelocity(Vector2.ZERO);

        } else {
            // Handle right/left movement
            Vector2 movementDirection = Vector2.ZERO;
            if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
                movementDirection = movementDirection.add(Vector2.RIGHT);
            }
            if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
                movementDirection = movementDirection.add(Vector2.LEFT);
            }
            setVelocity(movementDirection.mult(MOVEMENT_SPEED));
        }
    }


}

