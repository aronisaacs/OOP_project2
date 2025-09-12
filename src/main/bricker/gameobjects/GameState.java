package main.bricker.gameobjects;

import danogl.util.Counter;

public class GameState {
    private final Counter lives;
    private final Counter bricks;

    public GameState(int initialLives, int totalBricks) {
        lives = new Counter(initialLives);
        bricks = new Counter(totalBricks);
    }

    public void decrementLivesCounter(){
        lives.decrement();
    }
    public void incrementLivesCounter(){
        lives.increment();
    }

    public void incrementBricksCounter(){
        bricks.increment();
    }

    public void decrementBricksCounter(){
        bricks.decrement();
    }

    public int getBrickCounter() {return bricks.value();}
    public int getLivesCounter() { return lives.value(); }
    public boolean isVictory() { return bricks.value() == 0;}
    public boolean isGameOver() { return lives.value() <= 0; }
}


