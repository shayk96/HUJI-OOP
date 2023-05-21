package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Abstract decorator to add functionality to the remove brick strategy, following the decorator pattern.
 * All strategy decorators inherit from this class.
 */
public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy {

    private final CollisionStrategy strategy;

    /**
     * a constructior creating an instance of the class
     *
     * @param strategy the base collision strategy
     */
    public RemoveBrickStrategyDecorator(CollisionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * activates the base strategy onCollision method
     *
     * @param thisObj  the current object AKA brick
     * @param otherObj the ball or a puck
     * @param counter  counter that counts how many bricks are currently in the game
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        strategy.onCollision(thisObj, otherObj, counter);
    }

    /**
     * a getter for the game object collection
     *
     * @return the game object collection
     */
    public GameObjectCollection getGameObjectCollection() {
        return strategy.getGameObjectCollection();
    }
}
