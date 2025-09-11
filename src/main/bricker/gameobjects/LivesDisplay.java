
package main.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.TextRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.bricker.BrickerGameManager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;




public class LivesDisplay extends GameObject {
    private final List<GameObject> hearts = new ArrayList<>();
    private final GameObject numberDisplay;
    private final TextRenderable textRenderable;
    private final Renderable heartImage;
    private final BrickerGameManager gameManager;

    private final Vector2 heartSize = new Vector2(30, 30);
    private final Vector2 heartStartPos = new Vector2(20, 20);
    private final float heartSpacing = 35f;

    public LivesDisplay(int initialLives, ImageReader imageReader, BrickerGameManager gameManager) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.gameManager = gameManager;

        // Load assets
        heartImage = imageReader.readImage("assets/heart.png", true);
        textRenderable = new TextRenderable(String.valueOf(initialLives));
        textRenderable.setColor(getColorForLives(initialLives));

        numberDisplay = new GameObject(
                new Vector2(heartStartPos.x(), heartStartPos.y() + 40),
                new Vector2(50, 30),
                textRenderable
        );
        gameManager.addGameObject(numberDisplay, Layer.UI);

        // Create hearts (all added once)
        for (int i = 0; i < initialLives; i++) {
            Vector2 pos = heartStartPos.add(new Vector2(i * heartSpacing, 0));
            GameObject heart = new GameObject(pos, heartSize, heartImage);
            hearts.add(heart);
            gameManager.addGameObject(heart, Layer.UI);
        }
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

    private Color getColorForLives(int lives) {
        if (lives >= 3) return Color.GREEN;
        if (lives == 2) return Color.YELLOW;
        return Color.RED;
    }
}
