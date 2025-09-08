package main.bricker;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.bricker.gameobjects.Ball;
import main.bricker.gameobjects.Paddle;

import java.awt.Color;

public class BrickerGameManager extends GameManager {


    private static final int BORDER_WIDTH = 5;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_OFFSET_FROM_BOTTOM = 20;
    private static final int BALL_SIZE = 50;
    private static final int BALL_SPEED = 100;
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop.wav";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(800, 600)).run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        makeBackground(imageReader, windowController);
        makeBall(imageReader, soundReader, windowDimensions);
        makePaddle(imageReader, windowDimensions, inputListener);
        makeBackground(windowDimensions);
    }

    private void makeBackground(Vector2 windowDimensions) {
        Renderable wallImage = new RectangleRenderable(Color.CYAN);
        GameObject leftBorder = new GameObject(Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y()), wallImage);
        gameObjects().addGameObject(leftBorder);
        GameObject rightBorder = new GameObject(new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), wallImage);
        gameObjects().addGameObject(rightBorder);
        GameObject topBorder = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), BORDER_WIDTH),
                wallImage);
        gameObjects().addGameObject(topBorder);
    }

    private void makePaddle(ImageReader imageReader, Vector2 windowDimensions, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                windowDimensions , paddleImage
                , inputListener);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - PADDLE_OFFSET_FROM_BOTTOM));
        gameObjects().addGameObject(paddle);
    }

    private void makeBall(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
    }

    private void makeBackground(ImageReader imageReader, WindowController windowController) {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowController.getWindowDimensions(), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }
}