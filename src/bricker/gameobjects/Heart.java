package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Heart extends GameObject {
    private final String mainPaddleTag;
    private final CollisionStrategy collisionStrategy;

    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, String mainPaddleTag,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.mainPaddleTag = mainPaddleTag;
        this.collisionStrategy = collisionStrategy;
    }
    @Override
    public boolean shouldCollideWith(GameObject other){
        return other.getTag().equals(mainPaddleTag);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }
}

