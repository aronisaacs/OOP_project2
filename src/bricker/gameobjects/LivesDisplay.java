
package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.TextRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


/**
 * Displays the player's remaining lives using heart icons and a numeric display.
 * Hearts are shown or hidden based on the current number of lives.
 * The numeric display changes color based on the number of lives:
 * green for 3 or more, yellow for 2, and red for 1 or fewer.
 * @see danogl.GameObject
 * @author Aron Isaacs
 */
public class LivesDisplay extends GameObject {
    public static final String HEART_IMAGE = "assets/heart.png";
    public static final Vector2 HEART_SIZE = new Vector2(30, 30);
    private static final Vector2 HEART_START_POS = new Vector2(20, 20);
    private static final float HEART_SPACING = 35f;
    private static final Vector2 NUMBER_DISPLAY_SIZE = new Vector2(50, 30);
    private final Renderable heartImage;
    private final TextRenderable textRenderable;
    private final List<GameObject> hearts = new ArrayList<>();

    /**
     * Constructs a LivesDisplay with the specified initial lives, image reader, and game manager.
     * @param imageReader the ImageReader to load the heart image.
     * @param addObject a BiConsumer to add game objects to the game (used to add hearts and text).
     */
    public LivesDisplay(ImageReader imageReader, BiConsumer<GameObject,Integer> addObject) {
        super(Vector2.ZERO, Vector2.ZERO, null);

        // Load assets
        heartImage = imageReader.readImage(HEART_IMAGE, true);
        textRenderable = new TextRenderable(String.valueOf(GameState.INITIAL_LIVES));
        textRenderable.setColor(getColorForLives(GameState.INITIAL_LIVES));

        GameObject numberDisplay = new GameObject(
                new Vector2(HEART_START_POS.x(), HEART_START_POS.y() + HEART_SIZE.y() + 10),
                NUMBER_DISPLAY_SIZE, textRenderable
        );
        addObject.accept(numberDisplay, Layer.UI);

        // Create hearts (all added once)
        for (int i = 0; i < GameState.MAX_LIVES; i++) {
            Vector2 pos = HEART_START_POS.add(new Vector2(i * HEART_SPACING, 0));
            GameObject heart = new GameObject(pos, HEART_SIZE, null);
            hearts.add(heart);
            addObject.accept(heart, Layer.UI);
        }
        updateLives(GameState.INITIAL_LIVES);
    }

    /**
     * Update display: toggle heart visibility and update text.
     */
    public void updateLives(int newLives) {
        for (int i = 0; i < hearts.size(); i++) {
            if (i < newLives) {
                hearts.get(i).renderer().setRenderable(heartImage); // show
            } else {
                hearts.get(i).renderer().setRenderable(null);       // hide
            }
        }

        textRenderable.setString(String.valueOf(newLives));
        textRenderable.setColor(getColorForLives(newLives));
    }

    /*
     * Determines the color for the numeric display based on the number of lives.
     */
    private Color getColorForLives(int lives) {
        if (lives >= 3) return Color.GREEN;
        if (lives == 2) return Color.YELLOW;
        return Color.RED;
    }
}
