package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


public class BotGood extends GameObject {

    private static final int MOVEMENT_SPEED = 150;
    public static boolean isInstantiated = false;
    private final Vector2 windowDimensions;
    private final int minDistanceFromEdge;
    private final Ball ball;
    private final GameObjectCollection gameObjectCollection;
    private int numOfCollisions = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner       Position of the object, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object.
     * @param windowDimensions    the window dimensions of the game
     * @param minDistanceFromEdge the minimum distane that the paddle neest to keep from the edge
     */
    public BotGood(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Vector2 windowDimensions,
                   int minDistanceFromEdge, Ball ball, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable);
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
        this.ball = ball;
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * updates the game
     *
     * @param deltaTime time between refreshes
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setMovementDirection();
        checkPaddleBoundaries();
    }

    /**
     * dictated what happens upon collision
     *
     * @param other     the object collided with
     * @param collision Stores information regarding a given collision between two GameObject
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball) {
            numOfCollisions++;
        }
        if (numOfCollisions == 5) {
            gameObjectCollection.removeGameObject(this);
            numOfCollisions = 0;
            isInstantiated = false;
        }
    }

    /**
     * checks the location of the paddle and makes sure it doest go off the screen
     */
    private void checkPaddleBoundaries() {
        float paddleLocation = getTopLeftCorner().x();
        float leftBorder = minDistanceFromEdge;
        float rightBorder = windowDimensions.x() - minDistanceFromEdge - getDimensions().x();

        if (paddleLocation < leftBorder) {
            transform().setTopLeftCornerX(leftBorder);
        }
        if (paddleLocation > rightBorder) {
            transform().setTopLeftCornerX(rightBorder);
        }
    }


    /**
     * sets the paddle movement direction
     */
    private void setMovementDirection() {
        Vector2 movementDir = Vector2.ZERO;
        if (ball.getCenter().y() < this.getCenter().y()) {
            if (ball.getCenter().x() < this.getCenter().x()) {
                movementDir = movementDir.add(Vector2.LEFT);
            }
            if (ball.getCenter().x() > this.getCenter().x()) {
                movementDir = movementDir.add(Vector2.RIGHT);
            }
        }
        if (ball.getCenter().y() > this.getCenter().y()) {
            if (ball.getCenter().x() < windowDimensions.x() / 2) {
                movementDir = movementDir.add(Vector2.RIGHT);
            }
            if (ball.getCenter().x() > this.getCenter().x()) {
                movementDir = movementDir.add(Vector2.LEFT);
            }
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
