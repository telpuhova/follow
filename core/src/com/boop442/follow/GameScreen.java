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

public class GameScreen implements Screen {

    SpriteBatch batch;
    OrthographicCamera camera;

    private Rectangle follower;
    private Rectangle dot;
    private Rectangle exit;
//    private Rectangle barrier;
    private Array<Rectangle> barriers;


    Vector3 touchPos = new Vector3();

    private Texture dotImage;
    Animation<TextureRegion> followerAnimation; // Must declare frame type (TextureRegion)
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

        Gdx.app.log("Follow.java", "-------------CREATE----------------");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);


        // CREATING TEXTURES

        // load the images for the droplet and the bucket, 64x64 pixels each
        dotImage = new Texture(Gdx.files.internal("red_dot_64px.png"));
        exitImage = new Texture(Gdx.files.internal("shining_128px.png"));
        barrierImage = new Texture(Gdx.files.internal("test.jpg"));



        // Load the sprite sheet as a Texture
        followerSheet = new Texture(Gdx.files.internal("sprite-animation4.png"));

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

        // Initialize the Animation with the frame interval and array of frames
        followerAnimation = new Animation<TextureRegion>(0.025f, followerFrames);

        // time to 0
        stateTime = 0f;



        batch = new SpriteBatch();


        follower = new Rectangle();
        follower.x = 800 - 64;
        follower.y = 20;
        follower.width = 64;
        follower.height = 64;


        dot = new Rectangle();
        dot.x = follower.x;
        dot.y = 10000;
        dot.width = 64;
        dot.height = 64;

        exit = new Rectangle();
        exit.x = -20;
        exit.y = -10;
        exit.width = 128;
        exit.height = 128;

        barriers = new Array<Rectangle>();
        createBarrier();

        intersection = new Rectangle();



        //MUSIC AND SOUNDS:-----------------------------

        // load the drop sound effect and the rain background "music"
        //dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        // start the playback of the background music immediately
        //rainMusic.setLooping(true);
        //rainMusic.play();

    }

    private void createBarrier() {
        Rectangle barrier = new Rectangle();
//        barrier.x = MathUtils.random(0, 800-64);
        barrier.x = 800 / 2 - 64 / 2;
        barrier.y = 20;
        barrier.width = 64;
        barrier.height = 64;
        barriers.add(barrier);
    }

    @Override
    public void render(float delta) {
        run();
    }

    public void run() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);//openGL//dark blue screen
//        Gdx.gl.glClearColor(1f, 1f, 1f, 1);//openGL//white screen

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//openGL
        camera.update();


        //animation:
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion followerFrame = followerAnimation.getKeyFrame(stateTime, true);


        followerHead_x = follower.x + follower.width/2;
        followerHead_y = follower.y + follower.height;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (dot.x - followerHead_x >= 1) {
            //goes right
            goingRight = true;
            follower.x += 1;
            batch.draw(followerFrame, follower.x, follower.y);

            if (follower.y > 20) {
                follower.y = follower.y - 1;
            }
        }
        else if (followerHead_x - dot.x >= 1) {
            //goes left
            goingRight = false;
            followerFrame.flip(true, false);
            follower.x -= 1;
            batch.draw(followerFrame, follower.x, follower.y);
            followerFrame.flip(true, false);

            if (follower.y > 20) {
                follower.y = follower.y - 1;
            }

        } else if (dot.y - follower.y >= 250){
            //stands still
            batch.draw(followerFrame, follower.x, follower.y);

            if (follower.y > 20) {
                follower.y = follower.y - 1;
            }
        } else if (dot.y - follower.y < 250){
            //jumps
            if (dot.y - followerHead_y < 10) {
                game.setScreen(new LevelCompleteMenuScreen(game));
                dispose();
            } else {
                follower.y += 1;
                batch.draw(followerFrame, follower.x, follower.y);
            }
        }

//        batch.draw(followerImage, follower.x, follower.y);
        batch.draw(dotImage, dot.x, dot.y);
        batch.draw(exitImage, exit.x, exit.y);









        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            dot.x = touchPos.x - 64 / 2;
            dot.y = touchPos.y - 64/2;
        }


        Iterator<Rectangle> iter = barriers.iterator();
        while(iter.hasNext()) {
            Rectangle barrier = iter.next();
            batch.draw(barrierImage, barrier.x, barrier.y);
//            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
//            if(raindrop.y + 64 < 0) iter.remove();



            if(barrier.overlaps(follower)) {

                Intersector.intersectRectangles(barrier, follower, intersection);
                if(intersection.y > barrier.y) {
                    //Intersects with top side
                    if (intersection.width > 10) {
                        follower.y = barrier.y + barrier.height;
                    }
                }
                else if(intersection.x + intersection.width < barrier.x + barrier.width) {
                    //Intersects with left side
                    barrier.x++;
                }
                else if(intersection.x > barrier.x) {
                    //Intersects with right side
                    barrier.x--;
                }
                else if(intersection.y + intersection.height < barrier.y + barrier.height) {
                    //Intersects with bottom side

                }

//                dropSound.play();
//                iter.remove();
            }
        }

        batch.end();

        if(follower.overlaps(exit)) {
            Gdx.app.log("Follow.java", "-------------OVERLAPS----------------");
//            dropSound.play();
//            iter.remove();

            //level complete
//            levelComplete();
//            game.setScreen(new LevelCompleteMenuScreen(game));
//            dispose();
        }
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