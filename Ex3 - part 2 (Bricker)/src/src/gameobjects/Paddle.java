package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * creates the paddle game object and maneges it movement
 */
public class Paddle extends GameObject {

    protected static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistanceFromEdge;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner       Position of the object, in window coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of the window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object.
     * @param inputListener       the user input setting the movement direction
     * @param windowDimensions    the window dimensions of the game
     * @param minDistanceFromEdge the minimum distance that the paddle needs to keep from the edge
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    /**
     * updates the paddle status and keeps the paddle from the edge
     *
     * @param deltaTime the interval to wait between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setMovementDirection(); //set movement direction
        checkPaddleBoundaries(); //check paddle boundaries

    }

    /**
     * checks that the paddle is inside the boundaries and of not corrects it
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
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
