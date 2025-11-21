package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface defining a strategy for handling collisions between game objects.
 * Implementations of this interface can define specific behaviors to execute when a collision occurs.
 * @author Aron Isaacs
 */
public interface CollisionStrategy {

    /**
     * Method to be called when a collision occurs between two game objects.
     * @param thisObj the brick that was collided with
     * @param otherObj  the other game object involved in the collision
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
/*
public interface CollisionStrategy{
void onCollision(GameObject thisObj, GameObject otherObj);}

public class BasicCollisionStrategy implements CollisionStrategy{}

public abstract class CollisionStrategyDecorator implements CollisionStrategy{}

public class ExtraLife extends CollisionStrategyDecorator{}

public class ExplodingBricks extends CollisionStrategyDecorator{}

public class ExtraPuck extends CollisionStrategyDecorator{}

public class ExtraPaddle extends CollisionStrategyDecorator{}

public class DoubleStrategy extends CollisionStrategyDecorator{}

public class CollisionStrategyFactory extends CollisionStrategyDecorator{
}

 */