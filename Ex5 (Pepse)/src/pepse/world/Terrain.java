package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;


import java.awt.*;

/**
 * Responsible for the creation and management of terrain.
 *
 * @author Hila Ziv, Shay Kvasha
 */
public class Terrain {

    /**
     * the approximate color of the grounds block
     */
    private static final Color BASE_GROUND_COLOR_1 = new Color(212, 123, 74);

    /**
     * the ground tag (except 2 top rows)
     */
    public static final String GROUND_TAG = "ground";

    /**
     * the relative size of the window that the ground is going to cover
     */
    private static final float TERRAIN_HEIGHT = 0.7f;

    /**
     * the tag of the 2 top ground layers
     */
    public static final String GROUND_TAG_EXTERIOR = "exterior ground";

    /**
     * extra depth for the ground so the player won't see the background
     */
    private static final float EXTRA_PADDING = 240;


    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final float groundHeightAtX0;
    private final int exteriorGroundLayer;

    /**
     * initializes the class
     *
     * @param gameObjects      - The collection of all participating game objects.
     * @param groundLayer      - The number of the layer to which the created ground objects should be added.
     * @param windowDimensions - The dimensions of the windows.
     * @param seed             - A seed for a random number generator.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.y() * TERRAIN_HEIGHT;
        this.exteriorGroundLayer = groundLayer + 1;
    }

    /**
     * @param x - the block X location (an int)
     * @return The ground height (Y) at the given location.
     */
    public float groundHeightAt(float x) {
        float z = (float) (groundHeightAtX0 + (Math.sin(x / 5)) * 3 * Block.SIZE);
        return z - z % Block.SIZE;
    }

    /**
     * This method creates terrain in a given range of x-values.
     *
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        minX -= minX % Block.SIZE;
        maxX += Block.SIZE * 2 - (maxX % Block.SIZE);
        for (float blockX = minX; blockX < maxX; blockX += Block.SIZE) {
            boolean flag = true;
            int num = 0;
            float blockY = groundHeightAt(blockX);
            for (float blockHeight = blockY; blockHeight <= windowDimensions.y() + EXTRA_PADDING;
                 blockHeight += Block.SIZE) {
                Renderable renderer = new RectangleRenderable(
                        ColorSupplier.approximateColor(BASE_GROUND_COLOR_1));
                Vector2 blockLocation = new Vector2(blockX, blockHeight);
                GameObject block = new Block(blockLocation, renderer);
                if (flag) {
                    gameObjects.addGameObject(block, exteriorGroundLayer);
                    block.setTag(GROUND_TAG_EXTERIOR);
                    num++;
                    if (num == 2) {
                        flag = false;
                    }
                } else {
                    gameObjects.addGameObject(block, groundLayer);
                    block.setTag(GROUND_TAG);
                }
            }
        }
    }
}
