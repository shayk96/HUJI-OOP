package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

/**
 * responsible for the creation of a tree trunk
 *
 * @author Hila Ziv, Shay Kvasha
 * @see danogl.GameObject
 */
public class Trunk extends GameObject {

    /**
     * the trunk color
     */
    private static final Color TRUNK_COLOUR = new Color(100, 50, 20);

    /**
     * the trunk's tag
     */
    public static final String TRUNK_TAG = "trunk";

    private final GameObject[] leaves;


    /**
     * Construct a new tree trunk instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param leaves        the leaves surrounding the trunk
     */
    public Trunk(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObject[] leaves) {
        super(topLeftCorner, dimensions, renderable);
        this.leaves = leaves;
    }

    /**
     * This method creates a trunk in a given range position.
     *
     * @param gameObjects   - The collection of all participating game objects.
     * @param height        the trunks top left corner distance from the ground
     * @param layer         - The number of the layer to which the created objects should be added.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     * @param leaves        the leaves surrounding the trunk
     * @return a newly created trunk (as a gameObject)
     */
    public static GameObject create(GameObjectCollection gameObjects, int height, int layer, Vector2
            topLeftCorner, GameObject[] leaves) {
        GameObject trunk = new Trunk(topLeftCorner,
                new Vector2(Block.SIZE, Block.SIZE * height), new RectangleRenderable(TRUNK_COLOUR),
                leaves);
        gameObjects.addGameObject(trunk, layer);
        trunk.setTag(TRUNK_TAG);
        return trunk;
    }

    /**
     * a getter for an Array that holds the leaves of the trunk
     *
     * @return an array that holds the trunks leaves
     */
    public GameObject[] getLeaves() {
        return leaves;
    }
}
