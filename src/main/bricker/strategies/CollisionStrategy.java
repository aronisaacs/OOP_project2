package main.bricker.strategies;

import danogl.GameObject;
import main.bricker.gameobjects.Brick;

public interface CollisionStrategy {

    void onCollision(GameObject brick, GameObject other);
}
