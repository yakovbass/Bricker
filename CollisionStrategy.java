package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;

public interface CollisionStrategy {
    public void onCollision(GameObject firstObject, GameObject secondObject);
}
