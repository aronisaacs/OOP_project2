package bricker.brick_strategies;
import java.util.Random;
import java.util.random.RandomGenerator;

import bricker.main.BrickerGameManager;

/**
 * A factory class for creating collision strategies.
 * This class can be extended to include methods for creating different types of collision strategies.
 * @author Ron Stein
 */
public class CollisionStrategyFactory {
    private static final int DOUBLE_STRAT_COUNT = 2;
    private static Random random = new Random();
    private static final int NUM_STRATEGIES = 5;
    private static final int EXTRA_PUCK = 0;
    private static final int EXTRA_PADDLE = 1;
    private static final int EXPLODING_BRICKS = 2;
    private static final int EXTRA_LIFE = 3;
    private static final int DOUBLE_STRATEGY = 4;
    private static final int MAX_STRATEGIES = 3;

    /**
     * Constructs a CollisionStrategyFactory.
     */
    public CollisionStrategyFactory() {
    }

    /**
     * Builds and returns a basic collision strategy.
     *
     * @return a CollisionStrategy instance.
     */
    public CollisionStrategy buildCollisionStrategy(CollisionStrategy basic,
                                                    BrickerGameManager brickerGameManager) {
        int r = random.nextInt(2 * NUM_STRATEGIES);
        //1/2 chance to return basic strategy. r belongs to [5-9]
        if (r >= NUM_STRATEGIES) {
            return basic;
        //1/10 change to return double strategy. r = 4
        } else if (r == DOUBLE_STRATEGY) {
            return buildDouble(basic, brickerGameManager, new int[] {0});
        //4/10 change to return single special strategy r belongs to [0-3]
        } else {
            return buildSingle(r, basic, brickerGameManager);
        }
    }
    /*  * builds a double strategy by randomly selecting two strategies to decorate the basic strategy.
     *  If one of the selected strategies is a double strategy, it selects another strategy instead.
     *  maximum of three strategies can be decorated.
     */
    private CollisionStrategy buildDouble(CollisionStrategy basic, BrickerGameManager brickerGameManager,
                                          int[] strategyCount) {
        //Will attempt to add two single strategies, or recurse if double strategy is chosen
        for (int i = 0; i < DOUBLE_STRAT_COUNT; i++){
            if (strategyCount[0] >= MAX_STRATEGIES){
                break;
            }
            int r = random.nextInt(NUM_STRATEGIES);

            if(r == DOUBLE_STRATEGY){
                //is there room for two more strategies
                if(strategyCount[0] <= MAX_STRATEGIES - DOUBLE_STRAT_COUNT){
                    basic = buildDouble(basic, brickerGameManager, strategyCount);
                } else {
                    //choose another non double strategy
                    r = random.nextInt(NUM_STRATEGIES - 1);
                    basic = buildSingle(r, basic, brickerGameManager);
                }
            } else {
                basic = buildSingle(r, basic, brickerGameManager);
                strategyCount[0]++;
                }
            }
        return basic;
    }

    private CollisionStrategy buildSingle(int r, CollisionStrategy currentStrategy,
                                          BrickerGameManager brickerGameManager) {
        switch (r) {
            case EXTRA_PUCK:
                return new ExtraPuck(currentStrategy, brickerGameManager);
            case EXTRA_PADDLE:
                return new ExtraPaddle(currentStrategy, brickerGameManager);
            case EXPLODING_BRICKS:
                return new ExplodingBricks(currentStrategy, brickerGameManager);
            case EXTRA_LIFE:
                return new ExtraLife(currentStrategy, brickerGameManager);
            //should never reach here because the random bounds between puck and life
            default:
                return currentStrategy;
        }
    }
}
/*
private CollisionStrategy buildDouble(CollisionStrategy basic, BrickerGameManager brickerGameManager){
        int sOne = random.nextInt(NUM_STRATEGIES);
        int sTwo;
        int sThree;

        //first strategy is not double strategy
        if (sOne != DOUBLE_STRATEGY) {
            CollisionStrategy doubleStrategy = buildSingle(sOne, basic, brickerGameManager);
            sTwo = random.nextInt(NUM_STRATEGIES);
            if (sTwo == DOUBLE_STRATEGY) {
                sTwo = random.nextInt(NUM_STRATEGIES - 1);
                doubleStrategy = buildSingle(sTwo, doubleStrategy, brickerGameManager);
                sThree = random.nextInt(NUM_STRATEGIES - 1);
                return buildSingle(sThree, doubleStrategy, brickerGameManager);
            } else{
                return buildSingle(sTwo, doubleStrategy, brickerGameManager);
            }
        }
        //three strategies that are not double strategy
        else {
            sOne = random.nextInt(NUM_STRATEGIES - 1);
            CollisionStrategy tripleStrategy = buildSingle(sOne, basic, brickerGameManager);
            sTwo = random.nextInt(NUM_STRATEGIES - 1);
            tripleStrategy = buildSingle(sTwo, tripleStrategy, brickerGameManager);
            sThree = random.nextInt(NUM_STRATEGIES - 1);
            return buildSingle(sThree, tripleStrategy, brickerGameManager);
        }
    }
 */