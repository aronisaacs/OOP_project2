package main.bricker.strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import main.bricker.BrickerGameManager;


public class BasicCollisionStrategy implements  CollisionStrategy {

    private final BrickerGameManager brickerGameManager;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject brick, GameObject other) {
        System.out.println("collision with brick detected");
        brickerGameManager.removeGameObject(brick, Layer.STATIC_OBJECTS);
    }
}
