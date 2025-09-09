package main.bricker.gameobjects;


import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.bricker.BrickerGameManager;
import main.bricker.strategies.CollisionStrategy;

public class Brick extends GameObject {
    private CollisionStrategy collisionStrategy;
    private BrickerGameManager brickerGameManager;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, CollisionStrategy collisionStrategy,
                 BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.brickerGameManager =  brickerGameManager;

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
        brickerGameManager.getBrickCounter().decrement();

    }
}
