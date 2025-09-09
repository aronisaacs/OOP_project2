package main.bricker;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import main.bricker.gameobjects.Ball;
import main.bricker.gameobjects.Brick;
import main.bricker.gameobjects.LivesDisplay;
import main.bricker.gameobjects.Paddle;
import main.bricker.strategies.BasicCollisionStrategy;
import main.bricker.strategies.CollisionStrategy;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {


    private static final float BORDER_THICKNESS = 5;
    private static final float PADDLE_WIDTH = 100;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_OFFSET_FROM_BOTTOM = 100;
    private static final float BALL_SIZE = 50;
    private static final float BALL_SPEED = 100;
    private static final float BRICK_HEIGHT = 15f;
    private static final float EDGE_GAP = 5f; // Gap from window edges
    private static final float BRICK_GAP = 3f; // Gap between bricks
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop.wav";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private final Vector2 windowDimensions;
    private final int numBricksPerRow;
    private final int numRows;
    private static final int INITIAL_LIVES = 3;
    private int lives;
    private Ball ball;
    private WindowController windowController;
    private LivesDisplay livesDisplay;
    private Counter brickCounter;
    private UserInputListener inputListener;


    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numBricksPerRow, int numRows) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.numBricksPerRow = numBricksPerRow;
        this.numRows = numRows;
    }

    public static void main(String[] args) {
        int numBricksPerRow = 8;
        int numRows = 7;
        if (args.length == 2) {
            numBricksPerRow = Integer.parseInt(args[0]);
            numRows = Integer.parseInt(args[1]);
        }
        new BrickerGameManager("Bricker", new Vector2(800, 600), numBricksPerRow, numRows).run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        lives = INITIAL_LIVES;
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        makeBackground(imageReader, windowController);
        makeBall(imageReader, soundReader, windowDimensions);
        makePaddle(imageReader, windowDimensions, inputListener);
        CollisionStrategy collisionStrategy = new BasicCollisionStrategy(this);
        brickCounter = new Counter(0);
        makeBricks(imageReader, collisionStrategy);
        makeBackground();
        makeLivesDisplay(imageReader);
        this.inputListener = inputListener;


    }

    private void makeLivesDisplay(ImageReader imageReader) {
        // Instantiate and add LivesDisplay
        livesDisplay = new LivesDisplay(
                lives,
                INITIAL_LIVES,
                imageReader,
                windowDimensions,
                this
        );
        livesDisplay.addToGameObjects();
    }

    private void makeBricks(ImageReader imageReader,
                            CollisionStrategy collisionStrategy) {

        float totalGap = 2 * EDGE_GAP + (numBricksPerRow - 1) * BRICK_GAP;
        float brickWidth = (windowDimensions.x() - totalGap) / numBricksPerRow;

        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);

        for (int row = 0; row < numRows; row++) {
            float y = EDGE_GAP + row * (BRICK_HEIGHT + BRICK_GAP);
            for (int col = 0; col < numBricksPerRow; col++) {
                makeBrick(collisionStrategy, col, brickWidth,  y,  brickImage);
                makeBrick(collisionStrategy,  col, brickWidth,  y,  brickImage);
            }
        }
    }

    private void makeBrick(CollisionStrategy collisionStrategy,  int col, float brickWidth, float y, Renderable brickImage) {
        float x = EDGE_GAP + col * (brickWidth + BRICK_GAP);
        GameObject brick = new Brick(
            new Vector2(x, y),
            new Vector2(brickWidth, BRICK_HEIGHT),
                brickImage,
                collisionStrategy,
            this
        );
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
        brickCounter.increment();
    }

    private void makeBackground() {
        Renderable wallImage = new RectangleRenderable(Color.CYAN);
        GameObject leftBorder = new GameObject(Vector2.ZERO, new Vector2(BORDER_THICKNESS, windowDimensions.y()), wallImage);
        gameObjects().addGameObject(leftBorder);
        GameObject rightBorder = new GameObject(new Vector2(windowDimensions.x() - BORDER_THICKNESS, 0),
                new Vector2(BORDER_THICKNESS, windowDimensions.y()), wallImage);
        gameObjects().addGameObject(rightBorder);
        GameObject topBorder = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), BORDER_THICKNESS),
                wallImage);
        gameObjects().addGameObject(topBorder);
    }

    private void makePaddle(ImageReader imageReader, Vector2 windowDimensions, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        Vector2 initialPosition = new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_OFFSET_FROM_BOTTOM);
        GameObject paddle = new Paddle(initialPosition, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                windowDimensions , paddleImage
                , inputListener);
        gameObjects().addGameObject(paddle);
    }

    private void makeBall(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
        Vector2 initialPosition = windowDimensions.mult(0.5f);
        ball = new Ball(initialPosition, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
        // random initial velocity
        float ballSpeedX = BALL_SPEED;
        float ballSpeedY = BALL_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) {
            ballSpeedX *= -1;
        }
        if (random.nextBoolean()) {
            ballSpeedY *= -1;
        }
        ball.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
        // place ball in center of window
        gameObjects().addGameObject(ball);
    }

    private void makeBackground(ImageReader imageReader, WindowController windowController) {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowController.getWindowDimensions(), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    public void addGameObject(GameObject object, int layer) {
        gameObjects().addGameObject(object, layer);
    }

    public void removeGameObject(GameObject object, int layer) {
        gameObjects().removeGameObject(object, layer);
    }

    public Counter getBrickCounter() {
        return brickCounter;
    }

    // Override update to check for losing condition
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


        // Win by removing all bricks
        if (brickCounter.value() == 0) {
            boolean playAgain = windowController.openYesNoDialog("You win! Play again?");
            if (playAgain) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
            return;
        }

        // Win by pressing 'W'
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            boolean playAgain = windowController.openYesNoDialog("You win! Play again?");
            if (playAgain) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
            return;
        }

        if (ball.getCenter().y() > windowDimensions.y()) {
            lives--;
            livesDisplay.updateLives(lives); // Update display
            if (lives > 0) {
                resetBall();
            } else {
                boolean playAgain = windowController.openYesNoDialog("You lose! Play again?");
                if (playAgain) {
                    lives = INITIAL_LIVES;
                    livesDisplay.updateLives(lives); // Reset display
                    resetBall();
                } else {
                    windowController.closeWindow();
                }
            }
        }
    }

    // Helper to reset ball position and velocity
    private void resetBall() {
        ball.setCenter(windowDimensions.mult(0.5f));
        Random random = new Random();
        float ballSpeedX = BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        float ballSpeedY = BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        ball.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
    }
}