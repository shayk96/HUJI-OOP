package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

import static pepse.world.trees.Leaf.LEAF_TAG;

/**
 * A class that creates and updates an avatar that can move around the world.
 *
 * @author Hila Ziv , Shay Kvasha
 * @see danogl.GameObject
 */
public class Avatar extends GameObject {

    /**
     * base horizontal movement speed
     */
    private static final float VELOCITY_X = 300;

    /**
     * base vertical speed up
     */
    private static final float VELOCITY_Y = -350;

    /**
     * base vertical speed down (the gravity of the world)
     */
    private static final float GRAVITY = 300;

    /**
     * the tag of the avatar
     */
    private static final String AVATAR_TAG = "avatar";

    /**
     * dictates the dimensions of the avatar
     */
    private static final float AVATAR_SIZE_MULTIPLIER = 70;

    /**
     * the time interval between images of the idle animation
     */
    private static final double IDLE_ANIMATION_INTERVAL = 1;

    /**
     * the time interval between images of the run animation
     */
    private static final double RUN_ANIMATION_INTERVAL = 0.25;

    /**
     * the time interval between images of the flight animation
     */
    private static final double FLIGHT_ANIMATION_INTERVAL = 0.15;

    /**
     * the time interval between images of the jump animation
     */
    private static final double JUMP_ANIMATION_INTERVAL = 0.25;

    /**
     * the avatar's max flight energy
     */
    private static final float MAX_ENERGY = 100;

    /**
     * the energy use and waste value
     */
    private static final float ENERGY_WASTE_AND_REFILL_VALUE = 0.5f;

    /**
     * the time that the avatar can pass down through objects (except the ground)
     */
    private static final float PASS_DOWN_TIME = 0.75f;

    /**
     * the avatars in air time when jumping
     */
    private static final float JUMP_TIME = 0.8f;

    /**
     * the time between the jump animation renderable and the jump2 renderable
     */
    private static final float JUMP_ANIMATION_DELAY = .5f;

    /**
     * the minimum level of the avatar flight energy
     */
    private static final float MIN_ENERGY = 0;

    /**
     * the avatar acceleration rate than falling down
     */
    private static final float AVATAR_ACCELERATION_RATE = 500;

    /**
     *
     */
    private static final String EXTERIOR_GROUND_TAG = "exterior ground";

    /**
     * the avatar base energy
     */
    private float avatarEnergy = 100;



    private static UserInputListener inputListener;
    private static AnimationRenderable avatarIdleAnimation;
    private static AnimationRenderable avatarRunAnimation;
    private static AnimationRenderable avatarFlightAnimation;
    private static AnimationRenderable avatarJumpAnimation;
    private static Renderable avatarJumpAnimation2;
    private static Renderable avatarFallAnimation;
    private float lastY = getCenter().y();

    private boolean inJump = false;
    private boolean inFlight = false;
    private ScheduledTask jump = null;
    private boolean falling = false;
    private boolean onGround = true;


    /**
     * Construct a new Avatar instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        transform().setVelocityY(GRAVITY);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera. The avatar
     * can stand, walk, jump and fly, and never reaches the end of the world.
     *
     * @param gameObjects   - The collection of all participating game objects
     * @param layer         - The number of the layer to which the created avatar should be added.
     * @param topLeftCorner - The location of the top-left corner of the created avatar.
     * @param inputListener - Used for reading input from the user.
     * @param imageReader   - Used for reading images from disk or from within a jar.
     * @return A newly created object the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader) {
        createAvatarAnimation(imageReader);
        Avatar avatar = new Avatar(topLeftCorner, Vector2.ONES.mult(AVATAR_SIZE_MULTIPLIER),
                avatarIdleAnimation);
        Avatar.inputListener = inputListener;
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(AVATAR_TAG);
        return avatar;
    }

    /**
     * creates the avatar animation renderables
     *
     * @param imageReader - Used for reading images from disk or from within a jar.
     */
    private static void createAvatarAnimation(ImageReader imageReader) {
        createIdleAnimation(imageReader);
        createRunAnimation(imageReader);
        createFlightAnimation(imageReader);
        createJumpAnimation(imageReader);
        avatarJumpAnimation2 = imageReader.readImage("assets/jump_2.png", true);
        avatarFallAnimation = imageReader.readImage("assets/fall.png", true);
    }

    /**
     * creates the avatar jump animation renderable
     *
     * @param imageReader - Used for reading images from disk or from within a jar.
     */
    private static void createJumpAnimation(ImageReader imageReader) {
        Renderable[] avatarJump1 = {
                imageReader.readImage("assets/jump_0.png", true),
                imageReader.readImage("assets/jump_1.png", true)};
        avatarJumpAnimation = new AnimationRenderable(avatarJump1, JUMP_ANIMATION_INTERVAL);
    }

    /**
     * creates the avatar flight renderable
     *
     * @param imageReader - Used for reading images from disk or from within a jar.
     */
    private static void createFlightAnimation(ImageReader imageReader) {
        Renderable[] avatarFlight = {
                imageReader.readImage("assets/flight_0.png", true),
                imageReader.readImage("assets/flight_1.png", true),
                imageReader.readImage("assets/flight_2.png", true),
                imageReader.readImage("assets/flight_3.png", true),
                imageReader.readImage("assets/flight_4.png", true),
                imageReader.readImage("assets/flight_5.png", true)};
        avatarFlightAnimation = new AnimationRenderable(avatarFlight, FLIGHT_ANIMATION_INTERVAL);
    }

    /**
     * creates the avatar run renderable
     *
     * @param imageReader - Used for reading images from disk or from within a jar.
     */
    private static void createRunAnimation(ImageReader imageReader) {
        Renderable[] avatarRun = {
                imageReader.readImage("assets/run_0.png", true),
                imageReader.readImage("assets/run_1.png", true),
                imageReader.readImage("assets/run_2.png", true),
                imageReader.readImage("assets/run_3.png", true),
                imageReader.readImage("assets/run_4.png", true),
                imageReader.readImage("assets/run_5.png", true)};
        avatarRunAnimation = new AnimationRenderable(avatarRun, RUN_ANIMATION_INTERVAL);
    }

    /**
     * creates the avatar idle renderable
     *
     * @param imageReader - Used for reading images from disk or from within a jar.
     */
    private static void createIdleAnimation(ImageReader imageReader) {
        Renderable[] avatarIdle = {
                imageReader.readImage("assets/idle_0.png", true),
                imageReader.readImage("assets/idle_1.png", true),
                imageReader.readImage("assets/idle_2.png", true),
                imageReader.readImage("assets/idle_3.png", true)};
        avatarIdleAnimation = new AnimationRenderable(avatarIdle, IDLE_ANIMATION_INTERVAL);
    }

    /**
     * Called once per frame.
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation of this method.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateAvatarMovement();
    }

    /**
     * updates the avatar movements each time that update is executed according to the user input
     */
    private void updateAvatarMovement() {
        avatarFall();
        avatarRefillEnergy();
        avatarHorizontalMovement();
        if (avatarJumpDown()) {
            return;
        }
        avatarFlight();
        avatarJump();
    }

    /**
     * makes the avatar sub-come to the gravity of the world
     */
    private void avatarFall() {
        if (getVelocity().y() == 0) {
            transform().setVelocityY(GRAVITY);
        }
    }

    /**
     * handles the avatar horizontal movement.
     * flips the renderable if needed.
     */
    private void avatarHorizontalMovement() {
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            if (!inJump && !inFlight && !falling) {
                renderer().setRenderable(avatarRunAnimation);
            }
            if (renderer().isFlippedHorizontally()) {
                renderer().setIsFlippedHorizontally(false);
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            if (!inJump && !inFlight & !falling) {
                renderer().setRenderable(avatarRunAnimation);
            }
            renderer().setIsFlippedHorizontally(true);
        }
        transform().setVelocityX(xVel);
        if (xVel == 0 && !inJump && !inFlight && !falling) {
            renderer().setRenderable(avatarIdleAnimation);
        }
    }

    /**
     * checks if the avatar's energy needs refill and if the avatar is not using the energy.
     * if not than refill the energy, else not.
     */
    private void avatarRefillEnergy() {
        if (lastY == getCenter().y()) {
            if (avatarEnergy < MAX_ENERGY) {
                avatarEnergy += ENERGY_WASTE_AND_REFILL_VALUE;
            }
        }
        lastY = getCenter().y();
    }

    /**
     * handles the avatar jump down command
     *
     * @return true if the avatar jumped down, else false
     */
    private boolean avatarJumpDown() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_DOWN)
                && !onGround) {
            physics().preventIntersectionsFromDirection(null);
            new ScheduledTask(this, PASS_DOWN_TIME, false,
                    () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return true;
        }
        return false;
    }

    /**
     * handles the avatar jump (not down) command
     */
    private void avatarJump() {
        if (!inputListener.isKeyPressed(KeyEvent.VK_DOWN) && inputListener.isKeyPressed(KeyEvent.VK_SPACE)
                && !inJump) {
            transform().setVelocityY(VELOCITY_Y);
            renderer().setRenderable(avatarJumpAnimation);
            new ScheduledTask(this, JUMP_ANIMATION_DELAY, false,
                    () -> renderer().setRenderable(avatarJumpAnimation2));
            inJump = true;
            jump = new ScheduledTask(this, JUMP_TIME, false,
                    () -> transform().setVelocityY(GRAVITY));
        }
    }

    /**
     * handles the avatar flight command
     */
    private void avatarFlight() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)
                && (avatarEnergy > MIN_ENERGY)) {
            renderer().setRenderable(avatarFlightAnimation);
            falling = false;
            if (jump != null) {
                removeComponent(jump);
                jump = null;
            }
            inJump = true;
            inFlight = true;
            if (avatarEnergy > MIN_ENERGY) {
                transform().setVelocityY(VELOCITY_Y);
                avatarEnergy -= ENERGY_WASTE_AND_REFILL_VALUE;
            }
            if (avatarEnergy == MIN_ENERGY) {
                falling = true;
                inFlight = false;
                transform().setVelocityY(GRAVITY);
                transform().setAccelerationY(AVATAR_ACCELERATION_RATE);
            }
        }
        if ((!inputListener.isKeyPressed(KeyEvent.VK_SPACE) || !inputListener.isKeyPressed(KeyEvent.VK_SHIFT))
                && inFlight) {
            inFlight = false;
            falling = true;
            transform().setVelocityY(GRAVITY);
            transform().setAccelerationY(AVATAR_ACCELERATION_RATE);
        }
        if (falling) {
            renderer().setRenderable(avatarFallAnimation);
        }
    }

    /**
     * Called on the first frame of a collision.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        onGround = other.getTag().equals(EXTERIOR_GROUND_TAG);
        if (!other.getTag().equals(LEAF_TAG)){
            transform().setVelocityY(Vector2.ZERO.y());
        }
        inJump = false;
        inFlight = false;
        falling = false;
    }
}