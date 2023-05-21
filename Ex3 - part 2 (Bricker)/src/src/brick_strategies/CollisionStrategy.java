package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * an interface that dictates what happens upon a collision between two game objects
 */
public interface CollisionStrategy {

    /**
     * the function determines what happens upon collision with other objects, in this case the brick
     * disappears and the camera shifts to following the ball
     *
     * @param thisObj  the current object AKA brick
     * @param otherObj the ball or a puck
     * @param counter  counter that counts how many bricks are currently in the game
     */
    void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);

    /**
     * a getter for the object collection
     *
     * @return the game object collection
     */
    GameObjectCollection getGameObjectCollection();
}
