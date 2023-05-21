package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * a class that creates and updates a numeric life counter
 */
public class NumericLifeCounter extends GameObject {

    private final TextRenderable render;
    private final Counter livesCounter;
    private final String livesLeft;


    /**
     * construct a new game object
     *
     * @param livesCounter         counts the number of lives the player has left
     * @param topLeftCorner        Position of the object, in window coordinates (pixels).
     * @param dimensions           the dimensions of the object
     * @param gameObjectCollection a collection of the game objects
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);

        this.livesCounter = livesCounter;
        livesLeft = toString();
        render = new TextRenderable(toString());
        render.setColor(Color.red);
        GameObject numericCounter = new GameObject(topLeftCorner, dimensions, render);
        gameObjectCollection.addGameObject(numericCounter, Layer.UI);
    }

    /**
     * updates the game status and changes the number of lives left as necessary
     *
     * @param deltaTime the interval to wait between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (!toString().equals(livesLeft)) {
            render.setString(toString());

        }
    }

    /**
     * converts the number of lives left to a String representation of the number
     *
     * @return number of lives left as a String
     */
    @Override
    public String toString() {
        return String.valueOf(livesCounter.value());
    }
}
