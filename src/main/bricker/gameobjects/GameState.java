package main.bricker.gameobjects;

import danogl.util.Counter;

public class GameState {
    private final Counter lives;
    private final Counter bricks;


    public GameState(int initialLives, int totalBricks) {
        lives = new Counter(initialLives);
        bricks = new Counter(totalBricks);

    }

    public Counter getLives() { return lives; }
    public Counter getBricks() { return bricks; }

    public boolean isVictory() { return bricks.value() == 0;}
    public boolean isGameOver() { return lives.value() <= 0; }
}


