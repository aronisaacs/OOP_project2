package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.util.Random;
import static bricker.gameobjects.Ball.BALL_SIZE;
import static bricker.gameobjects.Ball.BALL_SOUND_PATH;

/**
 * A collision strategy that creates two extra pucks when a brick is hit.
 * Extends the CollisionStrategyDecorator to add this behavior on top of an existing strategy.
 * @author Ron Stein
 */
public class ExtraPuck extends CollisionStrategyDecorator {
    public static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    public static final float PUCK_SIZE = BALL_SIZE*0.75f;
    private final Random random = new Random();
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for the ExtraPuck strategy.
     * @param decorated          The CollisionStrategy to be decorated.
     * @param brickerGameManager The game manager to handle brick removal.
     */
    public ExtraPuck(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
            super(decorated);
            this.brickerGameManager = brickerGameManager;
            this.imageReader = brickerGameManager.getImageReader();
            this.soundReader = brickerGameManager.getSoundReader();
    }

    /**
     * Handles the collision event by creating two new pucks at the location of the hit brick.
     * @param thisObj the brick that was collided with
     * @param otherObj  the other game object involved in the collision
     */
    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        makePucks(thisObj);
    }

    /*creates a new pucks in the center of the brick that was hit
     */
    private void makePucks(danogl.GameObject thisObj){
        //creating the puck
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound puckSound = soundReader.readSound(BALL_SOUND_PATH);
        Ball puck1 = new Ball(thisObj.getTopLeftCorner(),
                new Vector2(PUCK_SIZE, PUCK_SIZE),puckImage, puckSound);
        Ball puck2 = new Ball(thisObj.getTopLeftCorner(),
                new Vector2(PUCK_SIZE, PUCK_SIZE),puckImage, puckSound);
        Ball[] pucks = {puck1, puck2};
        for(Ball puck : pucks){
            // this is to distinguish between the main ball and the pucks
            puck.setTag(BrickerGameManager.PUCK_TAG);
            //initializing the location and velocity of the puck
            puck.setCenter(thisObj.getCenter());
            double angle = random.nextDouble() * Math.PI;
            float velocityX = (float)Math.cos(angle)*Ball.BALL_SPEED;
            float velocityY = (float)Math.sin(angle)*Ball.BALL_SPEED;
            puck.setVelocity(new Vector2(velocityX, velocityY));
            this.brickerGameManager.addGameObject(puck, Layer.DEFAULT);
        }
    }
}
