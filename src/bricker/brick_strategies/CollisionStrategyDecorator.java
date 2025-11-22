package bricker.brick_strategies;

/**
 * Class CollisionStrategyDecorator is an abstract decorator for CollisionStrategy.
 * It implements the CollisionStrategy interface and holds a reference to a decorated strategy.
 * It delegates the onCollision method to the decorated strategy.
 * @author Ron Stein
 */
public abstract class CollisionStrategyDecorator implements CollisionStrategy{
    protected CollisionStrategy decoratedStrategy;

    /**
     * Constructor for CollisionStrategyDecorator.
     * @param decoratedStrategy the CollisionStrategy to be decorated.
     */
    public CollisionStrategyDecorator(CollisionStrategy decoratedStrategy) {
        this.decoratedStrategy = decoratedStrategy;
    }

    /**
     * Delegates the onCollision method to the decorated strategy.
     * @param thisObj  the brick that was collided with
     * @param otherObj the other game object involved in the collision
     */
    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        decoratedStrategy.onCollision(thisObj, otherObj);
    }
}
