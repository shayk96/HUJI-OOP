package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

/**
 * Represents a leaf in the game.
 * Each leaf has a unique random movement and a unique life cycle time - it starts on a tree, falls and fades
 * out and after a random time appears back on tree at the same location it was originally.
 * The avatar of the game can't go through the leaves.
 *
 * @author Hila Ziv, Shay Kvasha
 * @see danogl.GameObject
 */
public class Leaf extends GameObject {

    /**
     * The duration of the fade out.
     */
    private static final int FADE_OUT_TIME = 30;

    /**
     * The velocity in Y axis of a falling leaf.
     */
    private static final int FALL_SPEED = 150;

    /**
     * The maximum life cycle duration of a leaf.
     */
    private static final int MAX_LIFE_TIME = 300;

    /**
     * The initial angle of a leaf. Used for transition.
     */
    private static final float START_ANGLE = 0;

    /**
     * The final angle of a leaf. Used for transition.
     */
    private static final float END_ANGLE = 15;

    /**
     * The maximus time until a leaf starts moving or fall.
     */
    private static final int MAX_DELAY_TIME = 10;

    /**
     * The velocity of a leaf on X axis.
     */
    private static final float HORIZONTAL_SPEED = 100;

    /**
     * The initial opaqueness of a leaf (max possible opaqueness).
     */
    private static final float START_OPAQUENESS = 1;

    /**
     * The final opaqueness of a leaf (min possible opaqueness).
     */
    private static final float END_OPAQUENESS = 0;

    /**
     * The maximum time can pass from the moment a leaf hits the ground until it appears back on the tree.
     */
    private static final int MAX_DEATH_TIME = 45;

    /**
     * The tag for a falling leaf.
     */
    public static final String FALLING_LEAF_TAG = "falling leaf";

    /**
     * The tag for a leaf on the tree.
     */
    public static final String LEAF_TAG = "leaf";

    /**
     * The time for the leaf angle and size transitions.
     */
    private static final int ANGLE_AND_SIZE_TRANSITION_TIME = 5;

    /**
     * Increase the leaf x size by this value while moving.
     */
    private static final int LEAF_SIZE_INCREASE = 5;

    /**
     * The layer of the first two rows of the ground.
     */
    private static final int GROUND_EXTERIOR_LAYER = Layer.STATIC_OBJECTS + 1;

    /**
     * The transition time of the horizontal movement of the leaf.
     */
    private static final float HORIZONTAL_MOVEMENT_TRANSITION_TIME = 2;

    private final Vector2 topLeftCorner;
    private final Random random;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private Transition<Float> horizontalTransition;
    private final int fallingLeafLayer;

    /**
     * A constructor.
     *
     * @param topLeftCorner Position of the top left corner of the leaf.
     * @param dimensions    The dimensions of the leaf.
     * @param renderable    A Renderable object to render the leaf.
     * @param random        A Random object with a predetermined seed.
     * @param gameObjects   The collection of all participating game objects.
     * @param layer         The number of the layer to which the created leaf should be added.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Random random,
                GameObjectCollection gameObjects, int layer) {
        super(topLeftCorner, dimensions, renderable);
        this.topLeftCorner = topLeftCorner;
        this.random = random;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.fallingLeafLayer = layer + 1;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        leafInit(this);
    }

    /**
     * Initializes the state of the given leaf. Determines the time until the leaf starts moving
     * and the time until it falls.
     *
     * @param leaf Leaf object.
     */
    private void leafInit(GameObject leaf) {
        new ScheduledTask(leaf, random.nextInt(MAX_DELAY_TIME), false, () -> leafAngle(leaf));
        new ScheduledTask(leaf, random.nextInt(MAX_DELAY_TIME), false, () -> leafSizeAndFall(leaf));
    }

    /**
     * Determines the angle of the movement of the leaf.
     *
     * @param leaf Leaf object.
     */
    private void leafAngle(GameObject leaf) {
        new Transition<Float>(leaf, leaf.renderer()::setRenderableAngle, START_ANGLE, END_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, ANGLE_AND_SIZE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Changes the size of the leaf while moving and determines the time until it starts falling.
     *
     * @param leaf
     */
    private void leafSizeAndFall(GameObject leaf) {
        new Transition<Vector2>(leaf, leaf::setDimensions,
                new Vector2(Block.SIZE, Block.SIZE), new Vector2(Block.SIZE + LEAF_SIZE_INCREASE,
                Block.SIZE), Transition.CUBIC_INTERPOLATOR_VECTOR, ANGLE_AND_SIZE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new ScheduledTask(leaf, random.nextInt(MAX_LIFE_TIME), false, () -> fallLeaf(leaf));
    }

    /**
     * Handles the fall of the leaf.
     *
     * @param leaf
     */
    private void fallLeaf(GameObject leaf) {
        this.transform().setVelocityY(FALL_SPEED);
        gameObjects.removeGameObject(leaf, layer);
        gameObjects.addGameObject(leaf, fallingLeafLayer);
        leaf.setTag(FALLING_LEAF_TAG);

        gameObjects.layers().shouldLayersCollide(fallingLeafLayer, GROUND_EXTERIOR_LAYER, true);
        horizontalTransition = new Transition<Float>(leaf,
                (Float speedX) -> leaf.transform().setVelocityX(speedX), -HORIZONTAL_SPEED,
                HORIZONTAL_SPEED, Transition.LINEAR_INTERPOLATOR_FLOAT, HORIZONTAL_MOVEMENT_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        new Transition<Float>(leaf, leaf.renderer()::fadeOut, START_OPAQUENESS, END_OPAQUENESS,
                Transition.CUBIC_INTERPOLATOR_FLOAT, FADE_OUT_TIME, Transition.TransitionType.TRANSITION_ONCE,
                () -> new ScheduledTask(leaf, random.nextInt(MAX_DEATH_TIME),
                        false, () -> placeLeafBack(leaf)));
    }

    /**
     * Places the leaf back on the tree at its original location.
     *
     * @param leaf Leaf object.
     */
    private void placeLeafBack(GameObject leaf) {
        gameObjects.removeGameObject(this, fallingLeafLayer);
        gameObjects.addGameObject(this, layer);
        leaf.setTag(LEAF_TAG);
        leaf.setTopLeftCorner(topLeftCorner);
        leaf.renderer().setOpaqueness(START_OPAQUENESS);
        leafInit(leaf);
    }

    /**
     * Updates the game each frame.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getVelocity().y() == 0) {
            this.transform().setVelocityX(0);
        }
    }

    /**
     * Determines the behavior on a collision.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision A Collision object holding information regarding the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (!other.getTag().equals(LEAF_TAG)) {
            this.removeComponent(horizontalTransition);
            this.setVelocity(Vector2.ZERO);
        }
    }
}
