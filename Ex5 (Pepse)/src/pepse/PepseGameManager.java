package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import pepse.world.trees.Trunk;

import java.awt.*;

import static pepse.world.Terrain.GROUND_TAG;
import static pepse.world.Terrain.GROUND_TAG_EXTERIOR;
import static pepse.world.trees.Leaf.FALLING_LEAF_TAG;
import static pepse.world.trees.Leaf.LEAF_TAG;
import static pepse.world.trees.Trunk.TRUNK_TAG;

/**
 * The main class of the simulator.
 *
 * @author Hila Ziv, Shay Kvasha
 * @see danogl.GameManager
 */
public class PepseGameManager extends GameManager {

    /**
     * The time it takes the sun to do a loop.
     */
    private static final int CYCLE_LENGTH = 30;

    /**
     * the layer of the sunHalo.
     */
    private static final int HALO_LAYER = Layer.BACKGROUND + 10;

    /**
     * a seed for the random generator
     */
    private static final int seed = 789456123;

    /**
     * the tree layer
     */
    private static final int TREE_LAYER = Layer.BACKGROUND + 12;

    /**
     * the ground layer (except 2 top rows)
     */
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;

    /**
     * the layer of the top 2 ground layer
     */
    private static final int EXTERIOR_LAYER = Layer.STATIC_OBJECTS + 1;

    /**
     * the layer of the tree trunks
     */
    private static final int TRUNK_LAYER = TREE_LAYER - 1;

    /**
     * the layer of the falling leaves
     */
    private static final int FALLING_LEAF_LAYER = TREE_LAYER + 1;

    /**
     * the color of the sun halo
     */
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);

    private int buffer = 100;
    private GameObject avatar;
    private int startWorld;
    private int endWorld;
    private Terrain terrain;
    private Tree trees;


    /**
     * Runs the entire simulation.
     *
     * @param args - This argument should not be used.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }


    /**
     * The method will be called once when a GameGUIComponent is created, and again after every invocation of
     * windowController.resetGame().
     *
     * @param imageReader      - Contains a single method: readImage, which reads an image from disk.
     * @param soundReader      - Contains a single method: readSound, which reads a wav file from disk.
     * @param inputListener-   Contains a single method: isKeyPressed, which returns whether a given key is
     *                         currently pressed by the user or not.
     * @param windowController - Contains an array of helpful, self-explanatory methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        GameObjectCollection gameObjects = gameObjects();
        calculateWorldEdges(windowController);
        createTerrainAndTrees(windowController, gameObjects);
        createWorldEntities(windowController, gameObjects);
        createAvatar(imageReader, inputListener, windowController, gameObjects);
        gameObjects.layers().shouldLayersCollide(Layer.DEFAULT, EXTERIOR_LAYER, true);
        gameObjects.layers().shouldLayersCollide(Layer.DEFAULT, TREE_LAYER, true);
    }

    /**
     * @param imageReader      - Contains a single method: readImage, which reads an image from disk.
     * @param inputListener    - Contains a single method: isKeyPressed, which returns whether a given key
     *                         is currently pressed by the user or not.
     * @param windowController - Contains an array of helpful, self-explanatory methods concerning the window.
     * @param gameObjects      - The collection of all participating game objects.
     */
    private void createAvatar(ImageReader imageReader, UserInputListener inputListener, WindowController
            windowController, GameObjectCollection gameObjects) {
        Vector2 avatarInitialLocation = windowController.getWindowDimensions().mult(0.5f);
        avatar = Avatar.create(gameObjects, Layer.DEFAULT, avatarInitialLocation, inputListener, imageReader);
        setCamera(new Camera(avatar,
                (windowController.getWindowDimensions().mult(0.5f)).subtract(avatarInitialLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    /**
     * Creates the sun,sunHalo and night.
     *
     * @param windowController - Contains an array of helpful, self-explanatory methods concerning the window.
     * @param gameObjects      - The collection of all participating game objects.
     */
    private void createWorldEntities(WindowController windowController, GameObjectCollection gameObjects) {
        Sky.create(gameObjects, windowController.getWindowDimensions(), Layer.BACKGROUND);
        Night.create(gameObjects, Layer.FOREGROUND, windowController.getWindowDimensions(), CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjects, Layer.BACKGROUND, windowController.getWindowDimensions(),
                CYCLE_LENGTH);
        SunHalo.create(gameObjects, HALO_LAYER, sun, SUN_HALO_COLOR);
    }

    /**
     * creates the trees and terrain in the initialization of the game.
     *
     * @param windowController - Contains an array of helpful, self-explanatory methods concerning the window.
     * @param gameObjects      - The collection of all participating game objects.
     */
    private void createTerrainAndTrees(WindowController windowController, GameObjectCollection gameObjects) {
        terrain = new Terrain(gameObjects, GROUND_LAYER, windowController.getWindowDimensions(), seed);
        terrain.createInRange(startWorld, endWorld);
        trees = new Tree(gameObjects, terrain, seed, TREE_LAYER);
        trees.createInRange(startWorld, endWorld);
    }

    /**
     * calculates the edges of the world and the buffer needed.
     *
     * @param windowController - Contains an array of helpful, self-explanatory methods concerning the window.
     */
    private void calculateWorldEdges(WindowController windowController) {
        buffer += (int) (windowController.getWindowDimensions().x() / 2);
        startWorld = -Block.SIZE * 2 - buffer;
        endWorld = (int) windowController.getWindowDimensions().x() + Block.SIZE + buffer;
    }

    /**
     * Called once per frame.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation of this method.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateWorldRange();
        removeOutOfRangeObjects();
    }

    /**
     * checks the distance of the avatar X coordinates from the edges of the created world.
     * If the distance is less than the buffer specified, extends the world in the corresponding direction.
     * Updates the endWorld and StartWorld as needed.
     */
    private void updateWorldRange() {
        int middleWorld;
        if (startWorld > avatar.getCenter().x() - buffer) {
            middleWorld = startWorld;
            startWorld -= buffer;
            endWorld -= buffer;
            terrain.createInRange(startWorld, middleWorld);
            trees.setRandom(startWorld);
            trees.createInRange(startWorld, middleWorld);
        } else if (endWorld < avatar.getCenter().x() + buffer) {
            middleWorld = endWorld;
            endWorld += buffer;
            startWorld += buffer;
            terrain.createInRange(middleWorld, endWorld);
            trees.setRandom(middleWorld);
            trees.createInRange(middleWorld, endWorld);
        }
    }

    /**
     * deletes the game objects that are out of bounds. The boundaries are startWorld and endWorld.
     */
    private void removeOutOfRangeObjects() {
        for (GameObject obj : gameObjects()) {
            float objX = obj.getCenter().x();
            String objTag = obj.getTag();
            int layer = Layer.DEFAULT;
            if ((objX < startWorld - (startWorld % Block.SIZE)) ||
                    (endWorld - (endWorld % Block.SIZE) < objX)) {
                switch (objTag) {
                    case GROUND_TAG_EXTERIOR:
                        layer = EXTERIOR_LAYER;
                        break;
                    case GROUND_TAG:
                        layer = GROUND_LAYER;
                        break;
                    case TRUNK_TAG:
                        layer = TRUNK_LAYER;
                        for (GameObject leaf : ((Trunk) obj).getLeaves()) {
                            if (leaf.getTag().equals(LEAF_TAG)) {
                                gameObjects().removeGameObject(leaf, TREE_LAYER);
                            }
                            if (leaf.getTag().equals(FALLING_LEAF_TAG)) {
                                gameObjects().removeGameObject(leaf, FALLING_LEAF_LAYER);
                            }
                        }
                        break;
                }
                gameObjects().removeGameObject(obj, layer);
            }
        }
    }
}
