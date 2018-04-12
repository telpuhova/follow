package com.boop442.follow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import sun.rmi.runtime.Log;

public class GameScreen implements Screen {

    SpriteBatch batch;
    OrthographicCamera camera;

    private Rectangle follower;
    private Rectangle follower_body;
    private Rectangle dot;
    private Rectangle exit;
//    private Rectangle barrier;
    private Array<Rectangle> barriers;


    Vector3 touchPos = new Vector3();

    private Texture dotImage;
    Animation<TextureRegion> followerAnimation; // Must declare frame type (TextureRegion)

    Animation<TextureRegion> followerAnimation_moving; // Must declare frame type (TextureRegion)

    Animation<TextureRegion> followerAnimation_standing;

    Animation<TextureRegion> followerAnimation_up;

    private Texture followerSheet;
    private Texture exitImage;
    private Texture barrierImage;

    Follow game;

    //animation:

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

    // A variable for tracking elapsed time for the animation
    float stateTime;


    float followerHead_x;
    float followerHead_y;

    boolean goingRight;

    Rectangle intersection;




    public GameScreen(final Follow game) {

        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);


        // CREATING TEXTURES

        // load the images for the droplet and the bucket, 64x64 pixels each
        dotImage = new Texture(Gdx.files.internal("black-blue_dot_64px.png"));
        exitImage = new Texture(Gdx.files.internal("shining_128px4.png"));
        barrierImage = new Texture(Gdx.files.internal("test.jpg"));



        // Load the sprite sheet as a Texture
        followerSheet = new Texture(Gdx.files.internal("sprite-animation7.png"));

        // create a 2D array of TextureRegions
        TextureRegion[][] tmp = TextureRegion.split(followerSheet,
                followerSheet.getWidth() / FRAME_COLS,
                followerSheet.getHeight() / FRAME_ROWS);

        // place the regions into a 1D array. (from the top left, going across first)
        // (The Animation constructor requires a 1D array.)
        TextureRegion[] followerFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                followerFrames[index++] = tmp[i][j];
            }
        }

        TextureRegion[] followerFrames_moving = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            followerFrames_moving[i] = followerFrames[i];
        }

        TextureRegion followerFrames_standing = new TextureRegion();
        followerFrames_standing = followerFrames[4];

        TextureRegion followerFrames_up = new TextureRegion();
        followerFrames_up = followerFrames[5];


        // Initialize the Animation with the frame interval and array of frames
        followerAnimation = new Animation<TextureRegion>(0.025f, followerFrames);


        followerAnimation_moving = new Animation<TextureRegion>(0.1f, followerFrames_moving);

        followerAnimation_standing = new Animation<TextureRegion>(0.1f, followerFrames_standing);

        followerAnimation_up = new Animation<TextureRegion>(0.1f, followerFrames_up);



        // time to 0
        stateTime = 0f;



        batch = new SpriteBatch();


        follower = new Rectangle();
        follower.x = 800 - 64;
        follower.y = 20;
        follower.width = 64;
        follower.height = 64;


        follower_body = new Rectangle();
        follower_body.x = 800 - 44;
        follower_body.y = 20;
        follower_body.width = 48;
        follower_body.height = 64;


        dot = new Rectangle();
        dot.x = follower.x;
        dot.y = 10000;
        dot.width = 64;
        dot.height = 64;

        exit = new Rectangle();
        exit.x = -30;
        exit.y = -20;
        exit.width = 128;
        exit.height = 128;

        barriers = new Array<Rectangle>();
        createBarriers();

        intersection = new Rectangle();



        //MUSIC AND SOUNDS:-----------------------------

        // load the drop sound effect and the rain background "music"
        //dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        // start the playback of the background music immediately
        //rainMusic.setLooping(true);
        //rainMusic.play();

    }

    private void createBarriers() {

        if (game.level == 0) {
            Rectangle barrier = new Rectangle();
//        barrier.x = MathUtils.random(0, 800-64);
            barrier.x = 800 / 2 - 64 / 2;
            barrier.y = 20;
            barrier.width = 64;
            barrier.height = 64;
            barriers.add(barrier);
        } else if (game.level == 1) {
            Rectangle barrier1 = new Rectangle();
            Rectangle barrier2 = new Rectangle();
//        barrier.x = MathUtils.random(0, 800-64);
            barrier1.x = 800 / 2 + 64;
            barrier1.y = 20;
            barrier1.width = 64;
            barrier1.height = 64;
            barriers.add(barrier1);

            barrier2.x = 800 / 2 - 64 * 2;
            barrier2.y = 20;
            barrier2.width = 64;
            barrier2.height = 64;
            barriers.add(barrier2);
        } else if (game.level == 2) {
            Rectangle barrier1 = new Rectangle();
//        barrier.x = MathUtils.random(0, 800-64);
            barrier1.x = 600;
            barrier1.y = 20;
            barrier1.width = 64;
            barrier1.height = 64;
            barriers.add(barrier1);

            Rectangle barrier2 = new Rectangle();
            barrier2.x = 400;
            barrier2.y = 20;
            barrier2.width = 64;
            barrier2.height = 64;
            barriers.add(barrier2);

            Rectangle barrier3 = new Rectangle();
            barrier3.x = 200;
            barrier3.y = 20;
            barrier3.width = 64;
            barrier3.height = 64;
            barriers.add(barrier3);
        } else if (game.level >= 3) {
            Rectangle barrier1 = new Rectangle();
//        barrier.x = MathUtils.random(0, 800-64);
            barrier1.x = 600;
            barrier1.y = 20;
            barrier1.width = 64;
            barrier1.height = 64;
            barriers.add(barrier1);

            Rectangle barrier2 = new Rectangle();
            barrier2.x = 400;
            barrier2.y = 20;
            barrier2.width = 64;
            barrier2.height = 64;
            barriers.add(barrier2);

            Rectangle barrier3 = new Rectangle();
            barrier3.x = 200;
            barrier3.y = 20;
            barrier3.width = 64;
            barrier3.height = 64;
            barriers.add(barrier3);
        }
    }

    @Override
    public void render(float delta) {
        run();
    }

    public void run() {

        //SET UP

//        Gdx.gl.glClearColor(0, 0, 0.2f, 1);//openGL//dark blue screen
        Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1f);//openGL//white screen

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//openGL
        camera.update();


        //animation:
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion followerFrame = followerAnimation.getKeyFrame(stateTime, true);

        TextureRegion followerFrame_moving = followerAnimation_moving.getKeyFrame(stateTime, true);

        TextureRegion followerFrame_standing = followerAnimation_standing.getKeyFrame(stateTime, true);

        TextureRegion followerFrame_up = followerAnimation_up.getKeyFrame(stateTime, true);


        followerHead_x = follower.x + follower.width/2;
        followerHead_y = follower.y + follower.height;

        batch.setProjectionMatrix(camera.combined);




        batch.begin();

        //DRAW SINGLE IMAGES THAT DON'T NEED FLIPPING
        batch.draw(dotImage, dot.x, dot.y);
        batch.draw(exitImage, exit.x, exit.y);


        //CREATING DOT
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            dot.x = touchPos.x - 64 / 2;
            dot.y = touchPos.y - 64/2;
        }


        followerMoves(followerFrame_moving, followerFrame_standing, followerFrame_up); // FOLLOWER MOVING TOWARDS THE DOT
        processBarriers(); // LOOPING through barriers


        batch.end();


        //CHECKS IF USER WON
        if (follower.x - exit.x < 40) {
//            dropSound.play();
//            iter.remove();

            levelComplete();
        }
    }


    public void followerMoves(TextureRegion followerFrame_moving, TextureRegion followerFrame_standing, TextureRegion followerFrame_up) {
        if (dot.x - followerHead_x >= 1) {
            //goes right
            goingRight = true;
            follower.x += 1;
            follower_body.x += 1;
            batch.draw(followerFrame_moving, follower.x, follower.y);

            if (follower.y > 20) {
                follower.y--;
                follower_body.y--;
            }
        }
        else if (followerHead_x - dot.x >= 1) {
            //goes left
            goingRight = false;
            followerFrame_moving.flip(true, false);
            follower.x -= 1;
            follower_body.x -= 1;
            batch.draw(followerFrame_moving, follower.x, follower.y);
            followerFrame_moving.flip(true, false);

            if (follower.y > 20) {
                follower.y--;
                follower_body.y--;
            }

        } else if (dot.y - follower.y >= 250){
            //stands still
            batch.draw(followerFrame_standing, follower.x, follower.y);

            if (follower.y > 20) {
                follower.y--;
                follower_body.y--;
            }
        } else if (dot.y - follower.y < 250){
            //goes up
            if (dot.y - followerHead_y < 10) {
                followerDied();

            } else {
                follower.y++;
                follower_body.y++;
                batch.draw(followerFrame_up, follower.x, follower.y);
            }
        }
    }


    public void processBarriers() {
        Rectangle barrier;
        Rectangle barrierNext;
        boolean moving = false;

        for (int i = 0; i < barriers.size; i++) {
            barrier = barriers.get(i);
            batch.draw(barrierImage, barrier.x, barrier.y);
//            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
//            if(raindrop.y + 64 < 0) iter.remove();



            if (follower_body.overlaps(barrier)) {

                Intersector.intersectRectangles(barrier, follower_body, intersection);
                if ((intersection.y > barrier.y) && (intersection.width > 10)) {
                    //Intersects with top side
                    follower.y = barrier.y + barrier.height;
                    follower_body.y = barrier.y + barrier.height;
                    moving = false;
                }
                else if (intersection.x + intersection.width < barrier.x + barrier.width) {
                    //Intersects with left side
                    if (barrier.x + barrier.width >= 800) {
                        follower.x--;
                        follower_body.x--;
                    } else {
                        barrier.x++;
                        moving = true;
                    }
                }
                else if (intersection.x > barrier.x) {
                    //Intersects with right side
                    if (barrier.x <= 0) {
                        follower.x++;
                        follower_body.x++;
                    } else {
                        barrier.x--;
                        moving = true;
                    }
                }
                else if(intersection.y + intersection.height < barrier.y + barrier.height) {
                    //Intersects with bottom side
                    moving = false;
                }
                else {
                    moving = false;
                }



//                dropSound.play();
//                iter.remove();
            }

            if (moving) {

                for (int j = 0; j < barriers.size; j++) {
//                    if (j == i) { continue; }
                    barrierNext = barriers.get(j);

                    if (barrier.overlaps(barrierNext)) {

                        Intersector.intersectRectangles(barrier, barrierNext, intersection);
                        if (intersection.y > barrier.y) {
                            //Intersects with top side
                        }
                        else if (intersection.x + intersection.width < barrier.x + barrier.width) {
                            //Intersects with left side
                            barrierNext.x--;
                        }
                        else if (intersection.x > barrier.x) {
                            //Intersects with right side
                            barrierNext.x++;
                        }
                        else if (intersection.y + intersection.height < barrier.y + barrier.height) {
                            //Intersects with bottom side
                        }
//                dropSound.play();
//                iter.remove();
                    }
                }
            }
        }
    }



    public void levelComplete() {
        game.setScreen(new LevelCompleteMenuScreen(game));
        game.level++;
        dispose();
    }

    public void followerDied() {
        game.setScreen(new followerDiedMenuScreen(game));
        game.level = 0;
        dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
//        rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    @Override
    public void dispose() {
        //batch, images, sounds, music
        batch.dispose();
        dotImage.dispose();
        exitImage.dispose();
        followerSheet.dispose();
    }
}