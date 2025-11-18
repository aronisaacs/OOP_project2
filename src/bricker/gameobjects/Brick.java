package bricker.gameobjects;


import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.brick_strategies.CollisionStrategy;

/**
 * A brick in the Bricker game. When hit by a ball, it uses its collision strategy to determine what happens.
 * It also notifies the game manager to decrement the brick counter.
 * @see danogl.GameObject
 * @see bricker.brick_strategies.CollisionStrategy
 * @see bricker.main.BrickerGameManager
 * @author Aron Isaacs
 */
public class Brick extends GameObject {
    public static final float BRICK_GAP = 3f; // Gap between bricks
    public static final int DEFAULT_NUM_BRICKS_PER_ROW = 8;
    public static final int DEFAULT_NUM_ROWS = 7;
    public static final float BRICK_HEIGHT = 15f;
    public static final String BRICK_IMAGE_PATH = "assets/brick.png";
	private final int row;
	private final int col;
	private final CollisionStrategy collisionStrategy;

    /**
     * Constructs a Brick object with specified position, size, renderable, collision strategy, and game manager.
     * @param topLeftCorner the top-left corner position of the brick.
     * @param dimensions the dimensions (width and height) of the brick.
     * @param renderable the visual representation of the brick.
     * @param collisionStrategy the strategy to execute upon collision.
     */
    public Brick(int row, int col, Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
		this.row = row;
		this.col = col;
		this.collisionStrategy = collisionStrategy;
    }



    /**
     * Handles collision events by executing the brick's collision strategy
     * and notifying the game manager to decrement the brick counter.
     * @param other the other game object involved in the collision.
     * @param collision the collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }

	/**
	 * Get the row of the brick
	 * @return the row of the brick
	 */
	public int getRow() {
		return row;
	}
/**
	 * Get the column of the brick
	 * @return the column of the brick
	 */
	public int getCol() {
		return col;
	}
}
