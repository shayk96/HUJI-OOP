package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 *
 */
public class MockPaddle extends Paddle {

    public static boolean isInstantiated;
    private int numOfCollision;
    private final GameObjectCollection gameObjectCollection;
    private final int numCollisionsToDisappear;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner       Position of the object, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object.
     * @param inputListener       the user input setting the movement direction
     * @param windowDimensions    the window dimensions of the game
     * @param minDistanceFromEdge the minimum distane that the paddle neest to keep from the edge
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      UserInputListener inputListener, Vector2 windowDimensions,
                      GameObjectCollection gameObjectCollection, int minDistanceFromEdge,
                      int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        this.numCollisionsToDisappear = numCollisionsToDisappear;
        numOfCollision = 0;
        isInstantiated = true;
    }

    /**
     * the function handles what happens upon collision with another object
     *
     * @param other     the object collided with
     * @param collision the class that holds information about the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        numOfCollision++;
        if (numOfCollision == numCollisionsToDisappear) {
            gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
        }
    }

}