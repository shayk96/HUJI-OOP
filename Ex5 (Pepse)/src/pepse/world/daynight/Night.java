package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Responsible for switching between night and day.
 */
public class Night {

    /**
     * The opacity of the night mode.
     */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Tag for night object.
     */
    private static final String NIGHT_TAG = "night";

    /**
     * The initial value for the transition.
     */
    private static final Float INITIAL_TRANSITION_VALUE = 0f;

    /**
     * This function creates a black rectangular game object that covers the entire game window and changes its
     * opaqueness in a cyclic manner, in order to resemble day-to-night transitions.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created game object should be added.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The amount of seconds it should take the created game object to complete a full cycle.
     * @return A new game object representing day-to-night transitions.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, new RectangleRenderable(Color.black));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(
                night, night.renderer()::setOpaqueness, INITIAL_TRANSITION_VALUE, MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}
