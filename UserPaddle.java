package gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class UserPaddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private UserInputListener inputListener;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            setVelocity(Vector2.LEFT.mult(MOVEMENT_SPEED));
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            setVelocity(Vector2.RIGHT.mult(MOVEMENT_SPEED));
        }
    }

    /**
     * Construct a new GameObject instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param inputListener
     */
    public UserPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable
            , UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
    }
}
