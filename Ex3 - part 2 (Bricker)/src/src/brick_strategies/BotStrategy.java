package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.BotBad;
import src.gameobjects.BotGood;

import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator. Introduces a paddle being controlled by
 * the game in odds or against the player
 */
public class BotStrategy extends RemoveBrickStrategyDecorator {

    private static final Random random = new Random();
    private static final int PADDLE_WIDTH = 100;
    private static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 15;
    private final ImageReader imageReader;
    private final Vector2 windowDimensions;

    /**
     * a constructor creating an instance of the class
     *
     * @param toBeDecorated    the base strategy
     * @param imageReader      a renderer for the image
     * @param windowDimensions the dimensions of the game window
     */
    BotStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, Vector2 windowDimensions) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
    }

    /**
     * the function determines what happens upon collision with other objects, in this case the brick
     * disappears and ads paddle that is controlled by the game appears
     *
     * @param thisObj  the current object AKA brick
     * @param otherObj the ball or a puck
     * @param counter  counter that counts how many bricks are currently in the game
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        boolean goodBot = random.nextBoolean();
        if (goodBot && !BotGood.isInstantiated) {
            creatBot((Ball) otherObj, "assets/botGood.png", "goodBot");
            BotGood.isInstantiated = true;

        }
        if (!goodBot && !BotBad.isInstantiated) {
            creatBot((Ball) otherObj, "assets/botBad.png", "badBot");
            BotBad.isInstantiated = true;
        }

    }

    /**
     * creates the bots and adds them to the game objects
     *
     * @param ball    the game object representing the ball
     * @param imgPath the path to the img of the paddle
     * @param botKind the kind of bot being created. if True a good bot else bad bot
     */
    private void creatBot(Ball ball, String imgPath, String botKind) {
        Renderable botImg = imageReader.readImage(imgPath, true);
        Vector2 botDimensions = new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT);
        int botVerticalLocation = random.nextInt((int) windowDimensions.y());
        while (!(200 < botVerticalLocation && botVerticalLocation < 450)) {
            botVerticalLocation = random.nextInt((int) windowDimensions.y());
        }
        Vector2 botFirstLocation = new Vector2(random.nextInt(((int) windowDimensions.x()) -
                PADDLE_WIDTH - BORDER_WIDTH), botVerticalLocation);
        GameObject bot;
        if (botKind.equals("goodBot")) {
            super.getGameObjectCollection().addGameObject(new BotGood(botFirstLocation, botDimensions,
                    botImg, windowDimensions, BORDER_WIDTH, ball, super.getGameObjectCollection()));
        } else {
            super.getGameObjectCollection().addGameObject(new BotBad(botFirstLocation, botDimensions,
                    botImg, windowDimensions, BORDER_WIDTH, ball, super.getGameObjectCollection()));
        }

    }


}
