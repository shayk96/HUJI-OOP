package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the halo of sun.
 *
 * @author Hila Ziv, Shay Kvasha
 */
public class SunHalo {

    /**
     * The tag for sun halo object.
     */
    private static final String SUN_HALO_TAG = "sun halo";

    /**
     * The color of the halo.
     */
    private static final Vector2 HALO_COLOR = new Vector2(200, 200);

    /**
     * This function creates a halo around a given object that represents the sun.
     * The halo will be tied to the given sun, and will always move with it.
     *
     * @param gameObjects The collection of all participating game objects.
     * @param layer       The number of the layer to which the created halo should be added.
     * @param sun         A game object representing the sun (it will be followed by the created game object).
     * @param color       The color of the halo.
     * @return A new game object representing the sun's halo.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun,
                                    Color color) {
        GameObject sunHalo = new GameObject(Vector2.ZERO, HALO_COLOR, new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}
