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
    private int lives;
    private final int maxLives;
    private final Vector2 heartSize = new Vector2(30, 30);
    private final Vector2 heartStartPos = new Vector2(20, 20);
    private final float heartSpacing = 35f;
    private final BrickerGameManager gameManager;

    public LivesDisplay(int initialLives, int maxLives, ImageReader imageReader, Vector2 windowDimensions, BrickerGameManager gameManager) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.lives = initialLives;
        this.maxLives = maxLives;
        this.gameManager = gameManager;

        Renderable heartImage = imageReader.readImage("assets/heart.png", true);

        for (int i = 0; i < maxLives; i++) {
            Vector2 pos = heartStartPos.add(new Vector2(i * heartSpacing, 0));
            GameObject heart = new GameObject(pos, heartSize, heartImage);
            hearts.add(heart);
        }

        textRenderable = new TextRenderable(String.valueOf(lives));

        textRenderable.setColor(getColorForLives(lives));
        numberDisplay = new GameObject(new Vector2(heartStartPos.x(), heartStartPos.y() + 40), new Vector2(50, 30), textRenderable);
    }

    public void addToGameObjects() {
        for (int i = 0; i < lives; i++) {
            gameManager.addGameObject(hearts.get(i), Layer.UI);
        }
        gameManager.addGameObject(numberDisplay, Layer.UI);
    }

    public void updateLives(int newLives) {
        if (newLives < lives) {
            for (int i = newLives; i < lives; i++) {
                gameManager.removeGameObject(hearts.get(i), Layer.UI);
            }
        } else if (newLives > lives) {
            for (int i = lives; i < newLives && i < maxLives; i++) {
                gameManager.addGameObject(hearts.get(i), Layer.UI);
            }
        }
        lives = newLives;
        textRenderable.setString(String.valueOf(lives));
        textRenderable.setColor(getColorForLives(lives));
    }

    private Color getColorForLives(int lives) {
        if (lives >= 3) return Color.GREEN;
        if (lives == 2) return Color.YELLOW;
        return Color.RED;
    }
}