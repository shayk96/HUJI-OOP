package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

/**
 * Factory class for creating Collision strategies
 */
public class BrickStrategyFactory {

    private static final Random random = new Random();
    private final GameObjectCollection gameObjectCollection;
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final Vector2 windowDimensions;

    /**
     * creates an instance of the class
     *
     * @param gameObjectCollection the collection of the game objects
     * @param gameManager          the manager of the game. runs the game
     * @param imageReader          a renderer for the image
     * @param soundReader          a renderer for the sound
     * @param inputListener        an input class for receiving input from the player
     * @param windowController     controls the camera
     * @param windowDimensions     the dimensions of the game window
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                BrickerGameManager gameManager,
                                ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener inputListener,
                                WindowController windowController,
                                Vector2 windowDimensions) {
        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
    }

    /**
     * recursive functions that randomizes the strategy for each brick
     *
     * @param Strategy        the strategy of the brick
     * @param randomMin       the lower limit of the random generator
     * @param randomMax       the max limit of the random generator
     * @param numOfStrategies number of strategies to choose
     * @return a strategy for the brick
     */
    private CollisionStrategy getStrategyHelper(CollisionStrategy Strategy, int randomMin,
                                                int randomMax, int numOfStrategies) {
        int randomNum = randomMin + random.nextInt(randomMax - randomMin);
        if (numOfStrategies == 3) {
            return Strategy;
        }
        switch (randomNum) {
            case 0:
                return Strategy;
            case 1:
                Strategy = new PuckStrategy(Strategy, imageReader, soundReader);
                return Strategy;
            case 2:
                Strategy = new ChangeCameraStrategy(Strategy, windowController, gameManager);
                return Strategy;
            case 3:
                Strategy = new BotStrategy(Strategy, imageReader, windowDimensions);
                return Strategy;
            case 4:
                Strategy = new AddPaddleStrategy(Strategy, imageReader, inputListener, windowDimensions);
                return Strategy;
            default:
                Strategy = getStrategyHelper(Strategy, 1, 5, numOfStrategies);
                Strategy = getStrategyHelper(Strategy, 1, 6, numOfStrategies++);
                return Strategy;
        }
    }

    /**
     * calls a helper function and returns a strategy for the brick
     *
     * @return a strategy for the brick
     */
    public CollisionStrategy getStrategy() {
        return getStrategyHelper(new RemoveBrickStrategy(gameObjectCollection), 0, 6, 0);
    }


}
