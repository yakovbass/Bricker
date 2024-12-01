package bricker.main;

import brick_strategies.BasicCollisionStrategy;
import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.Component;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;


public class BrickerGameManager extends GameManager {
    private static final float BALL_SPEED = 150;
    private int numOfBricks;
    private int numOfRows;
    private GameObject ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private static final int MAX_LIVES = 3;
    private int numOfGames;
    private Counter allTheBricks;
    private UserInputListener inputListener;


    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,int numOfBricks,int numOfRows){
        super(windowTitle,windowDimensions);
        this.numOfBricks = numOfBricks;
        this.numOfRows = numOfRows;
        this.numOfGames = MAX_LIVES;
    }




    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader
            , UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.inputListener = inputListener;
        this.windowController = windowController;
        createBall(imageReader,soundReader,inputListener,windowController);
        createBackGround(imageReader,soundReader,inputListener,windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        createPaddle(imageReader,soundReader,inputListener,windowController);
        createWalls(imageReader,soundReader,inputListener,windowController);
        createBrick(imageReader,soundReader,inputListener,windowController);

    }
    private void createBrick(ImageReader imageReader,
                             SoundReader soundReader,
                             UserInputListener inputListener,
                             WindowController windowController){
        int widthOfBrick = (int)(windowDimensions.x())/(numOfBricks+1);
        float heightOfBrick = 15;

        Renderable brickImage =
                imageReader.readImage("assets/brick.png",false);
        CollisionStrategy basicStrategy = new BasicCollisionStrategy(gameObjects());

        int xPos = 40;  // Column position
        float yPos = 30; // Initial row position
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfBricks; j++) {
                int brickX = xPos + (j * widthOfBrick);  // x position for each brick
                float brickY = yPos + (i * heightOfBrick); // y position for each brick based on row index
                //float yPos = i * heightOfBrick;  // Row position (starting at y=150)

                // Create the brick at the calculated position with the calculated dimensions
                Brick brick = new Brick(new Vector2(brickX, brickY)
                        , new Vector2(widthOfBrick, heightOfBrick)
                        , brickImage, basicStrategy);
                allTheBricks = brick.get_bricks();
                gameObjects().addGameObject(brick);
            }
        }
    }
    private void createWalls(ImageReader imageReader,
                             SoundReader soundReader,
                             UserInputListener inputListener,
                             WindowController windowController){
        float wallThickness = 1; // Adjust thickness as needed
        // Left wall
        GameObject leftWall = new Walls(
                Vector2.ZERO,
                new Vector2(wallThickness, windowDimensions.y()),
                null
        );
        gameObjects().addGameObject(leftWall);
        // Right wall
        GameObject rightWall = new Walls(
                new Vector2(windowDimensions.x() - wallThickness, 0),
                new Vector2(wallThickness, windowDimensions.y()),
                null
        );
        gameObjects().addGameObject(rightWall);
        // Top wall
        GameObject topWall = new Walls(
                Vector2.ZERO,
                new Vector2(windowDimensions.x(), wallThickness),
                null
        );
        gameObjects().addGameObject(topWall);
    }


    private void createPaddle(ImageReader imageReader,
                         SoundReader soundReader,
                         UserInputListener inputListener,
                         WindowController windowController){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",true);
        GameObject userPaddle = new UserPaddle(Vector2.ZERO
                , new Vector2(100,15)
                ,paddleImage, inputListener);
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2,(int)windowDimensions.y()-30));
        gameObjects().addGameObject(userPaddle);

    }
    private void createBackGround(ImageReader imageReader,
                                  SoundReader soundReader,
                                  UserInputListener inputListener,
                                  WindowController windowController){
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",false);
        BackGrounds background = new BackGrounds(Vector2.ZERO,windowDimensions,backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        createHearts(imageReader,soundReader,inputListener,windowController,background);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

    }
    private void createHearts(ImageReader imageReader,
                              SoundReader soundReader,
                              UserInputListener inputListener,
                              WindowController windowController,
                              BackGrounds backGrounds){
        TextRenderable text = new TextRenderable(Integer.toString(this.numOfGames));
        if (this.numOfGames >= 3){
            text.setColor(Color.GREEN);
        }
        if (this.numOfGames == 2){
            text.setColor(Color.YELLOW);
        }
        if (this.numOfGames == 1){
            text.setColor(Color.RED);
        }
        GameObject life = new GameObject(new Vector2(40,450),new Vector2(40,40),text);
        gameObjects().addGameObject(life,Layer.UI);
        Renderable heartImage = imageReader.readImage("assets/heart.png",true);
        int width = 10;
        int height = 10;
        for (int i = 0; i < this.numOfGames; i++) {
            GameObject heart = new GameObject(new Vector2(width,height),new Vector2(30,30),heartImage);
            gameObjects().addGameObject(heart, Layer.UI);
            width+=30;
        }
    }
    private void createBall(ImageReader imageReader,
                            SoundReader soundReader,
                            UserInputListener inputListener,
                            WindowController windowController) {
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Renderable ballImage =
                imageReader.readImage("assets/ball.png",true);
        GameObject ball = new Ball(Vector2.ZERO,new Vector2(50,50),ballImage,collisionSound);
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()){
            ballVelX *= -1;
        }
        if (rand.nextBoolean()){
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX,ballVelY));
        Vector2 windowDimensions = windowController.getWindowDimensions();
        this.windowDimensions = windowDimensions;
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);
        this.ball = ball;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if (ballHeight > windowDimensions.y()){
            if (numOfGames == 1){
                prompt = "You Lose!";
            }
            else {
                numOfGames--;
                windowController.resetGame();
            }
        }
        else if (this.allTheBricks.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = "You Win! :)";
        }
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt)){
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    public static void main(String [] args){
        if (args.length == 2){
            int numOfBricks = Integer.parseInt(args[0]);
            int numOfRows = Integer.parseInt(args[1]);
            new BrickerGameManager("bricker",
                    new Vector2(700,500),numOfBricks,numOfRows).run();
        }
        new BrickerGameManager("bricker",
                new Vector2(700,500),1,1).run();
    }
}
