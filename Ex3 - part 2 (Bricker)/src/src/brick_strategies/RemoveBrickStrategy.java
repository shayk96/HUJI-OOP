package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Concrete brick strategy implementing CollisionStrategy interface. Removes holding brick on collision.
 */
public class RemoveBrickStrategy implements CollisionStrategy{

    private boolean first = true;
    private final GameObjectCollection gameObjectCollection;

    /**
     * creates an instance of the class
     *
     * @param gameObjectCollection the collection of game objects
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection){
        this.gameObjectCollection = gameObjectCollection;
    }


    /**
     * removes the brick from the game and decreases the number of bricks in the game by one
     *
     * @param thisObj  the current object AKA brick
     * @param otherObj the ball or a puck
     * @param counter  counter that counts how many bricks are currently in the game
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        if(first){
            first = false;
            gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
            counter.decrement();
        }
    }

    /**
     * a getter for the game objects collection
     *
     * @return collection of the game objects
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjectCollection;
    }
}
