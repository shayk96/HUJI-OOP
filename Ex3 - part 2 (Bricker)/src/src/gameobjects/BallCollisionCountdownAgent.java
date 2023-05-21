package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * An object of this class is instantiated on collision of ball with a brick with a change camera strategy.
 * It checks ball's collision counter every frame, and once it finds the ball has collided countDownValue
 * times since instantiation, it calls the strategy to reset the camera to normal.
 */
public class BallCollisionCountdownAgent extends GameObject {

    private final Ball ball;
    private final ChangeCameraStrategy owner;
    private final int numOfCollisions;
    private final int collisionsSoFar;

    /**
     * creates an instance of the class
     *
     * @param ball           game object representing the game ball
     * @param owner          the object that called the class
     * @param countDownValue number of hits before the camera retest
     */
    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.ball = ball;
        this.owner = owner;
        this.numOfCollisions = countDownValue;
        collisionsSoFar = ball.getCollisionCount();
    }

    /**
     * updates the game
     *
     * @param deltaTime time between refreshes
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCount() == numOfCollisions + collisionsSoFar) {
            owner.turnOffCameraChange();
        }
    }


}
