package gameobjects;

import brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

public class Brick extends GameObject {
    private CollisionStrategy onCollision;
    private static Counter remainingBricks = new Counter(0);
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy onCollision) {
        super(topLeftCorner, dimensions, renderable);
        this.onCollision = onCollision;
        remainingBricks.increment();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        onCollision.onCollision(this,other);
        remainingBricks.decrement();
    }
    public Counter get_bricks(){
        return this.remainingBricks;
    }
}
