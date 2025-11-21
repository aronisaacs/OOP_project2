package bricker.brick_strategies;

/**
 * Class CollisionStrategyDecorator is an abstract decorator for CollisionStrategy.
 * It implements the CollisionStrategy interface and holds a reference to a decorated strategy.
 * It delegates the onCollision method to the decorated strategy.
 */
public abstract class CollisionStrategyDecorator implements CollisionStrategy{

    protected CollisionStrategy decoratedStrategy;

    public CollisionStrategyDecorator(CollisionStrategy decoratedStrategy) {
        this.decoratedStrategy = decoratedStrategy;
    }

    @Override
    public void onCollision(danogl.GameObject thisObj, danogl.GameObject otherObj) {
        decoratedStrategy.onCollision(thisObj, otherObj);
    }
}
