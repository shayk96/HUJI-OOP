package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;

import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator. Introduces extra paddle to game window
 * which remains until colliding NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with other game objects.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator {

    private static final Random random = new Random();
    private static final int PADDLE_WIDTH = 100;
    private static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 15;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

    /**
     * a constructor creating an instance of the class
     *
     * @param toBeDecorated    the base strategy
     * @param imageReader      a renderer for the image
     * @param inputListener    an input class for receiving input from the player
     * @param windowDimensions the dimensions of the game window
     */
    AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                      UserInputListener inputListener, Vector2 windowDimensions) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * the function determines what happens upon collision with other objects, in this case the brick
     * disappears and a mock paddle appears
     *
     * @param thisObj  the current object AKA brick
     * @param otherObj the ball or a puck
     * @param counter  counter that counts how many bricks are currently in the game
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if (!MockPaddle.isInstantiated) {
            createMockPaddle();
        }

    }

    /**
     * creates and adds the mock paddle to the game objects
     */
    private void createMockPaddle() {
        Renderable paddleImg = imageReader.readImage("assets/paddle.png", true);
        Vector2 paddleDimensions = new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);
        Vector2 paddleFirstLocation = new Vector2(random.nextInt(((int) windowDimensions.x()) -
                PADDLE_WIDTH - BORDER_WIDTH), windowDimensions.y() / 2);
        super.getGameObjectCollection().addGameObject(new MockPaddle(paddleFirstLocation, paddleDimensions,
            paddleImg, inputListener, windowDimensions, super.getGameObjectCollection(), BORDER_WIDTH, 3));
    }
}
