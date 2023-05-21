package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * a class that dictates what happens upon a collision between two game objects
 */
public class CollisionStrategy {

    private final GameObjectCollection gameObjects;

    /**
     * the constructor for the class
     *
     * @param gameObjects holds the game objects
     */
    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * the function deletes the object that calls the function
     *
     * @param thisObj  the object that will be removed
     * @param otherObj not in use yet
     * @param counter  counts how many bricks are left
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
        counter.decrement();
    }
}
