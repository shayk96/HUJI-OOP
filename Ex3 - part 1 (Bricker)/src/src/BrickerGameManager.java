package src;

import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

import java.util.Random;

/**
 * a class that creates the game and manages it.
 */
public class BrickerGameManager extends GameManager {

    private static final String GAME_NAME = "Bricker";
    public static final int BORDER_WIDTH = 10;
    private static final int BALL_RADIUS = 20;
    private static final int BALL_SPEED = 200;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int BRICK_HEIGHT = 15;
    private static final int NUM_OF_ROWS = 5;
    private static final int NUM_OF_BRICKS_IN_ROW = 8;
    private static final int HIGHEST_ROW = 100;
    private static final int NUM_OF_LIVES = 4;
    private static final int GAME_DIM_X = 700;
    private static final int GAME_DIM_Y = 500;
    private GameObject ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private static final Random random = new Random();
    private Counter bricksCounter;
    private Counter livesCounter;
    private ImageReader imageReader;
    private SoundReader soundReader;


    public static void main(String[] args) {
        Vector2 gameDimensions = new Vector2(GAME_DIM_X, GAME_DIM_Y);
        new BrickerGameManager(GAME_NAME, gameDimensions).run();
    }

    /**
     * the cosntructor for the class' creates the window with the size given and the name given
     *
     * @param windowTitle      the title of the window
     * @param windowDimensions the window dimensions
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * initializes the game and creates all the needed objects
     *
     * @param imageReader      reads images to render
     * @param soundReader      reads sounds to use
     * @param inputListener    used to get input from the player
     * @param windowController provides control and info about the game
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.soundReader = soundReader;
        this.imageReader = imageReader;
        this.windowController = windowController;
        windowDimensions = windowController.getWindowDimensions();
        this.bricksCounter = new Counter();
        livesCounter = new Counter(NUM_OF_LIVES);
        createBall();         //create and add ball
        createPaddle(inputListener);        //create and add paddle
        createWalls();        //create and add wall
        setBackground();        //set background and make camera follow coordinates or object
        createBricks(imageReader, windowDimensions);        //create and add bricks
        createGraphicLifeCounter(imageReader);        //create and add graphic lives
        createNumericLifeCounter();        //create and add numeric lives
    }

    /**
     * creates the numeric life counter for the game
     */
    private void createNumericLifeCounter() {
        Vector2 numericCounterPosition = new Vector2(BORDER_WIDTH, 50);
        Vector2 numericCounterDimensions = new Vector2(20, 20);
        GameObject numericLifeCounter = new NumericLifeCounter(livesCounter, numericCounterPosition,
                numericCounterDimensions, gameObjects());
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);
    }

    /**
     * creates the graphic life counter gor the game
     *
     * @param imageReader used to get an image for the counter
     */
    private void createGraphicLifeCounter(ImageReader imageReader) {
        Renderable heartImg = imageReader.readImage("assets/heart.png", true);
        Vector2 heartDimensions = new Vector2(20, 20);
        Vector2 firstHeartLocation = new Vector2(BORDER_WIDTH, 15);
        GameObject graphicLifeCounter = new GraphicLifeCounter(firstHeartLocation, heartDimensions,
                livesCounter, heartImg, this.gameObjects(), NUM_OF_LIVES);
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
    }

    /**
     * updated the game status and checks if the game has ended
     *
     * @param deltaTime the interval of time between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkGameEnded();
    }

    /**
     * checks if the game ended and if yes asks the player if he wants to continue
     */
    private void checkGameEnded() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        if (ballHeight > windowDimensions.y()) {
            livesCounter.decrement();
            if (livesCounter.value() == 0) {
                prompt = "you lose! play again?";
            } else {
                initializeBallMovement(ball);
            }
        }
        if (bricksCounter.value() == 0) {
            prompt = "you win! play again?";
        }
        if (!prompt.isEmpty()) {
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * creats the bricks for the game
     *
     * @param imageReader     reads an image
     * @param windowDimension the dimensions of the game window
     */
    private void createBricks(ImageReader imageReader, Vector2 windowDimension) {
        Renderable brickImg = imageReader.readImage("assets/brick.png", false);

        float brickWidth = (windowDimension.x() - 10 - BORDER_WIDTH * 2) / NUM_OF_BRICKS_IN_ROW;
        Vector2 brickDimensions = new Vector2(brickWidth, BRICK_HEIGHT);
        CollisionStrategy collisionStrategy = new CollisionStrategy(gameObjects());

        for (int row = 0; row < NUM_OF_ROWS; row++) {
            for (int col = 0; col < NUM_OF_BRICKS_IN_ROW; col++) {
                Vector2 brickLocation = new Vector2(6 + BORDER_WIDTH + col * brickWidth,
                        HIGHEST_ROW + row + (row * BRICK_HEIGHT));
                gameObjects().addGameObject(new Brick(brickLocation, brickDimensions, brickImg,
                        collisionStrategy, bricksCounter), Layer.STATIC_OBJECTS);
                bricksCounter.increment();
            }
        }
    }

    /**
     * sets the background for the game
     */
    private void setBackground() {
        Renderable backgroundImg = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImg);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

        //camera follows coordinates or object
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * creats the paddle for the game
     *
     * @param inputListener used to get the movement direction of the paddle from the user
     */
    private void createPaddle(UserInputListener inputListener) {
        Renderable paddleImg = imageReader.readImage("assets/paddle.png", true);
        Vector2 paddleDimensions = new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);
        Vector2 paddleFirstLocation = new Vector2(windowDimensions.x() / 2 - 50,
                windowDimensions.y() - 10);
        GameObject paddle = new Paddle(paddleFirstLocation, paddleDimensions, paddleImg, inputListener,
                windowDimensions, BORDER_WIDTH);
        gameObjects().addGameObject(paddle);
    }

    /**
     * creates the ball of the game
     */
    private void createBall() {
        Renderable ballImg = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Vector2 ballDimensions = new Vector2(BALL_RADIUS, BALL_RADIUS);
        GameObject ball = new Ball(Vector2.ZERO, ballDimensions, ballImg, collisionSound);
        initializeBallMovement(ball);
        this.ball = ball;
        gameObjects().addGameObject(this.ball);
    }

    /**
     * randomizes the ball first movement from the center og the screen
     *
     * @param ball the ball game object in the game
     */
    private void initializeBallMovement(GameObject ball) {
        ball.setCenter(windowDimensions.mult(0.5f));
        //randomizes the initial ball movement
        Vector2 ballDirection = new Vector2(1, 1);
        if (random.nextBoolean()) {
            ballDirection = ballDirection.multX(-1);
        }
        if (random.nextBoolean()) {
            ballDirection = ballDirection.multY(-1);
        }
        ball.setVelocity(ballDirection.mult(BALL_SPEED));
    }

    /**
     * creates the boundaries of the game
     */
    private void createWalls() {
        Vector2 wallDimensionsVertical = new Vector2(BORDER_WIDTH, windowDimensions.y());
        Vector2 wallDimensionsHorizontal = new Vector2(windowDimensions.x(), BORDER_WIDTH);
        Vector2 rightWallPosition = new Vector2(windowDimensions.x() - BORDER_WIDTH, 0);

        GameObject[] wallObjects = {new GameObject(Vector2.ZERO, wallDimensionsVertical, null),
                new GameObject(rightWallPosition, wallDimensionsVertical, null),
                new GameObject(Vector2.ZERO, wallDimensionsHorizontal, null)};

        for (GameObject gameobject : wallObjects) {
            gameObjects().addGameObject(gameobject);
        }
    }
}
