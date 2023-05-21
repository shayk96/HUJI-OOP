package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * the class creates the ball in the game as a game object
 */
public class Ball extends GameObject {

    private final Sound collisionSound;
    private static final Counter collisionCounter = new Counter();

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object.
     * @param collisionSound The sound to make upon impact with another object
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }


    /**
     * the function changes the direction of the ball upon impact with another object. also, makes a sound
     *
     * @param other     the object the ball collided with
     * @param collision a class holding information about the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (!(other instanceof Ball)) {
            super.onCollisionEnter(other, collision);
            Vector2 newvel1 = getVelocity().flipped(collision.getNormal());
            setVelocity(newvel1);
            collisionSound.play();
            collisionCounter.increment();
        }
    }

    /**
     * a getter for the number of collisions the ball had
     *
     * @return number of collisions the ball had
     */
    public int getCollisionCount() {
        return collisionCounter.value();
    }
}
