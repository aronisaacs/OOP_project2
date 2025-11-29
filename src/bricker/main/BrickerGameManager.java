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
import bricker.brick_strategies.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;


/**
 * The main class of the Bricker game, responsible for initializing and managing the game state.
 * It sets up the game window, creates game objects, and handles game updates.
 * The game features a paddle, ball, bricks, and borders, with collision detection and game state management.
 * Players can win by destroying all bricks or lose by running out of lives.
 * The game can be restarted or exited based on player input.
 *
 * @author Aron Isaacs
 * @author Ron Stein
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
 * @see bricker.brick_strategies.CollisionStrategy
 * @see bricker.brick_strategies.BasicCollisionStrategy
 */
public class BrickerGameManager extends GameManager {
	/*Tags used to identify game objects */
	private static final String PUCK_TAG = "puck";
	private static final String HEART_TAG = "heart";
	private static final String BORDER_TAG = "border";
	private static final String MAIN_PADDLE_TAG = "main paddle";
	private static final String EXPLODED_TAG = "exploded";
	private static final String BRICK_TAG = "brick";
	/*Paths to relevant files */
	private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
	private static final String HEART_IMAGE_PATH = "assets/heart.png";
	private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
	private static final String EXPLODE_SOUND_PATH = "assets/explosion.wav";
	private static final String BALL_SOUND_PATH = "assets/blop.wav";
	private static final String BALL_IMAGE_PATH = "assets/ball.png";
	private static final String BRICK_IMAGE_PATH = "assets/brick.png";
	private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
	/*Messages for UI dialogs*/
	private static final String WIN_MESSAGE = "You win! Play again?";
	private static final String WINDOW_TITLE = "Bricker";
	private static final String LOSE_MESSAGE = "You lose! Play again?";
	/*Paddle constants*/
	private static final float PADDLE_HEIGHT = 15f;
	private static final float PADDLE_WIDTH = 100f;
	private static final float PADDLE_OFFSET_FROM_BOTTOM = 100f;
	private static final int SUB_PADDLES_MAX_HITS = 4;
	private static final int INITIAL_PADDLES = 0;
	/*Ball Brick Heart constants*/
	private static final float BALL_SIZE = 50f;
	private static final float PUCK_SIZE = BALL_SIZE * 0.75f;
	private static final float BALL_SPEED = 200f;
	private static final float BRICK_GAP = 3f; // Gap between bricks
	private static final int DEFAULT_NUM_BRICKS_PER_ROW = 8;
	private static final int DEFAULT_NUM_ROWS = 6;
	private static final float BRICK_HEIGHT = 15f;
	private static final int NUM_NEIGHBORS = 4;
	private static final float HALF = 0.5f;
	private final int numRows;
	private final int numBricksPerRow;
	private static final Vector2 HEART_SIZE = new Vector2(30, 30);
	private static final Vector2 HEART_VELOCITY = new Vector2(0, 100f);
	/*Game constants*/
	private static final float BORDER_THICKNESS = 5f;
	private static final Vector2 WINDOW_DIMENSIONS = new Vector2(800, 600);
	private static final int MAX_LIVES = 4;
	private static final int INITIAL_LIVES = 3;
	private final Vector2 windowDimensions;
	private final Random random = new Random();
	private SoundReader soundReader;
	private UserInputListener inputListener;
	private ImageReader imageReader;
	private WindowController windowController;
	private Ball ball;
	private GameState gameState;
	private LivesDisplay livesDisplay;

	/**
	 * The main method to start the Bricker game.
	 * Accepts optional command-line arguments for the number of bricks per row and the number of rows.
	 * If no arguments are provided, default values are used.
	 *
	 * @param args command-line arguments: [numBricksPerRow, numRows]
	 */
	public static void main(String[] args) {
		int numBricksPerRow = DEFAULT_NUM_BRICKS_PER_ROW;
		int numRows = DEFAULT_NUM_ROWS;
		if (args.length == 2) {
			numBricksPerRow = Integer.parseInt(args[0]);
			numRows = Integer.parseInt(args[1]);
		}
		new BrickerGameManager(WINDOW_TITLE, WINDOW_DIMENSIONS, numBricksPerRow, numRows).run();
	}

	/**
	 * Constructor for BrickerGameManager.
	 *
	 * @param WINDOW_TITLE     the title of the game window
	 * @param windowDimensions the dimensions of the game window
	 * @param numBricksPerRow  the number of bricks per row
	 * @param numRows          the number of rows of bricks
	 */
	public BrickerGameManager(String WINDOW_TITLE, Vector2 windowDimensions,
							  int numBricksPerRow, int numRows) {
		super(WINDOW_TITLE, windowDimensions);
		this.windowDimensions = windowDimensions;
		this.numBricksPerRow = numBricksPerRow;
		this.numRows = numRows;
	}

	/**
	 * Initializes the game by setting up the game window, creating game objects,
	 * and preparing the game state.
	 *
	 * @param imageReader      used to read images for rendering game objects
	 * @param soundReader      used to read sounds for game events
	 * @param inputListener    listens for user input (keyboard/mouse)
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
	 * and the game state. Sets up collision brick_strategies and the lives display.
	 */
	private void makeGameObjects() {
		makeBackground();
		makeBorders();
		gameState = new GameState(INITIAL_LIVES,
				numBricksPerRow * numRows, INITIAL_PADDLES, MAX_LIVES);
		makeBall();
		makePaddle(windowDimensions.y() - PADDLE_OFFSET_FROM_BOTTOM);
		makeBricks();
		livesDisplay = new LivesDisplay(imageReader, this, INITIAL_LIVES,
				MAX_LIVES, HEART_IMAGE_PATH, HEART_SIZE);
	}

	/*
	 * Creates the brick layout based on the specified number of rows and bricks per row.
	 * Each brick is assigned a collision strategy to handle interactions with the ball.
	 * @param collisionStrategy the strategy to apply when a brick collides with another object
	 */
	private void makeBricks() {
		// Calculate brick width based on available space and gaps
		float totalGap = 2 * BORDER_THICKNESS + (numBricksPerRow - 1) * BRICK_GAP;
		float brickWidth = (windowDimensions.x() - totalGap) / numBricksPerRow;
		Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
		// Use a basic collision strategy for bricks. main part to be changed for the final part of the
		// assignment!!
		CollisionStrategyFactory collisionStrategyFactory = new CollisionStrategyFactory();
		CollisionStrategy basicStrategy = new BasicCollisionStrategy(this);
		// Create bricks in a grid layout
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numBricksPerRow; col++) {
				makeBrick(collisionStrategyFactory.buildCollisionStrategy(basicStrategy,
						this), col, brickWidth, row, brickImage);
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
	private void makeBrick(CollisionStrategy collisionStrategy, int col, float brickWidth, int row,
						   Renderable brickImage) {
		// Calculate x position based on column index
		float x = BORDER_THICKNESS + col * (brickWidth + BRICK_GAP);
		float y = BORDER_THICKNESS + row * (BRICK_HEIGHT + BRICK_GAP);
		GameObject brick = new Brick(row, col, new Vector2(x, y), new Vector2(brickWidth, BRICK_HEIGHT),
				brickImage, collisionStrategy);
		gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
		brick.setTag(BRICK_TAG);
	}

	/*
	 * Creates the borders around the game window to contain the ball and paddle within the playable area.
	 * Borders are created on the left, right, and top sides of the window.
	 */
	private void makeBorders() {
		Renderable wallImage = new RectangleRenderable(Color.CYAN);
		GameObject leftBorder = new GameObject(Vector2.ZERO, new Vector2(BORDER_THICKNESS,
				windowDimensions.y()), wallImage);
		gameObjects().addGameObject(leftBorder);
		GameObject rightBorder = new GameObject(
				new Vector2(windowDimensions.x() - BORDER_THICKNESS, 0),
				new Vector2(BORDER_THICKNESS, windowDimensions.y()), wallImage);
		gameObjects().addGameObject(rightBorder);
		GameObject topBorder = new GameObject(Vector2.ZERO,
				new Vector2(windowDimensions.x(), BORDER_THICKNESS),
				wallImage);
		gameObjects().addGameObject(topBorder);
		GameObject[] borders = {leftBorder, rightBorder, topBorder};
		for (GameObject border : borders) {
			border.setTag(BORDER_TAG);
		}
	}

	/**
	 * Creates the paddle object at the specified Y position and adds it to the game.
	 * Paddles are always created in the middle of the X axis.
	 *
	 * @param paddleYPosition the Y position where the paddle should be created
	 */
	public void makePaddle(float paddleYPosition) {
		if (gameState.getPaddlesCounter() >= 2) {
			return;
		}
		Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
		Vector2 initialPosition = new Vector2(windowDimensions.x() / 2f, paddleYPosition);
		if (gameState.getPaddlesCounter() == 0) {
			GameObject paddle = new Paddle(initialPosition, new Vector2(PADDLE_WIDTH,
					PADDLE_HEIGHT),
					windowDimensions, paddleImage
					, inputListener);
			paddle.setTag(MAIN_PADDLE_TAG);
			gameObjects().addGameObject(paddle);
		} else {
			GameObject subPaddle = new SubPaddle(initialPosition, new Vector2(PADDLE_WIDTH,
					PADDLE_HEIGHT),
					windowDimensions, paddleImage
					, inputListener, this, BORDER_TAG);
			gameObjects().addGameObject(subPaddle);
		}
		gameState.incrementPaddlesCounter();
	}

	/*
	 * Creates the ball object, sets its initial position and velocity, and adds it to the game.
	 * The ball will bounce off walls, the paddle, and bricks, and its behavior
	 * is managed by collision detection.
	 */
	private void makeBall() {
		Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
		Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
		ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage,
				collisionSound);
		resetBall();
		gameObjects().addGameObject(ball);
	}

	/**
	 * Creates two puck objects at the location of the specified game object.
	 * Each puck is given a random initial velocity and added to the game.
	 *
	 * @param thisObj brick that was hit and where the pucks will be created
	 */
	public void makePucks(danogl.GameObject thisObj) {
		//creating the puck
		Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
		Sound puckSound = soundReader.readSound(BALL_SOUND_PATH);
		Ball puck1 = new Ball(thisObj.getTopLeftCorner(),
				new Vector2(PUCK_SIZE, PUCK_SIZE), puckImage, puckSound);
		Ball puck2 = new Ball(thisObj.getTopLeftCorner(),
				new Vector2(PUCK_SIZE, PUCK_SIZE), puckImage, puckSound);
		Ball[] pucks = {puck1, puck2};
		for (Ball puck : pucks) {
			// this is to distinguish between the main ball and the pucks
			puck.setTag(BrickerGameManager.PUCK_TAG);
			//initializing the location and velocity of the puck
			puck.setCenter(thisObj.getCenter());
			double angle = random.nextDouble() * Math.PI;
			float velocityX = (float) Math.cos(angle) * BALL_SPEED;
			float velocityY = (float) Math.sin(angle) * BALL_SPEED;
			puck.setVelocity(new Vector2(velocityX, velocityY));
			addGameObject(puck, Layer.DEFAULT);
		}
	}

	/**
	 * Spawns a heart object centered at the specified brick center position.
	 * The heart will move downwards with a predefined velocity.
	 *
	 * @param brickCenter the center position of the brick where the heart should be spawned
	 */
	public void spawnHeart(Vector2 brickCenter) {
		Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
		float x = brickCenter.x() - HEART_SIZE.x() / 2f;
		float y = brickCenter.y() - HEART_SIZE.y() / 2f;
		Heart heart = new Heart(new Vector2(x, y), HEART_SIZE,
				heartImage, MAIN_PADDLE_TAG, new HeartCollisionStrategy(this), HEART_TAG);
		addGameObject(heart, Layer.DEFAULT);
		heart.setVelocity(HEART_VELOCITY);
	}

	/*
	 * Resets the ball's position to the center of the window and assigns it a random initial velocity.
	 * The ball will start moving in a random direction when reset.
	 */
	private void resetBall() {
		ball.setCenter(windowDimensions.mult(HALF));
		Random random = new Random();
		float ballSpeedX = BALL_SPEED * (random.nextBoolean() ? 1 : -1);
		float ballSpeedY = BALL_SPEED * (random.nextBoolean() ? 1 : -1);
		ball.setVelocity(new Vector2(ballSpeedX, ballSpeedY));
	}

	/*
	 * Creates and sets the background image for the game window.
	 * The background image is scaled to fit the entire window dimensions.
	 */
	private void makeBackground() {
		Renderable backgroundImage =
				imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
		GameObject background =
				new GameObject(Vector2.ZERO, windowController.getWindowDimensions(), backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}

	/**
	 * Adds a game object to the specified layer in the game.
	 *
	 * @param object the game object to add
	 * @param layer  the layer to which the object should be added
	 */
	public void addGameObject(GameObject object, int layer) {
		gameObjects().addGameObject(object, layer);
	}

	/**
	 * Removes a game object from the specified layer in the game.
	 *
	 * @param object the game object to remove
	 * @param layer  the layer from which the object should be removed
	 */
	public void removeGameObject(GameObject object, int layer) {
		gameObjects().removeGameObject(object, layer);
	}

	/**
	 * Removes a brick from the game and decrements the brick counter in the game state.
	 *
	 * @param brick the brick game object to remove
	 */
	public void removeBrick(GameObject brick) {
		boolean removed = gameObjects().removeGameObject(brick, Layer.STATIC_OBJECTS);
		if (removed) {
			gameState.decrementBricksCounter();
		}
	}

	/**
	 * Handles the logic when a sub-paddle is hit by the ball.
	 * Increments the hit counter and removes the sub-paddle if it has been hit the maximum number of times.
	 *
	 * @param subPaddle the sub-paddle game object that was hit
	 */
	public void subPaddleHit(GameObject subPaddle) {
		//remove sub paddle and reset counter after 4 hits
		if (gameState.getSubPaddleHitsCounter() >= SUB_PADDLES_MAX_HITS) {
			gameObjects().removeGameObject(subPaddle);
			gameState.resetSubPaddleHitsCounter();
			gameState.decrementPaddlesCounter();
			//increment subPaddle hits counter if less than 4 hits
		} else {
			gameState.incrementSubPaddleHitsCounter();
		}
	}

	/**
	 * Handles the explosion of bricks when hit by the ball.
	 *
	 * @param brick the center brick that was hit
	 * @param ball  the ball that hit the brick
	 */
	public void explodeBricks(GameObject brick, GameObject ball) {
		if (brick.getTag().equals(EXPLODED_TAG)) {
			return;
		}
		brick.setTag(EXPLODED_TAG);
		Brick myBrick = (Brick) brick;
		Sound explodeSound = soundReader.readSound(EXPLODE_SOUND_PATH);
		explodeSound.play();
		int i = myBrick.getRow();
		int j = myBrick.getCol();
		Brick[] neighbors = new Brick[NUM_NEIGHBORS];
		int index = 0;
		//find neighbors and store them in an array
		for (GameObject go : gameObjects().objectsInLayer(Layer.STATIC_OBJECTS)) {
			if (go.getTag().equals(BRICK_TAG) || go.getTag().equals(EXPLODED_TAG)) {
				Brick nextBrick = (Brick) go;
				//left and right neighbors
				if (nextBrick.getCol() == j && Math.abs(nextBrick.getRow() - i) == 1) {
					neighbors[index] = nextBrick;
					index++;
					//up and down neighbors
				} else if (nextBrick.getRow() == i && Math.abs(nextBrick.getCol() - j) == 1) {
					neighbors[index] = nextBrick;
					index++;
				}
			}
		}
		//call onCollisionEnter on all neighboring Bricks
		for (Brick b : neighbors) {
			if (b == null) continue;
			if (!b.shouldCollideWith(ball)) continue;
			b.onCollisionEnter(ball, null);
		}
	}

	/**
	 * Updates the game state each frame, checking for victory or loss conditions.
	 * If the ball falls below the screen, the player loses a life and the ball is reset.
	 * If all bricks are destroyed, the player wins.
	 * The game can be restarted or exited based on player input.
	 *
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
		//remove any puck or heart objects that have fallen below the screen
		for (GameObject obj : gameObjects().objectsInLayer(Layer.DEFAULT)) {
			String currentTag = obj.getTag();
			if (!currentTag.equals(HEART_TAG) && !currentTag.equals(PUCK_TAG)) continue;
			if (obj.getCenter().y() > windowDimensions.y()) {
				gameObjects().removeGameObject(obj, Layer.DEFAULT);
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

	/**
	 * Gets the dimensions of the game window.
	 *
	 * @return the window dimensions as a Vector2
	 */
	public Vector2 getWindowDimensions() {
		return this.windowDimensions;
	}

	/**
	 * Determines if the player's lives increase by one.
	 */
	public void increaseLives() {
		gameState.incrementLivesCounter();
		livesDisplay.updateLives(gameState.getLivesCounter());
	}
}