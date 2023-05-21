package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * the class creates a graphic life counter and updates it as necessary
 */
public class GraphicLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;
    private final GameObject[] hearts = new GameObject[4];

    /**
     * Construct a new GameObject instance.
     *
     * @param widgetTopLeftCorner  Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions     Width and height in window coordinates.
     * @param livesCounter         A counter that counts hoe many lives are left.
     * @param widgetRenderable     The renderable representing the object.
     * @param gameObjectCollection The collection of objects in the game.
     * @param numOfLives           number of lives the player starts with.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable, GameObjectCollection gameObjectCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = numOfLives;

        Vector2 nextHeart = new Vector2(25, 0);

        for (int heartNum = 0; heartNum < numOfLives; heartNum++) {
            Vector2 widgetLocation = widgetTopLeftCorner.add(nextHeart.mult(heartNum));
            hearts[heartNum] = new GameObject(widgetLocation, widgetDimensions, widgetRenderable);
            gameObjectCollection.addGameObject(hearts[heartNum], Layer.UI);
        }
    }

    /**
     * updates the game status and removes hearts as necessary
     *
     * @param deltaTime the interval to wait between updates
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives) {
            gameObjectCollection.removeGameObject(hearts[numOfLives - 1], Layer.UI);
            numOfLives--;
        }
    }
}
