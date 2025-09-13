package bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.*;
import bricker.strategies.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The main class of the Bricker game, responsible for initializing and managing the game state.
 * It sets up the game window, creates game objects, and handles game updates.
 * The game features a paddle, ball, bricks, and borders, with collision detection and game state management.
 * Players can win by destroying all bricks or lose by running out of lives.
 * The game can be restarted or exited based on player input.
 * @see danogl.GameManager
 * @see danogl.gui.WindowController
 * @see danogl.gui.UserInputListener
 * @see danogl.gui.SoundReader
 * @see danogl.gui.ImageReader
 * @see danogl.util.Vector2
 * @see bricker.gameobjects.Ball
 * @see bricker.gameobjects.Paddle
 * @see bricker.gameobjects.Brick
 * @see bricker.gameobjects.LivesDisplay
 * @see bricker.strategies.CollisionStrategy
 * @see bricker.strategies.BasicCollisionStrategy
 * @author Aron Isaacs
 */
public class BrickerGameManager extends GameManager {

    private static final float BORDER_THICKNESS = 5f;

    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
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

    /**
     * Constructor for BrickerGameManager.
     * @param WINDOW_TITLE the title of the game window
     * @param windowDimensions the dimensions of the game window
     * @param numBricksPerRow the number of bricks per row
     * @param numRows the number of rows of bricks
     */
    public BrickerGameManager(String WINDOW_TITLE, Vector2 windowDimensions, int numBricksPerRow, int numRows) {
        super(WINDOW_TITLE, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.numBricksPerRow = numBricksPerRow;
        this.numRows = numRows;
    }
    /**
     * The main method to start the Bricker game.
     * Accepts optional command-line arguments for the number of bricks per row and the number of rows.
     * If no arguments are provided, default values are used.
     * @param args command-line arguments: [numBricksPerRow, numRows]
     */
    public static void main(String[] args) {
        int numBricksPerRow = Brick.DEFAULT_NUM_BRICKS_PER_ROW;
        int numRows = Brick.DEFAULT_NUM_ROWS;
        if (args.length == 2) {
            numBricksPerRow = Integer.parseInt(args[0]);
            numRows = Integer.parseInt(args[1]);
        }
        new BrickerGameManager(WINDOW_TITLE, WINDOW_DIMENSIONS, numBricksPerRow, numRows).run();
    }

    /**
     * Initializes the game by setting up the game window, creating game objects,
     * and preparing the game state.
     * @param imageReader    used to read images for rendering game objects
     * @param soundReader    used to read sounds for game events
     * @param inputListener  listens for user input (keyboard/mouse)
     * @param windowController controls the game window (open, close, reset)
     */
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

    /*
     * Creates and initializes all game objects including the background, borders, paddle, ball, bricks,
     * and the game state. Sets up collision strategies and the lives display.
     */
    private void makeGameObjects() {
        makeBackground();
        makeBorders();
        makeBall();
        makePaddle();
        makeBricks();
        gameState = new GameState(GameState.INITIAL_LIVES, numBricksPerRow * numRows);
        livesDisplay = new LivesDisplay(imageReader, this::addGameObject);
    }

    /*
     * Creates the brick layout based on the specified number of rows and bricks per row.
     * Each brick is assigned a collision strategy to handle interactions with the ball.
     * @param collisionStrategy the strategy to apply when a brick collides with another object
     */
    private void makeBricks() {
        // Calculate brick width based on available space and gaps
        float totalGap = 2 * BORDER_THICKNESS + (numBricksPerRow - 1) * Brick.BRICK_GAP;
        float brickWidth = (windowDimensions.x() - totalGap) / numBricksPerRow;
        Renderable brickImage = imageReader.readImage(Brick.BRICK_IMAGE_PATH, false);
        // Use a basic collision strategy for bricks. main part to be changed for the final part of the
        // assignment!!
        CollisionStrategy collisionStrategy = new BasicCollisionStrategy(this);
        // Create bricks in a grid layout
        for (int row = 0; row < numRows; row++) {
            float y = BORDER_THICKNESS + row * (Brick.BRICK_HEIGHT + Brick.BRICK_GAP);
            for (int col = 0; col < numBricksPerRow; col++) {
                makeBrick(collisionStrategy, col, brickWidth,  y,  brickImage);
            }
        }
    }

    /*
     * Creates a single brick at the specified column and row position with the given dimensions and image.
     * @param collisionStrategy the strategy to apply when this brick collides with another object
     * @param col the column index for the brick's position
     * @param brickWidth the width of the brick
     * @param y the y-coordinate for the brick's position
     * @param brickImage the image to use for rendering the brick
     */
    private void makeBrick(CollisionStrategy collisionStrategy,  int col, float brickWidth, float y, Renderable brickImage) {
        // Calculate x position based on column index
        float x = BORDER_THICKNESS + col * (brickWidth + Brick.BRICK_GAP);
        GameObject brick = new Brick(new Vector2(x, y), new Vector2(brickWidth, Brick.BRICK_HEIGHT),
                brickImage, collisionStrategy);
        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
    }
    /*
     * Creates the borders around the game window to contain the ball and paddle within the playable area.
     * Borders are created on the left, right, and top sides of the window.
     */
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

    /*
     * Creates the paddle object, positions it near the bottom of the window, and sets its dimensions and image.
     * The paddle is controlled by user input to move left and right.
     */
    private void makePaddle() {
        Renderable paddleImage = imageReader.readImage(Paddle.PADDLE_IMAGE_PATH, true);
        Vector2 initialPosition = new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - Paddle.PADDLE_OFFSET_FROM_BOTTOM);
        GameObject paddle = new Paddle(initialPosition, new Vector2(Paddle.PADDLE_WIDTH,
                Paddle.PADDLE_HEIGHT),
                windowDimensions , paddleImage
                , inputListener);
        gameObjects().addGameObject(paddle);
    }

    /*
     * Creates the ball object, sets its initial position and velocity, and adds it to the game.
     * The ball will bounce off walls, the paddle, and bricks, and its behavior is managed by collision detection.
     */
    private void makeBall() {
        Renderable ballImage = imageReader.readImage(Ball.BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(Ball.BALL_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(Ball.BALL_SIZE, Ball.BALL_SIZE), ballImage,
                collisionSound);
        resetBall();
        gameObjects().addGameObject(ball);
    }
    /*
     * Resets the ball's position to the center of the window and assigns it a random initial velocity.
     * The ball will start moving in a random direction when reset.
     */
    private void resetBall() {
        ball.setCenter(windowDimensions.mult(0.5f));
        Random random = new Random();
        float ballSpeedX = Ball.BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        float ballSpeedY = Ball.BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        ball.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
    }
    /*
     * Creates and sets the background image for the game window.
     * The background image is scaled to fit the entire window dimensions.
     */
    private void makeBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowController.getWindowDimensions(), backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Adds a game object to the specified layer in the game.
     * @param object the game object to add
     * @param layer the layer to which the object should be added
     */
    public void addGameObject(GameObject object, int layer) {
        gameObjects().addGameObject(object, layer);
    }

    /**
     * Removes a game object from the specified layer in the game.
     * @param object the game object to remove
     * @param layer the layer from which the object should be removed
     */
    public void removeGameObject(GameObject object, int layer) {
        gameObjects().removeGameObject(object, layer);
    }

    /**
     * Removes a brick from the game and decrements the brick counter in the game state.
     * @param brick the brick game object to remove
     */
    public void removeBrick(GameObject brick) {
        gameObjects().removeGameObject(brick, Layer.STATIC_OBJECTS);
        gameState.decrementBricksCounter();
    }




    /**
     * Updates the game state each frame, checking for victory or loss conditions.
     * If the ball falls below the screen, the player loses a life and the ball is reset.
     * If all bricks are destroyed, the player wins.
     * The game can be restarted or exited based on player input.
     * @param deltaTime the time elapsed since the last update
     */
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
            // Check for game over
            if (!gameState.isGameOver()) {
                resetBall();
            } else {
                showEndGameWindow(LOSE_MESSAGE);
            }
        }
    }

    /*
     * Displays an end-game dialog with the specified message, asking the player if they want to play again.
     * If the player chooses to play again, the game is reset; otherwise, the game window is closed.
     * @param msg the message to display in the dialog
     */
    private void showEndGameWindow(String msg) {
        boolean playAgain = windowController.openYesNoDialog(msg);
        if (playAgain) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }
}