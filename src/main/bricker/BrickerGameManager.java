package main.bricker;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.bricker.gameobjects.*;
import main.bricker.strategies.BasicCollisionStrategy;
import main.bricker.strategies.CollisionStrategy;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final float BORDER_THICKNESS = 5f;
    private static final float PADDLE_OFFSET_FROM_BOTTOM = 100f;
    private static final float BRICK_GAP = 3f; // Gap between bricks
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final int DEFAULT_NUM_BRICKS_PER_ROW = 8;
    private static final int DEFAULT_NUM_ROWS = 7;
    private static final int INITIAL_LIVES = 3;
    private static final Vector2 WINDOW_DIMENSIONS = new Vector2(800, 600);
    private static final String WINDOW_TITLE = "Bricker";
    private static final String WIN_MESSAGE = "You win! Play again?";
    private static final String LOSE_MESSAGE = "You lose! Play again?";
    private final Vector2 windowDimensions;
    private final int numBricksPerRow;
    private final int numRows;
    private UserInputListener inputListener;
    private SoundReader soundReader;
    private ImageReader imageReader;
    private WindowController windowController;
    private Ball ball;
    private GameState gameState;
    private LivesDisplay livesDisplay;


    public BrickerGameManager(String WINDOW_TITLE, Vector2 windowDimensions, int numBricksPerRow, int numRows) {
        super(WINDOW_TITLE, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.numBricksPerRow = numBricksPerRow;
        this.numRows = numRows;
    }

    public static void main(String[] args) {
        int numBricksPerRow = DEFAULT_NUM_BRICKS_PER_ROW;
        int numRows = DEFAULT_NUM_ROWS;
        if (args.length == 2) {
            numBricksPerRow = Integer.parseInt(args[0]);
            numRows = Integer.parseInt(args[1]);
        }
        new BrickerGameManager(WINDOW_TITLE, WINDOW_DIMENSIONS, numBricksPerRow, numRows).run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        makeGameObjects();
    }

    private void makeGameObjects() {
        makeBackground();
        makeBorders();
        makeBall();
        makePaddle();
        CollisionStrategy collisionStrategy = new BasicCollisionStrategy(this);
        makeBricks(collisionStrategy);
        gameState = new GameState(INITIAL_LIVES, numBricksPerRow * numRows);
        livesDisplay = new LivesDisplay(INITIAL_LIVES, imageReader, this);
    }

    private void makeBricks(CollisionStrategy collisionStrategy) {
        float totalGap = 2 * BORDER_THICKNESS + (numBricksPerRow - 1) * BRICK_GAP;
        float brickWidth = (windowDimensions.x() - totalGap) / numBricksPerRow;
        Renderable brickImage = imageReader.readImage(Brick.BRICK_IMAGE_PATH, false);
        for (int row = 0; row < numRows; row++) {
            float y = BORDER_THICKNESS + row * (Brick.BRICK_HEIGHT + BRICK_GAP);
            for (int col = 0; col < numBricksPerRow; col++) {
                makeBrick(collisionStrategy, col, brickWidth,  y,  brickImage);
            }
        }
    }

    private void makeBrick(CollisionStrategy collisionStrategy,  int col, float brickWidth, float y, Renderable brickImage) {
        float x = BORDER_THICKNESS + col * (brickWidth + BRICK_GAP);
        GameObject brick = new Brick(
            new Vector2(x, y),
            new Vector2(brickWidth, Brick.BRICK_HEIGHT),
                brickImage,
                collisionStrategy,
                this
        );
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
    }

    private void makeBorders() {
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

    private void makePaddle() {
        Renderable paddleImage = imageReader.readImage(Paddle.PADDLE_IMAGE_PATH, true);
        Vector2 initialPosition = new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_OFFSET_FROM_BOTTOM);
        GameObject paddle = new Paddle(initialPosition, new Vector2(Paddle.PADDLE_WIDTH,
                Paddle.PADDLE_HEIGHT),
                windowDimensions , paddleImage
                , inputListener);
        gameObjects().addGameObject(paddle);
    }

    private void makeBall() {
        Renderable ballImage = imageReader.readImage(Ball.BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(Ball.BALL_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(Ball.BALL_SIZE, Ball.BALL_SIZE), ballImage,
                collisionSound);
        resetBall();
        gameObjects().addGameObject(ball);
    }

    private void resetBall() {
        ball.setCenter(windowDimensions.mult(0.5f));
        Random random = new Random();
        float ballSpeedX = Ball.BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        float ballSpeedY = Ball.BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        ball.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
    }

    private void makeBackground() {
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

    public void decrementBrickCounter() {
        gameState.decrementBricksCounter();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Check victory
        if (gameState.isVictory() || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            showEndGameWindow(WIN_MESSAGE);
            return;
        }

        // Check ball falling below screen
        if (ball.getCenter().y() > windowDimensions.y()) {
            gameState.decrementLivesCounter();
            livesDisplay.updateLives(gameState.getLivesCounter()); // Update display

            if (!gameState.isGameOver()) {
                resetBall();
            } else {
                showEndGameWindow(LOSE_MESSAGE);
            }
        }
    }

    private void showEndGameWindow(String msg) {
        boolean playAgain = windowController.openYesNoDialog(msg);
        if (playAgain) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }
}