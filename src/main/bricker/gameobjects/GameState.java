package main.bricker.gameobjects;

import danogl.util.Counter;

public class GameState {
    private  Counter lives;
    private  Counter bricks;

    public GameState(int initialLives, int totalBricks) {
        lives = new Counter(initialLives);
        bricks = new Counter(totalBricks);
    }

    public Counter getBrickCounter() {return bricks;}
    public Counter getLives() { return lives; }
    public boolean isVictory() { return bricks.value() == 0;}
    public boolean isGameOver() { return lives.value() <= 0; }
}


