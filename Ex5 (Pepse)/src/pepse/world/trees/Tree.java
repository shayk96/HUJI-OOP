package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * Responsible for the creation and management of trees.
 *
 * @author Hila ziv, Shay Kvasha
 */
public class Tree {

    /**
     * the leaves approximate color
     */
    private static final Color LEAF_COLOUR = new Color(50, 200, 30);

    /**
     * the leaves (not falling) tag
     */
    private static final String LEAF_TAG = "leaf";

    /**
     * the trunks minimal height
     */
    private static final int TRUNK_MIN_HEIGHT = 8;

    /**
     * the trunks maximal height (minimal height + max height)
     */
    private static final int TRUNK_MAX_HEIGHT = 4;

    /**
     * the odds of a tree to appear
     */
    private static final int TREE_ODDS_OF_APPEARING = 101;

    /**
     * the max odds of tree to appear (if TREE_ODDS_OF_APPEARING < TREE_MAX_ODDS_OF_APPEARING)
     */
    private static final int TREE_MAX_ODDS_OF_APPEARING = 8;

    private Random random;
    private final GameObjectCollection gameObjects;
    private final Terrain terrain;
    private int seed;
    private final int layer;
    private final int trunkLayer;

    /**
     * @param gameObjects - The collection of all participating game objects.
     * @param terrain     - an instance of the class that responsible for the creation of the ground
     * @param seed        - A seed for a random number generator.
     * @param layer       - The number of the layer to which the created objects should be added.
     */
    public Tree(GameObjectCollection gameObjects, Terrain terrain, int seed, int layer) {
        this.gameObjects = gameObjects;
        this.terrain = terrain;
        this.seed = seed;
        this.layer = layer;
        setRandom(0);
        this.trunkLayer = layer - 1;
    }

    public void setRandom(int coordinateX) {
        random = new Random(Objects.hash(coordinateX, seed));
    }

    /**
     * This method creates trees in a given range of x-values.
     *
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE)
     */
    public void createInRange(int minX, int maxX) {
        minX -= minX % Block.SIZE;
        maxX += maxX % Block.SIZE;
        for (int locationX = minX; locationX < maxX; locationX += Block.SIZE) {
            if (random.nextInt(TREE_ODDS_OF_APPEARING) <= TREE_MAX_ODDS_OF_APPEARING) {
                int trunkHeight = TRUNK_MIN_HEIGHT + random.nextInt(TRUNK_MAX_HEIGHT);
                Vector2 topLeftCorner = new Vector2(locationX, terrain.groundHeightAt(locationX) -
                        trunkHeight * Block.SIZE);
                GameObject[] leaves = createLeaves(trunkHeight, topLeftCorner);
                Trunk.create(gameObjects, trunkHeight, trunkLayer, topLeftCorner, leaves);
            }
        }
    }

    /**
     * This method creates trees in a given range of x-values.
     *
     * @param height        - the height of the first leaf
     * @param topLeftCorner - the X location of the first leaf
     */
    private GameObject[] createLeaves(int height, Vector2 topLeftCorner) {
        int leafNum = 0;
        int numberOfLeavesInRow = (int) (((Math.ceil(height / 2f)) * 2) - 3);
        GameObject[] leaves = new GameObject[numberOfLeavesInRow * numberOfLeavesInRow];
        Vector2 leafDimensions = new Vector2(Block.SIZE, Block.SIZE);
        Vector2 leavesStartLocation = new Vector2((topLeftCorner.x() - Block.SIZE *
                (numberOfLeavesInRow / 2)), (topLeftCorner.y() - Block.SIZE * (numberOfLeavesInRow / 2)));
        RectangleRenderable leafRender = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOUR));
        for (int rowIndex = 0; rowIndex < numberOfLeavesInRow; rowIndex++) {
            for (int colIndex = 0; colIndex < numberOfLeavesInRow; colIndex++) {
                Vector2 leafCorner = new Vector2(leavesStartLocation.x() + Block.SIZE * colIndex,
                        leavesStartLocation.y() + Block.SIZE * rowIndex);
                GameObject leaf = new Leaf(leafCorner, leafDimensions, leafRender,
                        random, gameObjects, layer);
                gameObjects.addGameObject(leaf, layer);
                leaf.setTag(LEAF_TAG);
                leaves[leafNum] = leaf;
                leafNum++;
            }
        }
        return leaves;
    }
}