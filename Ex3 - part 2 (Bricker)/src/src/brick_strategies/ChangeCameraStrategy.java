package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator. Changes camera focus from ground to ball
 * until ball collides NUM_BALL_COLLISIONS_TO_TURN_OFF times.
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {

    private final WindowController windowController;
    private final BrickerGameManager gameManager;
    private static BallCollisionCountdownAgent ballCounter;


    /**
     * creates an instance of the class
     *
     * @param toBeDecorated    the base strategy
     * @param windowController controls the camera
     * @param gameManager      the manager of the game. runs the game
     */
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                                BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.windowController = windowController;
        this.gameManager = gameManager;
    }

        /**
         * the function determines what happens upon collision with other objects, in this case the brick
         * disappears and the camera shifts to following the ball
         *
         * @param thisObj  the current object AKA brick
         * @param otherObj the ball or a puck
         * @param counter  counter that counts how many bricks are currently in the game
         */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (gameManager.getCamera() == null) {
            ballCounter = new BallCollisionCountdownAgent((Ball) otherObj, this, 4);
            getGameObjectCollection().addGameObject(ballCounter);
            Camera ballCamera = new Camera(otherObj, Vector2.ZERO, windowController.getWindowDimensions().mult(1.2f),
                    windowController.getWindowDimensions());
            gameManager.setCamera(ballCamera);
        }
    }

    /**
     * switches the camera to its regular view
     */
    public void turnOffCameraChange() {
        gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(ballCounter);
    }

}

