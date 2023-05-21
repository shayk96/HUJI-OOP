package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 *
 * @author Hila Ziv, Shay Kvasha
 */
public class Sun {
    /**
     * The radius of the sun.
     */
    private static final float SUN_RADIUS = 100;

    /**
     * A divider to determine the initial position of the sun in Y axis.
     */
    private static final float SUN_Y_LOCATION_DIVIDER = 10;

    /**
     * The tag for sun object.
     */
    private static final String SUN_TAG = "sun";

    /**
     * The initial angle for the transition of the sun movement.
     */
    private static final Float INITIAL_TRANSITION_VALUE = 0f;

    /**
     * The final angle for the transition of the sun movement.
     */
    private static final Float FINAL_TRANSITION_VALUE = 360f;

    /**
     * A multiplier to get elliptic movement of th sun.
     */
    private static final Float ELLIPTIC_RADIUS = 1.5f;

    private static float sunStartY;


    /**
     * Calculates the desired position of the sun according to the given angle, so it would move in
     * elliptical path.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param angleInSky       The angle indicating the desired sun position.
     * @return Vector2 representing the new position of the sun.
     */
    private static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky) {
        float middleX = windowDimensions.x() / 2;
        float middleY = windowDimensions.y() / 2;
        float sunRadius = middleY - sunStartY;
        angleInSky = (float) Math.toRadians(angleInSky);
        float sunX = (float) (middleX + Math.sin(angleInSky) * sunRadius * ELLIPTIC_RADIUS);
        float sunY = (float) (middleY - Math.cos(angleInSky) * sunRadius);
        return new Vector2(sunX, sunY);
    }

    /**
     * This function creates a yellow circle that moves in the sky in an elliptical path
     * (in camera coordinates).
     *
     * @param gameObjects      The collection of all participating game objects.
     * @param layer            The number of the layer to which the created sun should be added.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The amount of seconds it should take the created game object to complete a
     *                         full cycle.
     * @return A new game object representing the sun.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength) {
        sunStartY = windowDimensions.y() / SUN_Y_LOCATION_DIVIDER;
        Vector2 sunStartPos = new Vector2(windowDimensions.x() / 2, sunStartY);
        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(SUN_RADIUS, SUN_RADIUS),
                new OvalRenderable(Color.yellow));
        sun.setCenter(sunStartPos);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN_TAG);
        new Transition<Float>(sun, angle -> sun.setCenter(calcSunPosition(windowDimensions, angle)),
                INITIAL_TRANSITION_VALUE, FINAL_TRANSITION_VALUE, Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }
}
