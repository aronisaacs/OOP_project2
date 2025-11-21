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

public class ExtraPuck extends CollisionStrategyDecorator {
    public static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    public static final float PUCK_SIZE = BALL_SIZE*0.75f;
    private final Random random = new Random();
    private ImageReader imageReader;
    private SoundReader soundReader;
    private BrickerGameManager brickerGameManager;

    public ExtraPuck(CollisionStrategy decorated, BrickerGameManager brickerGameManager) {
            super(decorated);
            this.brickerGameManager = brickerGameManager;
            this.imageReader = brickerGameManager.getImageReader();
            this.soundReader = brickerGameManager.getSoundReader();
    }

    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        //creating a new puck
        makePuck(thisObj);
    }
    /*  * creates a new puck in the center of the brick that was hit
     */
    private void makePuck(danogl.GameObject thisObj){
        //creating the puck
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound puckSound = soundReader.readSound(BALL_SOUND_PATH);
        Ball puck = new Ball(thisObj.getTopLeftCorner(), new Vector2(PUCK_SIZE, PUCK_SIZE),puckImage, puckSound);

        //initializing the location and velocity of the puck
        puck.setCenter(thisObj.getCenter());
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float)Math.cos(angle)*Ball.BALL_SPEED;
        float velocityY = (float)Math.sin(angle)*Ball.BALL_SPEED;
        puck.setVelocity(new Vector2(velocityX, velocityY));
        this.brickerGameManager.addGameObject(puck, Layer.DEFAULT);
    }
}
