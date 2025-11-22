package bricker.brick_strategies;
import java.util.Random;
import java.util.random.RandomGenerator;

import bricker.main.BrickerGameManager;

/**
 * A factory class for creating collision strategies.
 * This class can be extended to include methods for creating different types of collision strategies.
 * @author Ron Stein
 */
public class CollisionStrategyFactory{
    private static Random random = new Random();
    private static final int NUM_STRATEGIES = 5;
    private static final int EXTRA_PUCK = 0;
    private static final int EXTRA_PADDLE = 1;
    private static final int EXPLODING_BRICKS = 2;
    private static final int EXTRA_LIFE = 3;
    private static final int DOUBLE_STRATEGY = 4;

    /**
     * Constructs a CollisionStrategyFactory.
     */
    public CollisionStrategyFactory() {}

    /**
     * Builds and returns a basic collision strategy.
     * @return a CollisionStrategy instance.
     */
    public CollisionStrategy buildCollisionStrategy(BrickerGameManager brickerGameManager) {
//        int r = random.nextInt(2 * NUM_STRATEGIES);
        int r = random.nextInt(10);
        CollisionStrategy basicStrategy = new BasicCollisionStrategy(brickerGameManager);
        switch (r) {
            case EXTRA_PUCK:
                return new ExtraPuck(basicStrategy, brickerGameManager);
            case EXTRA_PADDLE:
                return new ExtraPaddle(basicStrategy, brickerGameManager);
            case EXPLODING_BRICKS:
                return new ExplodingBricks(basicStrategy, brickerGameManager);
            case EXTRA_LIFE:
                return new ExtraLife(basicStrategy, brickerGameManager);
//            case DOUBLE_STRATEGY:
////                return new DoubleStrategy(new DoubleStrategy(basicStrategy, brickerGameManager));
            default:
                return basicStrategy;
        }
    }
}
