package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;


public class BasicCollisionStrategy implements CollisionStrategy {

    private final GameObjectCollection gameObjectCollection;

    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
    }


    @Override
    public void onCollision(GameObject firstObject, GameObject secondObject) {
        System.out.println("collision with brick detected");
        gameObjectCollection.removeGameObject(firstObject);
    }
}
