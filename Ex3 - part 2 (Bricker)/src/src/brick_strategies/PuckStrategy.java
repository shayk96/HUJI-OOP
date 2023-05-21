package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator. Introduces several pucks instead of
 * brick once removed.
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator {

    private static final int puckWidth = 15;
    private final ImageReader imageReader;
    private final SoundReader soundReader;

    /**
     * creates an instance of the class
     *
     * @param toBeDecorated the base strategy
     * @param imageReader   a renderer for the image
     * @param soundReader   a renderer for the sound
     */
    PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader) {
        super(toBeDecorated);

        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * the function determines what happens upon collision with other objects, in this case the brick
     * disappears and pucks
     *
     * @param thisObj  the current object AKA brick
     * @param otherObj the ball or a puck
     * @param counter  counter that counts how many bricks are currently in the game
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Renderable puckImg = imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Vector2 puckDimensions = new Vector2(puckWidth, puckWidth);
        Vector2[] puckVelDir = {new Vector2(0, 200), new Vector2(200, 200),
                new Vector2(-200, 200)};
        Vector2 puckLocation = otherObj.getCenter();
        for (int i = 0; i <= 2; i++) {
            Puck puck = new Puck(puckLocation, puckDimensions, puckImg, collisionSound);
            puck.setVelocity(puckVelDir[i]);
            super.getGameObjectCollection().addGameObject(puck);
        }
    }


}
